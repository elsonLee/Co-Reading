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
import android.widget.FrameLayout;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

public class PdfFragment extends Fragment implements OnPageChangeListener {	
	private final String TAG = "PdfFragment";
	private final int REQUEST_CHOOSER = 1234;	
	private String m_curPdfUriString = "";
    private Integer m_pageNumber = 1;

	private static ViewGroup mContainerView;
	private static PDFView m_pdfView = null;
	private PainterView mPainterView;
	   
	private Painter mPainter;
      
    @Override
	public void onStart() {
        super.onStart();

        backToDisplay();
        mContainerView.invalidate();
    }
    
    @Override
	public void onStop() {
        super.onStop();        
    }
	
	@Override
	public void onCreate (Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		if (m_curPdfUriString.isEmpty()) {
			Intent getContentIntent = FileUtils.createGetContentIntent();
			Intent intent = Intent.createChooser(getContentIntent,
					"Select a PDF file");
			startActivityForResult(intent, REQUEST_CHOOSER);
		}

		mPainter = Painter.getInstance();
		if (mPainterView == null) {
			mPainterView = new PainterView(getActivity());
			PainterViewManager.getInstance().add(mPainterView);
		}

        mContainerView = ContainerView.getInstance(getActivity());     
	}
	
	@Override
	public void onDestroy () {
		super.onDestroy();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		if (m_pdfView == null) {
			m_pdfView = new PDFView(getActivity(), null);
		}

		mContainerView.removeAllViews();
		mContainerView.addView(m_pdfView, 0);
		mContainerView.addView(mPainterView, 1);
		mContainerView.setVisibility(mContainerView.getVisibility());
		return mContainerView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CHOOSER:
			if (resultCode == Activity.RESULT_OK) {
				final Uri uri = data.getData();
				String uriString = uri.toString();
				if (uri != null && FileUtils.isLocal(FileUtils.getPath(getActivity(), uri))) {

					if (m_pdfView != null) {
						File file = FileUtils.getFile(getActivity(), Uri.parse(uriString));
						if (file != null && file.exists()) {
							Log.d("file", "file:"+file);							
							display(file, true);
							m_curPdfUriString = uriString;
						}
					}
				}
			}
			break;
		default:
			break;
		}		
	}
	
    void backToDisplay() {
    	if (m_curPdfUriString.isEmpty() == false) {
    		File file = FileUtils.getFile(getActivity(), Uri.parse(m_curPdfUriString));
    		display(file, false);
    	}
    }

  /*
    public void about() {
        if (!displaying(ABOUT_FILE))
            display(ABOUT_FILE, true);
    }
    */

    /*
    private void display(String assetFileName, boolean jumpToFirstPage) {
        if (jumpToFirstPage) m_pageNumber = 1;
        // setTitle(m_pdfName = assetFileName);
        
        m_pdfView.fromAsset(assetFileName)
                .defaultPage(m_pageNumber)
                .onPageChange(this)
                .load();
    }
    */
    
    private void display(File file, boolean jumpToFirstPage) {
    	if (jumpToFirstPage) m_pageNumber = 1;
    	
    	m_pdfView.fromFile(file)
    		.defaultPage(m_pageNumber)
    		.onPageChange(this)
    		.load();    	
    }

	@Override
	public void onPageChanged(int page, int pageCount) {
		m_pageNumber = page;
		Log.i(TAG, "page:" + page + " pageCount:" + pageCount);
	}
	
	public static View getPdfView() {
		return m_pdfView;
	}
	
	public View getPainterView() {
		return mPainterView;
	}
	
	/*
    @Override
    public void onBackPressed() {
        if (ABOUT_FILE.equals(m_pdfName)) {
            display(SAMPLE_FILE, true);
        } else {
            super.onBackPressed();
        }
    }
    */
}
