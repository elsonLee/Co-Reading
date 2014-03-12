package com.example.co_reading;

import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.example.co_reading.util.CacheStringKeyMap;

public class BlueToothManager extends TransceiverImp {
	
/*	public class BtDeviceStruct {
		public String m_name;
		public String m_address;
		public boolean m_paired;
		
		public BtDeviceStruct() {
			m_name = null;
			m_address = null;
			m_paired = false;
		}
	};*/
	
	public static final int 	REQUEST_ENABLE_BT = 10;
	public static final int		REQUEST_DIALOG_BT = 11;
	
	public static UUID	m_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
	
	private static CacheStringKeyMap<String, BluetoothDevice>		m_pairedDevList;
	private static CacheStringKeyMap<String, BluetoothDevice>		m_foundDevList;
	private static BluetoothAdapter	m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	private static BlueToothManager	m_instance;
	
	private BlueToothManager(){
		m_pairedDevList = new CacheStringKeyMap<String, BluetoothDevice>();
		m_foundDevList = new CacheStringKeyMap<String, BluetoothDevice>();
		
		// m_UUID = new UUID((long)0x04c6093b00001000l, (long)0x800000805f9b34fbl);
	}
	
	public static BlueToothManager getInstance() {
		if (null == m_instance)
			m_instance = new BlueToothManager();
		return m_instance;
	}

	@Override
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
	public boolean isSupported() {
		if (null == m_BluetoothAdapter) 
			return false;		
		return true;
	}
	
	@Override
	public boolean discovery() {
		if (null == m_BluetoothAdapter)
			return false;
		
		return m_BluetoothAdapter.startDiscovery();
	}
	
	public CacheStringKeyMap<String, BluetoothDevice> getPairedList() {
		Set<BluetoothDevice> pairedDevices = m_BluetoothAdapter.getBondedDevices();
		
		m_pairedDevList.clear();
		
    	if (pairedDevices.size() > 0) {  		
    		for (BluetoothDevice btDevice : pairedDevices) {
    			m_pairedDevList.put(btDevice.getAddress()+"(Paired)", btDevice);
    		}
    	}
    	return m_pairedDevList;
	}
	
	public CacheStringKeyMap<String, BluetoothDevice> getFoundList() {
		return m_foundDevList;
	}
	
	public void addToFoundList(BluetoothDevice btDevice) {
		if (null != btDevice /* && false == m_foundDevList.contains(btDeviceData) */) {
			m_foundDevList.put(btDevice.getAddress(), btDevice);
		}
	}
	
	public void clearFoundList() {
		if (null != m_foundDevList)
			m_foundDevList.clear();
	}
	
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

}
