package com.example.co_reading;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.app.Activity;
import android.util.Log;

public class ContainerView extends FrameLayout {
	private Activity mActivity;
	private final String TAG = "ContainerView";

	ContainerView(Activity ctx) {
		super(ctx);
		mActivity = ctx;
	}

	@Override
	public boolean dispatchTouchEvent (MotionEvent ev) {
		Log.i(TAG, "capture motion event");
		return true;
	}
}
