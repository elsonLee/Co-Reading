package com.example.co_reading.util;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
 
public class PdfDB {
	private final String TAG = PdfDB.class.getSimpleName();

    private Context mContext = null; 
    private PdfDBHelper mDBHelper = null; 
    private SQLiteDatabase mPdfDB = null; 
 
    private static final String DATABASE_NAME = "PDF.db";  
    private static final int DATABASE_VERSION = 1;
    public static final String MAP_TABLE = "TBMap";
    private static final String LEGAL_TABLE[] = {
    	MAP_TABLE,
    };
    private String mTableName;
 
    public PdfDB(Context context){ 
        mContext = context; 
    }

    public void open(String tableName) throws SQLException {
    	if (checkTable(tableName) == false)
    	   throw new SQLException("failed to find table:" + tableName);
    	
    	mTableName = tableName;
        mDBHelper = new PdfDBHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION); 
        mPdfDB = mDBHelper.getWritableDatabase(); 
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
   
    public void createTable() throws SQLException {
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
        String strPath = "PATH=\"" +  path + "\"";
        
        cur = mPdfDB.query(mTableName, col, strPath, null, null, null, null);
        Log.i(TAG, "cursor has rows " + cur.getCount());
        cur.moveToFirst();
        SHA1 = cur.getString(0);
        
        if (cur !=null)
        	cur.close();

        Log.i(TAG, "finished query sha1 for " + path);        
        return SHA1;
    }     

}