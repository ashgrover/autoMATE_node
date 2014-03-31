package com.automate.node.managers.connection;

import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;

public class ConnectionManager extends ManagerBase<ConnectionListener> implements IConnectionManager {

	public enum ConnectedState {
		CONNECTED,
		CONNECTING,
		DISCONNECTED
	}
	
	private ConnectedState connectedState;
	
	public ConnectionManager() {
		super(ConnectionListener.class);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ConnectionListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onConnecting() {
		for(ConnectionListener listener : mListeners) {
			try {
				listener.onConnecting();
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onConnected(String sessionKey) {
		for(ConnectionListener listener : mListeners) {
			try {
				listener.onConnected(sessionKey);
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDisconnected() {
		for(ConnectionListener listener : mListeners) {
			try {
				listener.onDisconnected();
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener");
				e.printStackTrace();
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IListener
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onBind(Class<? extends IListener> listenerClass) {
		//no bindings
	}

	@Override
	public void onUnbind(Class<? extends IListener> listenerClass) {
		//no bindings
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IConnectionManager
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void scheduleDisconnect(long milis) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ManagerBase
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void unbindSelf() {
		//no bindings
	}

	@Override
	protected void bindSelf() {
		// no bindings
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
	protected void performInitialUpdate(ConnectionListener listener) {
		// TODO Auto-generated method stub

	}

}
