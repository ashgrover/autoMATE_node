package com.automate.node.managers.authentication;

import com.automate.node.managers.messagehandlers.IMessageHandler; 
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.server.messages.ServerAuthenticationMessage;

public class AuthenticationMessageHandler implements IMessageHandler<ServerAuthenticationMessage, String> {

	private IAuthenticationManager authenticationManager;

	public AuthenticationMessageHandler(IAuthenticationManager manager) {
		this.authenticationManager = manager;
	}

	@Override
	public Message<ClientProtocolParameters> handleMessage(ServerAuthenticationMessage message, String params) {
		int responseCode = message.responseCode;
		switch(responseCode) {
		case 200:
			String sessionKey = message.sessionKey;
			authenticationManager.onAuthenticated(Long.parseLong(message.username.substring(1)), params, sessionKey);
			break;
		default:
			authenticationManager.onAuthenticationFailure(Long.parseLong(message.username.substring(1)), params);
			break;
		}
		return null;
	}

}
