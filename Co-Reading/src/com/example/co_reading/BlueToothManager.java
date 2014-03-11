package com.example.co_reading;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

public class BlueToothManager extends TransceiverImp {
	
	public static final int 	REQUEST_ENABLE_BT = 10;
	
	private static List<String>		m_pairedDevList;
	private static List<String>		m_foundDevList;
	private static BluetoothAdapter	m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	private static BlueToothManager	m_instance;
	
	private BlueToothManager(){}
	
	public static BlueToothManager getInstance() {
		if (null == m_instance)
			m_instance = new BlueToothManager();
		return m_instance;
	}

	@Override
	public void open(Activity activity) {
		if (null != m_BluetoothAdapter && false == m_BluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}		
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
	
	public List<String> getPairedList() {
		Set<BluetoothDevice> pairedDevices = m_BluetoothAdapter.getBondedDevices();
		List<String> mArrayDevicesText = new ArrayList<String>();
    	if (pairedDevices.size() > 0) {
    		// Loop through paired devices    		
    		for (BluetoothDevice device : pairedDevices) {
    			mArrayDevicesText.add(device.getName()+"\n"+device.getAddress()+"(Paired)");
    		}
    	}    	
    	return mArrayDevicesText;
	}

}
