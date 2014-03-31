package com.automate.node.messaging.deprecated;

import java.net.Socket;

import com.automate.node.messaging.deprecated.IMessageManager;

public interface IPackageReceiveThread {

	public abstract void setManager(IMessageManager manager);

	public abstract void cancel();

	public abstract void start();

	public abstract void newSocketAvailable(Socket socket);

	void onDisconnected();

}
