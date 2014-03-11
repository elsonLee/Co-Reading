package com.example.co_reading;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;

public class TabListener implements ActionBar.TabListener {

	Fragment fragment;

	public TabListener(Fragment fragment) {
		// TODO Auto-generated method stub
		this.fragment = fragment;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		Log.v("TabListener", "onTabSelected");
		ft.replace(R.id.fragment_container, fragment);
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		Log.v("TabListener", "onTabUnselected");
		ft.remove(fragment);
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		Log.v("TabListener", "onTabReselected");
	}
}