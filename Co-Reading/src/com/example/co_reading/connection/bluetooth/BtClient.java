package com.example.co_reading.connection.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.co_reading.connection.EndPoint;
import com.example.co_reading.util.BinarySerialization;
import com.example.co_reading.util.ISerialization;

public class BtClient extends BtConnection implements EndPoint {
	
	private String TAG = BtClient.class.getSimpleName();
	
	private BluetoothDevice mDevice = null;

	private final ISerialization mSerialization;

	private BluetoothSocket mSocket = null;

	private Thread mWorkThread = null;
	
	public BtClient(BluetoothDevice device) throws IOException {
		this(device, new BinarySerialization());
	}
	
	public BtClient(BluetoothDevice device, ISerialization serialization) throws IOException {
		
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
		
		mSerialization = serialization;
		
		Initialize(mSocket, mSerialization);

	}
	
	public void cancel() {
		try {
			mSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		
		while (true) {
			Object obj = null;
			try {
				obj = readObj();
			} catch (Exception e) {
				Log.e(TAG, "read object error");
			}

			if (obj != null) {
				notifyReceived(obj);
			}
		}

	}

	@Override
	public void run() {
		update();
	}

	@Override
	public void start() {

		if (mWorkThread != null) {
			return;
		}
		
		mWorkThread = new Thread(this, "Client");
		mWorkThread.setDaemon(true);
		mWorkThread.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}


}