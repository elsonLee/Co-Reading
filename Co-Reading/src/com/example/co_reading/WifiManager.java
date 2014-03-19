package com.example.co_reading;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;

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
