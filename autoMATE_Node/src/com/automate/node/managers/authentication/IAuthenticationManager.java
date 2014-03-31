package com.automate.node.managers.authentication;

import com.automate.node.managers.IManager; 
import com.automate.node.managers.connection.ConnectionListener;
import com.automate.node.managers.message.MessageListener;

public interface IAuthenticationManager extends IManager<AuthenticationListener>, MessageListener, ConnectionListener, AuthenticationListener {

	public boolean signIn(String username, String password);
	
	public boolean signOut();

	public void reconnect();
	
}
