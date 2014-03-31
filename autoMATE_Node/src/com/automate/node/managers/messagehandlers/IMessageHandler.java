package com.automate.node.managers.messagehandlers;

import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.server.ServerProtocolParameters;
import com.automate.protocol.server.messages.ServerAuthenticationMessage;

/**
 * Delegate for handling incoming messages
 * @author ian.kabeary
 *
 * @param <M> The type of message this delegate handles
 * @param <Params> container class for additional parameters.
 */
public interface IMessageHandler<M extends Message<ServerProtocolParameters>, Params> {
	
	/**
	 * Handle the message upon receipt.
	 * @param message the message received from the server
	 * @return a response message if response is required by protocol spec.
	 */
	Message<ClientProtocolParameters> handleMessage(ServerAuthenticationMessage message, Void params);
	
}
