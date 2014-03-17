package com.example.co_reading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

public class BlueToothManager implements ITransceiverOps {
	
	public static final int REQUEST_ENABLE_BT = 10;
	public static final int	REQUEST_DIALOG_BT = 11;
	
	public static UUID	m_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
	
	// private static CacheStringKeyMap<String, BluetoothDevice>		m_pairedDevList;
	// private static CacheStringKeyMap<String, BluetoothDevice>		m_foundDevList;
	private static ArrayList<BluetoothDevice> m_pairedDevList;
	private static ArrayList<BluetoothDevice> m_foundDevList;
	
	private static BluetoothAdapter	m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	private static BlueToothManager	m_instance;
	
	private BlueToothManager(){
		// m_pairedDevList = new CacheStringKeyMap<String, BluetoothDevice>();
		// m_foundDevList = new CacheStringKeyMap<String, BluetoothDevice>();
		m_pairedDevList = new ArrayList<BluetoothDevice>();
		m_foundDevList = new ArrayList<BluetoothDevice>();
	}
	
	public static BlueToothManager getInstance() {
		if (null == m_instance)
			m_instance = new BlueToothManager();
		return m_instance;
	}

	/* ITransceiverOps implementation */

	public boolean isSupported() {
		if (m_BluetoothAdapter == null) 
			return false;		
		return true;
	}

	public boolean open(Activity activity) {
		if (null != m_BluetoothAdapter) {
			if (false == m_BluetoothAdapter.isEnabled()) {
				
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			} else {
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
	
	// public CacheStringKeyMap<String, BluetoothDevice> getPairedList() {
	public List<BluetoothDevice> getPairedList() {

		Set<BluetoothDevice> pairedDevices = m_BluetoothAdapter.getBondedDevices();
		
		m_pairedDevList.clear();
		
    	if (pairedDevices.size() > 0) {  		
    		for (BluetoothDevice btDevice : pairedDevices) {
    			// m_pairedDevList.put(btDevice.getAddress(), btDevice);
    			m_pairedDevList.add(btDevice);
    		}
    	}
    	// return m_pairedDevList.values();
    	return m_pairedDevList;
	}
	
	// public CacheStringKeyMap<String, BluetoothDevice> getFoundList() {
	public List<BluetoothDevice> getFoundList() {
		// return m_foundDevList.values();
		return m_foundDevList;
	}
	
	public void addToFoundList(BluetoothDevice btDevice) {
		if (null != btDevice && m_foundDevList.contains(btDevice) == false) {
			// m_foundDevList.put(btDevice.getAddress(), btDevice);
			m_foundDevList.add(btDevice);
		}
	}
	
	public void clearFoundList() {
		if (null != m_foundDevList)
			m_foundDevList.clear();
	}
	
	/*
	public BluetoothDevice getDeviceFromPairedList(String address) {
		if (m_pairedDevList.containsKey(address))
			return m_pairedDevList.get(address);
		
		return null;
	}
	
	public BluetoothDevice getDeviceFromFoundList(String address) {
		if (m_foundDevList.containsKey(address))
			return m_foundDevList.get(address);
		
		return null;
	}
	*/

}
