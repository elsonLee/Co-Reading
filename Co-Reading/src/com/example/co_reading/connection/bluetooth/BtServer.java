package com.example.co_reading.connection.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.co_reading.connection.EndPoint;
import com.example.co_reading.util.BinarySerialization;
import com.example.co_reading.util.ISerialization;

public class BtServer extends BtConnection implements EndPoint {
	
	private final String TAG = BtServer.class.getSimpleName();
	
	private final ISerialization mSerialization;
	
    private final BluetoothServerSocket mServerSocket;

    private BluetoothSocket mSocket = null;

    private Thread mWorkThread = null;

    private final String NAME = "Co-Reading";
    
    public BtServer() {
    	this(new BinarySerialization());
    }
    
    public BtServer(ISerialization serialization) {
        BluetoothServerSocket tmpSocket = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmpSocket = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord(NAME, BlueToothManager.m_UUID );
        } catch (IOException e) { }

        mServerSocket = tmpSocket;
        
        // FIXME: link should be async
        BluetoothSocket socket = null;
        try {
        	socket = mServerSocket.accept();
        } catch (IOException e) {
        	Log.e(TAG, "accept connection error");
        }

        // If a connection was accepted
        if (socket != null) {
        	// Do work to manage the connection (in a separate thread)
            // manageConnectedSocket(socket);
            try {
				mServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        mSerialization = serialization;
        
        Initialize(mSocket, mSerialization);
    }
 
	public void update() {
		
		while (true) {
			Object obj = null;
			try {
				obj = readObj();
			} catch (Exception e) {
				Log.e(TAG, "read object error");
			}

			if (obj != null) {
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
		
		mWorkThread = new Thread(this, "Client");
		mWorkThread.setDaemon(true);
		mWorkThread.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
