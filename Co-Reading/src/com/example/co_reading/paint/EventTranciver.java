package com.example.co_reading.paint;


import android.content.Context;
import android.view.MotionEvent;

enum DISPATCH_TYPE {
	STUB_TRANCIVER,
	FILE_TRANCIVER,
	UDNEF_TRANCIVER
}

public abstract class EventTranciver {
	SerializedData mContainer;
	IDataArrivedListener mListener;

	EventTranciver(IDataArrivedListener listener) {
		mListener = listener;
	}

    public abstract void addObject(MotionEvent ev);
    
    public abstract void flush();
    
    public static EventTranciver getDispatcher(Context c, IDataArrivedListener listener, DISPATCH_TYPE type) {
    	switch (type) {
    	case FILE_TRANCIVER:
    		return new FileEventTranciver(c, listener);
    	case STUB_TRANCIVER:
    	default:
    		return new StubEventDispatcher(listener);
    	}
    }
}
