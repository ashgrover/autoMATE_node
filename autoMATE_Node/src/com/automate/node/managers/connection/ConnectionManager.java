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
	}

	@Override
	public void onConnected(String sessionKey) {
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
	}

	@Override
	public void onDisconnected() {
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
