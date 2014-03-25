package com.example.co_reading.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.util.Base64;
import android.util.Log;

public class BinarySerialization implements ISerialization {
	
	private final String TAG = BinarySerialization.class.getSimpleName();
	
	@Override
	public void write(CoByteBuffer buffer, Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);  	
		} catch (IOException e) {
			Log.e(TAG, "write Object error");
		}  
		
		byte[] objBytes = Base64.encode(baos.toByteArray(), Base64.DEFAULT);

		int len = objBytes.length;

		int pos = buffer.position();
		int limit = buffer.limit();
		if (pos + len > limit) {
			len = limit - len;
			Log.e(TAG, "buffer overflow");
		}
		
		System.arraycopy(objBytes, 0, buffer.getBuffer(), pos, len);
		buffer.updateRead(len);
	}

	@Override
	public Object read(CoByteBuffer buffer) {


		return null;
	}

	@Override
	public int getLengLength() {
		return 4;
	}

	@Override
	public void writeLength(CoByteBuffer buffer, int length) {
		buffer.putInt(length);
	}

	@Override
	public int readLength(CoByteBuffer buffer) {
		return buffer.getInt();
	}

}
