package com.example.co_reading;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public class BtConnectServer extends Thread {
	
    private final BluetoothServerSocket mServerSocket;
    private final String NAME = "Co-Reading";
    
    public BtConnectServer() {
        BluetoothServerSocket tmpSocket = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmpSocket = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord(NAME, BlueToothManager.m_UUID );
        } catch (IOException e) { }

        mServerSocket = tmpSocket;
    }
 
    @Override
    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                socket = mServerSocket.accept();
            } catch (IOException e) {
                break;
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
                break;
            }
        }
    }
 
    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mServerSocket.close();
        } catch (IOException e) { }
    }


}
