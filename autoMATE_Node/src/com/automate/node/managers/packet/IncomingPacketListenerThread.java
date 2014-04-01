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
							if(cancelled) return;
						} catch (InterruptedException e) {}
					}
				}
				while(true) {
					String line;
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					StringBuilder sb = new StringBuilder();
					while(true) {
						line = reader.readLine();
						if(line == null) throw new IOException();
						else if(line.equals("\0")) break;
						sb.append(line);
						sb.append('\n');
					}
					if(sb.length() > 0) {
						threadpool.submit(new HandleInputTask(manager, sb.toString()));
					}
				}
			} catch(IOException e) {
				if(socket != null && !socket.isClosed()) {
					try {
						socket.close();
					} catch (IOException e1) {}
					socket = null;
				}
				manager.onReceiveIoException();
			} catch(Exception e) {
				if(socket != null && !socket.isClosed()) {
					try {
						socket.close();
					} catch (IOException e1) {
						manager.onReceiveError();
					}
					socket = null;
					e.printStackTrace();
				}
			}
		}
	}

	public void cancel() {
		cancelled = true;
		if(socket != null) {
			try {
				socket.close();
			} catch (IOException e) {}
			socket = null;
		}
		threadpool.shutdown();
		synchronized (socketLock) {
			socketLock.notify();
		}
	}
}
