/*Copyright (C) 2014  ElsonLee & WenPin Cui

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.example.co_reading;

import java.io.File;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.co_reading.painting.PaintingView;
import com.ipaulpro.afilechooser.utils.FileUtils;

public class PdfFragment extends Fragment {	
	private final String TAG = PdfFragment.class.getSimpleName();

	private final int REQUEST_CHOOSER = 1234;	
	private String mCurPdfUriString = "";
	private File mFile;	
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
        	mPaintingView.fromFile(mFile)
        				.onPageChange(mPaintingView).onDrawListener(mPaintingView)
        				.onLoad(mPaintingView).enableSwipe(true).load();
        	getActivity().getActionBar().getSelectedTab().setText(mFile.getName().trim());
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
						}
					}
				}
			break;
		default:
			break;
		}		
	}
	
	public void setDrawMode(boolean on) {
		mDrawMode = on;
		if (mPaintingView != null)
			mPaintingView.setDrawMode(on);
	}
}
