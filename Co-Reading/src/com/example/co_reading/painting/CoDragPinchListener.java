package com.example.co_reading.painting;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.joanzapata.pdfview.util.DragPinchListener;

public class CoDragPinchListener extends DragPinchListener {
	private static final String TAG = DragPinchListener.class.getSimpleName();

	@Override
    public boolean onTouch(View v, MotionEvent event) {
		Log.i(TAG, "on Touch");
    	return super.onTouch(v, event);
    }
}
