package com.example.co_reading.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.StreamCorruptedException;

import android.util.Log;

public class BinarySerialization implements ISerialization {
	
	private final String TAG = BinarySerialization.class.getSimpleName();
	
	private InputStream mInputStream;
	
	private OutputStream mOutputStream;
	
	public BinarySerialization() {}

	@Override
	/** initialize should be called before other operations */
	public void initialize(InputStream inputStream, OutputStream outputStream) {
		mInputStream = inputStream;
		mOutputStream = outputStream;
	}

	@Override
	public Object read() {
		Object obj = null;
		ObjectInputStream objInputStream = null;
		try {
			objInputStream = new ObjectInputStream(mInputStream);
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, "create ObjectInputStream error");
		}
		
		try {
			obj = objInputStream.readObject();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return obj;
	}

	@Override
	public void write(Object object) {
		ObjectOutputStream objOutputStream = null;
		try {
			objOutputStream = new ObjectOutputStream(mOutputStream);
		} catch (IOException e) {
			Log.e(TAG, "create ObjectOutputStream error");
		}
		
		try {
			objOutputStream.writeObject(object);
		} catch (IOException e) {
			Log.e(TAG, "write Object to Stream error");
		}
	}
}
