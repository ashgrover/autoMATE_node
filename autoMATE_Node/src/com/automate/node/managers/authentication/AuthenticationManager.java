package com.automate.node.managers.authentication;

import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;
import com.automate.node.managers.connection.IConnectionManager;
import com.automate.node.managers.message.IMessageManager;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.server.ServerProtocolParameters;

public class AuthenticationManager extends ManagerBase<AuthenticationListener> implements IAuthenticationManager {

	private IMessageManager messageManager;
	private IConnectionManager connectionManager;
	
	private AuthenticationManager(IMessageManager messageManager, IConnectionManager connectionManager) {
		super(AuthenticationListener.class);
		this.messageManager = messageManager;
		this.connectionManager = connectionManager;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from MessageListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onMessageSet(Message<ClientProtocolParameters> message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessageNotSent(Message<ClientProtocolParameters> message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessageReceived(Message<ServerProtocolParameters> message) {
		// TODO Auto-generated method stub

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ConnectionManager
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onConnecting() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(String sessionKey) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from AuthenticationManager
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onAuthenticating(long nodeId, String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAuthenticationFailure(long nodeId, String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAuthenticated(long nodeId, String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoggedOut() {
		// TODO Auto-generated method stub

	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onBind(Class<? extends IListener> listenerClass) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onUnbind(Class<? extends IListener> listenerClass) {
		// TODO Auto-generated method stub
		
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IAuthenticationManager
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean signIn(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean signOut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reconnect() {
		// TODO Auto-generated method stub

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ManagerBase
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void unbindSelf() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void bindSelf() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setupInitialState() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void teardown() {
		// TODO Auto-generated method stub

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ListenerBinder
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void performInitialUpdate(AuthenticationListener listener) {
		// TODO Auto-generated method stub

	}

}
