package com.automate.node.managers;

public interface IManager<T extends IListener> extends IListenerBinder<T> {

	public void start();
	
	public void stop();
	
}
