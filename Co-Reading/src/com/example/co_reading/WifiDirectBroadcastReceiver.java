package com.example.co_reading;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
	
	private WifiP2pManager mManager;

	private Channel mChannel;

	private WifiActivity mActivity;
	
	public WifiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
			WifiActivity activity) {
		super();
		
		mManager = manager;
		mChannel = channel;
		mActivity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				// Wifi p2p is enabled
			} else {
				// Wifi p2p is not enabled
			}
				
		} else
		if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			// TODO
		} else
		if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			// TODO
		} else
		if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			// TODO
		}
	}

}
