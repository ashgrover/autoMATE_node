package com.automate.node.managers;

public interface IListenerBinder <T extends IListener> {

	public void bind(T listener);
	public void unbind(T listener);	
	
}
