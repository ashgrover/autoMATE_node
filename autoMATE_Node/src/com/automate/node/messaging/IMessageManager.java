package com.automate.node.messaging;

import com.automate.node.IManager;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;

public interface IMessageManager extends IManager {
	
	public void sendMessage(Message<ClientProtocolParameters> message);
	
}
