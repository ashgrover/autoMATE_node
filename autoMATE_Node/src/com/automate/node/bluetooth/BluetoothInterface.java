package com.automate.node.bluetooth;

import com.automate.node.managers.discovery.DiscoveryListener;

public interface BluetoothInterface {

	public void turnOffBluetooth();

	void turnOnBluetooth(DiscoveryListener callback);
	
}
