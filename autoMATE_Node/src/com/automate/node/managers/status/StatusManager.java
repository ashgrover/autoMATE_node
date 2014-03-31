package com.automate.node.managers.status;

import java.util.List;

import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;
import com.automate.node.managers.connection.IConnectionManager;
import com.automate.node.managers.message.IMessageManager;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.models.Status;
import com.automate.protocol.server.ServerProtocolParameters;

public class StatusManager extends ManagerBase<StatusListener> implements IStatusManager {

	private IMessageManager messageManager;
	private IConnectionManager connectionManager;
	
	private StatusManager(IMessageManager messageManager, IConnectionManager connectionManager) {
		super(StatusListener.class);
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
	// Inherited from StatusListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onStatusUpdateRequested() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusUpdateSent(List<Status<?>> statuses) {
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
	// Inherited from IStatusManager
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public List<Status<?>> getStatuses() {
		// TODO Auto-generated method stub
		return null;
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
	protected void performInitialUpdate(StatusListener listener) {
		// TODO Auto-generated method stub

	}

}
