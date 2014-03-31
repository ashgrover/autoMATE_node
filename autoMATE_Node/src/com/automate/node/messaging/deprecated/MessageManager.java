package com.automate.node.messaging.deprecated;

import java.util.HashMap;

import com.automate.node.InitializationException;
import com.automate.node.managers.messagehandlers.IMessageHandler;
import com.automate.node.status.deprecated.IStatusManager;
import com.automate.protocol.Message;
import com.automate.protocol.Message.MessageType;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.server.ServerProtocolParameters;

public class MessageManager implements IMessageManager {

	
	private HashMap<MessageType, IMessageHandler> messageHandlers = new HashMap<Message.MessageType, IMessageHandler>();
	
	private HashMap<MessageType, IMessageHandler<? extends Message<ServerProtocolParameters>,?>> handlers;
	
	@Override
	public void initialize() throws InitializationException{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void terminate() throws Exception {
		// TODO Auto-generated method stub
		
	}
	public void sendMessage(Message<ClientProtocolParameters> message){
		
		
	}

	@Override
	public void handleInput(String message) {
		// TODO Auto-generated method stub
		
	}
	
	

}
