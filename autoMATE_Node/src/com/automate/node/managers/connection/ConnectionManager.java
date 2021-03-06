package com.automate.node.managers.connection;

import java.util.Timer;
import java.util.TimerTask;

import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;

public class ConnectionManager extends ManagerBase<ConnectionListener> implements IConnectionManager {

	public enum ConnectedState {
		CONNECTED,
		CONNECTING,
		DISCONNECTED
	}

	private ConnectedState connectedState;

	private Timer timer;

	private String sessionKey;

	public ConnectionManager() {
		super(ConnectionListener.class);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ConnectionListener
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onConnecting() {
		if(connectedState == ConnectedState.CONNECTING) return;
		this.connectedState = ConnectedState.CONNECTING;
		synchronized (mListeners) {
			for(ConnectionListener listener : mListeners) {
				try {
					listener.onConnecting();
				} catch (RuntimeException e) {
					System.out.println("Error notifying listener");
					e.printStackTrace();
				}
			}
		}
		System.out.println("--> Connecting to server...");
	}

	@Override
	public void onConnected(String sessionKey) {
		if(connectedState == ConnectedState.CONNECTED) return;
		this.connectedState = ConnectedState.CONNECTED;
		this.sessionKey = sessionKey;
		synchronized (mListeners) {
			for(ConnectionListener listener : mListeners) {
				try {
					listener.onConnected(sessionKey);
				} catch (RuntimeException e) {
					System.out.println("Error notifying listener");
					e.printStackTrace();
				}
			}
		}
		System.out.println("--> Connected to server!");
	}

	@Override
	public void onDisconnected() {
		if(connectedState == ConnectedState.DISCONNECTED) return;
		this.connectedState = ConnectedState.DISCONNECTED;
		this.sessionKey = null;
		synchronized (mListeners) {
			for(ConnectionListener listener : mListeners) {
				try {
					listener.onDisconnected();
				} catch (RuntimeException e) {
					System.out.println("Error notifying listener");
					e.printStackTrace();
				}
			}
		}
		System.out.println("--> Connecting lost.");
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
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				disconnect();
			}
		}, milis);
	}

	@Override
	public void disconnect() {
		this.onDisconnected();
	}

	@Override
	public String getSessionKey() {
		return sessionKey;
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
		timer = new Timer("Connection Timer");
		connectedState = ConnectedState.DISCONNECTED;
	}

	@Override
	protected void teardown() {
		timer.cancel();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ListenerBinder
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void performInitialUpdate(ConnectionListener listener) {
		switch (connectedState) {
		case CONNECTED:
			listener.onConnected(sessionKey);
			break;

		case CONNECTING:
			listener.onConnecting();
			break;

		case DISCONNECTED:
			listener.onDisconnected();
			break;

		default:
			break;
		}
	}

}
