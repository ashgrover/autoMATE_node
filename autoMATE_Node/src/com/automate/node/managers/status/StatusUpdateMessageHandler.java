package com.automate.node.managers.status;

import com.automate.node.managers.message.IMessageManager; 
import com.automate.node.managers.messagehandlers.IMessageHandler;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.node.messages.NodeStatusUpdateMessage;
import com.automate.protocol.server.messages.ServerNodeStatusUpdateMessage;

public class StatusUpdateMessageHandler implements IMessageHandler<ServerNodeStatusUpdateMessage, Void> {

	private IStatusManager statusManager;
	private IMessageManager messageManager;

	public StatusUpdateMessageHandler(IStatusManager statusManager, IMessageManager messageManager) {
		this.statusManager = statusManager;
		this.messageManager = messageManager;
	}

	@Override
	public Message<ClientProtocolParameters> handleMessage(ServerNodeStatusUpdateMessage message, Void params) {
		statusManager.onStatusUpdateRequested();
		return new NodeStatusUpdateMessage(messageManager.getProtocolParameters(), statusManager.getStatuses());
	}

}
