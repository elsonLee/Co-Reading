package com.example.co_reading.util;

public interface ISerialization {
	
	public void write(CoByteBuffer buffer, Object object);
	
	public Object read(CoByteBuffer buffer);
	
	public int getLengLength();
	
	public void writeLength(CoByteBuffer buffer, int length);
	
	public int readLength(CoByteBuffer buffer);

}
