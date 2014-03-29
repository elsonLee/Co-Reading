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
package com.example.co_reading.connection.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

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
				Log.d(TAG, "connect successful");
			} catch (IOException e) {
				try {
					mSocket.close();
				} catch (IOException e1) {
					throw new IOException();
				}
				throw new IOException();
			}

			// Initialize
			Initialize(mSocket.getInputStream(), mSocket.getOutputStream());
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