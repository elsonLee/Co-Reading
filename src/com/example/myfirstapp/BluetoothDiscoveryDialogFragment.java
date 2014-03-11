package com.example.myfirstapp;

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
	
	private ListView listView;

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			ArrayAdapter mArrayAdapter = (ArrayAdapter)listView.getAdapter();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				mArrayAdapter.add(device.getName()+"\n"+device.getAddress());
				
				ProgressBar mProgressBar = (ProgressBar)listView.findViewById(R.id.progresscircle);
				mProgressBar.setVisibility(listView.GONE);	
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
				// TODO Auto-generated method stub
				BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				if (mBluetoothAdapter.startDiscovery()) {
					ProgressBar mProgressBar = (ProgressBar)listView.findViewById(R.id.progresscircle);
					mProgressBar.setVisibility(listView.VISIBLE);				
				}
			}
		});
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		getActivity().registerReceiver(mReceiver, filter);
		
		builder.setView(view);
//		.setPositiveButton("fire", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				
//			}
//		})
//		.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				
//			}
//		});		
		
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
    	// If there are paired devices
    	Log.d("BluetoothDevice", "List BluetoothDevices... :");
    	
    	List<String> mArrayDevices = new ArrayList<String>();
    	if (pairedDevices.size() > 0) {
    		// Loop through paired devices    		
    		for (BluetoothDevice device : pairedDevices) {
    			mArrayDevices.add(device.getName()+"\n"+device.getAddress()+"(Paired)");
    		}
    	}
    	
    	ArrayAdapter mArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mArrayDevices);
    	listView = (ListView)view.findViewById(R.id.bluetooth_devicelist);
    	listView.setAdapter(mArrayAdapter);	
		
		return builder.create();		
	}
}
