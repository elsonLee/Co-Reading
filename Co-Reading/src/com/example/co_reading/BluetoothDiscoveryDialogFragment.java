package com.example.co_reading;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

public class BluetoothDiscoveryDialogFragment extends DialogFragment {
	
	private ListView 			m_listView;
	private BlueToothManager	m_BlueToothManager = BlueToothManager.getInstance();

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			ArrayAdapter mArrayAdapter = (ArrayAdapter)m_listView.getAdapter();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				mArrayAdapter.add(device.getName()+"\n"+device.getAddress());
				
				ProgressBar mProgressBar = (ProgressBar)m_listView.findViewById(R.id.progresscircle);
				mProgressBar.setVisibility(m_listView.GONE);	
			}
		}
	};
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_discoverydevice, null);
		
		// button listener
		Button mScanButton = (Button)view.findViewById(R.id.discoverydevice_scanbutton);
		mScanButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (true == m_BlueToothManager.discovery()) {
					ProgressBar mProgressBar = (ProgressBar)m_listView.findViewById(R.id.progresscircle);
					mProgressBar.setVisibility(m_listView.VISIBLE);				
				}
			}
		});
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		getActivity().registerReceiver(mReceiver, filter);
		
		builder.setView(view);
    	
    	List<String> mArrayDevicesText = m_BlueToothManager.getPairedList();
    	
    	ArrayAdapter mArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mArrayDevicesText);
    	m_listView = (ListView)view.findViewById(R.id.bluetooth_devicelist);
    	m_listView.setAdapter(mArrayAdapter);	
		
		return builder.create();		
	}
}
