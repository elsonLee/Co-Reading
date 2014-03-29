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

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.co_reading.R;
import com.example.co_reading.connection.ITransceiverOps;
import com.example.co_reading.connection.bluetooth.BlueToothManager;
import com.example.co_reading.connection.bluetooth.BtDeviceAdapter;

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
