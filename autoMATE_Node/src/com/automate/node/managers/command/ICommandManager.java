package com.automate.node.managers.command;

import com.automate.node.managers.IManager;
import com.automate.node.managers.connection.ConnectionListener;
import com.automate.node.managers.message.MessageListener;

public interface ICommandManager extends IManager<CommandListener>, MessageListener, ConnectionListener, CommandListener {
	
}
