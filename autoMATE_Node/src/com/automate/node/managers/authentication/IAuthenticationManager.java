package com.automate.node.managers.authentication;

import com.automate.node.managers.IManager; 
import com.automate.node.managers.connection.ConnectionListener;
import com.automate.node.managers.discovery.DiscoveryListener;
import com.automate.node.managers.message.MessageListener;

public interface IAuthenticationManager extends IManager<AuthenticationListener>, MessageListener, ConnectionListener, AuthenticationListener, 
	DiscoveryListener{

	public void reconnect();

	boolean signIn(long nodeId, String password);
	
}
