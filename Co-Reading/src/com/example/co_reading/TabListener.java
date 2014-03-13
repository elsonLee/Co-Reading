package com.example.co_reading;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;

public class TabListener implements ActionBar.TabListener {

	private Fragment m_fragment = null;
	private boolean m_isAdded = false;

	public TabListener(Fragment fragment) {
		Log.v("tablistener", "Tablistener constructor");
		this.m_fragment = fragment;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.v("TabListener", "onTabSelected");
		ft.replace(R.id.fragment_container, m_fragment);
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		Log.v("TabListener", "onTabUnselected:"+m_fragment);
		ft.remove(m_fragment);
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		Log.v("TabListener", "onTabReselected");
	}
}