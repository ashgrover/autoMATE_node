package com.automate.node.managers.warning;

import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;
import com.automate.node.managers.authentication.AuthenticationListener;
import com.automate.node.managers.connection.IConnectionManager;
import com.automate.node.managers.message.IMessageManager;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.server.ServerProtocolParameters;

public class WarningManager extends ManagerBase<WarningListener> implements IWarningManager {

	private IMessageManager messageManager;
	private IConnectionManager connectionManager;
	
	private WarningManager(IMessageManager messageManager, IConnectionManager connectionManager) {
		super(WarningListener.class);
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
	// Inherited from ConnectionListener
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
	// Inherited from WarningListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onWarningEmitted(String warning) {
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
	// Inherited from IWarningManager
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void emitWarning(String Warning) {
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
	protected void performInitialUpdate(WarningListener listener) {
		// TODO Auto-generated method stub

	}

}
