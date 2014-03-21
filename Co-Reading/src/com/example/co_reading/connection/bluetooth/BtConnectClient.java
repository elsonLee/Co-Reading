package com.example.co_reading.connection.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BtConnectClient {
	
	private String TAG = BtConnectClient.class.getSimpleName();
	
	private BluetoothDevice mDevice = null;
	private BluetoothSocket mSocket = null;
	private BtWorkThread mWorkThread = null;
	
	public BtConnectClient(BluetoothDevice device) throws IOException {
		
		BluetoothSocket tmpSocket = null;
		mDevice = device;
		
		try {
			tmpSocket = mDevice.createInsecureRfcommSocketToServiceRecord(BlueToothManager.m_UUID);
		} catch (IOException e) {
			throw new IOException();
		}
		
		if (tmpSocket != null) {
			mSocket = tmpSocket;
			try {
				mSocket.connect();
			} catch (IOException e) {
				try {
					mSocket.close();
				} catch (IOException e1) {
					throw new IOException();
				}
				throw new IOException();
			}
		}

		mWorkThread = new BtWorkThread(mSocket);
		mWorkThread.start();
	}
	
	public void write(byte[] bytes) {
		mWorkThread.write(bytes);
	}
	
	public void cancel() {
		try {
			mSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
