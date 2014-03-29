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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.example.co_reading.connection.Connection;
import com.example.co_reading.connection.ITransceiverOps;

public class BlueToothManager implements ITransceiverOps {
	
	public static final int REQUEST_ENABLE_BT = 10;
	public static final int	REQUEST_DIALOG_BT = 11;
	
	public static final int	ROLE_CLIENT = 12;
	public static final int	ROLE_SERVER = 13;
	public static final int	ROLE_NOCONNECTION = 14;

	public static UUID	m_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
	
	private static ArrayList<BluetoothDevice> m_pairedDevList;
	private static ArrayList<BluetoothDevice> m_foundDevList;
	
	private static BluetoothAdapter	m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	private static BlueToothManager	mInstance;
	
	private static BtClient mClient = null;
	private static BtServer mServer = null;
	
	private static int mRole = ROLE_NOCONNECTION;
	
	private BlueToothManager(){
		m_pairedDevList = new ArrayList<BluetoothDevice>();
		m_foundDevList = new ArrayList<BluetoothDevice>();
	}
	
	public static BlueToothManager getInstance() {
		if (mInstance == null)
			mInstance = new BlueToothManager();
		return mInstance;
	}

	public static int getRole() {
		return mRole;
	}
	
	public BtClient getClient(BluetoothDevice btDevice) throws IOException {
		if (btDevice != null && mClient == null && mServer == null) {
			mClient = new BtClient(btDevice);
			mClient.start();
			if (mClient != null) {
				mRole = ROLE_CLIENT;
			}
		}

		return mClient;
	}

	public BtServer getServer() throws IOException {
		if (mServer == null && mClient == null) {
			mServer = new BtServer();
			mServer.start();
			if (mServer != null) {
				mRole = ROLE_SERVER;
			}
		}

		return mServer;
	}
	
	public Connection getConnection() throws IOException {
		Connection connection = null;
		switch (mRole) {
		case ROLE_CLIENT:
			connection = getClient(null);
			break;
		case ROLE_SERVER:
			connection = getServer();
			break;
		}
		
		return connection;
	}

	/* ITransceiverOps implementation */

	public boolean isSupported() {
		if (m_BluetoothAdapter == null) 
			return false;		
		return true;
	}

	public boolean open(Activity activity) {
		if (m_BluetoothAdapter != null) {
			if (m_BluetoothAdapter.isEnabled() == false) {
				
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT); } else {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void close(Activity activity) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public boolean discovery() {
		if (null == m_BluetoothAdapter)
			return false;
		
		return m_BluetoothAdapter.startDiscovery();
	}
	
	public List<BluetoothDevice> getPairedList() {

		Set<BluetoothDevice> pairedDevices = m_BluetoothAdapter.getBondedDevices();
		
		m_pairedDevList.clear();
		
    	if (pairedDevices.size() > 0) {  		
    		for (BluetoothDevice btDevice : pairedDevices) {
    			m_pairedDevList.add(btDevice);
    		}
    	}
    	return m_pairedDevList;
	}
	
	public List<BluetoothDevice> getFoundList() {
		return m_foundDevList;
	}
	
	public void addToFoundList(BluetoothDevice btDevice) {
		if (null != btDevice && m_foundDevList.contains(btDevice) == false) {
			m_foundDevList.add(btDevice);
		}
	}
	
	public void clearFoundList() {
		if (null != m_foundDevList)
			m_foundDevList.clear();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}
}
