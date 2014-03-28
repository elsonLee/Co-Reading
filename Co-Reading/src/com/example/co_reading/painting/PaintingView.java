package com.example.co_reading.painting;

import java.io.*;

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
import com.example.co_reading.util.Encrypt;
import com.example.co_reading.util.PDFDB;
import com.joanzapata.pdfview.PDFView;

import com.joanzapata.pdfview.listener.*;

public class PaintingView extends PDFView 
			implements OnLoadCompleteListener, OnDrawListener, OnPageChangeListener {
    private static final String TAG = PaintingView.class.getSimpleName();
    private Bitmap  mBitmap;
    private Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;
    private int	    mColor = Color.argb(0x30, 0x0, 0xf0, 0x00);
    private boolean mDrawMode;
    private boolean mLoadComplete = false;
    private int 	mCurPage;
    private int     mDefaultPage;
    private float   mXoffset;
    private float   mYoffset;
    private int     mPageWidth;
    private int     mPageHeight;
    private float   mOptimalRatio;
    private PDFDB   mDB;
    private File    mFile;
    private String  mFileSHA1;
    private boolean mDirty = false;

    public PaintingView(Context context, AttributeSet set) {
        super(context, set);

        dragPinchManager = new CoDragPinchManager(this);
    }
    
    @Override
    public Configurator fromFile(File file) {
    	mFile = file;
    	DBInit();
    	return super.fromFile(file).defaultPage(mDefaultPage);
    }
    
    private void DBInit() {
    	String path = mFile.getPath();    	

    	try {
    		mDB = new PDFDB(getContext());
    		mDB.open(PDFDB.SHA1_TABLE);
    		if (mDB.getSHA1(path) == null) {
    			Log.i(TAG, "" + path + " doesn't exist in db, create it");
    			mDB.insertItem(Encrypt.SHA1_file(mFile), path, 1);
    		} else
    			Log.i(TAG, "" + path + " already exist, skip SHA-1 calc");
    		mFileSHA1 = mDB.getSHA1(path);
    		mDefaultPage = mDB.getDefaultPage(path);
    		Log.i(TAG, "get sha1 " + mFileSHA1);
    		Log.i(TAG, "default page:" + mDefaultPage);
    		mDB.createPaintTable(mFileSHA1);
    		if (mDefaultPage == -1)
    			mDefaultPage = 1;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void loadComplete(int nbPages) {
    	mPageWidth = getPageWidth();
    	mPageHeight = getPageHeight();
    	mOptimalRatio = getOptimalPageWidth()/mPageWidth;

        Log.i(TAG, "--> page width:" + mPageWidth + "page height:" + mPageHeight + " ratio:" + mOptimalRatio);
        mBitmap = Bitmap.createBitmap(mPageWidth, mPageHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mLoadComplete = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "w:" + w + " h:" + h + " oldw:" + oldw + " oldh:" + oldh);

    	mOptimalRatio = getOptimalPageWidth()/mPageWidth;
    }    

    @Override
    protected void onDraw(Canvas canvas) {
        Log.v(TAG, "on Draw");
        super.onDraw(canvas);
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
        mDirty = true;
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();

        x -= mXoffset;
        y -= mYoffset;
        
        x = toRealScale(x) / mOptimalRatio;
        y = toRealScale(y) / mOptimalRatio;

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
	public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {	
		mXoffset = getCurrentXOffset() + (mCurPage - 1) * pageWidth;
		mYoffset = getCurrentYOffset();
		float zoom = getZoom() * mOptimalRatio;

		if (mLoadComplete) {
			canvas.save();
			canvas.scale(zoom, zoom);
        	canvas.drawColor(mColor);
        	canvas.drawPath(mPath, Brush.getPaint());
            canvas.drawBitmap(mBitmap, getMatrix(), mBitmapPaint);
            canvas.restore();
        }
	}
    
   	@Override
    public void onPageChanged(int pageNum, int pageCount) {
    	Log.i(TAG, "current page:" + pageNum + "/" + pageCount);
    	Log.i(TAG, "dirty? " + mDirty);
    	mCurPage = pageNum;
		mDB.updateDefaultPageNum(mFileSHA1, pageNum);
		if (mDirty == true)
			mDB.insertBitmap(mFileSHA1, pageNum - 1, mBitmap);
		Bitmap bm = mDB.getBitmap(mFileSHA1, pageNum);
		if (bm != null) {
			mBitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
			mCanvas = new Canvas(mBitmap);
		} else if (mBitmap != null) {
			mBitmap.eraseColor(mColor);
		}
		
		mDirty = false;
		invalidate();
    }

    public void setDrawMode(boolean on) {
        Log.i(TAG, "draw mode " + on);
        mDrawMode = on;
    }

    public boolean getDrawMode() {
        return mDrawMode;
    }
}
