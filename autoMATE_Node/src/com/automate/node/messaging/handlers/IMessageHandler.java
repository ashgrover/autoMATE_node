package com.automate.node.messaging.handlers;

import com.automate.protocol.Message;
import com.automate.protocol.server.ServerProtocolParameters;

/**
 * Delegate for handling incoming messages
 * @author ian.kabeary
 *
 * @param <M> The type of message this delegate handles
 * @param <Params> container class for additional parameters.
 */
public interface IMessageHandler<M extends Message<ServerProtocolParameters>, Params> {
	
}
