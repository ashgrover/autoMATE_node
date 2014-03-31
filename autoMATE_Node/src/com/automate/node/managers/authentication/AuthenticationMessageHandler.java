package com.automate.node.managers.authentication;

import com.automate.node.managers.messagehandlers.IMessageHandler;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.server.ServerProtocolParameters;
import com.automate.protocol.server.messages.ServerAuthenticationMessage;

public class AuthenticationMessageHandler implements
		IMessageHandler<ServerAuthenticationMessage, Void> {

	private IAuthenticationManager manager;

	public AuthenticationMessageHandler(IAuthenticationManager manager) {
		this.manager = manager;
	}

	@Override
	public Message<ClientProtocolParameters> handleMessage(ServerAuthenticationMessage message, Void params) {
		return null;
	}

}
