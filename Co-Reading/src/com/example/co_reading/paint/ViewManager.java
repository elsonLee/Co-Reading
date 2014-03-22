package com.example.co_reading.paint;

import java.io.File;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

public class ViewManager extends RelativeLayout implements OnPageChangeListener {
	private final String TAG = "ContainerView";

	private boolean mDrawMode = false;
	private int mPageNum = 1;
	private PDFView mPdfView;
	private Painting mPainting;
	
	public ViewManager(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		mPainting = new Painting(ctx);
	}

	public boolean loadPdfView(File file, boolean jumpToFirstPage) {
		if (jumpToFirstPage)
			mPageNum = 1;

		mPdfView = new PDFView(getContext(), null);

	   	mPdfView.fromFile(file).defaultPage(mPageNum).onPageChange(this).load();
	   	
	   	return true;
	}

	public void show() {
		removeAllViews();
		if (mPdfView != null)
			addView(mPdfView);
		addView(mPainting);
		setVisibility(View.VISIBLE);
	}

	@Override
	public void onPageChanged(int page, int pageCount) {
		Log.d(TAG, "page:" + page + " pageCount:" + pageCount);

		mPageNum = page;
		if (mPainting instanceof OnPageChangeListener)
			mPainting.onPageChanged(page, pageCount);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {		
		if (mDrawMode)
			mPainting.dispatchTouchEvent(ev);
		else
			mPdfView.dispatchTouchEvent(ev);

		return true;
	}

	public void setDrawMode(boolean draw) {
		mDrawMode = draw;
	}

	public void toggleDrawMode() {
		mDrawMode = !mDrawMode;
	}

	public void setPaintingVisibility(int visibility) {
		mPainting.setVisibility(visibility);
	}
}
