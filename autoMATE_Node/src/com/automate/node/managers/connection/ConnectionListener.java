package com.automate.node.managers.connection;

import com.automate.node.managers.IListener;

public interface ConnectionListener extends IListener {

	public void onConnecting();
	
	public void onConnected(String sessionKey);
	
	public void onDisconnected();
	
}
