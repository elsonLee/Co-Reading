package com.example.co_reading.util;

import java.io.Serializable;

public class Packet implements Serializable {

	private static final long serialVersionUID = 3325687393518576184L;

	public int mId;
	
	public Packet(int id) {
		mId = id;
	}
}
