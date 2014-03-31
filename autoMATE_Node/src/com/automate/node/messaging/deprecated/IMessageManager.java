package com.automate.node.messaging.deprecated;

import com.automate.node.IManager;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;

public interface IMessageManager extends IManager {

	public void sendMessage(Message<ClientProtocolParameters> message);

	public void handleInput(String message);
	
}
