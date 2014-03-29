package com.example.co_reading.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
 
public class PDFDB {
	private final String TAG = PDFDB.class.getSimpleName();

    private Context mContext = null; 
    private PdfDBHelper mDBHelper = null; 
    private SQLiteDatabase mPdfDB = null; 
 
    private static final String DATABASE_NAME = "co_writing.db";  
    private static final int DATABASE_VERSION = 1;
    public static final String SHA1_TABLE = "TBMap";
    private static final String LEGAL_TABLE[] = {
    	SHA1_TABLE,
    };
    private String mTableName;
 
    public PDFDB(Context context){ 
        mContext = context;
    }

    public void open(String tableName) throws SQLException {
        mTableName = tableName;
    	if (checkTable(mTableName) == false)
    	   throw new SQLException("failed to find table:" + mTableName);
    	
        mDBHelper = new PdfDBHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION); 
        mPdfDB = mDBHelper.getWritableDatabase();
        if (tabbleIsExist(mTableName) == false) {
        	Log.i(TAG, mTableName + " doesn't exist, create ...");
        	createTable();
        }
        Log.i(TAG, "open");
    }

    public void close(){ 
    	Log.i(TAG, "close");
        mDBHelper.close(); 
    }
    
    private boolean checkTable(String tableName) {
    	boolean result = false;
    	for (String name : LEGAL_TABLE) {
    		if (name.equals(tableName))
    			result = true;
    	}
    	return result;
    }
   
    private void createTable() throws SQLException {
       String sql = "drop table " + mTableName;

       try {
    	   mPdfDB.execSQL(sql);
       } catch (SQLException e) {
    	   Log.e(TAG, "table " + mTableName + " doesn't exist");
       }
	    
       sql = "create table if not exists " + mTableName  
                + " (sha1 text primary key, path text, defaultPage integer);";

       mPdfDB.execSQL(sql);  
       Log.i(TAG, "Succeed create table" + mTableName);
    }
 
    public void cleanTable() throws SQLException {

       	mPdfDB.delete(mTableName, null, null);  

        Log.i(TAG, "Clear table " + mTableName);        
    }      
   
    public void insertItem(String SHA1, String path, int jumpNum)
    			throws SQLException {
       	String sqlstr = "insert into " + mTableName + 
    					" (sha1, path, defaultPage) values (?,?,?);";
    	Object[] args = new Object[]{SHA1, path, jumpNum};
        
        mPdfDB.execSQL(sqlstr,args);  

    	Log.i(TAG, "insertItem to " + mTableName);        
    }     
 
    public String getSHA1(String path) throws SQLException {
    	Cursor cur = null;
        String SHA1 = null;
   
        String col[] = {"sha1", "path", "defaultPage"};
        String strPath = "PATH='" +  path.trim() + "'";
        
        cur = mPdfDB.query(mTableName, col, strPath, null, null, null, null);
        if (cur.getCount() == 0)
        	return null;

        cur.moveToFirst();
        SHA1 = cur.getString(0);
        
        if (cur !=null)
        	cur.close();

        Log.i(TAG, "finished query sha1 for " + path);        
        return SHA1;
    }
    
    public int getDefaultPage(String path) throws SQLException {
    	Cursor cur = null;
        int defaultPage;
   
        String col[] = {"sha1", "path", "defaultPage"};
        String strPath = "PATH='" +  path.trim() + "'";
        
        cur = mPdfDB.query(mTableName, col, strPath, null, null, null, null);
        if (cur.getCount() == 0)
        	return -1;

        cur.moveToFirst();
        defaultPage = cur.getInt(2);
        
        if (cur !=null)
        	cur.close();

        Log.i(TAG, "finished query default page for " + path);        
        return defaultPage;
    }
    
    public void updateDefaultPageNum(String SHA1, int newVal) {
    	ContentValues cv = new ContentValues();
    	cv.put("defaultPage", newVal);
    	mPdfDB.update(mTableName, cv, "SHA1 = ?", new String[]{SHA1});
    }
    
    private boolean tabbleIsExist(String tableName){  
        boolean result = false;  
        if(tableName == null){  
             return false;  
        }  
            
        Cursor cursor = null;  
        try {  
         	String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"+tableName.trim()+"' ";  
            cursor = mPdfDB.rawQuery(sql, null);  
            if(cursor.moveToNext()){  
                int count = cursor.getInt(0);  
                if(count>0)
                    result = true;  
            }  
        } catch (Exception e) {  
          	e.printStackTrace();
        }                  
        return result;  
    }
    
    public void deletePaintTable(String name) {
   		String sql = "drop table " + name;

		try {
			mPdfDB.execSQL(sql);
		} catch (SQLException e) {
			Log.e(TAG, "table " + mTableName + " doesn't exist");
		}
    }
    
    public void createPaintTable(String name) {
    	/**
    	 * argument: SHA1 of PDF file
    	 */
    	name = "SHA1" + name;
 
    	String sql = "create table if not exists " + name  
                	+ " (pageNum integer primary key, bitmap blob);";

    	mPdfDB.execSQL(sql);  
    	Log.i(TAG, "Succeed create table" + name);
    }
    
    public Bitmap getBitmap(String name, int pageNum) throws SQLException {
    	Cursor cur = null;
    	name = "SHA1" + name;

        String col[] = {"bitmap"};
        String where = "pageNum = " + pageNum;
        byte[] bin;
        
        cur = mPdfDB.query(name, col, where, null, null, null, null);
        if (cur.getCount() == 0) {
        	Log.i(TAG, "can't find bitmap for pagenum " + pageNum);
        	return null;
        }

        cur.moveToFirst();
        bin = cur.getBlob(cur.getColumnIndex("bitmap"));
        
        if (cur !=null)
        	cur.close();

        Log.i(TAG, "finished query bitmap for pagenum " + pageNum);
        
        ByteArrayInputStream is = new ByteArrayInputStream(bin);
        
        return BitmapFactory.decodeStream(is);
    }
    
    public void insertBitmap(String name, int pageNum, Bitmap bm) {
    	name = "SHA1" + name;
    	mPdfDB.delete(name, "pageNum = " + pageNum, null);
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	bm.compress(Bitmap.CompressFormat.PNG, 80, os);
    	ContentValues cv = new ContentValues();
    	cv.put("pageNum", pageNum);
    	cv.put("bitmap", os.toByteArray());
    	mPdfDB.insert(name, null, cv);
    }
}