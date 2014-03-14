package com.example.co_reading;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class TabListener implements ActionBar.TabListener {

	private Fragment m_fragment = null;

	public TabListener(Fragment fragment) {
		m_fragment = fragment;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.fragment_container, m_fragment);
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(m_fragment);
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}
}
