package com.automate.node.managers.command;

import java.util.List;  

import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;
import com.automate.node.managers.connection.ConnectionManager;
import com.automate.node.managers.connection.IConnectionManager;
import com.automate.node.managers.message.IMessageManager;
import com.automate.node.utilities.FanGpioInterface;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.models.CommandArgument;
import com.automate.protocol.models.Type;
import com.automate.protocol.node.messages.NodeCommandMessage;
import com.automate.protocol.server.ServerProtocolParameters;
import com.automate.protocol.server.messages.ServerNodeCommandMessage;

public class CommandManager extends ManagerBase<CommandListener> implements ICommandManager {

	private IMessageManager messageManager;
	private IConnectionManager connectionManager;
	private FanGpioInterface fanGpioUtility;
	
	private CommandMessageHandler messageHandler;

	public CommandManager(IMessageManager messageManager, IConnectionManager connectionManager, FanGpioInterface fanGpioUtility) {
		super(CommandListener.class);
		this.messageManager = messageManager;
		this.connectionManager = connectionManager;
		this.fanGpioUtility = fanGpioUtility;
		
		this.messageHandler = new CommandMessageHandler(this, messageManager);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from MessageListener
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onMessageSent(Message<ClientProtocolParameters> message) {
		if(message instanceof NodeCommandMessage) {
			long commandId = ((NodeCommandMessage) message).commandId;
			int responseCode = ((NodeCommandMessage) message).responseCode;
			String responseMessage = ((NodeCommandMessage) message).message;
			onCommandResult(commandId, responseCode, responseMessage);
		}
	}
	public void onMessageNotSent(Message<ClientProtocolParameters> message) {}

	@Override
	public void onMessageReceived(Message<ServerProtocolParameters> message) {
		if(message instanceof ServerNodeCommandMessage) {
			Message<ClientProtocolParameters> response = messageHandler.handleMessage((ServerNodeCommandMessage) message, null);
			if(response != null) {
				messageManager.sendMessage(response);
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ConnectionListener
	/////////////////////////////////////////////////////////////////////////////////////////////////

	public void onConnecting() {}
	public void onConnected(String sessionKey) {}

	@Override
	public void onDisconnected() {
		this.fanGpioUtility.setSpeedOff(); // turn off when connection is lost
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from CommandListener
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onCommandReceived(String commandName, List<CommandArgument<?>> commandArgs, long commandId) {
		synchronized(mListeners) {
			for (CommandListener listener : mListeners) {
				try {
					listener.onCommandReceived(commandName, commandArgs, commandId);
				} catch (RuntimeException e) {
					System.err.println("Error notifying listeners! " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onCommandResult(long commandId, int responseCode, String responseMessage) {
		synchronized(mListeners) {
			for (CommandListener listener : mListeners) {
				try {
					listener.onCommandResult(commandId, responseCode, responseMessage);
				} catch (RuntimeException e) {
					System.err.println("Error notifying listeners! " + e.getMessage());
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
		if(listenerClass.equals(ConnectionManager.class)) {
			this.onDisconnected();
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ICommandManager
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public int executeCommand(String commandName, List<CommandArgument<?>> commandArgs, long commandId) {
		if (commandName.equalsIgnoreCase("power on")) {
			this.fanGpioUtility.setSpeedSlow();
			return 200; // OK

		} else if (commandName.equalsIgnoreCase("power off")) {
			this.fanGpioUtility.setSpeedOff();
			return 200; // OK

		} else if (commandName.equalsIgnoreCase("set speed")) {
			if(commandArgs.size() == 1) {
				if(commandArgs.get(0).type == Type.STRING) {
					@SuppressWarnings("unchecked")
					CommandArgument<String> speedArgument = (CommandArgument<String>) commandArgs.get(0);
					if(speedArgument.name.equalsIgnoreCase("speed")) {
						if (commandArgs.get(0).value.equals("Low")) { // COMMENT: must use equals method.  "==" will compare object reference id
							this.fanGpioUtility.setSpeedSlow();
							return 200; // OK
						} else if (commandArgs.get(0).value.equals("Medium")) {
							this.fanGpioUtility.setSpeedMedium();
							return 200; // OK
						} else if (commandArgs.get(0).value.equals("High")) {
							this.fanGpioUtility.setSpeedFast();
							return 200; // OK
						}
					}
				}
			}
			return 402; // INVALID_ARGS
		} else {
			return 401; // INVALID_COMMAND
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ManagerBase
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void unbindSelf() {
		this.messageManager.unbind(this);
		this.connectionManager.unbind(this);
	}

	@Override
	protected void bindSelf() {
		this.messageManager.bind(this);
		this.connectionManager.bind(this);
	}

	protected void setupInitialState() {}
	protected void teardown() {}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ListenerBinder
	/////////////////////////////////////////////////////////////////////////////////////////////////

	protected void performInitialUpdate(CommandListener listener) {}

}
