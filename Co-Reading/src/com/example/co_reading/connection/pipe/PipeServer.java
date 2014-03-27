package com.example.co_reading.connection.pipe;

import java.io.IOException;

import com.example.co_reading.connection.Server;
import com.example.co_reading.util.ISerialization;

public class PipeServer extends Server {
	
	private String TAG = PipeClient.class.getSimpleName();

	public PipeServer() throws IOException {
		super();
	}
	
	public PipeServer(ISerialization serialization) throws IOException {
		super(serialization);
	}

	public void cancel() {

	}
	
	/**
	 * initialize(InputStream, OutputStream, 
	 */
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}