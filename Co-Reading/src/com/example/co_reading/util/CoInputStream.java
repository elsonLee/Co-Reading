package com.example.co_reading.util;

import java.io.IOException;
import java.io.InputStream;

public class CoInputStream {
	
	private InputStream mInputStream;
	
	private CoInputStream() {}

	public CoInputStream(InputStream inputStream) {
		mInputStream = inputStream;
	}
	
    public int read(CoByteBuffer buffer) throws IOException {
        int readBytes = mInputStream.read(buffer.getBuffer(), buffer.position(), buffer.limit()-buffer.position()+1);
        buffer.updateWrite(readBytes);
        return readBytes;
    }

}
