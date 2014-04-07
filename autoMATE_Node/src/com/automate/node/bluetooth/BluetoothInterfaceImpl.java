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
		try {
			String url = "btspp://localhost:" + 
					new UUID(0x1101).toString() +
					";name=autoMATE-bluetooth-service";
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
