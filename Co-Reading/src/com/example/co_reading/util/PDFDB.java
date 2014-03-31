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
package com.example.co_reading.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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
    public static final String MAP_TABLE = "MapTable";

    public PDFDB(Context context){ 
        mContext = context;
    }

    public void open() throws SQLException {
    	Log.i(TAG, "open");

        mDBHelper = new PdfDBHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION); 
        mPdfDB = mDBHelper.getWritableDatabase();
        createMapTable();
    }

    public void close(){ 
    	Log.i(TAG, "close");
        mDBHelper.close(); 
    }    
   
    private void createMapTable() throws SQLException {
       String sql = "create table if not exists " + MAP_TABLE  
                	+ " (sha1 text primary key, path text, defaultPage integer);";

       mPdfDB.execSQL(sql);  
       Log.i(TAG, "Succeed create table" + MAP_TABLE);
    }
 
    public void cleanMapTable() throws SQLException {

       	mPdfDB.delete(MAP_TABLE, null, null);  

        Log.i(TAG, "Clear table " + MAP_TABLE);        
    }
    
    public ArrayList<String> queryFileList() throws SQLException {
       	Cursor cur = null;
        ArrayList<String> bookList = new ArrayList<String>();
   
        String col[] = {"path"};
        /* path has special char, we should quote it */
        String where = "*";
        
        cur = mPdfDB.query(MAP_TABLE, col, null, null, null, null, null);
        if (cur.getCount() == 0) {
        	Log.i(TAG, "nothing - -");
        	return null;
        }

        cur.moveToFirst();
        for (int i = 0; i < cur.getCount(); i++) {
            String s = cur.getString(cur.getColumnIndex("path"));
            Log.i(TAG, "found " + s);
            bookList.add(s);
            cur.moveToNext();
        }
        
        if (cur !=null)
        	cur.close();
        
        return bookList;
    }
   
    public void insertNewMap(String SHA1, String path, int defaultPage)
    			throws SQLException {
       	        
        ContentValues cv = new ContentValues();
        cv.put("sha1", SHA1);
        cv.put("path", path);
        cv.put("defaultPage", defaultPage);
        mPdfDB.insert(MAP_TABLE, null, cv);

    	Log.i(TAG, "insert " + path + " to " + MAP_TABLE);        
    }     
 
    public String getSHA1(String path) throws SQLException {
    	Cursor cur = null;
        String SHA1 = null;
   
        String col[] = {"sha1", "path", "defaultPage"};
        String strPath = "PATH='" +  path.trim() + "'";
        
        cur = mPdfDB.query(MAP_TABLE, col, strPath, null, null, null, null);
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
   
        String col[] = {"defaultPage"};
        /* path has special char, we should quote it */
        String strPath = "PATH='" +  path.trim() + "'";
        
        cur = mPdfDB.query(MAP_TABLE, col, strPath, null, null, null, null);
        if (cur.getCount() == 0)
        	return -1;

        cur.moveToFirst();
        defaultPage = cur.getInt(cur.getColumnIndex("defaultPage"));
        
        if (cur !=null)
        	cur.close();

        Log.i(TAG, "finished query default page for " + path);        
        return defaultPage;
    }
    
    public void updateDefaultPageNum(String SHA1, int newVal) {
    	ContentValues cv = new ContentValues();
    	cv.put("defaultPage", newVal);
    	mPdfDB.update(MAP_TABLE, cv, "SHA1 = ?", new String[]{SHA1});
    }
    
    public void deletePaintTable (String name) throws SQLException {
   		String sql = "drop table " + name;

   		mPdfDB.execSQL(sql);
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