package com.example.co_reading.paint;

import android.util.Log;
import android.graphics.*;

public class Brush {
    private final String TAG = "Brush";

    private Paint mPaint;
    private static Brush mSelf;

    protected Brush() {
        Log.v(TAG, "Painter constructor");

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

    public static Brush getInstance() {
    	if (mSelf == null)
    		mSelf = new Brush();
    	return mSelf;
    }
}
