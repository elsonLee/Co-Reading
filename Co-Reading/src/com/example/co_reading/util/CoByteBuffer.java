package com.example.co_reading.util;

import android.util.Log;

/** simulate ByteBuffer in NIO, cuz btSocket can send byte[] only! */
/** could read & write to mLimit */
public class CoByteBuffer {
	
	private String TAG = CoByteBuffer.class.getSimpleName();
	
	/* LITTLE ENDIAN */

	private CoByteBuffer() {}
	
	private byte[] mBuffer = null;
	
	private int mCapacity;
	
	private int mPosition;
	
	private int mLimit;
	
	public CoByteBuffer(int capacity) {
		if (capacity > 0) {
			mBuffer = new byte[capacity];
			mCapacity = capacity;
			mPosition = 0;
			mLimit = mCapacity-1;
		}
	}
	
	public byte[] getBuffer() {
		return mBuffer;
	}
	
	public boolean hasRemaining() {
		if (mLimit-mPosition >= 0)
			return true;
		else
			return false;
	}
	
	public int limit() {
		return mLimit;
	}
	
	public int position() {
		return mPosition;
	}
	
	public int capacity() {
		return mCapacity;
	}
	
	public int remaining() {
		return (mLimit-mPosition + 1);
	}
	
	public void updateRead(int byteNum) {
		mPosition += byteNum;
		
		if (mPosition > mLimit) {
			Log.e(TAG, "updateRead Overflow");
		}
	}
	
	public void updateWrite(int byteNum) {
		mPosition += byteNum;
		
		if (mPosition > mLimit) {
			Log.e(TAG, "updateRead Overflow");
		}
	}
	
	public CoByteBuffer putInt(int value) {
		for (int i = 0; i < 4; i++) {
			if (mLimit >= mPosition) {
				mBuffer[mPosition++] = (byte)((value>>i) & 0xFF);
			} else {
				Log.e(TAG, "putInt Overflow");
			}
		}
		
		return this;
	}
	
	public int getInt() {
		int data = 0;
		
		for (int i = 0; i < 4; i++) {
			data |= (mBuffer[mPosition++] << i);
		}
		
		return data;
	}
	
	public CoByteBuffer flip() {
		mLimit = mPosition-1;
		mPosition = 0;
		
		return this;
	}
	
	public CoByteBuffer clear() {
		mPosition = 0;
		mLimit = mCapacity-1;
		
		return this;
	}
	
	public CoByteBuffer compact() {
		int remain = remaining();
		System.arraycopy(mBuffer, mPosition, mBuffer, 0, remain);
		mPosition = remain;
		mLimit = mCapacity-1;
		
		return this;
	}
}
