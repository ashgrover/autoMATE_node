package com.automate.node.managers.authentication;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import com.automate.node.managers.IListener; 
import com.automate.node.managers.ManagerBase;
import com.automate.node.managers.connection.ConnectionListener;
import com.automate.node.managers.connection.IConnectionManager;
import com.automate.node.managers.message.IMessageManager;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.client.messages.ClientAuthenticationMessage;
import com.automate.protocol.server.ServerProtocolParameters;
import com.automate.protocol.server.messages.ServerAuthenticationMessage;

public class AuthenticationManager extends ManagerBase<AuthenticationListener> implements IAuthenticationManager {

	public enum AuthenticatedState {
		NOT_AUTHENTICATED,
		AUTHENTICATING,
		AUTHENTICATED
	}
	
	private AuthenticatedState state;
	
	private IMessageManager messageManager;
	private IConnectionManager connectionManager;
	private AuthenticationMessageHandler messageHandler;
	private Timer timer;
	private boolean reconnect;
	private long nodeId;
	private String password;
	
	public AuthenticationManager(IMessageManager messageManager, IConnectionManager connectionManager) {
		super(AuthenticationListener.class);
		this.messageManager = messageManager;
		this.connectionManager = connectionManager;
		this.messageHandler = new AuthenticationMessageHandler(this);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from MessageListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onMessageSet(Message<ClientProtocolParameters> message) {
		if(message instanceof ClientAuthenticationMessage) {
			this.onAuthenticating(Long.parseLong(((ClientAuthenticationMessage) message).username), ((ClientAuthenticationMessage) message).password);
		}
	}

	@Override
	public void onMessageNotSent(Message<ClientProtocolParameters> message) {
		if(message instanceof ClientAuthenticationMessage) {
			this.onAuthenticationFailure(Long.parseLong(((ClientAuthenticationMessage) message).username), ((ClientAuthenticationMessage) message).password);
		}
	}

	@Override
	public void onMessageReceived(Message<ServerProtocolParameters> message) {
		if(message instanceof ServerAuthenticationMessage) {
			Message<ClientProtocolParameters> response = messageHandler.handleMessage((ServerAuthenticationMessage) message, null);
			if(response != null) {
				messageManager.sendMessage(response);
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ConnectionManager
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onConnecting() {}

	@Override
	public void onConnected(String sessionKey) {}

	@Override
	public void onDisconnected() {
		if(reconnect) {
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					reconnect();
				}
			}, 15000);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from AuthenticationManager
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onAuthenticating(long nodeId, String password) {
		this.state = AuthenticatedState.AUTHENTICATING;
		this.nodeId = nodeId;
		this.password = password;
		for(AuthenticationListener listener : mListeners) {
			try {
				listener.onAuthenticating(nodeId, password);
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener.");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onAuthenticationFailure(long nodeId, String password) {
		this.state = AuthenticatedState.NOT_AUTHENTICATED;
		this.nodeId = -1;
		this.password = null;
		for(AuthenticationListener listener : mListeners) {
			try {
				listener.onAuthenticationFailure(nodeId, password);
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener.");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onAuthenticated(long nodeId, String password) {
		this.state = AuthenticatedState.AUTHENTICATED;
		writeCredentials(nodeId, password);
		for(AuthenticationListener listener : mListeners) {
			try {
				listener.onAuthenticationFailure(nodeId, password);
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener.");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onLoggedOut() {
		this.state = AuthenticatedState.NOT_AUTHENTICATED;
		this.nodeId = -1;
		this.password = null;
		this.reconnect = false;
		for(AuthenticationListener listener : mListeners) {
			try {
				listener.onLoggedOut();
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener.");
				e.printStackTrace();
			}
		}
		if(connectionManager != null) {
			this.connectionManager.disconnect();
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onBind(Class<? extends IListener> listenerClass) {
	}
	
	@Override
	public void onUnbind(Class<? extends IListener> listenerClass) {
		if(listenerClass.equals(ConnectionListener.class)) {
			signOut();
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IAuthenticationManager
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean signIn(long nodeId, String password) {
		ClientAuthenticationMessage message = new ClientAuthenticationMessage(messageManager.getProtocolParameters(), String.valueOf(nodeId), password);
		this.reconnect = true;
		messageManager.sendMessage(message);
		return true;
	}

	@Override
	public boolean signOut() {
		onLoggedOut();
		return true;
	}

	@Override
	public void reconnect() {
		if(nodeId >= 0 && password != null) {
			signIn(nodeId, password);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ManagerBase
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void unbindSelf() {
		this.messageManager.unbind(this);
		this.connectionManager.unbind(this);
	}

	@Override
	protected void bindSelf() {
		this.messageManager.bind(this);
		this.messageManager.bind(this);
	}

	@Override
	protected void setupInitialState() {
		this.timer = new Timer("Reconnect timer");
		this.state = AuthenticatedState.NOT_AUTHENTICATED;
	}

	@Override
	protected void teardown() {
		timer.cancel();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ListenerBinder
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void performInitialUpdate(AuthenticationListener listener) {
		switch(state) {
			case AUTHENTICATING:
				listener.onAuthenticating(nodeId, password);
				break;
	
			case AUTHENTICATED:
				listener.onAuthenticated(nodeId, password);
				break;
			
			default: break;
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// private utility methods
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void writeCredentials(long nodeId, String password) {
		try {
			File outfile = new File("resources/credentials.properties");
			PrintWriter writer = new PrintWriter(outfile);
			writer.println("credentials.nodeId=" + nodeId);
			writer.println("credentials.password=" + password);
			writer.close();
		} catch (IOException e) {
			System.out.println("Error writing credentials to disk.");
		}
		
	}
	
}
