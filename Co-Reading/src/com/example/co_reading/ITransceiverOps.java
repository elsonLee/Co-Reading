package com.example.co_reading;

import android.app.Activity;

public interface ITransceiverOps {

	boolean isSupported();
	
	boolean open(Activity activity);
	void close(Activity activity);
	boolean discovery();
	
	/*
	 * connect...
	 */
	
}
