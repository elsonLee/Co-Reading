/*Copyright (C) 2014  ElsonLee & WenPin Cui

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/	
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