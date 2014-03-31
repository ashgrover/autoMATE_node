package com.automate.node.managers.message;

import com.automate.node.managers.IListener;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.server.ServerProtocolParameters;

public interface MessageListener extends IListener {

	public void onMessageSet(Message<ClientProtocolParameters> message);
	
	public void onMessageNotSent(Message<ClientProtocolParameters> message);
	
	public void onMessageReceived(Message<ServerProtocolParameters> message);
	
}
