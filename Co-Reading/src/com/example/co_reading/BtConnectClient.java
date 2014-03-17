package com.example.co_reading;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BtConnectClient implements Runnable {
	
	private final String TAG = BtConnectClient.class.getSimpleName();
	
	// private final UUID mUUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
	
	private BluetoothSocket mSocket = null;
	
	private BluetoothDevice mDevice = null;
	
	private Thread mWriteThread;
	
	private InputStream mInStream;
	
	private OutputStream mOutStream;
	
	public BtConnectClient(BluetoothDevice device) {
		// BluetoothSocket tmpSocket = null;
		mDevice = device;
		
		/*
		try {
			tmpSocket = device
					.createInsecureRfcommSocketToServiceRecord(BlueToothManager.m_UUID);
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		
		mSocket = tmpSocket;
		*/
	}
	
	public void connect() {
		if (mWriteThread != null) {
			throw new IllegalStateException("BtConnectClient objects are not reuseable");
		}
		mWriteThread = new Thread(this);
		mWriteThread.start();
	}
	
	@Override
	public void run() {
		
		// TODO: cancel discovery
		
		try {
			if (mSocket == null) {
				mSocket = mDevice.createInsecureRfcommSocketToServiceRecord(BlueToothManager.m_UUID);
			} else if (mDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
				throw new IOException();
			}
			
			if (!mSocket.isConnected()) {
				mSocket.connect();
			}
			
			mInStream = mSocket.getInputStream();
			mOutStream = mSocket.getOutputStream();

		} catch (IOException e) {
			e.printStackTrace();
			try {
				if (mSocket.isConnected())
					mSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		// TODO: manageConnectedSocket
	}
	
	public void cancel() {
		try {
			mSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
