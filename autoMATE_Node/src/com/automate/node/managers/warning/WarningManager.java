package com.automate.node.managers.warning;

import java.util.Set;

import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;
import com.automate.node.managers.authentication.AuthenticationListener;
import com.automate.node.managers.connection.IConnectionManager;
import com.automate.node.managers.message.IMessageManager;
import com.automate.node.managers.message.MessageManager;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.node.messages.NodeWarningMessage;
import com.automate.protocol.server.ServerProtocolParameters;

public class WarningManager extends ManagerBase<WarningListener> implements IWarningManager {

	private IMessageManager messageManager;
	private IConnectionManager connectionManager;
	
	
	private WarningManager(IMessageManager messageManager, IConnectionManager connectionManager) {
		super(WarningListener.class);
		this.messageManager = messageManager;
		this.connectionManager = connectionManager;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from MessageListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onMessageSet(Message<ClientProtocolParameters> message) {
		if(message instanceof NodeWarningMessage){
			onWarningEmitted(((NodeWarningMessage)message).message);
		}
		
	}

	@Override
	public void onMessageNotSent(Message<ClientProtocolParameters> message) {
		if(message instanceof NodeWarningMessage){
			emitWarning(((NodeWarningMessage)message).message);
		}
		
	}

	@Override
	public void onMessageReceived(Message<ServerProtocolParameters> message) {
		// TODO Auto-generated method stub

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ConnectionListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onConnecting() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(String sessionKey) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from WarningListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onWarningEmitted(String warning) {
		for(WarningListener listener : mListeners){
			try{
				listener.onWarningEmitted(warning);
			}
			catch(RuntimeException e){
				System.out.println(e.getStackTrace());
			}
		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IListener
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onBind(Class<? extends IListener> listenerClass) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnbind(Class<? extends IListener> listenerClass) {
		// TODO Auto-generated method stub

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IWarningManager
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void emitWarning(String warning) {
		// TODO Auto-generated method stub
		NodeWarningMessage mWarning;
		mWarning = new NodeWarningMessage(messageManager.getProtocolParameters(),0,warning);
		messageManager.sendMessage(mWarning);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ManagerBase
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void unbindSelf() {
		// TODO Auto-generated method stub
		this.messageManager.unbind(this);
		this.connectionManager.unbind(this);
	
	}

	@Override
	protected void bindSelf() {
		// TODO Auto-generated method stub
		this.messageManager.bind(this);
		this.connectionManager.bind(this);
	}

	@Override
	protected void setupInitialState() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void teardown() {
		// TODO Auto-generated method stub

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ListenerBinder
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void performInitialUpdate(WarningListener listener) {
		// TODO Auto-generated method stub
	
	}

}
