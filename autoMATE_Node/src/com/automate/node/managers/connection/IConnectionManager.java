package com.automate.node.managers.connection;

import com.automate.node.managers.IManager;

public interface IConnectionManager extends IManager<ConnectionListener>, ConnectionListener {

	public void scheduleDisconnect(long milis);

	public void disconnect();
	
}
