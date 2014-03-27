package com.example.co_reading.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.co_reading.util.ISerialization;

public class Connection {
	
	private String TAG = Connection.class.getSimpleName();
	
	//private BluetoothSocket mSocket = null; 
	
	private ISerialization mSerialization = null;
	
	private InputStream mInputStream = null;
	
	private OutputStream mOutputStream = null;
	
	private Listener[] mListeners = {};
	
	//private volatile boolean mIsConnected;
	
	public Connection(ISerialization serialization) {
		mSerialization = serialization;
	}

	public void Initialize(InputStream inputStream, OutputStream outputStream) {
		
		mInputStream = inputStream;
		mOutputStream = outputStream;
		
		mSerialization.initialize(mInputStream, mOutputStream);
	}
	
	/*
	public void close() {
		boolean wasConnected = mIsConnected;
		mIsConnected = false;
		try {
			mSocket.close();
		} catch (IOException e) {
			Log.e(TAG, "Close Socket error");
		}
		
		if (wasConnected == true) {
			notifyDisconnected();
		}

		setConnected(false); } void setConnected(boolean isConnected) { mIsConnected = isConnected; } 
	public boolean isConnected() {
		return mIsConnected;
	}
	*/
	
	public Object readObj() throws Exception {
		
		Object obj = mSerialization.read();

		return obj;
	}
	
	public void send(Object object) {
		
		mSerialization.write(object);
	}
	
	public void addListener (Listener listener) {
		if (listener == null) throw new IllegalArgumentException("listener cannot be null.");
		Listener[] listeners = mListeners;
		int n = listeners.length;
		for (int i = 0; i < n; i++)
			if (listener == listeners[i]) return;
		Listener[] newListeners = new Listener[n + 1];
		newListeners[0] = listener;
		System.arraycopy(listeners, 0, newListeners, 1, n);
		mListeners = newListeners;

		Log.i(TAG, "Connection listener added: " + listener.getClass().getName());
	}

	public void removeListener (Listener listener) {
		if (listener == null) throw new IllegalArgumentException("listener cannot be null.");
		Listener[] listeners = mListeners;
		int n = listeners.length;
		if (n == 0) return;
		Listener[] newListeners = new Listener[n - 1];
		for (int i = 0, ii = 0; i < n; i++) {
			Listener copyListener = listeners[i];
			if (listener == copyListener) continue;
			if (ii == n - 1) return;
			newListeners[ii++] = copyListener;
		}
		mListeners = newListeners;

		Log.i(TAG, "Connection listener removed: " + listener.getClass().getName());
	}
	
	protected void notifyConnected() {
		Listener[] listeners = mListeners;
		for (int i = 0, n = listeners.length; i < n; i++) {
			listeners[i].connected();
		}
	}
	
	protected void notifyDisconnected() {
		Listener[] listeners = mListeners;
		for (int i = 0, n = listeners.length; i < n; i++) {
			listeners[i].disconnected();
		}
	}	
	
	protected void notifyIdle() {
		Listener[] listeners = mListeners;
		for (int i = 0, n = listeners.length; i < n; i++) {
			listeners[i].idle();
		}
	}
	
	protected void notifyReceived(Object object) {
		Listener[] listeners = mListeners;
		for (int i = 0, n = listeners.length; i < n; i++) {
			listeners[i].received(object);
		}
	}
}