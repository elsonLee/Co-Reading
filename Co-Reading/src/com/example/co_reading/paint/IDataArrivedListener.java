package com.example.co_reading.paint;

public interface IDataArrivedListener {
	/**
	 *
	 * @param data the data received by wireless module
	 *
	 */
	public void onDataArrived(SerializedData data);
}
