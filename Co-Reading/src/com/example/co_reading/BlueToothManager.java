package com.example.co_reading;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.example.co_reading.util.CacheStringKeyMap;

public class BlueToothManager extends TransceiverImp {
	
	public class BtDeviceStruct {
		public String m_name;
		public String m_address;
		public boolean m_paired;
		
		public BtDeviceStruct() {
			m_name = null;
			m_address = null;
			m_paired = false;
		}
	};
	
	public static final int 	REQUEST_ENABLE_BT = 10;
	public static final int		REQUEST_DIALOG_BT = 11;
	
	private static CacheStringKeyMap<String, BtDeviceStruct>		m_pairedDevList;
	private static CacheStringKeyMap<String, BtDeviceStruct>		m_foundDevList;
	private static BluetoothAdapter	m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	private static BlueToothManager	m_instance;
	
	private BlueToothManager(){
		m_pairedDevList = new CacheStringKeyMap<String, BtDeviceStruct>();
		m_foundDevList = new CacheStringKeyMap<String, BtDeviceStruct>();
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
	
	public CacheStringKeyMap<String, BtDeviceStruct> getPairedList() {
		Set<BluetoothDevice> pairedDevices = m_BluetoothAdapter.getBondedDevices();
		
		m_pairedDevList.clear();
		
    	if (pairedDevices.size() > 0) {  		
    		for (BluetoothDevice device : pairedDevices) {
    			BtDeviceStruct btDevice = new BtDeviceStruct();
    			btDevice.m_name = device.getName();
    			btDevice.m_address = device.getAddress();
    			btDevice.m_paired = true;
    			m_pairedDevList.put(btDevice.m_address, btDevice);
    		}
    	}
    	return m_pairedDevList;
	}
	
	public CacheStringKeyMap<String, BtDeviceStruct> getFoundList() {
		return m_foundDevList;
	}
	
	public void addToFoundList(BtDeviceStruct btDeviceData) {
		if (null != btDeviceData.m_name /* && false == m_foundDevList.contains(btDeviceData) */) {
			m_foundDevList.put(btDeviceData.m_address, btDeviceData);
		}
	}
	
	public void clearFoundList() {
		if (null != m_foundDevList)
			m_foundDevList.clear();
	}

}
