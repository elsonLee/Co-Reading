package com.example.co_reading.connection.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.example.co_reading.connection.Client;
import com.example.co_reading.util.BinarySerialization;
import com.example.co_reading.util.ISerialization;

//public class BtClient extends BtConnection implements EndPoint {
public class BtClient extends Client {
	
	private String TAG = BtClient.class.getSimpleName();
	
	private BluetoothDevice mDevice = null;

	private BluetoothSocket mSocket = null;

	public BtClient(BluetoothDevice device) throws IOException {
		this(device, new BinarySerialization());
	}
	
	public BtClient(BluetoothDevice device, ISerialization serialization) throws IOException {
		super(serialization);
		
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
		
	}
	
	public void cancel() {
		try {
			mSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}


}