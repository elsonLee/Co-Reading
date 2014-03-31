package com.example.co_reading;

import java.util.ArrayList;

import com.example.co_reading.util.PDFDB;

import android.app.ListActivity;
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
       	listView = (ListView)findViewById(android.R.id.list);
    	pdfDB = new PDFDB(this);
    	pdfDB.open();
    	bookList = pdfDB.queryFileList();
    	listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, bookList));
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
    	Log.i(TAG, "clicked item " + bookList.get((int)id));
    }
}
