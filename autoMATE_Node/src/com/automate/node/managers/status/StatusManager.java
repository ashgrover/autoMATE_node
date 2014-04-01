package com.automate.node.managers.status;

import java.util.ArrayList;
import java.util.List;

import com.automate.node.device.FanInterface;
import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;
import com.automate.node.managers.message.IMessageManager;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.models.Status;
import com.automate.protocol.models.Type;
import com.automate.protocol.node.messages.NodeStatusUpdateMessage;
import com.automate.protocol.server.ServerProtocolParameters;
import com.automate.protocol.server.messages.ServerNodeStatusUpdateMessage;

public class StatusManager extends ManagerBase<StatusListener> implements IStatusManager {

	private IMessageManager messageManager;
	private FanInterface fanGpioUtility;

	private StatusUpdateMessageHandler messageHandler;

	public StatusManager(IMessageManager messageManager, FanInterface fanGpioUtility) {
		super(StatusListener.class);
		this.messageManager = messageManager;
		this.fanGpioUtility = fanGpioUtility;
		this.messageHandler = new StatusUpdateMessageHandler(this, messageManager);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from MessageListener
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onMessageSent(Message<ClientProtocolParameters> message) {
		if(message instanceof NodeStatusUpdateMessage) {
			this.onStatusUpdateSent(((NodeStatusUpdateMessage) message).statuses);
		}
	}

	public void onMessageNotSent(Message<ClientProtocolParameters> message) {}

	@Override
	public void onMessageReceived(Message<ServerProtocolParameters> message) {
		if(message instanceof ServerNodeStatusUpdateMessage) {
			/*
			 * COMMENTS: When a status update message is received, the handler will return the response message.
			 * The handler calls getStatuses() for the current status of the node.
			 */
			Message<ClientProtocolParameters> response = messageHandler.handleMessage((ServerNodeStatusUpdateMessage) message, null);
			if(response != null) {
				messageManager.sendMessage(response);
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from StatusListener
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onStatusUpdateRequested() {
		/*
		 * COMMENTS: This method just forwards onStatusUpdateRequested events to listeners
		 */
		synchronized (mListeners) {
			for(StatusListener listener : mListeners) {
				try {
					listener.onStatusUpdateRequested();
				} catch (RuntimeException e) {
					System.err.println("Error notifying listener.");
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onStatusUpdateSent(List<Status<?>> statuses) {
		/*
		 * COMMENTS: This method just forwards onStatusUpdateSent events to listeners
		 */
		synchronized (mListeners) {
			for(StatusListener listener : mListeners) {
				try {
					listener.onStatusUpdateSent(statuses);
				} catch (RuntimeException e) {
					System.err.println("Error notifying listener.");
					e.printStackTrace();
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IListener
	/////////////////////////////////////////////////////////////////////////////////////////////////

	public void onBind(Class<? extends IListener> listenerClass) {}
	public void onUnbind(Class<? extends IListener> listenerClass) {}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IStatusManager
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public List<Status<?>> getStatuses() {
		/*
		 * COMMENTS:  I moved this code here from onStatusUpdateRequested.
		 */
		int speed = fanGpioUtility.getFanSpeed();
		List<Status<?>> statusList = new ArrayList<Status<?>>();

		if (speed == 0) {
			statusList.add(Status.newStatus("Power On", Type.BOOLEAN, false));
		} else {
			statusList.add(Status.newStatus("Power On", Type.BOOLEAN, true));
			if(speed == 1) {
				statusList.add(Status.newStatus("Speed", Type.STRING, "Low"));
			} else if(speed == 2) {
				statusList.add(Status.newStatus("Speed", Type.STRING, "Medium"));
			} else if(speed == 3) {
				statusList.add(Status.newStatus("Speed", Type.STRING, "High"));
			} 
		}
		return statusList;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ManagerBase
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void unbindSelf() {
		/*
		 * COMMENTS: Use this method to detach as a listener from the dependencies
		 */
		messageManager.unbind(this);
	}

	@Override
	protected void bindSelf() {
		/*
		 * COMMENTS: Use this method to attach as a listener to the dependencies
		 */
		messageManager.bind(this);
	}

	protected void setupInitialState() { /* no state or resources */ }
	protected void teardown() { /* no state or resources */ }

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ListenerBinder
	/////////////////////////////////////////////////////////////////////////////////////////////////

	protected void performInitialUpdate(StatusListener listener) { /* no state */}

}
