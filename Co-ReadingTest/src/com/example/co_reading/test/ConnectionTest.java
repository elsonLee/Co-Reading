package com.example.co_reading.test;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import android.util.Log;

import com.example.co_reading.connection.Client;
import com.example.co_reading.connection.INetworkListener;
import com.example.co_reading.connection.Server;
import com.example.co_reading.connection.pipe.PipeClient;
import com.example.co_reading.connection.pipe.PipeServer;
import com.example.co_reading.util.Packet;

public class ConnectionTest extends TestCase {
	
	private final String TAG = ConnectionTest.class.getSimpleName();

	static {
		System.loadLibrary("gen_pipe");
	}
	
	FileDescriptor[] fd1;
	FileDescriptor[] fd2;

    FileInputStream fileReadStream1;
    FileOutputStream fileWriteStream1;
    FileInputStream fileReadStream2;
    FileOutputStream fileWriteStream2;
    
    Client client;
    Server server;
    
    List<Object> clientPackList = new ArrayList<Object>();
    List<Object> serverPackList = new ArrayList<Object>();

    public native FileDescriptor[] createpipe(); 
    public native void closefd(FileDescriptor fdesc); 
	
	public ConnectionTest(String name) {
		super(name);
		
		fd1 = createpipe();
		fd2 = createpipe();
		
        fileReadStream1 = new FileInputStream(fd1[0]);
        fileWriteStream1 = new FileOutputStream(fd1[1]);
        fileReadStream2 = new FileInputStream(fd2[0]);
        fileWriteStream2 = new FileOutputStream(fd2[1]);
        
        try {
			client = new PipeClient();
			client.Initialize(fileReadStream1, fileWriteStream2);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
			server = new PipeServer();
			server.Initialize(fileReadStream2, fileWriteStream1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testReadObj() {
		fail("Not yet implemented");
	}

	public void testSend() {

		clientPackList.add(new Packet(1));
		clientPackList.add(new Packet(10));
		clientPackList.add(new Packet(100));
		serverPackList.add(new Packet(2));
		serverPackList.add(new Packet(20));
		serverPackList.add(new Packet(200));

		client.addListener(new INetworkListener() {
			
			int mCnt = 0;

			@Override
			public void onNetworkConnected() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onNetworkDisconnected() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onNetworkReceivedObj(Object object) {
				if (object instanceof Packet) {
					Packet pack = (Packet) object;
					TestCase.assertEquals(true, pack.equals(clientPackList.get(mCnt++)));
				}	
			}
		});

		server.addListener(new INetworkListener() {
			
			int mCnt = 0;

			@Override
			public void onNetworkConnected() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onNetworkDisconnected() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onNetworkReceivedObj(Object object) {
				if (object instanceof Packet) {
					Packet pack = (Packet) object;
					TestCase.assertEquals(true, pack.equals(serverPackList.get(mCnt++)));
				}	
			}
		});
		
		client.start();
		server.start();
		
		client.send(clientPackList.get(0));
		client.send(clientPackList.get(1));
		server.send(serverPackList.get(0));
		server.send(serverPackList.get(1));
		client.send(clientPackList.get(2));
		server.send(serverPackList.get(2));
		
	}

	public void testAddListener() {
		fail("Not yet implemented");
	}

	public void testRemoveListener() {
		fail("Not yet implemented");
	}

	public void testNotifyConnected() {
		fail("Not yet implemented");
	}

	public void testNotifyDisconnected() {
		fail("Not yet implemented");
	}

	public void testNotifyIdle() {
		fail("Not yet implemented");
	}

	public void testNotifyReceived() {
		fail("Not yet implemented");
	}

}
