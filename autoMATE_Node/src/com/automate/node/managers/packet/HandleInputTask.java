package com.automate.node.managers.packet;

public class HandleInputTask implements Runnable {

	private IPacketManager manager;
	private String message;

	public HandleInputTask(IPacketManager manager, String message) {
		this.manager = manager;
		this.message = message;
	}

	@Override
	public void run() {
		manager.onPacketReceived(message);
	}
	
	
}
