package com.automate.node.messaging;

import com.automate.protocol.node.messages.NodeCommandMessage;
import com.automate.protocol.node.messages.NodeStatusUpdateMessage;
import com.automate.protocol.node.messages.NodeWarningMessage;

public interface IMessageSender {
	/**
	 * Send Node command message to the server.
	 * @param message
	 */
	public void sendMessage(NodeCommandMessage message);
	
	/**
	 * Send Node Status Update to the server.
	 * @param message
	 */
	public void sendMessage(NodeStatusUpdateMessage message);
	
	/**
	 * Send node warning message to the server.
	 * @param message
	 */
	public void sendMessage(NodeWarningMessage message);
	
	public interface MessageSentListener {
		public void messageSent();
	}
	
}
