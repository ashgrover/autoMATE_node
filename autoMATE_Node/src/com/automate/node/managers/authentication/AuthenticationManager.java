package com.automate.node.managers.authentication;

import java.io.IOException;
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
	public void onMessageSent(Message<ClientProtocolParameters> message) {
		if(message instanceof ClientAuthenticationMessage) {
			this.onAuthenticating(Long.parseLong(((ClientAuthenticationMessage) message).username.substring(1)), ((ClientAuthenticationMessage) message).password);
		}
	}

	@Override
	public void onMessageNotSent(Message<ClientProtocolParameters> message) {
		if(message instanceof ClientAuthenticationMessage) {
			this.onAuthenticationFailure(Long.parseLong(((ClientAuthenticationMessage) message).username.substring(1)), 
					((ClientAuthenticationMessage) message).password);
		}
		scheduleReconnect();
	}

	@Override
	public void onMessageReceived(Message<ServerProtocolParameters> message) {
		if(message instanceof ServerAuthenticationMessage) {
			Message<ClientProtocolParameters> response = messageHandler.handleMessage((ServerAuthenticationMessage) message, password);
			if(response != null) {
				messageManager.sendMessage(response);
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ConnectionListener
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onConnecting() {}

	@Override
	public void onConnected(String sessionKey) {}

	@Override
	public void onDisconnected() {
		if(reconnect) {
			scheduleReconnect();
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from DiscoveryListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onBroadcastSent() {}
	@Override
	public void onPairRequest() {}
	@Override
	public void onDeviceInformationSent() {}
	@Override
	public void onPairFailed() {}

	@Override
	public void onWifiCredsProvided(String ssid, String username, String passphrase) {
		try {
			Runtime.getRuntime().exec("sudo ./scripts/wifi-connect.sh");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPairSuccess(long nodeId, String password) {
		signIn(nodeId, password);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from AuthenticationManager
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onAuthenticating(long nodeId, String password) {
		this.state = AuthenticatedState.AUTHENTICATING;
		synchronized (mListeners) {
			for(AuthenticationListener listener : mListeners) {
				try {
					listener.onAuthenticating(nodeId, password);
				} catch (RuntimeException e) {
					System.out.println("Error notifying listener.");
					e.printStackTrace();
				}
			}
		}
		connectionManager.onConnecting();
	}

	@Override
	public void onAuthenticationFailure(long nodeId, String password) {
		this.state = AuthenticatedState.NOT_AUTHENTICATED;
		synchronized (mListeners) {
			for(AuthenticationListener listener : mListeners) {
				try {
					listener.onAuthenticationFailure(nodeId, password);
				} catch (RuntimeException e) {
					System.out.println("Error notifying listener.");
					e.printStackTrace();
				}
			}
		}
		connectionManager.onDisconnected();
	}

	@Override
	public void onAuthenticated(long nodeId, String password, String sessionKey) {
		this.state = AuthenticatedState.AUTHENTICATED;
		synchronized (mListeners) {
			for(AuthenticationListener listener : mListeners) {
				try {
					listener.onAuthenticated(nodeId, password, sessionKey);
				} catch (RuntimeException e) {
					System.out.println("Error notifying listener.");
					e.printStackTrace();
				}
			}
		}
		connectionManager.onConnected(sessionKey);
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
			this.reconnect = false;
			onDisconnected();
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IAuthenticationManager
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean signIn(long nodeId, String password) {
		ClientAuthenticationMessage message = new ClientAuthenticationMessage(messageManager.getProtocolParameters(), "$" + String.valueOf(nodeId), password);
		this.reconnect = true;
		messageManager.sendMessage(message);
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
		this.connectionManager.bind(this);
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
			listener.onAuthenticated(nodeId, password, connectionManager.getSessionKey());
			break;

		default: break;
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// private utility methods
	/////////////////////////////////////////////////////////////////////////////////////////////////

	private void scheduleReconnect() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				reconnect();
			}
		}, 15000);
		System.out.println("Reconnecting in 15 sec.");
	}
	
}
