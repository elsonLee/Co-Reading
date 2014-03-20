package com.example.co_reading;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.joanzapata.pdfview.listener.OnPageChangeListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.util.Log;
import android.view.MotionEvent;

public class PainterView extends View implements OnPageChangeListener {
	private final String TAG = "PainterView";
    private Bitmap  mBitmap;
    private Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;
    private int mColor = Color.argb(0x30, 0x0, 0xf0, 0x00);

    public PainterView(Context c) {
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
        Log.i(TAG, "on Draw");

        canvas.drawColor(mColor);

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        canvas.drawPath(mPath, Painter.getPaint());
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
        mCanvas.drawPath(mPath, Painter.getPaint());
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();

        //TODO: Save motion event here
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
        
        /* just for test */
        BtConnectClient client = null;
		try {
			client = BlueToothManager.getInstance().getClient(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (client != null) {
        	byte[] tmpByte = new byte[1];
        	tmpByte[0] = (byte)event;
        	client.write(tmpByte);
        }
        
        return true;
    }
    
	@Override
	public void onPageChanged(int page, int pageCount) {
		Log.i(TAG, "page:" + page + " pageCount:" + pageCount);
	}

    public void rePaint(JSONObject obj) {
        Log.i(TAG, "repaint ...");
        mCanvas.drawColor(Color.BLACK);
        mBitmap.eraseColor(Color.BLACK);
        invalidate();

        for (int index = 0; index < obj.length() / 2; index++) {
            try {
                JSONArray array = obj.getJSONArray(((Integer)index).toString());
                double x = (Double)(array.get(1));
                double y = (Double)(array.get(2));
                onTouchEvent((Integer)(array.get(0)), (float)x, (float)y);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
