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
package com.example.co_reading.connection.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.co_reading.connection.Server;
import com.example.co_reading.util.BinarySerialization;
import com.example.co_reading.util.ISerialization;
import com.example.co_reading.util.Packet;

public class BtServer extends Server {
	
	private final String TAG = BtServer.class.getSimpleName();
	
    private final BluetoothServerSocket mServerSocket;

    private BluetoothSocket mSocket = null;

    private final String NAME = "Co-Reading";
    
    public BtServer() throws IOException {
    	this(new BinarySerialization());
    }
    
    public BtServer(ISerialization serialization) throws IOException {
    	super(serialization);

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
        	Log.e(TAG, "accept connection successful");
        } catch (IOException e) {
        	Log.e(TAG, "accept connection error");
        }

        // If a connection was accepted
        if (socket != null) {
        	// Do work to manage the connection (in a separate thread)
            // manageConnectedSocket(socket);
        	/*
            try {
				mServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/

            // Initialize
            Initialize(socket.getInputStream(), socket.getOutputStream());

        } else {
            try {
				mServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
    }

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
