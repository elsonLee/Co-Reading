package com.example.co_reading;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;

public class BtConnectThread extends Thread {
	
	private final String TAG = BtConnectThread.class.getSimpleName();
	
	private BluetoothSocket mSocket = null;
	
	private InputStream mInStream = null;
	
	private OutputStream mOutStream = null;
	
	public BtConnectThread(BluetoothSocket socket) {

		mSocket = socket;
		
		try {
			mInStream = mSocket.getInputStream();
			mOutStream = mSocket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		byte[] buffer = new byte[1024];
		int bytes;
		
		while (true) {
			try {
				bytes = mInStream.read(buffer);
				// handle
				Thread.yield();
			} catch (IOException e) {
				break;
			}
		}
		
	}
	
	public void write(byte[] bytes) {
		try {
			mOutStream.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cancel() {
		try {
			mSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
