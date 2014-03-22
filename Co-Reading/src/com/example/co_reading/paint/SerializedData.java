package com.example.co_reading.paint;

import android.view.MotionEvent;
import java.io.Serializable;
import java.util.ArrayList;

public class SerializedData implements Serializable {
	private static final long serialVersionUID = 1L;

	public static class Elem implements Serializable {
		private static final long serialVersionUID = 2L;
		public int event = 0x55;
		public float x = 0xaa;
		public float y = 0xaa;
	}

	public ArrayList<Elem> mList;
	
	SerializedData() {
		mList = new ArrayList<Elem>();
	}
	
	public void add(MotionEvent ev) {
		Elem d = new Elem();
        d.x = ev.getX();
        d.y = ev.getY();
        d.event = ev.getAction();
        mList.add(d);
	}
	
	public int size() {
		return mList.size();
	}
	
	public int getElemNum() {
		return mList.size();
	}
}
