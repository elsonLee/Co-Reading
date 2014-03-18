package com.example.co_reading;

import java.io.IOException;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BtConnectClient implements Runnable {
	
	private final BluetoothDevice mDevice;
	private final BluetoothSocket mSocket;
	
	public BtConnectClient(BluetoothDevice device) {
		
		BluetoothSocket tmpSocket = null;
		mDevice = device;
		
		try {
			tmpSocket = mDevice.createRfcommSocketToServiceRecord(BlueToothManager.m_UUID);
		} catch (IOException e) {
			
		}
		
		mSocket = tmpSocket;
	}
	
	@Override
	public void run() {
		
		// cancel discovery
		
		try {
			mSocket.connect();
		} catch (IOException e) {
			e.printStackTrace();
			
			try {
				mSocket.close();
			} catch (IOException closeException) {
				
			}
		}
		
		// manageConnected Socket
	}
	
	public void cancel() {
		try {
			mSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
