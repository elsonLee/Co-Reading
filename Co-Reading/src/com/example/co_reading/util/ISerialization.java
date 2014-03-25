package com.example.co_reading.util;

import java.io.InputStream;
import java.io.OutputStream;


public interface ISerialization {
	
	public void initialize(InputStream inputStream, OutputStream outputStream);
	
	public Object read();
	
	public void write(Object object);
}
