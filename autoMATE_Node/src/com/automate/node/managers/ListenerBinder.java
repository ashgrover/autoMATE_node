package com.automate.node.managers;

import java.util.ArrayList; 

public abstract class ListenerBinder <T extends IListener> implements IListenerBinder<T> {

	protected ArrayList<T> mListeners = new ArrayList<T>();
	protected boolean mBindAllowed;
	private final Class<T> mListenerClass;
	
	public ListenerBinder(Class<T> listenerClass) {
		this.mListenerClass = listenerClass;
	}
	
	public void bind(T listener) {
		if(mBindAllowed) {
			synchronized (mListeners) {
				if(listener != null) {
					mListeners.add(listener);
					performInitialUpdate(listener);
					listener.onBind(mListenerClass);
				} else {
					System.out.println(getClass().getName() + " - Attempt to add null listener object.");
				}
			}
		}
	}
	
	public void unbind(T listener) {
		if(mBindAllowed) {
			synchronized (mListeners) {
				if(listener != null) {
					listener.onUnbind(mListenerClass);
				} else {
					System.out.println(getClass().getName() + " - Attempt to remove null listener object.");
				}
			}
		}
	}
	
	protected void unbindAll() {
		synchronized (mListeners) {
			for(T listener : mListeners) {
				unbind(listener);
			}
			mListeners.clear();
		}
	}
	
	protected abstract void performInitialUpdate(T listener);
	
}
