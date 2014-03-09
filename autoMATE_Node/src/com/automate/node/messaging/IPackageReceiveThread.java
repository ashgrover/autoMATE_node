package com.automate.node.messaging;

import com.automate.node.messaging.IMessageManager;

public interface IPackageReceiveThread {

	public abstract void setManager(IMessageManager manager);

	public abstract void cancel();

	public abstract void start();

}
