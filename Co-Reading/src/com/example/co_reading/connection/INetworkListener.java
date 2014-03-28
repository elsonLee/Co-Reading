package com.example.co_reading.connection;

public interface INetworkListener {

	public void onNetworkConnected();
	
	public void onNetworkDisconnected();
	
	public void onNetworkReceivedObj(Object object);
	
	// public void idle() {};

}
