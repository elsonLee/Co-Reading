package com.example.co_reading;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;

public class TabListener implements ActionBar.TabListener {

	private final String TAG = TabListener.class.getSimpleName();

	private Fragment mFragment = null;

	public TabListener(Fragment fragment) {
		mFragment = fragment;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.i(TAG, "onTabSelected");
		ft.replace(R.id.fragment_container, mFragment);
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		Log.i(TAG, "onTabUnselected");
		ft.remove(mFragment);
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}
}
