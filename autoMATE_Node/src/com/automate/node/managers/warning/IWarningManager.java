package com.automate.node.managers.warning;

import com.automate.node.managers.IManager;
import com.automate.node.managers.connection.ConnectionListener;
import com.automate.node.managers.message.MessageListener;

public interface IWarningManager extends IManager<WarningListener>, MessageListener, ConnectionListener, WarningListener {

	public void emitWarning(String warning);
	
}
