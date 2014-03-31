package com.automate.node.managers.message;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;
import com.automate.node.managers.connection.ConnectionListener;
import com.automate.node.managers.connection.IConnectionManager;
import com.automate.node.managers.packet.IPacketManager;
import com.automate.protocol.IncomingMessageParser;
import com.automate.protocol.Message;
import com.automate.protocol.MessageFormatException;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.server.ServerProtocolParameters;
import com.automate.util.xml.XmlFormatException;

public class MessageManager extends ManagerBase<MessageListener> implements
IMessageManager {

	private IPacketManager packetManager;
	private IConnectionManager connectionManager;
	private IncomingMessageParser<ServerProtocolParameters> incomingMessageParser;
	private HashMap<Integer, Message<ClientProtocolParameters>> messageHistory = new HashMap<Integer, Message<ClientProtocolParameters>>();
	private String sessionKey;
	private int majorVersion;
	private int minorVersion;

	public MessageManager(IPacketManager packetManager, IConnectionManager connectionManager, 
			IncomingMessageParser<ServerProtocolParameters> incomingMessageParser, int majorVersion, int minorVersion) {
		super(MessageListener.class);
		this.packetManager = packetManager;
		this.connectionManager = connectionManager;
		this.incomingMessageParser = incomingMessageParser;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from PacketListener
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onPacketReceived(String packet) {
		try {
			Message<ServerProtocolParameters> message = incomingMessageParser.parse(packet);
			onMessageReceived(message);
		} catch (XmlFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessageFormatException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void onEmptyPacketReceived() {}
	public void onReceiveIoException() {}
	public void onNoSocketProvided() {}
	public void onReceiveError() {}

	@Override
	public void onPacketSent(int packetId) {
		Message<ClientProtocolParameters> message = messageHistory.remove(packetId);
		if(message != null) {
			onMessageSet(message);
		}
	}

	@Override
	public void onSendIoException(int packetId) {
		Message<ClientProtocolParameters> message = messageHistory.remove(packetId);
		if(message != null) {
			onMessageNotSent(message);
		}
	}

	@Override
	public void onSendNoServerAddress(int packetId) {
		Message<ClientProtocolParameters> message = messageHistory.remove(packetId);
		if(message != null) {
			onMessageNotSent(message);
		}
	}

	@Override
	public void onSendNoServerPort(int packetId) {
		Message<ClientProtocolParameters> message = messageHistory.remove(packetId);
		if(message != null) {
			onMessageNotSent(message);
		}
	}

	@Override
	public void onSendError(int packetId) {
		Message<ClientProtocolParameters> message = messageHistory.remove(packetId);
		if(message != null) {
			onMessageNotSent(message);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ConnectionListener
	/////////////////////////////////////////////////////////////////////////////////////////////////

	public void onConnecting() {}

	@Override
	public void onConnected(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	@Override
	public void onDisconnected() {
		this.sessionKey = null;
		packetManager.setReceiveSocket(null);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from MessageListener
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onMessageSet(Message<ClientProtocolParameters> message) {
		synchronized (mListeners) {
			for(MessageListener listener: mListeners){
				try {
					listener.onMessageSet(message);
				} catch (RuntimeException e) {
					System.out.println("Error notifying listener.");
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onMessageNotSent(Message<ClientProtocolParameters> message) {
		synchronized (mListeners) {
			for(MessageListener listener: mListeners){
				try {
					listener.onMessageNotSent(message);
				} catch (RuntimeException e) {
					System.out.println("Error notifying listener.");
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onMessageReceived(Message<ServerProtocolParameters> message) {
		synchronized (mListeners) {
			for(MessageListener listener: mListeners){
				try {
					listener.onMessageReceived(message);
				} catch (RuntimeException e) {
					System.out.println("Error notifying listener.");
					e.printStackTrace();
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IListener
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onBind(Class<? extends IListener> listenerClass) {}

	@Override
	public void onUnbind(Class<? extends IListener> listenerClass) {
		if(listenerClass.equals(ConnectionListener.class)) {
			onDisconnected();
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IMessageManager
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void sendMessage(Message<ClientProtocolParameters> message) {
		if(sessionKey == null) {
			onMessageNotSent(message);
			return;
		}
		StringBuilder xmlBuilder = new StringBuilder();
		try {
			message.toXml(xmlBuilder, 0);
			messageHistory.put(packetManager.sendPacket(xmlBuilder.toString()), message);
		} catch (Exception e) {
			onMessageNotSent(message);
		}
	}

	@Override
	public void sendMessage(Message<ClientProtocolParameters> message, MessageListener listener) {
		throw new UnsupportedOperationException("Implement this when it is needed.");
	}

	@Override
	public ClientProtocolParameters getProtocolParameters() {
		return new ClientProtocolParameters(majorVersion, minorVersion, sessionKey);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ManagerBase
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void unbindSelf() {
		this.connectionManager.unbind(this);
		this.packetManager.unbind(this);

	}

	@Override
	protected void bindSelf() {
		this.packetManager.bind(this);
		this.connectionManager.bind(this);

	}

	@Override
	protected void setupInitialState() {
		this.sessionKey = null;
	}

	@Override
	protected void teardown() {
		this.messageHistory.clear();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ListenerBinder
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void performInitialUpdate(MessageListener listener) {}

}
