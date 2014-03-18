package com.example.co_reading;

import java.util.ArrayList;

public class PainterManager {
	private static PainterManager mManager;
	private ArrayList<Painter> mList = new ArrayList<Painter>();
	private boolean mVisible = false;

	public static PainterManager getInstance() {
		if (mManager == null)
			mManager = new PainterManager();
		return mManager;
	}
	
	public boolean add(Painter painter) {
		return mList.add(painter);
	}
	
	public boolean remove(Painter painter) {
		return mList.remove(painter);
	}
	
	public void setVisible(boolean visible) {
		mVisible = visible;
		for (Painter t : mList) {
			t.setVisible(visible);
		}
	}
	
	public boolean isVisible() {
		return mVisible;
	}
	
	public void toggle() {
		mVisible = !mVisible;
		setVisible(mVisible);
	}
}
