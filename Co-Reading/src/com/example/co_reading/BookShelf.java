package com.example.co_reading;

import java.io.File;
import java.util.ArrayList;

import com.example.co_reading.util.PDFDB;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

public class BookShelf extends ListActivity {
	private PDFDB pdfDB;
	private ArrayList<String> bookList;
	private ListView listView;
	private final String TAG = "BookShelf";
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.book_shelf);
    	bookList = new ArrayList<String>();
       	listView = (ListView)findViewById(android.R.id.list);
    	pdfDB = new PDFDB(this);
    	pdfDB.open();
    	ArrayList<String> queryResult = pdfDB.queryFileList();
    	bookList.addAll(queryResult);

    	listView.setAdapter(new ArrayAdapter<String>(this, 
    				android.R.layout.simple_expandable_list_item_1,
    				bookList));
    }
    
    @Override
    protected void onStart() {
    	super.onStart();

    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();   	
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	if (bookList == null) {
    		Log.i(TAG, "book list is null");
    	} else
    		Log.i(TAG, "it is ok");
    	Log.i(TAG, "clicked item " + bookList.get((int)id));
    	File file = new File(bookList.get((int)id));
    	if (file.exists()) {
    		Intent intent = new Intent("com.example.co_reading.LOAD_PDF");
    		intent.setData(Uri.fromFile(file));
    		startActivity(intent);
    	}
    }
}
