package com.example.co_reading;

import java.io.File;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.co_reading.painting.PaintingView;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.joanzapata.pdfview.listener.*;

public class PdfFragment extends Fragment 
		implements OnPageChangeListener {	

	private final String TAG = PdfFragment.class.getSimpleName();

	private final int REQUEST_CHOOSER = 1234;	

	private String mCurPdfUriString = "";
	private File mFile;
	private int defaultPage = 1;
	private int curPage;
	
	private boolean mDrawMode;

	public PaintingView mPaintingView;

	@Override
	public void onCreate (Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");

		if (mCurPdfUriString.isEmpty()) {
			Intent getContentIntent = FileUtils.createGetContentIntent();
			Intent intent = Intent.createChooser(getContentIntent,
					"Select a PDF file");
			startActivityForResult(intent, REQUEST_CHOOSER);
		}
	}
	
    @Override
	public void onStart() {
		Log.i(TAG, "onStart, mFile " + mFile);
        super.onStart();
        /**
         * PDFView need explicitly reloaded each time
         */
        if (mFile != null) {
        	mPaintingView.fromFile(mFile).defaultPage(curPage)
        					.onPageChange(this).onDrawListener(mPaintingView)
        					.onLoad(mPaintingView).enableSwipe(true).load();
        }
    }
    
    @Override
	public void onStop() {
		Log.i(TAG, "onStop");
        super.onStop();        
    }
	
	@Override
	public void onDestroy () {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {

		Log.i(TAG, "onCreateView");

        mPaintingView = (PaintingView)inflater.inflate(R.layout.pdfview_frag, null);
		setDrawMode(mDrawMode);
		return mPaintingView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult");
		switch (requestCode) {
		case REQUEST_CHOOSER:
			if (resultCode == Activity.RESULT_OK) {
				final Uri uri = data.getData();
				String uriString = uri.toString();
				if (uri != null && FileUtils.isLocal(FileUtils.getPath(getActivity(), uri))) {
					mFile = FileUtils.getFile(getActivity(), Uri.parse(uriString));
					if (mFile != null && mFile.exists()) {
							Log.d(TAG, "file:"+ mFile);
							mCurPdfUriString = uriString;
							curPage = defaultPage;
						}
					}
				}
			break;
		default:
			break;
		}		
	}
	
	@Override
	public void onPageChanged(int page, int pageCount) {
		Log.d(TAG, "page:" + page + " pageCount:" + pageCount);
		curPage = page;
		mPaintingView.onPageChanged(page);
	}
	
	public void setDrawMode(boolean on) {
		mDrawMode = on;
		if (mPaintingView != null)
			mPaintingView.setDrawMode(on);
	}
}
