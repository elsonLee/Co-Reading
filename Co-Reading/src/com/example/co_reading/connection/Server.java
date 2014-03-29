package com.example.co_reading.connection;

import java.io.IOException;

import android.util.Log;

import com.example.co_reading.util.BinarySerialization;
import com.example.co_reading.util.ISerialization;
import com.example.co_reading.util.Packet;

public class Server extends Connection implements EndPoint {
	
	private String TAG = Server.class.getSimpleName();
	
	private Thread mWorkThread = null;
	
	private boolean mRunning = false;
	
	private Object mRunningLock = new Object();
	
	public Server() throws IOException {
		this(new BinarySerialization());
	}
	
	public Server(ISerialization serialization) throws IOException {
		super(serialization);
	}
	
	public void update() {
		
		while (mRunning) {
			Object obj = null;
			try {
				obj = readObj();
			} catch (Exception e) {
				//Log.e(TAG, "read object error");
				//this.stop();
			}

			if (obj != null) {
				// just for test
				/*
				if (obj instanceof Packet) {
					Packet pack = (Packet) obj;
					Log.d(TAG, "Received obj: mId="+ pack.mId);
				}
				*/
				notifyReceived(obj);
			}
		}

	}

	@Override
	public void run() {
		update();
	}

	@Override
	public void start() {

		if (mWorkThread != null) {
			return;
		}
		
		synchronized (mRunningLock) {
			mRunning = true;
		}
		
		mWorkThread = new Thread(this, "Client");
		mWorkThread.setDaemon(true);
		mWorkThread.start();
	}

	@Override
	public void stop() {
		synchronized (mRunningLock) {
			mRunning = false;
		}
	}

	@Override
	public void close() {
		
	}

}