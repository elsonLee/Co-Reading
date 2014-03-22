package com.example.co_reading.paint;


import android.content.Context;
import android.view.MotionEvent;

enum DISPATCH_TYPE {
	STUB_DISPATCHER,
	FILE_DISPATCHER,
	UDNEF_DISPATCHER
}

public abstract class EventDispatcher {
	SerializedData mContainer;

	
    public abstract void addObject(MotionEvent ev);

    public abstract SerializedData getObject();
    
    public abstract void flush();
    
    public static EventDispatcher getDispatcher(Context c, DISPATCH_TYPE type) {
    	switch (type) {
    	case FILE_DISPATCHER:
    		return new FileEventDispatcher(c);
    	case STUB_DISPATCHER:
    	default:
    		return new StubEventDispatcher();
    	}
    }
}
