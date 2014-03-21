package com.example.co_reading.connection.bluetooth.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Switch;

import com.example.co_reading.R;
import com.example.co_reading.connection.bluetooth.BlueToothManager;

public class BtDialogFragContainer extends Activity {
	
	private final String TAG = BtDialogFragContainer.class
			.getSimpleName();
	
	private BlueToothManager mBluetoothManager = BlueToothManager
			.getInstance();
	
	public static final String ACTION_BTDIALOG = "com.example.co_reading.ACTION_BTDIALOG";
	
	private Fragment mCurFrag = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bt_dialog_frag_container);
	}
	
	void addDialogFrag(Fragment fragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		if (getFragmentManager().findFragmentByTag("btdialog") == null) {
			ft.add(R.id.dialog_container, fragment, "btdialog");
		} else {
			ft.replace(R.id.dialog_container, fragment, "btdialog");
		}

		ft.commit();
	}
	
	void removeDialogFrag() {
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
			if (mCurFrag == null) {
				mCurFrag = new BtClientDialogFrag();
			}
			addDialogFrag(mCurFrag);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		removeDialogFrag();
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {

    	case BlueToothManager.REQUEST_ENABLE_BT:
            if (resultCode != RESULT_CANCELED) {
            	if (mCurFrag == null) {
            		mCurFrag = new BtClientDialogFrag();
            	}
            	addDialogFrag(mCurFrag);
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
    	
    	// FIXME: should cache
        Fragment fragment = null;
    	if (isClient == true) {
    		fragment = new BtClientDialogFrag();
    	} else {
    		fragment = new BtServerDialogFrag();
    	}

        addDialogFrag(fragment);
    }
}