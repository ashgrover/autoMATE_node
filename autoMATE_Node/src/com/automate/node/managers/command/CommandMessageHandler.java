package com.automate.node.managers.command;

import com.automate.node.managers.message.IMessageManager;
import com.automate.node.managers.messagehandlers.IMessageHandler;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.node.messages.NodeCommandMessage;
import com.automate.protocol.server.messages.ServerNodeCommandMessage;

public class CommandMessageHandler implements IMessageHandler<ServerNodeCommandMessage, Void> {

	private ICommandManager commandManager;
	private IMessageManager messageManager;

	public CommandMessageHandler(ICommandManager commandManager, IMessageManager messageManager) {
		this.commandManager = commandManager;
		this.messageManager = messageManager;
	}

	@Override
	public Message<ClientProtocolParameters> handleMessage(ServerNodeCommandMessage message, Void params) {
		commandManager.onCommandReceived(message.name, message.args, message.commandId); // notify the listeners
		int result = commandManager.executeCommand(message.name, message.args, message.commandId); // execute the command
		
		ClientProtocolParameters parameters = messageManager.getProtocolParameters();
		String responseMessage = null;
		
		if(result == 200) {
			responseMessage = "OK";
		} else if(result == 401) {
			responseMessage = "INVALID_COMMAND";
		} else if(result == 403) {
			responseMessage = "INVALID_ARGS";
		}
		
		return new NodeCommandMessage(parameters, message.commandId, result, responseMessage);
	}

}
