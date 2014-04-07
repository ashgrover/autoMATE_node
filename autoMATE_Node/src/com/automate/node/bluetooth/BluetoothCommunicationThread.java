package com.automate.node.bluetooth;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import com.automate.node.managers.discovery.DiscoveryListener;

public class BluetoothCommunicationThread extends Thread {

	private StreamConnectionNotifier service;

	private ExecutorService processThread;

	private boolean cancelled;

	private DiscoveryListener callback;
	
	public BluetoothCommunicationThread(StreamConnectionNotifier service, DiscoveryListener callback) {
		this.service = service;
		processThread = Executors.newSingleThreadExecutor();
		this.callback = callback;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while(!cancelled) {
			try {
				StreamConnection connection = service.acceptAndOpen();
				processThread.submit(new BluetoothProtocolHandler(connection, callback));
			} catch (IOException e) {
				System.err.println("Error in bluetooth thread.");
				e.printStackTrace();
			}
		}
	}
	
	public void cancel() {
		this.cancelled = true;
		try {
			service.close();
		} catch (IOException e) {}
	}
	
}
