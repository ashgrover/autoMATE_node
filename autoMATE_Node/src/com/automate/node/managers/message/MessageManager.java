package com.automate.node.managers.message;

import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;
import com.automate.node.managers.connection.IConnectionManager;
import com.automate.node.managers.packet.IPacketManager;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.server.ServerProtocolParameters;

public class MessageManager extends ManagerBase<MessageListener> implements
		IMessageManager {

	private IPacketManager packetManager;
	private IConnectionManager connectionManager;
	
	public MessageManager(IPacketManager packetManager, IConnectionManager connectionManager) {
		super(MessageListener.class);
		this.packetManager = packetManager;
		this.connectionManager = connectionManager;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from PacketListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onPacketReceived(String packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEmptyPacketReceived() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveIoException() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNoSocketProvided() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveError() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPacketSent(int packetId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSendIoException(int packetId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSendNoServerAddress(int packetId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSendNoServerPort(int packetId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSendError(int packetId) {
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
	// Inherited from MessageListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onMessageSet(Message<ClientProtocolParameters> message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessageNotSent(Message<ClientProtocolParameters> message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessageReceived(Message<ServerProtocolParameters> message) {
		// TODO Auto-generated method stub

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
	// Inherited from IMessageManager
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void sendMessage(Message<ClientProtocolParameters> message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(Message<ClientProtocolParameters> message,
			MessageListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientProtocolParameters getProtocolParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ManagerBase
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void unbindSelf() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void bindSelf() {
		// TODO Auto-generated method stub

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
	protected void performInitialUpdate(MessageListener listener) {
		// TODO Auto-generated method stub

	}

}
