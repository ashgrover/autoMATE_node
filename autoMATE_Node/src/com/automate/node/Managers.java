package com.automate.node;

import com.automate.node.managers.authentication.IAuthenticationManager;
import com.automate.node.managers.command.ICommandManager;
import com.automate.node.managers.connection.IConnectionManager;
import com.automate.node.managers.message.IMessageManager;
import com.automate.node.managers.packet.IPacketManager;
import com.automate.node.managers.status.IStatusManager;
import com.automate.node.managers.warning.IWarningManager;

public class Managers {

	public IPacketManager packetManager;
	public IConnectionManager connectionManager;
	public IMessageManager messageManager;
	public IAuthenticationManager authenticationManager;
	public ICommandManager commandManager;
	public IStatusManager statusManager;
	public IWarningManager warningManager;

	
}
