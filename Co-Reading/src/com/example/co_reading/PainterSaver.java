package com.example.co_reading;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.util.Base64;
import android.view.MotionEvent;
import android.util.Log;

public class PainterSaver {
	private Context mContext;
	private final String TAG = "PainterSaver";
	private final String FILENAME = "PainterSaver";
	private File mFile;
	private int mPos = 0;
    PainterMetadata mPmd; 

	PainterSaver(Context c) {
		mContext = c;
		mFile = new File(mContext.getFilesDir(), FILENAME);
		if (mFile.delete())
			Log.i(TAG, "deleted");
		else
			Log.i(TAG, "failed to delete");
		Log.i(TAG, FILENAME + " size:" + mFile.length());
	}
	
    public void addObject(MotionEvent ev) {
    	if (mPmd == null)
    		mPmd = new PainterMetadata();
    		
    	mPmd.add(ev);
    }
    
    public void commit() {
        try {  
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            ObjectOutputStream oos = new ObjectOutputStream(baos);  
            oos.writeObject(mPmd);  
  
            String touchBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);  
            FileOutputStream fos = new FileOutputStream(mFile, true);
            fos.write(touchBase64.getBytes());
            Log.i(TAG, "write " + touchBase64.getBytes().length + " Bytes");
            Log.i(TAG, "file size:" + mFile.length());
            fos.close();
        } catch (IOException e) {  
            e.printStackTrace();  
        }
    }

    public PainterMetadata getObject() {
    	PainterMetadata pmd = null;
        try {
        	int readed;
            FileInputStream fis = new FileInputStream(mFile);

            byte[] buf = new byte[fis.available()];
            Log.i(TAG, "pos:" + mPos + " avail:" + fis.available());
            readed = fis.read(buf, 0, buf.length);
            if (readed == -1)
            	Log.i(TAG, "EOF detected");            
            fis.close();
 
            byte[] base64Bytes = Base64.decode(buf, Base64.DEFAULT);  
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);  
            ObjectInputStream ois = new ObjectInputStream(bais);
            try {
            	pmd = (PainterMetadata)ois.readObject();
            } catch (Exception e) {
            	e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
        	Log.i(TAG, "file not found");
        } catch (EOFException e) {  
            Log.i(TAG, "end of file"); 
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
        return pmd;
    }
}
