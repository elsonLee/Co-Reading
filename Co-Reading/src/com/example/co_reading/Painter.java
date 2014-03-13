package com.example.co_reading;

import android.util.Log;
import android.os.Bundle;
import android.app.*;
import android.view.*;
import android.graphics.*;

public class Painter extends Activity implements SurfaceHolder.Callback, Runnable {
    private final String TAG = "Painter";
    public SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    Painter() {
        Log.i(TAG, "Painter constructor");
    }

    public static Painter getInstance() {
        return new Painter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Activity create");

        mSurfaceView = new SurfaceView(this);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new Painter());
        mSurfaceView.setVisibility(View.VISIBLE);
        try {
            Canvas canvas = mSurfaceHolder.lockCanvas();
            if (canvas != null) {
                Paint paint = new Paint();
                paint.setStrokeWidth(2.0f);
                paint.setColor(Color.RED);
                canvas.drawText("hello", 50, 50, paint);
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Log.i(TAG, "run");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surface changed");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surface created");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surface destroyed");
    }
}
