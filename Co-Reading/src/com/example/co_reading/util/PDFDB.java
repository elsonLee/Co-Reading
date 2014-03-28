package com.example.co_reading.util;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
	    
       sql = "CREATE TABLE IF NOT EXISTS " + mTableName  
                + " (SHA1 TEXT PRIMARY KEY, PATH TEXT, JNUM INTEGER);";

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
    					" (SHA1, PATH, JNUM) values (?,?,?);";
    	Object[] args = new Object[]{SHA1, path, jumpNum};
        
        mPdfDB.execSQL(sqlstr,args);  

    	Log.i(TAG, "insertItem to " + mTableName);        
    }     
 
    public String getSHA1(String path) throws SQLException {
    	Cursor cur = null;
        String SHA1 = null;
   
        String col[] = {"sha1", "path", "jnum"};
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
}