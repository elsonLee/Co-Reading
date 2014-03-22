package com.example.co_reading.paint;

import android.content.Context;
import android.graphics.*;
import android.view.View;
import android.util.Log;
import android.view.MotionEvent;

import com.joanzapata.pdfview.listener.OnPageChangeListener;

public class Painting extends View implements OnPageChangeListener, IDataArrivedListener{
	private final String TAG = "Painting";

    private Bitmap  mBitmap;
    private Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;
    private int 	mColor = Color.argb(0x30, 0x0, 0xf0, 0x00);
    EventTranciver mTranciver;
    private int		counter;

    public Painting(Context c) {
        super(c);

        Log.i(TAG, "View constructor");
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "w:" + w + " h:" + h + " oldw:" + oldw + " oldh:" + oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.v(TAG, "on Draw");

        canvas.drawColor(mColor);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, Brush.getPaint());
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, Brush.getPaint());
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();
   
        if (mTranciver == null)
            mTranciver =  EventTranciver.getDispatcher(getContext(), this, DISPATCH_TYPE.FILE_TRANCIVER);
        if (counter++ < 100)
        	mTranciver.addObject(event);
        if (counter == 100) {
        	mTranciver.flush();
        }

        return onTouchEvent(action, x, y);
    }

    public boolean onTouchEvent(int event, float x, float y) {
        switch (event) {
        case MotionEvent.ACTION_DOWN:
            touch_start(x, y);
            invalidate();
            break;
        case MotionEvent.ACTION_MOVE:
            touch_move(x, y);
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
            touch_up();
            invalidate();
            break;
        }      
        return true;
    }
   
	@Override
	public void onPageChanged(int page, int pageCount) {
		Log.i(TAG, "page:" + page + " pageCount:" + pageCount);

	}
	
	@Override
	public void onDataArrived(SerializedData data) {
		int index = 0;
		Log.i(TAG, "new data arrived");
		if (mTranciver != null) {
			for (SerializedData.Elem d : data.mList) {
				Log.i(TAG, "get event[" + index + "]:" + d.event + " x:" + d.x + " y:" + d.y);
				index++;
			}
			counter = 0;
			mTranciver = null;
			System.gc();
		}
	}
}
