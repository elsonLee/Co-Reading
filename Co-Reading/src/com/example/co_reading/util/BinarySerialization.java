package com.example.co_reading.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BinarySerialization implements ISerialization {
	
	private final ByteArrayInputStream mbyteArrayInputStream = new ByteArrayInputStream();
	private final ByteArrayOutputStream mbyteArrayOutputStream = new ByteArrayOutputStream();

	@Override
	public void write(CoByteBuffer buffer, Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object read(CoByteBuffer buffer) {
		// TODO Auto-generated method stub
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
