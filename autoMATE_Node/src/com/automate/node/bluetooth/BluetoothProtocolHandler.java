package com.automate.node.bluetooth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.microedition.io.StreamConnection;

import com.automate.node.managers.discovery.DiscoveryListener;


public class BluetoothProtocolHandler implements Runnable {

	private DiscoveryListener callback;
	private StreamConnection connection;

	public BluetoothProtocolHandler(StreamConnection connection, DiscoveryListener callback) {
		this.connection = connection;
		this.callback = callback;
	}

	@Override
	public void run() {
		try {
			OutputStream os = connection.openOutputStream();
			InputStream is = connection.openInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			PrintWriter writer = new PrintWriter(os);

			while(true) {
				String line = reader.readLine();
				if(line == null) return;
				System.out.println("Received bluetooth message: " + line);
				if(line.equals("initPairing")) {
					writer.println("ackInitPairing");
					callback.onPairRequest();
				} else if(line.equals("sendDeviceInfo")) {
					writer.println("deviceInfo(1,Desk Fan Prototype,1,0)");
					callback.onDeviceInformationSent();
				} else if(line.equals("pairingFailed")) {
					writer.println("ackPairingFailed");
					callback.onPairFailed();
				} else if(line.startsWith("wifiCreds")) {
					String [] parts = line.substring(10, line.length() - 1).split(",");
					writer.println("ackWifiCreds");
					callback.onWifiCredsProvided(parts[0], parts[1], parts[2]);
				} else if(line.startsWith("pairingSuccess")) {
					String [] parts = line.substring(15, line.length() - 1).split(",");
					writer.println("ackPairingSuccess");
					callback.onPairSuccess(Long.parseLong(parts[0]), parts[1]);
				}
				writer.flush();
			}
		} catch (IOException e) {
			System.err.println("Error in bluetooth protocol handler.");
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (IOException e) {}
		}

	}

}
