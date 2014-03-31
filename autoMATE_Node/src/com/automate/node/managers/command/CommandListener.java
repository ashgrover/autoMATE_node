package com.automate.node.managers.command;

import java.util.List;

import com.automate.node.managers.IListener;
import com.automate.protocol.models.CommandArgument;

public interface CommandListener extends IListener {

	public void onCommandReceived(String commandName, List<CommandArgument<?>> commandArgs, long commandId);
	
	public void onCommandResult(long commandId, int responseCode, String responseMessage);
	
}
