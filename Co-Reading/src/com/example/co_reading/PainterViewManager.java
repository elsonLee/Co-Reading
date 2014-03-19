package com.example.co_reading;

import java.util.ArrayList;
import android.view.View;

public class PainterViewManager {
	private static PainterViewManager mManager;
	private ArrayList<PainterView> mList = new ArrayList<PainterView>();

	public static PainterViewManager getInstance() {
		if (mManager == null)
			mManager = new PainterViewManager();
		return mManager;
	}
	
	public boolean add(PainterView painterView) {
		return mList.add(painterView);
	}
	
	public boolean remove(PainterView painterView) {
		return mList.remove(painterView);
	}
}
