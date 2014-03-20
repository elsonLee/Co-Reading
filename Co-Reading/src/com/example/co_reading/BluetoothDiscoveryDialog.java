package com.example.co_reading;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class BluetoothDiscoveryDialog extends Activity {
	
	private final String TAG = BluetoothDiscoveryDialog.class
			.getSimpleName();
	
	private BlueToothManager mBluetoothManager = BlueToothManager
			.getInstance();
	
	public static final String ACTION_BTDIALOG = "com.example.co_reading.ACTION_BTDIALOG";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.btdialog_frag);
	}
	
	void openDiscoveryDialog() {
		if (getFragmentManager().findFragmentByTag("btdialog") == null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			Fragment dialogFrag = new BluetoothDiscoveryDialogFrag();
			ft.add(R.id.dialog_container, dialogFrag, "btdialog");
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
			openDiscoveryDialog();
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
            	openDiscoveryDialog();
            }
            else
                Log.e(TAG, "REQUEST_ENABLE_BT failed!");
            break;
    	default:
            break;
    	}

    }
}