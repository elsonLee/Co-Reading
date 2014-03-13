package com.example.co_reading;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

// public class MainActivity extends Activity implements OnPageChangeListener {
public class MainActivity extends Activity {

//    public static final String 	SAMPLE_FILE = "sample.pdf";
//    public static final String 	ABOUT_FILE = "about.pdf";
    
    private TransceiverImp	m_TransceiverManager = null;
    private DialogFragment	m_TransceiverDiscDialog = null;

    // find id by view
    //private PDFView 		m_pdfView = null;

    //private String 		m_pdfName = SAMPLE_FILE;
    //private Integer 		m_pageNumber = 1;    
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                    
        setContentView(R.layout.myfirstactivity_main);
        
        // m_pdfView = (PDFView)findViewById(R.id.pdfView);
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.main_activity_actions, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.action_addtab:
    		ActionBar.Tab newTab = null;
    		ActionBar actionBar = getActionBar();            
    		newTab = actionBar.newTab().setText("newTab");            
    		newTab.setTabListener(new TabListener(new FragmentTab()));
            
            actionBar.addTab(newTab);
            return true;
            
    	case R.id.action_bluetooth:		// TODO: support bluetooth & wifi
    		if (null == m_TransceiverManager || null == m_TransceiverDiscDialog) {
    			m_TransceiverManager = BlueToothManager.getInstance();
    			m_TransceiverDiscDialog = new BluetoothDiscoveryDialogFragment();
    		}
    		
    		if (true == m_TransceiverManager.isSupported()) {
    			if (true == m_TransceiverManager.open(this) && null != m_TransceiverDiscDialog) {
        			m_TransceiverDiscDialog.show(getFragmentManager(), "nothing");
    			}
    		}    		
    		return true;
    		
    	case R.id.action_search:
    		//openSearch();
    		//about();
    		return true;
    		
    	case R.id.action_settings:
    		//openSettings();
    		//setting();
    		return true;
    		
    	default:
    		return super.onOptionsItemSelected(item);    	
    	}    	
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    	case BlueToothManager.REQUEST_ENABLE_BT:
    		if (resultCode != RESULT_CANCELED) {
    			if (null != m_TransceiverDiscDialog)
    				m_TransceiverDiscDialog.show(getFragmentManager(), "nothing");
    		}
    		else
    			Log.d("onActivityResult", "REQUEST_ENABLE_BT failed!");
    		break;
    	default:
    		break;    	
    	}
    	
    }
    
//    void afterViews() {
//        display(m_pdfName, false);
//    }
//
//    public void about() {
//        if (!displaying(ABOUT_FILE))
//            display(ABOUT_FILE, true);
//    }
//    
//    public void setting() {
//    	if (!displaying(SAMPLE_FILE))
//    		display(SAMPLE_FILE, true);    	
//    }
//
//    private void display(String assetFileName, boolean jumpToFirstPage) {
//        if (jumpToFirstPage) m_pageNumber = 1;
//        setTitle(m_pdfName = assetFileName);
//        
//        m_pdfView.fromAsset(assetFileName)
//                .defaultPage(m_pageNumber)
//                .onPageChange(this)
//                .load();
        
//        m_pdfViewtest.fromAsset("sampletest.pdf")
//        .defaultPage(m_pageNumber)
//        .onPageChange(this)
//        .load();
//    }

//    @Override
//    public void onPageChanged(int page, int pageCount) {
//    	m_pageNumber = page;
//        //setTitle(format("%s %s / %s", pdfName, page, pageCount));
//    }

//    @Override
//    public void onBackPressed() {
//        if (ABOUT_FILE.equals(m_pdfName)) {
//            display(SAMPLE_FILE, true);
//        } else {
//            super.onBackPressed();
//        }
//    }

//    private boolean displaying(String fileName) {
//        return fileName.equals(m_pdfName);
//    }
}