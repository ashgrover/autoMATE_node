package com.automate.node.managers.status;

import java.util.ArrayList;
import java.util.List;

import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;
import com.automate.node.managers.connection.IConnectionManager;
import com.automate.node.managers.message.IMessageManager;
import com.automate.node.utilities.FanGpioUtility;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.models.Status;
import com.automate.protocol.models.Type;
import com.automate.protocol.node.messages.NodeStatusUpdateMessage;
import com.automate.protocol.server.ServerProtocolParameters;

public class StatusManager extends ManagerBase<StatusListener> implements IStatusManager {

	private IMessageManager messageManager;
	private IConnectionManager connectionManager;
	private FanGpioUtility fanGpioUtility;
	private int nodeId;
	private ClientProtocolParameters cpp;
	
	private StatusManager(IMessageManager messageManager, IConnectionManager connectionManager) {
		super(StatusListener.class);
		this.messageManager = messageManager;
		this.connectionManager = connectionManager;
		this.fanGpioUtility = new FanGpioUtility();
		
		this.nodeId = 0;
		this.cpp = messageManager.getProtocolParameters();
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
		
		int speed = fanGpioUtility.getFanSpeed();
		Status<?> currentStatus = null;
		
		// get the current status
		
		if (speed == 0) {
			currentStatus = Status.newStatus("Table Fan", Type.INTEGER, 0);
			
		} else if (speed == 1) {
			currentStatus = Status.newStatus("Table Fan", Type.INTEGER, 1);
			
		} else if (speed == 2) {
			currentStatus = Status.newStatus("Table Fan", Type.INTEGER, 2);
			
		} else if (speed == 3) {
			currentStatus = Status.newStatus("Table Fan", Type.INTEGER, 3);
			
		}
		
		List<Status<?>> statusList = new ArrayList<Status<?>>();
		statusList.add(currentStatus);
		
		// TODO
		this.cpp = null;
		NodeStatusUpdateMessage updateMessage = new NodeStatusUpdateMessage(this.cpp, this.nodeId , statusList);
		
		messageManager.sendMessage(updateMessage);
		
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
