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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

 public class PdfDBHelper extends SQLiteOpenHelper {	 
	 public PdfDBHelper(Context context, String name, CursorFactory factory,
	        int version) {
	     super(context, name, factory, version);
	     // TODO Auto-generated constructor stub
	 }
	
	 @Override
	 public void onCreate(SQLiteDatabase db) {
	     // TODO Auto-generated method stub
	 }
	   @Override
	 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	     // TODO Auto-generated method stub
	 }
 }