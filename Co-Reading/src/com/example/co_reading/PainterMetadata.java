package com.example.co_reading;

import android.view.MotionEvent;
import java.io.Serializable;
import java.util.ArrayList;

public class PainterMetadata implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static class Data implements Serializable {
		public int event = 0x55;
		public float x = 0xaa;
		public float y = 0xaa;
	}
	public ArrayList<Data> mList;

	
	PainterMetadata() {
		mList = new ArrayList<Data>();
	}
	
	public void add(MotionEvent ev) {
		Data d = new Data();
        d.x = ev.getX();
        d.y = ev.getY();
        d.event = ev.getAction();
        mList.add(d);
	}
}
