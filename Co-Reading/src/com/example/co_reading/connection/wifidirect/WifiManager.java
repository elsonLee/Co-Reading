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
package com.example.co_reading.connection.wifidirect;


/*
public class WifiManager implements ITransceiverOps {
	
	private WifiP2pManager mManager;
	
	private Channel mChannel;
	
	private BroadcastReceiver mReceiver;
	
	private IntentFilter mIntentFilter;
	
	private WifiManager() {
		
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, this.getMainLooper(), null);			// FIXME: has problem with getMainLooper
		mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);
		
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	}
	
	public void onResume() {
		registerReceiver(mReceiver, mIntentFilter);
	}
	
	public void onPause() {
		unregisterReceiver(mReceiver);
	}

	@Override
	public boolean isSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean open(Activity activity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close(Activity activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean discovery() {
		// TODO Auto-generated method stub
		return false;
	}
}
*/
