package com.example.co_reading;

import java.io.IOException;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BtConnectClient extends Thread {
	
	private final String TAG = BtConnectClient.class.getSimpleName();
	
	// private final UUID mUUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
	
	private final BluetoothSocket mSocket;
	private final BluetoothDevice mDevice;
	
	public BtConnectClient(BluetoothDevice device) {
		BluetoothSocket tmpSocket = null;
		mDevice = device;
		
		try {
			tmpSocket = device
					.createInsecureRfcommSocketToServiceRecord(BlueToothManager.m_UUID);
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		
		mSocket = tmpSocket;
	}
	
	@Override
	public void run() {
		
		// TODO: cancel discovery
		
		try {
			mSocket.connect();
		} catch (IOException e) {
			e.printStackTrace();
			try {
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
