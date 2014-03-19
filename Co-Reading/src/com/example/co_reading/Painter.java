package com.example.co_reading;

import android.util.Log;
import android.graphics.*;

//TODO: set paint's color etc ...
public class Painter {
    private final String TAG = "Painter";

    private Paint mPaint;
    private static Painter mSelf;

    private Painter() {
        Log.i(TAG, "Painter constructor");

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }
    
    public static Paint getPaint() {
    	return getInstance().mPaint;
    }

    public static Painter getInstance() {
    	if (mSelf == null)
    		mSelf = new Painter();
    	return mSelf;
    }
}
