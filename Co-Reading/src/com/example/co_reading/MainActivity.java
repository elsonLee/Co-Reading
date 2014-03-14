package com.example.co_reading;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    private TransceiverImp	m_TransceiverManager = null;
    private DialogFragment	m_TransceiverDiscDialog = null;
    private Painter mPainter;
    
    private RetainedFragment m_retainedFragment = null;
    private OnRestoreData m_restoreData = null;
    
    public class OnRestoreData {
    	List<ActionBar.Tab> m_tabList = new ArrayList<ActionBar.Tab>();
    	int m_curTabPos;
    	
     	void addToTabList(ActionBar.Tab tab) {
    		m_tabList.add(tab);
    	}
    	
    	void restoreNavigationTab(ActionBar actionBar) {
    		
            if (m_tabList.isEmpty() == false) {
            	for (ActionBar.Tab mtab : m_tabList) {
            		actionBar.addTab(mtab);
            	}
            }
            
            actionBar.setSelectedNavigationItem(m_curTabPos);
    	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfirstactivity_main);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        FragmentManager fm = getFragmentManager();
        m_retainedFragment = (RetainedFragment) fm.findFragmentByTag("restoreData");
        
        if (m_retainedFragment == null) {
        	m_retainedFragment = new RetainedFragment();
        	fm.beginTransaction().add(m_retainedFragment, "restoreData").commit();
        	if (m_restoreData == null) 
        		m_restoreData = new OnRestoreData();
        	m_retainedFragment.setData(m_restoreData);
        } else {
        	m_restoreData = m_retainedFragment.getData();
        	ActionBar mactionBar = getActionBar();
        	m_restoreData.restoreNavigationTab(mactionBar);
        }
    }
    
    @Override
    protected void onDestroy() {
    	ActionBar actionBar = getActionBar(); 
    	m_restoreData.m_curTabPos = actionBar.getSelectedNavigationIndex();
    	m_retainedFragment.setData(m_restoreData);
    	
    	super.onDestroy();
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
    		newTab.setTabListener(new TabListener(new PdfFragment()));
    		
    		actionBar.addTab(newTab);
    		m_restoreData.addToTabList(newTab);
    		
    		if (actionBar.getNavigationItemCount() > 1)
    			actionBar.setSelectedNavigationItem(actionBar.getNavigationItemCount()-1);
            
            return true;

    	case R.id.action_bluetooth:		// TODO: support bluetooth & wifi
            if (null == m_TransceiverManager || null == m_TransceiverDiscDialog) {
                m_TransceiverManager = BlueToothManager.getInstance();
                m_TransceiverDiscDialog = new BluetoothDiscoveryDialog();
            }

            if (true == m_TransceiverManager.isSupported()) {
                if (true == m_TransceiverManager.open(this) && null != m_TransceiverDiscDialog) {
                    m_TransceiverDiscDialog.show(getFragmentManager(), "nothing");
                }
            }
            return true;

        case R.id.action_painter:
            mPainter = Painter.getInstance();
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
}
