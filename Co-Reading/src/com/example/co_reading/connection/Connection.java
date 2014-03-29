/*Copyright (C) 2014  ElsonLee & WenPin Cui

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/	
package com.example.co_reading.connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.util.Log;

import com.example.co_reading.util.ISerialization;

public class Connection {
	
	private String TAG = Connection.class.getSimpleName();
	
	private ISerialization mSerialization = null;
	
	private InputStream mInputStream = null;
	
	private OutputStream mOutputStream = null;
	
	private ArrayList<INetworkListener> mListeners = new ArrayList<INetworkListener>();
	
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
	
	public void addListener (INetworkListener listener) {
		if (listener == null) throw new IllegalArgumentException("listener cannot be null.");
		
		if (mListeners.contains(listener) == true) {
			return;
		}
		
		mListeners.add(listener);
		
		Log.i(TAG, "Connection listener added: " + listener.getClass().getName());
	}

	public void removeListener (INetworkListener listener) {
		if (listener == null) throw new IllegalArgumentException("listener cannot be null.");

		if (mListeners.contains(listener) == false) {
			return;
		}
		
		mListeners.remove(listener);

		Log.i(TAG, "Connection listener removed: " + listener.getClass().getName());
	}
	
	protected void notifyConnected() {
		for (INetworkListener listenerIter : mListeners)
			listenerIter.onNetworkConnected();
	}
	
	protected void notifyDisconnected() {
		for (INetworkListener listenerIter : mListeners)
			listenerIter.onNetworkDisconnected();
	}	
	
	protected void notifyIdle() {
		/*
		for (INetworkListener listenerIter : mListeners)
			listenerIter.onNetworkDisconnected();
		*/
	}
	
	protected void notifyReceived(Object object) {
		for (INetworkListener listenerIter : mListeners)
			listenerIter.onNetworkReceivedObj(object);
	}
}
