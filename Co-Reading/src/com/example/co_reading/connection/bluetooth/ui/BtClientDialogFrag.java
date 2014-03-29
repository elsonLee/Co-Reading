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
package com.example.co_reading.connection.bluetooth.ui;

import java.io.IOException;
import java.util.List;

import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.co_reading.R;
import com.example.co_reading.connection.ITransceiverOps;
import com.example.co_reading.connection.bluetooth.BlueToothManager;
import com.example.co_reading.connection.bluetooth.BtDeviceAdapter;

public class BtClientDialogFrag extends Fragment {

	private final String TAG = BtClientDialogFrag.class
			.getSimpleName();

	private ListView mListView;
	private View mDialogView;
	private BtDeviceAdapter mBtArrayAdapter;
	private BlueToothManager mBlueToothManager = BlueToothManager
			.getInstance();

	private ITransceiverOps mItransceiverOps = null;

	public void registerITransceiverObserver(ITransceiverOps ops) {
		if (ops != null)
			mItransceiverOps = ops;
	}

	public void removeITransceiverObserver() {
		mItransceiverOps = null;
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice btDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				mBlueToothManager.addToFoundList(btDevice);
				List<BluetoothDevice> mDevList = (List<BluetoothDevice>) mBlueToothManager
						.getPairedList();
				mDevList.addAll((List<BluetoothDevice>) mBlueToothManager
						.getFoundList());
				mBtArrayAdapter.updateBtDevices(mDevList);

				ProgressBar mProgressBar = (ProgressBar) mDialogView
						.findViewById(R.id.progresscircle);
				mProgressBar.setVisibility(mListView.GONE);

			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED
					.equals(action)) {
				// pairing
				Log.d(TAG, "pairing response");

			} else if (BluetoothDevice.ACTION_UUID.equals(action)) {
				// get uuid
				BluetoothDevice btDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Parcelable[] uuidExtra = intent
						.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);

				if (uuidExtra == null) {
					Log.e(TAG, "UUID == null");
				}

				Log.d(TAG, "uuid: " + uuidExtra.toString());
			}
		}
	};
	
	@Override
	public void onCreate (Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);

		Log.i(TAG, "onCreate");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {

		mDialogView = inflater.inflate(R.layout.bt_client_dialog_frag,
				null);

		mListView = (ListView) mDialogView
				.findViewById(R.id.bluetooth_devicelist);
		mListView.setAdapter(mBtArrayAdapter);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long id) {
						BtDeviceAdapter mArrayAdapter = (BtDeviceAdapter) arg0
								.getAdapter();
						BluetoothDevice clientDev = (BluetoothDevice) mArrayAdapter
								.getItem(pos);

						try {
							/* don't need client instance here */
							Log.i(TAG, "clientDev: " + clientDev);
							mBlueToothManager.getClient(clientDev);

						} catch (IllegalArgumentException e1) {
							e1.printStackTrace();
						} catch (IOException e) {
							Toast.makeText(getActivity(), "Can not connect to " + clientDev.getName() + " Now! Please try again!", Toast.LENGTH_SHORT).show();
						}
					}

				});

		// button listener
		Button mScanButton = (Button) mDialogView
				.findViewById(R.id.discoverydevice_scanbutton);
		mScanButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mBlueToothManager.discovery() == true) {
					mBlueToothManager.clearFoundList();
					ProgressBar mProgressBar = (ProgressBar) mDialogView
							.findViewById(R.id.progresscircle);
					// mProgressBar.setVisibility(m_listView.VISIBLE);
				}
			}
		});
		
		List<BluetoothDevice> mArrayDevicesData = mBlueToothManager
				.getPairedList();

		mBtArrayAdapter = new BtDeviceAdapter(getActivity());
		mBtArrayAdapter.updateBtDevices(mArrayDevicesData);
		mListView.setAdapter(mBtArrayAdapter);

		return mDialogView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		getActivity().registerReceiver(mReceiver, filter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(mReceiver);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Log.i(TAG, "onDestroy");
	}

}