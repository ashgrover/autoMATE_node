package com.automate.node.managers.command;

import java.util.List;

import com.automate.node.managers.IManager;
import com.automate.node.managers.connection.ConnectionListener;
import com.automate.node.managers.message.MessageListener;
import com.automate.protocol.models.CommandArgument;

public interface ICommandManager extends IManager<CommandListener>, MessageListener, ConnectionListener, CommandListener {
	
	public int executeCommand(String commandName, List<CommandArgument<?>> commandArgs, long commandId);
	
}
