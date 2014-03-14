package com.example.co_reading;

import android.app.Fragment;
import android.os.Bundle;

public class RetainedFragment extends Fragment {

	private MainActivity.OnRestoreData data;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setData(MainActivity.OnRestoreData data) {
        this.data = data;
    }

    public MainActivity.OnRestoreData getData() {
        return data;
    }
}
