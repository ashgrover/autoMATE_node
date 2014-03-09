package com.automate.node.messaging;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.automate.node.messaging.IMessageSender.MessageSentListener;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;

public class PacketSendTask implements Runnable {
	
	private Message<ClientProtocolParameters> message;
	private Socket socket; 
	private MessageSentListener listener;
	private static final Logger logger = LogManager.getLogger();
	
	public PacketSendTask(Message<ClientProtocolParameters> message, Socket socket, MessageSentListener listener){
		this.message = message;
		this.socket = socket;
		this.listener = listener;
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			StringBuilder xmlBuilder = new StringBuilder();
			message.toXml(xmlBuilder, 0);
			String messageXml = xmlBuilder.toString();
			logger.trace("Message contents:\n{}", xmlBuilder);
			writer.print(messageXml);
			socket.close();
			if(listener != null) {
				listener.messageSent();
			}
		}
		catch(Exception e){
			
		}
		
	}
	
	
}
