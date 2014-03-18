package com.example.co_reading;

import java.util.ArrayList;
import android.view.View;

public class PainterViewManager {
	private static PainterViewManager mManager;
	private ArrayList<PainterView> mList = new ArrayList<PainterView>();
	private boolean mBypass = true;

	public static PainterViewManager getInstance() {
		if (mManager == null)
			mManager = new PainterViewManager();
		return mManager;
	}
	
	public boolean add(PainterView painterView) {
		painterView.bypass = mBypass;
		return mList.add(painterView);
	}
	
	public boolean remove(PainterView painterView) {
		return mList.remove(painterView);
	}
	
	public void setBypassMode(boolean bypass) {
		mBypass = bypass;
		for (PainterView pv : mList)
			pv.bypass = bypass;
	}
	
	public boolean getBypassMode() {
		return mBypass;
	}
	
	public void setVisibility(boolean visibility) {
		int visible = visibility? View.VISIBLE : View.INVISIBLE;

		for (PainterView pv : mList)
			pv.setVisibility(visible);
	}
	
	public void toggleBypassMode() {
		setBypassMode(!mBypass);
	}
}
