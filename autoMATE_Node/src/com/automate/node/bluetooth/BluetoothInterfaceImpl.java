package com.automate.node.bluetooth;

import java.io.IOException;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnectionNotifier;

import com.automate.node.managers.discovery.DiscoveryListener;

public class BluetoothInterfaceImpl implements BluetoothInterface {

	private BluetoothCommunicationThread communicationThread;
	
	@Override
	public void turnOnBluetooth(DiscoveryListener callback) {
		StreamConnectionNotifier service = null;
		LocalDevice local = null;
		try {
			local = LocalDevice.getLocalDevice();
			local.setDiscoverable(DiscoveryAgent.GIAC);
			UUID uuid = new UUID("04c6093b00001000800000805f9b34fb", false);
			
			String url = "btspp://localhost:" + uuid.toString() + ";name=autoMATE-bluetooth-service";
			LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
			service = (StreamConnectionNotifier) Connector.open(url);
			this.communicationThread = new BluetoothCommunicationThread(service, callback);
			communicationThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void turnOffBluetooth() {
		communicationThread.cancel();
	}

}
