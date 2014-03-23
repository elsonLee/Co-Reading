package com.example.co_reading.paint;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
 
public class PaintingDB {
    private Context mContext = null; 
    private PaintingDBHelper mDBHelper = null; 
    private SQLiteDatabase mTestDatabase = null; 
 
    private static final String DATABASE_NAME = "CoWriting.db";  
    private static final int DATABASE_VERSION = 1;  
    private static final String TABLE_SEED = "TBseed";  
    private static final String TABLE_INFO = "TBinfo";  
 
    public PaintingDB(Context context){ 
        mContext = context; 
    }

    public void open(){
        mDBHelper = new PaintingDBHelper(mContext,DATABASE_NAME, null, DATABASE_VERSION); 
        mTestDatabase = mDBHelper.getWritableDatabase(); 
        Log.i("testSeedDB", "open");
    }

    public void close(){ 
         mDBHelper.close(); 
    }  
   
    public void CreateSeedTable() {  
    // 创建数据表是先删除以前的，以免出错
       String sql = "drop table "+ TABLE_SEED;
    try {
        mTestDatabase.execSQL(sql);
    } catch (SQLException e) {
    }   
    // second create table
        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_SEED  
                + " (ID INTEGER PRIMARY KEY, ToyID INTEGER,ToySeed BLOB,ToyMemo TEXT);";  
        try {  
        mTestDatabase.execSQL(sql);  
        } catch (SQLException ex) {  
        }  
    Log.i("testSeedDB", "CreateSeedTable");
    }      
    public void CreateInfoTable() {  
    // first delete old table
    String sql = "drop table"+ TABLE_INFO;
    try {
        mTestDatabase.execSQL(sql);
    } catch (SQLException e) {
    }   
    // second create table
    sql = "CREATE TABLE IF NOT EXISTS " + TABLE_INFO  
                + " (ToyID INTEGER PRIMARY KEY,ToySeed BLOB,ToyMemo TEXT not null);";  
        try {  
        mTestDatabase.execSQL(sql);  
        } catch (SQLException ex) {  
        }  
    }      
   
    public void CleanSeedTable() {  
        try {  
        mTestDatabase.delete(TABLE_SEED, null, null);  
        } catch (SQLException e) {  
        } 
    Log.i("testSeedDB", "ClearSeedTable");        
    }      
   
    public void insertSeedItem(long ToyID, byte[]ToySeed) {  
    String sqlstr = "insert into " + TABLE_SEED + " (ToyID, ToySeed,ToyMemo) values (?,?,?);";
    Object[] args = new Object[]{ToyID,ToySeed,null};
        try{
        mTestDatabase.execSQL(sqlstr,args);  
        } catch (SQLException ex) {  
        }  
    Log.i("testSeedDB", "insertSeedItem");        
       
    }     
 
    public byte[] GetSeedItem(long ToyID) {  
    	Cursor cur = null;
        byte[] strSeed = null;
   
        String col[] = {"ToyID", "ToySeed" ,"ToyMemo"};
        String strToy = "ToyID=" +  new Integer((int) ToyID).toString();
        try{
        	cur = mTestDatabase.query(TABLE_SEED, col, strToy, null, null, null, null);
            cur.moveToFirst();
            strSeed = cur.getBlob(1);
        } catch (SQLException ex) {  
        }  
        if (cur !=null) cur.close();
        Log.i("testSeedDB", strToy);        
        return strSeed;
    }     

}