package com.example.co_reading.connection.bluetooth;

import java.io.IOException;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.co_reading.connection.Listener;
import com.example.co_reading.util.CoByteBuffer;
import com.example.co_reading.util.CoInputStream;
import com.example.co_reading.util.CoOutputStream;

public class BtConnection {
	
	private String TAG = BtConnection.class.getSimpleName();
	
	private BluetoothSocket mSocket = null; 
	
	private CoByteBuffer mWriteBuffer = null, mReadBuffer = null;

	private CoInputStream mInStream = null;
	
	private CoOutputStream mOutStream = null;
	
	private Listener[] mListeners = {};
	
	private Object mListenerLock = new Object();
	
	private Object mWriteLock = new Object();
	
	private int mCurObjLength;
	
	private volatile boolean mIsConnected;
	
	public void Initialize(BluetoothSocket socket, int writeBufferSize, int readBufferSize) {
		mCurObjLength = 0;
		mSocket = socket;
		try {
			mInStream = new CoInputStream(mSocket.getInputStream());
			mOutStream = new CoOutputStream(mSocket.getOutputStream());
		} catch (IOException e) {
			Log.e(TAG, "get Input/Output Sream error");
			return;
		}
		
		mWriteBuffer = new CoByteBuffer(writeBufferSize);
		mReadBuffer = new CoByteBuffer(readBufferSize);
		mReadBuffer.flip();
	}
	
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

		setConnected(false);
	}
	
	void setConnected(boolean isConnected) {
		mIsConnected = isConnected;
	}
	
	public boolean isConnected() {
		return mIsConnected;
	}
	
	/* FIXME: should be implemented in serialization class */
	private int getLenLength() {
		return 4;	// int
	}
	
	/* FIXME: should be implemented in serialization class */
	private int readLength(CoByteBuffer buffer) {
		return buffer.getInt();	 // header: length
	}
	
	public Object readObj() throws Exception {
		
		/* get current object length */
		if (mCurObjLength == 0) {
			
			int lenLength = getLenLength();		// FIXME: determined by serialization method
			if (mReadBuffer.remaining() < lenLength) {
				mReadBuffer.compact();
				try {
					int byteRead = mInStream.read(mReadBuffer);
					mReadBuffer.flip();
					if (byteRead == -1) throw new IOException("Connection is closed");
					
					// still no data satisfy
					if (mReadBuffer.remaining() < lenLength) return null;

				} catch (IOException e) {
					Log.e(TAG, "read to buffer error");
				}
			}
			
			// get package length
			mCurObjLength = mReadBuffer.getInt();	// FIXME: determined by serialization method
			
			if (mCurObjLength <= 0) throw new Exception("Invalid object length: " + mCurObjLength);
			if (mCurObjLength > mReadBuffer.capacity())
				throw new Exception("Unable to read object larger than read buffer: " + mCurObjLength);
		}
		
		// get package
		int length = mCurObjLength;
		if (mReadBuffer.remaining() < length) {
			mReadBuffer.compact();
			int byteRead = mInStream.read(mReadBuffer);
			mReadBuffer.flip();
			if (byteRead == -1) throw new IOException("Connection is closed");
			
			if (mReadBuffer.remaining() < length) return null;
		}
		
		mCurObjLength = 0;
		
		//int startPosition = mReadBuffer.position();
		//int oldLimit = mReadBuffer.limit();
		//mReadBuffer.limit();
		Object object = null;	// FIXME
		{
			// TODO: serialization mReadBuffer
		}
		
		return object;
	}
	
	private void writeToSocket() {
		
		CoByteBuffer buffer = mWriteBuffer;
		buffer.flip();
		
		while (buffer.hasRemaining()) {
			try {
				mOutStream.write(mWriteBuffer);
			} catch (IOException e) {
				Log.e(TAG, "Write to Socket error");
			}
		}
	}
	
	// public void send(byte[] sendData) {
	public int send(Object object) {
		
		synchronized (mWriteLock) {
			int start = mWriteBuffer.position();
			int lenLength = this.getLenLength();	// FIXME: should be get through serialization method
			
			// serialization.write(mWriteBuffer, object);	// TODO
			
			int end = mWriteBuffer.position();
			
			// write data length
			mWriteBuffer.position(start);
			// serialization.writeLength(mWriteBuffer, end - lenLength - start);	// TODO
			mWriteBuffer.position(end);
			
			
			writeToSocket();
			
			return end - start;
		}
	}
	
	public void addListener (Listener listener) {
		if (listener == null) throw new IllegalArgumentException("listener cannot be null.");
		synchronized (mListenerLock) {
			Listener[] listeners = mListeners;
			int n = listeners.length;
			for (int i = 0; i < n; i++)
				if (listener == listeners[i]) return;
			Listener[] newListeners = new Listener[n + 1];
			newListeners[0] = listener;
			System.arraycopy(listeners, 0, newListeners, 1, n);
			mListeners = newListeners;
		}

		Log.i(TAG, "Connection listener added: " + listener.getClass().getName());
	}

	public void removeListener (Listener listener) {
		if (listener == null) throw new IllegalArgumentException("listener cannot be null.");
		synchronized (mListenerLock) {
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
		}

		Log.i(TAG, "Connection listener removed: " + listener.getClass().getName());
	}
	
	void notifyConnected() {
		Listener[] listeners = mListeners;
		for (int i = 0, n = listeners.length; i < n; i++) {
			listeners[i].connected();
		}
	}
	
	void notifyDisconnected() {
		Listener[] listeners = mListeners;
		for (int i = 0, n = listeners.length; i < n; i++) {
			listeners[i].disconnected();
		}
	}	
	
	void notifyIdle() {
		Listener[] listeners = mListeners;
		for (int i = 0, n = listeners.length; i < n; i++) {
			listeners[i].idle();
		}
	}
	
	void notifyReceived(Object object) {
		Listener[] listeners = mListeners;
		for (int i = 0, n = listeners.length; i < n; i++) {
			listeners[i].received(object);
		}
	}
}
