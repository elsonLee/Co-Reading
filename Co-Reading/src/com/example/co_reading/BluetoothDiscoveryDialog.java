package com.example.co_reading;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

public class BluetoothDiscoveryDialog extends DialogFragment {
	
	private final String TAG = BluetoothDiscoveryDialog.class.getSimpleName();
	
	private ListView m_listView;
	private View m_dialogView;
	private	BluetoothDeviceAdapter m_BtArrayAdapter;
	private BlueToothManager m_BlueToothManager = BlueToothManager.getInstance();
	
	private ITransceiverOps m_ItransceiverOps = null;
	
	public void registerITransceiverObserver(ITransceiverOps ops) {
		if (ops != null)
			m_ItransceiverOps = ops;
	}
	
	public void removeITransceiverObserver() {
		m_ItransceiverOps = null;
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				m_BlueToothManager.addToFoundList(btDevice);
				List<BluetoothDevice> mDevList = (List<BluetoothDevice>)m_BlueToothManager.getPairedList();
				mDevList.addAll((List<BluetoothDevice>)m_BlueToothManager.getFoundList());
				m_BtArrayAdapter.updateBtDevices(mDevList);
				
				ProgressBar mProgressBar = (ProgressBar)m_dialogView.findViewById(R.id.progresscircle);
				mProgressBar.setVisibility(m_listView.GONE);
			}
			else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				// pairing
				Log.d(TAG, "pairing response");
			}
			else if (BluetoothDevice.ACTION_UUID.equals(action)) {
				// get uuid
				BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
				
				if (uuidExtra == null) {
					Log.e(TAG, "UUID == null");
				}
				
				Log.d(TAG, "uuid: "+uuidExtra.toString());
			}
		}
	};
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		m_dialogView = inflater.inflate(R.layout.dialog_discoverydevice, null);
		
		m_listView = (ListView)m_dialogView.findViewById(R.id.bluetooth_devicelist);
		m_listView.setAdapter(m_BtArrayAdapter);
		m_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long id) {
				BluetoothDeviceAdapter mArrayAdapter = (BluetoothDeviceAdapter)arg0.getAdapter();
				BluetoothDevice clientDev = (BluetoothDevice)mArrayAdapter.getItem(pos);
				
				/*
				int mboundState = clientDev.getBondState();
				switch (mboundState) {
				case BluetoothDevice.BOND_NONE:
					return;
				case BluetoothDevice.BOND_BONDING:
					while(clientDev.getBondState() != BluetoothDevice.BOND_BONDED);
					// no break
				case BluetoothDevice.BOND_BONDED:
					Log.d("Bt", "bonded");
					break;					
				default:
					return;
				}
				
				if (clientDev.fetchUuidsWithSdp() == false) {
					Log.d("fetch uuid", "fetch failed");
					return;
				}
				*/
				
				try {
					BluetoothSocket btSocket = clientDev.createInsecureRfcommSocketToServiceRecord(BlueToothManager.m_UUID);
					// BluetoothSocket btSocket = clientDev.createRfcommSocketToServiceRecord(BlueToothManager.m_UUID);
					// Method m = clientDev.getClass().getMethod("createInsecureRfcommSocket", new Class[] {int.class});
					// BluetoothSocket btSocket = (BluetoothSocket)m.invoke(clientDev, Integer.valueOf(1));

					if (btSocket != null) {
						Log.d(TAG, "begin to connect");
						btSocket.connect();
						
						// test for transfer
						InputStream tmpIn = null;
						OutputStream tmpOut = null;
						try {
							tmpIn = btSocket.getInputStream();
							tmpOut = btSocket.getOutputStream();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						int EXIT_CMD = -1;
						int KEY_RIGHT =1;
						int KEY_LEFT = 2;
						byte cmdRight = (byte) KEY_RIGHT;
						byte cmdLeft = (byte) KEY_LEFT;
						byte cmdExit = (byte) EXIT_CMD;
						
						tmpOut.write(cmdLeft);
						tmpOut.write(cmdRight);
						Log.d(TAG, "end transfer");
						
					} else {
						Log.d(TAG, "get Socket error");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		// button listener
		Button mScanButton = (Button)m_dialogView.findViewById(R.id.discoverydevice_scanbutton);
		mScanButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (true == m_BlueToothManager.discovery()) {
					m_BlueToothManager.clearFoundList();
					ProgressBar mProgressBar = (ProgressBar)m_dialogView.findViewById(R.id.progresscircle);
					// mProgressBar.setVisibility(m_listView.VISIBLE);
				}
			}
		});
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		getActivity().registerReceiver(mReceiver, filter);
		
		builder.setView(m_dialogView);
    	
		// CacheStringKeyMap<String, BluetoothDevice> mArrayDevicesData = m_BlueToothManager.getPairedList();
		List<BluetoothDevice> mArrayDevicesData = m_BlueToothManager.getPairedList();
    	
		// ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mArrayDevicesData.getCachedKeyList());
    	// m_listView.setAdapter(mArrayAdapter);	

		m_BtArrayAdapter = new BluetoothDeviceAdapter(getActivity());
		m_BtArrayAdapter.updateBtDevices(mArrayDevicesData);
		m_listView.setAdapter(m_BtArrayAdapter);
		
		return builder.create();		
	}
}
