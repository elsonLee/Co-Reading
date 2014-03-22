package com.example.co_reading.paint;

import java.io.*;


import android.content.Context;
import android.util.Base64;
import android.view.MotionEvent;
import android.widget.Toast;
import android.util.Log;

public class FileEventDispatcher extends EventDispatcher {
	private final String TAG = "EventDispatcher";
	private Context mContext;

	private final String FILENAME = "EventDispatcher";
	private File mFile;

	FileEventDispatcher(Context c) {
		mContext = c;
		mFile = new File(mContext.getFilesDir(), FILENAME);
		mFile.delete();
		Log.i(TAG, FILENAME + " size:" + mFile.length());
	}
	
    public void addObject(MotionEvent ev) {
    	if (mContainer == null)
    		mContainer = new SerializedData();
    		
    	mContainer.add(ev);
    }

    public void flush() {
        try {  
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            ObjectOutputStream oos = new ObjectOutputStream(baos);  
            oos.writeObject(mContainer);  
  
            String touchBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);  
            FileOutputStream fos = new FileOutputStream(mFile, true);
            fos.write(touchBase64.getBytes());
            String msg = "write " + touchBase64.getBytes().length + " Bytes";
        	Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

            Log.i(TAG, "file size:" + mFile.length());
            fos.close();
        } catch (IOException e) {  
            e.printStackTrace();
        }
    }

    public SerializedData getObject() {
    	SerializedData data = null;
    	FileInputStream fis = null;

    	try {
            fis = new FileInputStream(mFile);
    	} catch (FileNotFoundException e) {
    		Log.i(TAG, "file not found for read");
    		return null;
    	}
    	
    	int len = (int)(mFile.length());
    	byte[] buf = new byte[len];

    	try {
	    	int readed = fis.read(buf);
			if (readed == -1)
				Log.i(TAG, "EOF detected");            
			fis.close();
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
            return null;
        }
   
        return data;
    }
}
