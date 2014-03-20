package com.example.co_reading;

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

public class BluetoothDiscoveryDialogFrag extends Fragment {

	private final String TAG = BluetoothDiscoveryDialogFrag.class
			.getSimpleName();

	private ListView m_listView;
	private View m_dialogView;
	private BluetoothDeviceAdapter m_BtArrayAdapter;
	private BlueToothManager m_BlueToothManager = BlueToothManager
			.getInstance();

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
				BluetoothDevice btDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				m_BlueToothManager.addToFoundList(btDevice);
				List<BluetoothDevice> mDevList = (List<BluetoothDevice>) m_BlueToothManager
						.getPairedList();
				mDevList.addAll((List<BluetoothDevice>) m_BlueToothManager
						.getFoundList());
				m_BtArrayAdapter.updateBtDevices(mDevList);

				ProgressBar mProgressBar = (ProgressBar) m_dialogView
						.findViewById(R.id.progresscircle);
				mProgressBar.setVisibility(m_listView.GONE);

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

		m_dialogView = inflater.inflate(R.layout.dialog_discoverydevice,
				null);

		m_listView = (ListView) m_dialogView
				.findViewById(R.id.bluetooth_devicelist);
		m_listView.setAdapter(m_BtArrayAdapter);

		m_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long id) {
						BluetoothDeviceAdapter mArrayAdapter = (BluetoothDeviceAdapter) arg0
								.getAdapter();
						BluetoothDevice clientDev = (BluetoothDevice) mArrayAdapter
								.getItem(pos);

						try {
							/* don't need client instance here */
							Log.i(TAG, "clientDev: " + clientDev);
							m_BlueToothManager.getClient(clientDev);

						} catch (IllegalArgumentException e1) {
							e1.printStackTrace();
						} catch (IOException e) {
							Toast.makeText(getActivity(), "Can not connect to " + clientDev.getName() + " Now! Please try again!", Toast.LENGTH_SHORT).show();
						}
					}

				});

		// button listener
		Button mScanButton = (Button) m_dialogView
				.findViewById(R.id.discoverydevice_scanbutton);
		mScanButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (m_BlueToothManager.discovery() == true) {
					m_BlueToothManager.clearFoundList();
					ProgressBar mProgressBar = (ProgressBar) m_dialogView
							.findViewById(R.id.progresscircle);
					// mProgressBar.setVisibility(m_listView.VISIBLE);
				}
			}
		});

		List<BluetoothDevice> mArrayDevicesData = m_BlueToothManager
				.getPairedList();

		m_BtArrayAdapter = new BluetoothDeviceAdapter(getActivity());
		m_BtArrayAdapter.updateBtDevices(mArrayDevicesData);
		m_listView.setAdapter(m_BtArrayAdapter);

		return m_dialogView;
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
