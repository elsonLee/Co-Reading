package com.example.co_reading;

import android.util.Log;
import android.content.Context;
import android.app.*;
import android.view.*;
import android.graphics.*;
import org.json.*;

public class Painter {
    private final String TAG = "Painter";

    private Fragment mFragment;
    private Paint mPaint;
    public PainterView mPainterView;
    
    private boolean mVisible;

    private JSONObject mJsonObj;
    private int index;

    public Painter(Fragment fragment) {
        Log.i(TAG, "Painter constructor");

        mFragment = fragment;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        mPainterView = new PainterView(mFragment.getActivity());

        index = 0;
        mJsonObj = new JSONObject();
    }

    public class PainterView extends View {
        private Bitmap  mBitmap;
        private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;

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

            canvas.drawColor(0xAAAAAAAA);

            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

            canvas.drawPath(mPath, mPaint);
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
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            int action = event.getAction();
            JSONArray jsonArray = new JSONArray();

            try {
                jsonArray.put(action).put(x).put(y);
                mJsonObj.putOpt(((Integer)index).toString(), jsonArray);
                index++;
                Log.i(TAG, "index:" + index + " Point X:" + x + " Point Y:" + y);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return onTouchEvent(action, x, y);
        }

        public boolean onTouchEvent(int event, float x, float y) {
        	if (!mVisible)
        		return false;

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

    public JSONObject getJsonObj() {
        return mJsonObj;
    }

    public void rePaint(JSONObject obj) {
        mPainterView.rePaint(obj);
    }

    public void setVisible(boolean visible) {
    	mVisible = visible;
    	mPainterView.setVisibility(View.VISIBLE);

 
//        if (visible) {
//            mPainterView.setVisibility(View.VISIBLE);
//        } else {
//            mPainterView.setVisibility(View.INVISIBLE);
//        }
    }
}
