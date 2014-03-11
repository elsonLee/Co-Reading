package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;


//@EActivity(R.layout.activity_main)
//@OptionsMenu(R.menu.actionbar)

public class MainActivity extends Activity implements OnPageChangeListener {

    public static final String SAMPLE_FILE = "sample.pdf";

    public static final String ABOUT_FILE = "about.pdf";
    
    private static final int REQUEST_ENABLE_BT = 10;
    
    private BluetoothAdapter mBluetoothAdapter;

    //@ViewById
    PDFView pdfView;

    //@NonConfigurationInstance
    String pdfName = SAMPLE_FILE;

    //@NonConfigurationInstance
    Integer pageNumber = 1;
    
//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//    	public void onReceive(Context context, Intent intent) {
//    		String action = intent.getAction();
//    		List<String> mArrayDevices = new ArrayList<String>();
//    		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//    			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//    			mArrayDevices.add(device.getName()+"\n"+device.getAddress());
//    			Log.d("BroadcastReceiver", device.getName()+":"+device.getAddress());
//    			LayoutInflater inflater = getLayoutInflater();
//    			ListView view = (ListView)inflater.inflate(R.layout.dialog_discoverydevice, null);
//    			ArrayAdapter mArrayAdapter = (ArrayAdapter)view.getAdapter();
//    			mArrayAdapter.add(mArrayDevices);
//    		}
//    	}
//    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                    
        setContentView(R.layout.myfirstactivity_main);
        
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, filter);
        
        pdfView = (PDFView)findViewById(R.id.pdfView);
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
    	// return true;
    	getMenuInflater().inflate(R.menu.main_activity_actions, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.action_addtab:
    		ActionBar.Tab newTab;
    		ActionBar actionBar = getActionBar();            
    		newTab = actionBar.newTab().setText("newTab");            
    		newTab.setTabListener(new TabListener(new FragmentTab()));
            
            actionBar.addTab(newTab);
            return true;
    	case R.id.action_bluetooth:
    		if (openBluetooth()) {
    			pairBluetoothDevices();
    		}
    		
    		return true;
    	case R.id.action_search:
    		//openSearch();
    		about();
    		return true;
    	case R.id.action_settings:
    		//openSettings();
    		setting();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);    	
    	}
    	
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    	case REQUEST_ENABLE_BT:
    		if (resultCode != RESULT_CANCELED) {
    			Log.d("onActivityResult", "REQUEST_ENABLE_BT success!");
    			pairBluetoothDevices();
    		}
    		else
    			Log.d("onActivityResult", "REQUEST_ENABLE_BT failed!");
    		break;
    	default:
    		break;    	
    	}
    	
    }
    
    private boolean openBluetooth() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// TODO Device does not support Bluetooth
			return false;
		}
		
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			return false;
		}
		return true;
    }
    
    private void pairBluetoothDevices() {
    	if (mBluetoothAdapter == null) {
    		return;
    	}
    	
    	//if (mBluetoothAdapter.startDiscovery()) {
    		Log.d("pairBluetoothDevices", "bluetooth start discovery success!");
    		BluetoothDiscoveryDialogFragment mBluetoothDialog = new BluetoothDiscoveryDialogFragment();	
    		mBluetoothDialog.show(getFragmentManager(), "nothing");
    	//}    	
    }

    //@AfterViews
    void afterViews() {
        display(pdfName, false);
    }

    //@OptionsItem
    public void about() {
        if (!displaying(ABOUT_FILE))
            display(ABOUT_FILE, true);
    }
    
    public void setting() {
    	if (!displaying(SAMPLE_FILE))
    		display(SAMPLE_FILE, true);    	
    }

    private void display(String assetFileName, boolean jumpToFirstPage) {
        if (jumpToFirstPage) pageNumber = 1;
        setTitle(pdfName = assetFileName);
        
        pdfView.fromAsset(assetFileName)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        //setTitle(format("%s %s / %s", pdfName, page, pageCount));
    }

    @Override
    public void onBackPressed() {
        if (ABOUT_FILE.equals(pdfName)) {
            display(SAMPLE_FILE, true);
        } else {
            super.onBackPressed();
        }
    }

    private boolean displaying(String fileName) {
        return fileName.equals(pdfName);
    }
}

/*
public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfirstactivity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
    	// return true;
    	getMenuInflater().inflate(R.menu.main_activity_actions, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.action_search:
    		openSearch();
    		return true;
    	case R.id.action_settings:
    		//openSettings();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);    	
    	}
    	
    }
    
    public void sendMessage(View view) {
//    	Intent intent = new Intent(this, DisplayMessageActivity.class);
//    	EditText editText = (EditText)findViewById(R.id.edit_message);
//    	String message = editText.getText().toString();
//    	
//    	intent.putExtra(EXTRA_MESSAGE, message);
//    	startActivity(intent);
    }
    
    protected void openSearch() {
    	Log.i(null, "Open Search");
    }
    
} */
