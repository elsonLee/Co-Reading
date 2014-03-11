package com.example.co_reading;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.co_reading.BlueToothManager.BtDeviceStruct;
import com.example.co_reading.util.CacheStringKeyMap;

public class BluetoothDiscoveryDialogFragment extends DialogFragment {
	
	private ListView 			m_listView;
	private View				m_dialogView;
	private BlueToothManager	m_BlueToothManager = BlueToothManager.getInstance();

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	
		// FIXME: each touch just found one device now.
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			ArrayAdapter mArrayAdapter = (ArrayAdapter)m_listView.getAdapter();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				BtDeviceStruct btDevData = m_BlueToothManager.new BtDeviceStruct();
				btDevData.m_name = device.getName();
				btDevData.m_address = device.getAddress();
				btDevData.m_paired = false;
				m_BlueToothManager.addToFoundList(btDevData);
				mArrayAdapter.clear();
				mArrayAdapter.addAll(m_BlueToothManager.getPairedList().getCachedKeyList().addAll(m_BlueToothManager.getFoundList().getCachedKeyList()));
				
				//ProgressBar mProgressBar = (ProgressBar)m_dialogView.findViewById(R.id.progresscircle);
				//mProgressBar.setVisibility(m_listView.GONE);
			}
		}
	};
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		m_dialogView = inflater.inflate(R.layout.dialog_discoverydevice, null);
		
		// list view
		m_listView = (ListView)m_dialogView.findViewById(R.id.bluetooth_devicelist);
		
		// button listener
		Button mScanButton = (Button)m_dialogView.findViewById(R.id.discoverydevice_scanbutton);
		mScanButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (true == m_BlueToothManager.discovery()) {
					m_BlueToothManager.clearFoundList();
					ProgressBar mProgressBar = (ProgressBar)m_dialogView.findViewById(R.id.progresscircle);
					mProgressBar.setVisibility(m_listView.VISIBLE);
				}
			}
		});
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		getActivity().registerReceiver(mReceiver, filter);
		
		builder.setView(m_dialogView);
    	
		CacheStringKeyMap<String, BtDeviceStruct> mArrayDevicesData = m_BlueToothManager.getPairedList();
    	
		ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mArrayDevicesData.getCachedKeyList());
    	m_listView.setAdapter(mArrayAdapter);	
		
		return builder.create();		
	}
}
