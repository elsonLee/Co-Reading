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
import android.widget.TextView;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

public class FragmentTab extends Fragment implements OnPageChangeListener {
	
	private static final int REQUEST_CHOOSER = 1234;
	
	private View m_rootView = null;
	private String m_currentFileName = "";
	
	// find id by view
	public static final String 	SAMPLE_FILE = "sample.pdf";
	public static final String 	ABOUT_FILE = "about.pdf";
	private PDFView m_pdfView = null;
	
    private String 			m_pdfName = SAMPLE_FILE;
    private Integer 		m_pageNumber = 1;    
	
	@Override
	public void onCreate (Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		m_pdfView = new PDFView(getActivity(), null);
		
		Log.v("tab", "onCreate"+m_pdfView);
		Intent getContentIntent = FileUtils.createGetContentIntent();
		Intent intent = Intent.createChooser(getContentIntent, "Select a PDF file");
		startActivityForResult(intent, REQUEST_CHOOSER);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {

		//Log.v("tab", "onCreateView");
		//m_rootView = inflater.inflate(R.layout.fragment_pdfview, container, false);
		m_rootView = m_pdfView;
		
		//m_pdfView = (PDFView)m_rootView.findViewById(R.id.pdfView);
		//TextView textView = (TextView)m_rootView.findViewById(R.id.pdfViewfragment);
		
		return m_rootView;
	}
	
	@Override
	public void onDestroy () {
		super.onDestroy();
		Log.v("tab", "onDestroy");
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CHOOSER:
			if (resultCode == Activity.RESULT_OK) {
				final Uri uri = data.getData();
				String path = FileUtils.getPath(getActivity(), uri);
				if (path != null && FileUtils.isLocal(path)) {
					// open file
					if (m_rootView != null) {
						Log.d("open pdf", "file path:"+path);
						// display(path, true);
						File file = FileUtils.getFile(getActivity(), uri);
						if (file != null) {
							Log.d("file", "file:"+file);
							display(file, true);
						}
						//TextView textView = (TextView)m_rootView.findViewById(R.id.pdfViewfragment);
						//textView.setText(path);
					}
				}
			}
			break;
		default:
			break;
		}		
	}
	
    void afterViews() {
        display(m_pdfName, false);
    }

    public void about() {
        if (!displaying(ABOUT_FILE))
            display(ABOUT_FILE, true);
    }
    
    public void setting() {
    	if (!displaying(SAMPLE_FILE))
    		display(SAMPLE_FILE, true);    	
    }

    private void display(String assetFileName, boolean jumpToFirstPage) {
        if (jumpToFirstPage) m_pageNumber = 1;
        // setTitle(m_pdfName = assetFileName);
        
        m_pdfView.fromAsset(assetFileName)
                .defaultPage(m_pageNumber)
                .onPageChange(this)
                .load();
    }
    
    private void display(File file, boolean jumpToFirstPage) {
    	if (jumpToFirstPage) m_pageNumber = 1;
    	
    	m_pdfView.fromFile(file)
    		.defaultPage(m_pageNumber)
    		.onPageChange(this)
    		.load();    	
    }

	@Override
	public void onPageChanged(int page, int pageCount) {
		// TODO Auto-generated method stub
		m_pageNumber = page;
	}
	
//    @Override
//    public void onBackPressed() {
//        if (ABOUT_FILE.equals(m_pdfName)) {
//            display(SAMPLE_FILE, true);
//        } else {
//            super.onBackPressed();
//        }
//    }
    
    private boolean displaying(String fileName) {
        return fileName.equals(m_pdfName);
    }

}
