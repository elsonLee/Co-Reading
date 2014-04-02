package com.example.co_reading;

import java.io.File;
import java.util.*;

import com.example.co_reading.util.PDFDB;
import com.ipaulpro.afilechooser.utils.FileUtils;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.util.Log;

public class BookShelf extends ListActivity {
	private final String TAG = "BookShelf";

	private PDFDB pdfDB;
	private ArrayList<String> bookList;
	private final String newFile = "Open other ...";
	private final int REQUEST_CHOOSER = 1234;
	private File mFile;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	ListView lv = new ListView(this);
    	lv.setId(android.R.id.list);
    	lv.setBackgroundColor(Color.GRAY);
    	setContentView(lv);
    }
	
	protected void onStart() {
		super.onStart();

		SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.book_shelf,
				new String[]{"icon", "bookName"}, new int[]{R.id.thumbnail, R.id.bookname});

		setListAdapter(adapter);
	}
	
	protected List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	pdfDB = new PDFDB(this);
    	pdfDB.open();
    	bookList = pdfDB.queryFileList();
    	bookList.add(newFile);
    	
    	for (String s:bookList) {
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("icon", R.drawable.ic_launcher);
    		map.put("bookName", s.replaceAll("\\.(pdf|PDF)$", "").replaceAll("^.*/", ""));
    		list.add(map);
    	}
		
		return list;
	}
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Log.i(TAG, "clicked item " + bookList.get((int)id));
    	if (bookList.get((int)id).equals(newFile)) {
    		Log.i(TAG, "new file ...");
			Intent getContentIntent = FileUtils.createGetContentIntent();
			Intent intent = Intent.createChooser(getContentIntent,
					"Select a PDF file");
			startActivityForResult(intent, REQUEST_CHOOSER);
    	} else {		
    		mFile = new File(bookList.get((int)id));
        	loadPDF();
    	}
    }
    
    protected void loadPDF() {
    	if (mFile.exists()) {
			Intent intent = new Intent("com.example.co_reading.LOAD_PDF");
			intent.setData(Uri.fromFile(mFile));
			startActivity(intent);
    	} else {
    		Log.e(TAG, "" + mFile + " doesn't exist");
    	}
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult");
		switch (requestCode) {
		case REQUEST_CHOOSER:
			if (resultCode == Activity.RESULT_OK) {
				final Uri uri = data.getData();
				String uriString = uri.toString();
				if (uri != null && FileUtils.isLocal(FileUtils.getPath(this, uri))) {
					mFile = FileUtils.getFile(this, Uri.parse(uriString));
					if (mFile != null && mFile.exists()) {
							Log.d(TAG, "file:"+ mFile);
							loadPDF();
						}
					}
				}
			break;
		default:
			break;
		}		
	}
}
