package com.example.co_reading.paint;

import android.os.Handler;
import android.os.Message;

public class UIUpdateHandler extends Handler {
	private Painting mPainting;
	
	UIUpdateHandler(Painting p) {
		mPainting = p;
	}

    @Override
    public void handleMessage(Message msg) {
	    switch (msg.what) {
	        case (EventTranciver.UPDATE_UI): {
	        	mPainting.handle_ui_update();
	            break;
	        }
	        default:
	            break;
	    }
    }
}
