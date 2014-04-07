package com.automate.node.managers.discovery;

import com.automate.node.managers.IManager;

public interface IDiscoveryManager extends IManager<DiscoveryListener>, DiscoveryListener {

	public void sendBroadcast();
	
}
