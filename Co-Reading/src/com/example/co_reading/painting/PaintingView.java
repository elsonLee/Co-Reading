package com.example.co_reading.painting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.example.co_reading.painting.Brush;
import com.joanzapata.pdfview.*;

public class PaintingView extends PDFView {
    private static final String TAG = PaintingView.class.getSimpleName();
    private Bitmap  mBitmap;
    private Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;
    private int	    mColor = Color.argb(0x30, 0x0, 0xf0, 0x00);
    private boolean mDrawMode;

    public PaintingView(Context context, AttributeSet set) {
        super(context, set);

        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        dragPinchManager = new CoDragPinchManager(this);
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
        super.onDraw(canvas);
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
        Log.i(TAG, "on Touch Event");
        if (mDrawMode == false) {
            return true;
        }
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();

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

    public void setDrawMode(boolean on) {
        Log.i(TAG, "draw mode " + on);
        mDrawMode = on;
    }

    public boolean getDrawMode() {
        return mDrawMode;
    }
}
