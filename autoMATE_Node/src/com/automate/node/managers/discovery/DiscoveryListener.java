package com.automate.node.managers.discovery;

import com.automate.node.managers.IListener;

public interface DiscoveryListener extends IListener {

	public void onBroadcastSent();

	public void onPairRequest();
	
	public void onDeviceInformationSent();
	
	public void onPairFailed();
	
	public void onPairSuccess(long nodeId, String password);

	public void onWifiCredsProvided(String ssid, String username, String passphrase);
	
}
