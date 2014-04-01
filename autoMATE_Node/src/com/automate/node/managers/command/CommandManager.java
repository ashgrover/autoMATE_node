package com.automate.node.managers.command;

import java.util.List; 

import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;
import com.automate.node.managers.connection.IConnectionManager;
import com.automate.node.managers.message.IMessageManager;
import com.automate.node.utilities.FanGpioUtility;
import com.automate.protocol.Message;
import com.automate.protocol.client.ClientProtocolParameters;
import com.automate.protocol.models.CommandArgument;
import com.automate.protocol.server.ServerProtocolParameters;

public class CommandManager extends ManagerBase<CommandListener> implements ICommandManager {

	private IMessageManager messageManager;
	private IConnectionManager connectionManager;
	private FanGpioUtility fanGpioUtility;
	
	public CommandManager(IMessageManager messageManager, IConnectionManager connectionManager, FanGpioUtility fanGpioUtility) {
		super(CommandListener.class);
		this.messageManager = messageManager;
		this.connectionManager = connectionManager;
		this.fanGpioUtility = fanGpioUtility;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from MessageListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onMessageSent(Message<ClientProtocolParameters> message) {
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
	// Inherited from CommandListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onCommandReceived(String commandName,
			List<CommandArgument<?>> commandArgs, long commandId) {
		
		// calls executeCommand() - put in iCommandManager
		if (commandName.equalsIgnoreCase("power on")) {
			this.fanGpioUtility.setSpeedSlow();

		} else if (commandName.equalsIgnoreCase("power off")) {
			this.fanGpioUtility.setSpeedOff();

		} else if (commandName.equalsIgnoreCase("set speed")) {
			if (commandArgs.get(0).value == "Low") {
				this.fanGpioUtility.setSpeedSlow();
				
			} else if (commandArgs.get(0).value == "Medium") {
				this.fanGpioUtility.setSpeedMedium();
				
			} else if (commandArgs.get(0).value == "High") {
				this.fanGpioUtility.setSpeedFast();
			}
		}
		
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
	public void onCommandResult(long commandId, int responseCode,
			String responseMessage) {
		// TODO Auto-generated method stub
		
		synchronized(mListeners) {
			for (CommandListener listener : mListeners) {
				try {
					listener.onCommandResult(commandId, responseCode, responseMessage);
				} catch (RuntimeException e) {
					System.err.println("Error notifying listeners! " + e.getMessage());
					e.printStackTrace();
				}
			}
		};

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
	// Inherited from ICommandManager
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void executeCommand(String commandName, List<CommandArgument<?>> commandArgs, long commandId) {
		// TODO Auto-generated method stub
		
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
	protected void performInitialUpdate(CommandListener listener) {
		// TODO Auto-generated method stub

	}

}
