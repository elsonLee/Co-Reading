package com.example.co_reading.connection;

public interface EndPoint extends Runnable {
	
	// public void addListener(Listener listener);
	
	// public void removeListener(Listener listener);
	
	public void run();
	
	public void start();
	
	public void stop();
	
	public void close();

}
