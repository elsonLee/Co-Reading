package com.example.co_reading.util;

import java.io.Serializable;

public class Packet implements Serializable {

	private static final long serialVersionUID = 3325687393518576184L;

	public int mEvent;
	public float mX;
	public float mY;

	public Packet(int event, float x, float y) {
		mEvent = event;
		mX = x;
		mY = y;
	}
}
