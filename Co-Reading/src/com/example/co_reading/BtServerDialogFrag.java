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

public class BtServerDialogFrag extends Fragment {

	private final String TAG = BtServerDialogFrag.class
			.getSimpleName();

	private ListView mListView;
	private View mDialogView;
	private BtDeviceAdapter mBtArrayAdapter;
	private BlueToothManager mBlueToothManager = BlueToothManager
			.getInstance();

	private ITransceiverOps mItransceiverOps = null;

	@Override
	public void onCreate (Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);

		Log.i(TAG, "onCreate");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {

		mDialogView = inflater.inflate(R.layout.bt_server_dialog_frag,
				null);

		Button connectBtn = (Button) mDialogView
				.findViewById(R.id.bluetooth_createconnect);
		connectBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				((Button) v).setText(R.string.bluetooth_waitconncet);
				
				/* don't need server instance here */
				try {
					mBlueToothManager.getServer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		return mDialogView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Log.i(TAG, "onDestroy");
	}
}
