package com.example.co_reading.util;

import java.io.IOException;
import java.io.OutputStream;

public class CoOutputStream extends OutputStream {
	
	private OutputStream mOutputStream;

	private CoOutputStream() {}
	
	public CoOutputStream(OutputStream outputStream) {
		mOutputStream = outputStream;
	}

	public void write(CoByteBuffer buffer) throws IOException {
		mOutputStream.write(buffer.getBuffer(), 0, buffer.limit()+1);
	}

	@Override
	public void write(int oneByte) throws IOException {
		
	}

}
