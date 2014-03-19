package com.example.co_reading;

import java.io.File;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;

public class ContainerView extends RelativeLayout implements OnPageChangeListener {
	private final String TAG = "ContainerView";
	private static ContainerView mSelf;
	private Fragment mFragment; // which fragment we are attached?

	private boolean mDrawMode = false;
	private int mVisibility = View.VISIBLE;
	
	private int mPageNum;
	private File mFile;
	private PDFView mPdfView;
	private PainterView mPainterView;
	private Context mContext;

	ContainerView(Context ctx) {
		super(ctx);
		mContext = ctx;
	}

	public static ContainerView getInstance(Context ctx) {
		if (mSelf == null)
			mSelf = new ContainerView(ctx);
		return mSelf;
	}
	
	public static ContainerView getInstance() {
		return mSelf;
	}

	public void setContext(Fragment fragment) {
		mFragment = fragment;
	}

	public boolean loadPdfView(File file, boolean jumpToFirstPage) {
		if (jumpToFirstPage) mPageNum = 1;
		mFile = file;
		
		if (mPdfView == null) {
			mPdfView = new PDFView(mFragment.getActivity(), null);
		}

	   	mPdfView.fromFile(file)
	   	.defaultPage(mPageNum)
		.onPageChange(this)
		.load();
	   	
	   	display();
	   	
	   	return true;
	}
	
	public void replacePainterView(PainterView painter) {
		mPainterView = painter;
	}

	public void display() {
		removeAllViews();
		addView(mPdfView, 0);
		addView(mPainterView, 1);
		setVisibility(mVisibility);
	}

	@Override
	public void onPageChanged(int page, int pageCount) {
		mPageNum = page;
		Log.i(TAG, "page:" + page + " pageCount:" + pageCount);
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
