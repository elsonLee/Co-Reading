package com.example.co_reading;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.app.Activity;
import android.util.Log;

public class ContainerView extends FrameLayout {
	private Activity mActivity;
	private final String TAG = "ContainerView";
	private static ContainerView mSelf;
	private boolean mDrawMode = false;
	private int mVisibility = View.VISIBLE;

	ContainerView(Activity ctx) {
		super(ctx);
		mActivity = ctx;
	}
	
	public static ContainerView getInstance(Activity ctx) {
		if (mSelf == null)
			mSelf = new ContainerView(ctx);
		return mSelf;
	}

	@Override
	public boolean dispatchTouchEvent (MotionEvent ev) {		
		View v0, v1;
		
		v0 = getChildAt(0);
		v1 = getChildAt(1);

		if (v0 != null && v1 != null) {
			if (!mDrawMode)
				v0.dispatchTouchEvent(ev);
			else
				v1.dispatchTouchEvent(ev);
		} else {
			Log.i(TAG, "both view is null");
		}

		return true;
	}
	
	public void setDrawMode(boolean draw) {
		mDrawMode = draw;
	}

	public void toggleDrawMode() {
		mDrawMode = !mDrawMode;
	}
	
	public void setVisibility(int visibility) {
		mVisibility = visibility;
		View v = getChildAt(1);

// in case we pass touch event to invisible view
		if (mVisibility == View.INVISIBLE)
			mDrawMode = false;

		if (v != null)
			v.setVisibility(visibility);
		else
			Log.i(TAG, "painter view is null");
	}

	public int getVisibility() {
		return mVisibility;
	}
}
