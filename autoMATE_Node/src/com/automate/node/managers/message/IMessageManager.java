package com.automate.node.managers.message;

import com.automate.node.managers.IManager;
import com.automate.node.managers.connection.ConnectionListener;
import com.automate.node.managers.packet.PacketListener;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;

public interface IMessageManager extends IManager<MessageListener>, PacketListener, ConnectionListener, MessageListener {

	public void sendMessage(Message<ClientProtocolParameters> message);
	
	public void sendMessage(Message<ClientProtocolParameters> message, MessageListener listener);
	
	public ClientProtocolParameters getProtocolParameters();
	
}
