package com.example.co_reading;

import android.view.SurfaceHolder;
import android.util.Log;

public final class SurfaceViewCallback implements SurfaceHolder.Callback {
	private final String TAG = "SurfaceViewDrawer";
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.i(TAG, "surface changed");
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, "surface created");
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "surface destroyed");
	}
}
