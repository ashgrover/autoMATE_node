package com.automate.node.managers.authentication;

import com.automate.node.managers.IListener;

public interface AuthenticationListener extends IListener {

	public void onAuthenticating(long nodeId, String password);
	
	public void onAuthenticationFailure(long nodeId, String password);
	
	public void onAuthenticated(long nodeId, String password);
	
}
