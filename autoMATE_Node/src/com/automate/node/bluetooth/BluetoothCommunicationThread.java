package com.automate.node.bluetooth;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import com.automate.node.managers.discovery.DiscoveryListener;

public class BluetoothCommunicationThread extends Thread {

	private StreamConnectionNotifier service;

	private boolean cancelled;

	private DiscoveryListener callback;

	private StreamConnection connection;
	
	public BluetoothCommunicationThread(StreamConnectionNotifier service, DiscoveryListener callback) {
		this.service = service;
		this.callback = callback;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while(!cancelled) {
			try {
				System.out.println("acceptAndOpen");
				connection = service.acceptAndOpen();
				new BluetoothProtocolHandler(connection, callback).run();
			} catch (InterruptedIOException e) {
				break;
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
			connection.close();
		} catch (Exception e) {}
	}
	
}
