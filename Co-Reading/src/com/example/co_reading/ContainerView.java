package com.example.co_reading;

import java.io.File;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

public class ContainerView extends RelativeLayout implements OnPageChangeListener {
	private final String TAG = "ContainerView";

	private boolean mDrawMode = false;
	
	private int mPageNum = 1;
	private File mFile;
	private PDFView mPdfView;
	private PainterView mPainterView;

	public ContainerView(Context ctx) {
		super(ctx);
		mPainterView = new PainterView(ctx);
	}
	
	public ContainerView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		mPainterView = new PainterView(ctx);
	}
	
	public ContainerView(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);
		mPainterView = new PainterView(ctx);
	}

	public boolean loadPdfView(File file, boolean jumpToFirstPage) {
		if (jumpToFirstPage) mPageNum = 1;
		mFile = file;
		
		if (mPdfView == null) {
			mPdfView = new PDFView(getContext(), null);
		}

	   	mPdfView.fromFile(file).defaultPage(mPageNum).onPageChange(this).load();
	   	
	   	return true;
	}
	
	public void replacePainterView(PainterView painter) {
		mPainterView = painter;
	}

	public void display() {
		removeAllViews();
		if (mPdfView != null)
			addView(mPdfView);
		addView(mPainterView);
		setVisibility(View.VISIBLE);
	}

	@Override
	public void onPageChanged(int page, int pageCount) {
		mPageNum = page;
		Log.i(TAG, "page:" + page + " pageCount:" + pageCount);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {		
		if (mDrawMode)
			mPainterView.dispatchTouchEvent(ev);
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
}
