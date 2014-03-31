package com.automate.node.managers.packet;

import java.io.BufferedReader;  
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class IncomingPacketListenerThread extends Thread {

	private ExecutorService threadpool;
	private boolean cancelled;
	private IPacketManager manager;
	private Socket socket;
	private final Object socketLock = new Object();

	public IncomingPacketListenerThread(ExecutorService threadpool) {
		super("PackageReceiveThread");
		this.threadpool = threadpool;
	}

	/**
	 * @param manager the manager to set
	 */
	public void setManager(IPacketManager manager) {
		this.manager = manager;
	}

	public void setSocket(Socket socket) {
		if(cancelled) throw new IllegalStateException("Cannot set a socket after listen thread cancelled.");
		synchronized (socketLock) {
			if(this.socket != null && !this.socket.isClosed()) {
				try {
					this.socket.close();
				} catch (IOException e) {}
			}
			this.socket = socket;
			socketLock.notify();
		}
	}
	@Override
	public void run() {
		while(!cancelled) {
			try {
				synchronized (socketLock) {
					if(socket == null) {
						try {
							socketLock.wait();
						} catch (InterruptedException e) {}
					}
				}
				while(true) {
					String line;
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					StringBuilder sb = new StringBuilder();
					while(!(line = reader.readLine()).equals("\0")) {
						sb.append(line);
						sb.append('\n');
					}
					threadpool.submit(new HandleInputTask(manager, sb.toString()));
				}
			} catch(IOException e) {
				if(socket != null && !socket.isClosed()) {
					try {
						socket = null;
						socket.close();
					} catch (IOException e1) {
						manager.onReceiveIoException();
					}
				}
			} catch(Exception e) {
				if(socket != null && !socket.isClosed()) {
					try {
						socket = null;
						socket.close();
					} catch (IOException e1) {
						manager.onReceiveError();
					}
				}
			}
		}
	}

	public void cancel() {
		if(socket != null) {
			try {
				cancelled = true;
				socket = null;
				socket.close();
				threadpool.shutdown();
			} catch (IOException e) {}
		}
	}
}
