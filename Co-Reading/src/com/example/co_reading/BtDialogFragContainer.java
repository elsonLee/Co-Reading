package com.example.co_reading;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Switch;

public class BtDialogFragContainer extends Activity {
	
	private final String TAG = BtDialogFragContainer.class
			.getSimpleName();
	
	private BlueToothManager mBluetoothManager = BlueToothManager
			.getInstance();
	
	public static final String ACTION_BTDIALOG = "com.example.co_reading.ACTION_BTDIALOG";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bt_dialog_frag_container);
	}
	
	void openDiscoveryDialog(Fragment fragment) {
		if (getFragmentManager().findFragmentByTag("btdialog") == null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			Fragment dialogFrag = fragment;
			ft.add(R.id.dialog_container, dialogFrag, "btdialog");
			ft.commit();
		}
	}
	
	void replaceDiscoveryDialog(Fragment fragment) {
		if (getFragmentManager().findFragmentByTag("btdialog") == null) {
			openDiscoveryDialog(fragment);
		} else {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.dialog_container, fragment, "btdialog");
			ft.commit();
		}
	}
	
	void closeDiscoveryDialog() {
		Fragment btDialog = getFragmentManager().findFragmentByTag("btdialog");
		if (btDialog != null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.remove(btDialog);
			ft.commit();
		}
	}
	
	@Override 
	protected void onResume() {
		super.onResume();
		
		if (mBluetoothManager.open(this) == true) {
			Fragment fragment = new BtClientDialogFrag();
			openDiscoveryDialog(fragment);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		closeDiscoveryDialog();
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {

    	case BlueToothManager.REQUEST_ENABLE_BT:
            if (resultCode != RESULT_CANCELED) {
            	Fragment fragment = new BtClientDialogFrag();
            	openDiscoveryDialog(fragment);
            }
            else
                Log.e(TAG, "REQUEST_ENABLE_BT failed!");
            break;
    	default:
            break;
    	}
    }
    
    public void onClickToggle(View view) {
    	boolean isClient = ((Switch) view).isChecked();
    	
        Fragment fragment = null;
    	if (isClient == true) {
    		fragment = new BtClientDialogFrag();
    	} else {
    		// fragment = new Blue
    	}

        openDiscoveryDialog(fragment);
    }
}