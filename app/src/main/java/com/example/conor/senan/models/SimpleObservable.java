package com.example.conor.senan.models;

import java.util.ArrayList;

public class SimpleObservable<T> implements EasyObservable<T> {
	
	private final ArrayList<OnChangeListener<T>> listeners = new ArrayList<OnChangeListener<T>>();
	
	
	public void addListener(OnChangeListener<T> listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	public void removeListener(OnChangeListener<T> listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	protected void notifyListeners(final T model) {
		synchronized (listeners) {
			for (OnChangeListener<T> listener : listeners) {
				listener.onChange(model);
			}
		}
	}
	
}