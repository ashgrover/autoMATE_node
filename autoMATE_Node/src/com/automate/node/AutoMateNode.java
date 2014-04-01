package com.automate.node;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.automate.node.managers.authentication.AuthenticationManager;
import com.automate.node.managers.authentication.IAuthenticationManager;
import com.automate.node.managers.command.CommandManager;
import com.automate.node.managers.command.ICommandManager;
import com.automate.node.managers.connection.ConnectionManager;
import com.automate.node.managers.connection.IConnectionManager;
import com.automate.node.managers.message.IMessageManager;
import com.automate.node.managers.message.MessageManager;
import com.automate.node.managers.packet.IPacketManager;
import com.automate.node.managers.packet.IncomingPacketListenerThread;
import com.automate.node.managers.packet.PacketManager;
import com.automate.node.managers.status.IStatusManager;
import com.automate.node.managers.status.StatusManager;
import com.automate.node.managers.warning.IWarningManager;
import com.automate.node.managers.warning.WarningManager;
import com.automate.node.utilities.FanGpioInterface;
import com.automate.protocol.IncomingMessageParser;
import com.automate.protocol.Message;
import com.automate.protocol.Message.MessageType;
import com.automate.protocol.MessageSubParser;
import com.automate.protocol.server.ServerProtocolParameters;
import com.automate.protocol.server.subParsers.ServerAuthenticationMessageSubParser;
import com.automate.protocol.server.subParsers.ServerNodeCommandMessageSubParser;
import com.automate.protocol.server.subParsers.ServerNodeStatusUpdateMessageSubParser;
import com.automate.protocol.server.subParsers.ServerPingMessageSubParser;

public class AutoMateNode {

	private Properties configurationProperties;
	private boolean started;
	private Managers managers;
	private FanGpioInterface gpioUtility;

	public AutoMateNode(Properties configurationProperties) {
		this.configurationProperties = configurationProperties;
	}

	public void start() throws InitializationException {
		if(!started) {
			initSubsystems();
			started = true;
		} else {
			throw new IllegalStateException("Node cannot be started twice.");
		}
	}

	public void stop() {
		if(started) {
			terminateSubsystems();
			//gpioUtility.setSpeedOff();
			started = false;
		} else {
			throw new IllegalStateException("Node cannot be stopped twice.");
		}
	}

	private void initSubsystems() throws InitializationException {
		try {
			this.managers = new Managers();
			/*
			this.gpioUtility = new FanGpioUtility();
			gpioUtility.setSpeedOff();
			*/
			managers.packetManager = createPacketManager();
			managers.connectionManager = createConnectionManager();
			managers.messageManager = createMessageManager();
			managers.authenticationManager = createAuthenticationManager();
			managers.commandManager = createCommandManager();
			managers.statusManager = createStatusManager();
			managers.warningManager = createWarningManager();

			managers.packetManager.start();
			managers.connectionManager.start();
			managers.messageManager.start();
			managers.authenticationManager.start();
			managers.commandManager.start();
			managers.statusManager.start();
			managers.warningManager.start();
		} catch (RuntimeException e) {
			throw new InitializationException(e);
		}
	}

	private IWarningManager createWarningManager() {
		return new WarningManager(managers.messageManager, managers.connectionManager);
	}

	private IStatusManager createStatusManager() {
		return new StatusManager(managers.messageManager, gpioUtility);
	}

	private ICommandManager createCommandManager() {
		return new CommandManager(managers.messageManager, managers.connectionManager, gpioUtility);
	}

	private IAuthenticationManager createAuthenticationManager() {
		long nodeId = -1;
		String password = null;
		File credentialsFile = new File("resources/credentials.properties");
		if(credentialsFile.exists()) {
			Properties credentials = new Properties();
			try {
				credentials.load(new FileInputStream(credentialsFile));
				nodeId = Long.parseLong(credentials.getProperty("credentials.nodeId"));
				password = credentials.getProperty("credentials.password");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new AuthenticationManager(managers.messageManager, managers.connectionManager, nodeId, password);
	}

	private IMessageManager createMessageManager() {
		int majorVersion = Integer.parseInt(configurationProperties.getProperty("version.major"));
		int minorVersion = Integer.parseInt(configurationProperties.getProperty("version.minor"));
		IncomingMessageParser<ServerProtocolParameters> incomingMessageParser = new IncomingMessageParser<ServerProtocolParameters>(
				createMessageSubParsers());
		return new MessageManager(managers.packetManager, managers.connectionManager, incomingMessageParser, majorVersion, minorVersion);
	}

	private HashMap<String, MessageSubParser<? extends Message<ServerProtocolParameters>, ServerProtocolParameters>> createMessageSubParsers() {
		HashMap<String, MessageSubParser<? extends Message<ServerProtocolParameters>, ServerProtocolParameters>> subParsers = 
				new HashMap<String, MessageSubParser<? extends Message<ServerProtocolParameters>,ServerProtocolParameters>>();
		
		subParsers.put(MessageType.AUTHENTICATION.toString(), new ServerAuthenticationMessageSubParser());
		subParsers.put(MessageType.COMMAND_CLIENT.toString(), new ServerNodeCommandMessageSubParser());
		subParsers.put(MessageType.PING.toString(), new ServerPingMessageSubParser());
		subParsers.put(MessageType.STATUS_UPDATE_CLIENT.toString(), new ServerNodeStatusUpdateMessageSubParser());
		
		return subParsers;
	}

	private IConnectionManager createConnectionManager() {
		return new ConnectionManager();
	}

	private IPacketManager createPacketManager() {
		ExecutorService threadpool = Executors.newFixedThreadPool(Integer.parseInt(configurationProperties.getProperty("receive.threads")));
		IncomingPacketListenerThread listenerThread = new IncomingPacketListenerThread(threadpool);
		ExecutorService packetSendThreadpool  = Executors.newFixedThreadPool(Integer.parseInt(configurationProperties.getProperty("send.threads")));;
		String serverAddress = configurationProperties.getProperty("server.address");
		int serverPort = Integer.parseInt(configurationProperties.getProperty("server.port"));
		return new PacketManager(listenerThread, packetSendThreadpool, serverAddress, serverPort);
	}

	private void terminateSubsystems() {
		managers.warningManager.stop();
		managers.statusManager.stop();
		managers.commandManager.stop();
		managers.authenticationManager.stop();
		managers.messageManager.stop();
		managers.connectionManager.stop();
		managers.packetManager.stop();
		
		this.managers = null;
	}

}
