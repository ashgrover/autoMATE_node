package com.automate.node.managers.warning;

import java.util.ArrayList;

import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;
import com.automate.node.managers.connection.ConnectionManager.ConnectedState;
import com.automate.node.managers.connection.ConnectionListener;
import com.automate.node.managers.connection.IConnectionManager;
import com.automate.node.managers.message.IMessageManager;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.node.messages.NodeWarningMessage;
import com.automate.protocol.server.ServerProtocolParameters;

public class WarningManager extends ManagerBase<WarningListener> implements IWarningManager {

	private IMessageManager messageManager;
	private IConnectionManager connectionManager;

	private ArrayList<String> warningQueue = new ArrayList<String>();
	
	private ConnectedState state;
	
	public WarningManager(IMessageManager messageManager, IConnectionManager connectionManager) {
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
	public void onConnecting() {}

	@Override
	public void onConnected(String sessionKey) {
		this.state = ConnectedState.CONNECTED;
		while(!warningQueue.isEmpty()) {
			String warning = warningQueue.remove(0);
			emitWarning(warning);
		}
	}

	@Override
	public void onDisconnected() {
		this.state = ConnectedState.DISCONNECTED;
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
		if(listenerClass.equals(ConnectionListener.class)) {
			this.onDisconnected();
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IWarningManager
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void emitWarning(String warning) {
		if(state == ConnectedState.DISCONNECTED) {
			this.warningQueue.add(warning);
		} else {
			NodeWarningMessage mWarning = new NodeWarningMessage(messageManager.getProtocolParameters(),0,warning);
			messageManager.sendMessage(mWarning);
		}
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
		this.state = ConnectedState.DISCONNECTED;
	}

	@Override
	protected void teardown() {
		this.warningQueue.clear();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ListenerBinder
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void performInitialUpdate(WarningListener listener) {
		
	}

}
