package com.automate.node.managers.packet;

import java.net.Socket;
import java.util.concurrent.ExecutorService;

import com.automate.node.managers.IListener;
import com.automate.node.managers.ManagerBase;

public class PacketManager extends ManagerBase<PacketListener> implements
IPacketManager {

	private IncomingPacketListenerThread listenerThread;
	private ExecutorService packetSendThreadpool;

	private int nextPacketId = 0;
	private final Object packetIdLock = new Object();

	private String serverAddress;
	private int serverPort;
	
	public PacketManager(IncomingPacketListenerThread listenerThread, ExecutorService packetSendThreadpool, String serverAddress, int serverPort) {
		super(PacketListener.class);
		this.listenerThread = listenerThread;
		this.packetSendThreadpool = packetSendThreadpool;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from PacketListener
	/////////////////////////////////////////////////////////////////////////////////////////////////


	@Override
	public void onPacketReceived(String packet) {
		for(PacketListener listener : mListeners) {
			try {
				listener.onPacketReceived(packet);
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onEmptyPacketReceived() {
		for(PacketListener listener : mListeners) {
			try {
				listener.onEmptyPacketReceived();
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onReceiveIoException() {
		for(PacketListener listener : mListeners) {
			try {
				listener.onReceiveIoException();
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onNoSocketProvided() {
		for(PacketListener listener : mListeners) {
			try {
				listener.onNoSocketProvided();
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onReceiveError() {
		for(PacketListener listener : mListeners) {
			try {
				listener.onReceiveError();
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onPacketSent(int packetId) {
		for(PacketListener listener : mListeners) {
			try {
				listener.onPacketSent(packetId);
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onSendIoException(int packetId) {
		for(PacketListener listener : mListeners) {
			try {
				listener.onSendIoException(packetId);
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onSendNoServerAddress(int packetId) {
		for(PacketListener listener : mListeners) {
			try {
				listener.onSendNoServerAddress(packetId);
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onSendNoServerPort(int packetId) {
		for(PacketListener listener : mListeners) {
			try {
				listener.onSendNoServerPort(packetId);
			} catch (RuntimeException e) {
				System.out.println("Error notifying listener");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onSendError(int packetId) {
		for(PacketListener listener : mListeners) {
			try {
				listener.onSendError(packetId);
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
		// no bindings
	}

	@Override
	public void onUnbind(Class<? extends IListener> listenerClass) {
		// no bindings
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from IPacketManager
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public long sendPacket(String packet) {
		int packetId;
		synchronized (packetIdLock) {
			packetId = nextPacketId++;
		}
		packetSendThreadpool.submit(new PacketSendTask(packet, packetId, serverAddress, serverPort, this));
		return packetId;
	}

	@Override
	public void setReceiveSocket(Socket receiveSocket) {
		this.listenerThread.setSocket(receiveSocket);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ManagerBase
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void unbindSelf() {
		// no bindings
	}

	@Override
	protected void bindSelf() {
		// no bindings
	}

	@Override
	protected void setupInitialState() {
		this.listenerThread.start();
	}

	@Override
	protected void teardown() {
		this.listenerThread.cancel();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Inherited from ListenerBinder
	/////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void performInitialUpdate(PacketListener listener) {}
}
