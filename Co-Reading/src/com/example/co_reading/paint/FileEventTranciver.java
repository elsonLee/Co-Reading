package com.example.co_reading.paint;

import java.io.*;
import java.util.TimerTask;
import java.util.Timer;

import android.content.Context;
import android.os.Looper;
import android.util.Base64;
import android.view.MotionEvent;
import android.widget.Toast;
import android.util.Log;

public class FileEventTranciver extends EventTranciver {
	private final String TAG = "FileEventTranciver";
	private Context mContext;

	private final String FILENAME = "FileEventTranciver";
	private File mFile;
	private Timer mTimer;

	FileEventTranciver(Context c, IDataArrivedListener listener) {
		super(listener);
		mContext = c;
		mFile = new File(mContext.getFilesDir(), FILENAME);
		mFile.delete();
		mTimer = new Timer();
		mContainer = new SerializedData();
		Log.i(TAG, FILENAME + " size:" + mFile.length());
	}

    public void addObject(MotionEvent ev) {
    	mContainer.add(ev);
    }

    public void flush() {
    	if (mContainer.getElemNum() == 0)
    		return;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            ObjectOutputStream oos = new ObjectOutputStream(baos);  
            oos.writeObject(mContainer);  
            mContainer.removeAll();

            String touchBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);  
            FileOutputStream fos = new FileOutputStream(mFile, true);
            fos.write(touchBase64.getBytes());
            String msg = "write " + touchBase64.getBytes().length + " Bytes";
        	Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

            Log.i(TAG, "file size:" + mFile.length());
            fos.close();
           	mTimer.schedule(new TimerTask() {
           		public void run() {
           			Looper.prepare();
           			FileEventTranciver.this.waitForData();
           			Looper.loop();
           		}
           	}, 3*1000);
        } catch (IOException e) {  
            e.printStackTrace();
        }
    }
    
    private void waitForData() {
    	SerializedData data = null;
    	FileInputStream fis = null;

    	try {
            fis = new FileInputStream(mFile);
    	} catch (FileNotFoundException e) {
    		Log.i(TAG, "file not found for read");
    		return;
    	}
    	
    	int len = (int)(mFile.length());
    	byte[] buf = new byte[len];

    	try {
	    	int readed = fis.read(buf);
			if (readed == -1)
				Log.i(TAG, "EOF detected");            
			fis.close();
			mFile.delete();
	    	String msg = "Readed " + readed + " Bytes";
	    	Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}

		byte[] base64Bytes = Base64.decode(buf, Base64.DEFAULT);  
		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);  
 
        try {
    		ObjectInputStream ois = new ObjectInputStream(bais);  
            data = (SerializedData)ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        mListener.onDataArrived(data);
    }

}
