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
import android.widget.RelativeLayout;

import com.ipaulpro.afilechooser.utils.FileUtils;

public class PdfFragment extends Fragment {	
	private final String TAG = "PdfFragment";
	private final int REQUEST_CHOOSER = 1234;	

	private String mCurPdfUriString = "";

	private ContainerView mContainerView;
	private PainterView mPainterView;

	@Override
	public void onCreate (Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");

        mContainerView = ContainerView.getInstance();
		mContainerView.setContext(this);

		if (mCurPdfUriString.isEmpty()) {
			Intent getContentIntent = FileUtils.createGetContentIntent();
			Intent intent = Intent.createChooser(getContentIntent,
					"Select a PDF file");
			startActivityForResult(intent, REQUEST_CHOOSER);
		}
	}
	
    @Override
	public void onStart() {
		Log.i(TAG, "onStart");
        super.onStart();
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

		if (mPainterView == null) {
			mPainterView = new PainterView(getActivity());
			PainterViewManager.getInstance().add(mPainterView);
			mContainerView.replacePainterView(mPainterView);
		}

		return mContainerView;
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
					File file = FileUtils.getFile(getActivity(), Uri.parse(uriString));
					if (file != null && file.exists()) {
							Log.d(TAG, "file:"+file);
							mContainerView.loadPdfView(file, true);
							mCurPdfUriString = uriString;
						}
					}
				}
			break;
		default:
			break;
		}		
	}

}
