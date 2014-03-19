package com.example.co_reading;

import android.app.Activity;

public interface ITransceiverOps {

	boolean isSupported();
	
	/* true: opened, false: opening */
	boolean open(Activity activity);
	void close(Activity activity);
	boolean discovery();
	
	void onResume();
	void onPause();
	
	/*
	 * connect...
	 */
	
}
