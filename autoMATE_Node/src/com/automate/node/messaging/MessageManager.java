package com.automate.node.messaging;

import java.util.HashMap;

import com.automate.node.InitializationException;
import com.automate.node.messaging.handlers.IMessageHandler;
import com.automate.node.status.IStatusManager;
import com.automate.protocol.Message;
import com.automate.protocol.Message.MessageType;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.server.ServerProtocolParameters;

public class MessageManager implements IMessageManager {


	private IMessageReceiver messageReceiver;
	private IMessageSender messageSender;
	private IMessageHandler messageHandler;
	//private IStatusManager statusManager;
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
	
	

}
