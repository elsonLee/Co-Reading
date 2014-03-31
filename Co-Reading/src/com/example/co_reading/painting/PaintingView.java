/*Copyright (C) 2014  ElsonLee & WenPin Cui

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/	
package com.example.co_reading.painting;

import java.io.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.example.co_reading.connection.Connection;
import com.example.co_reading.connection.INetworkListener;
import com.example.co_reading.connection.bluetooth.BlueToothManager;
import com.example.co_reading.util.Encrypt;
import com.example.co_reading.util.PDFDB;
import com.example.co_reading.util.Packet;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnDrawListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

public class PaintingView extends PDFView 
    implements OnLoadCompleteListener, OnDrawListener, OnPageChangeListener {
    private static final String TAG = PaintingView.class.getSimpleName();

    private final int POINT_MSG = 0;

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
    private ExecutorService mDBThreadPool;
    private File    mFile;
    private String  mFileSHA1;
    private boolean mDirty = false;
    private final int GUIUPDATE = 0xa5;
    private Handler mHandler;
    private FutureTask<Integer> mLastTask;
    private byte[]  mBitmapLock = new byte[]{};

    private Handler mPointHandler;
    
    public PaintingView(Context context, AttributeSet set) {
        super(context, set);

        dragPinchManager = new CoDragPinchManager(this);
        
        mPointHandler = new Handler( new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				if (msg.what == POINT_MSG) {
					Packet pack = (Packet) msg.obj;
					onTouchEvent(pack.mEvent, pack.mX, pack.mY);
				}
				return false;
			}
		});
    }
    
    @Override
    public Configurator fromFile(File file) {
    	mFile = file;
    	loadDB();
    	return super.fromFile(file).defaultPage(mDefaultPage);
    }
    
    private void loadDB() {
    	String path = mFile.getPath();    	

    	try {
            mDB = new PDFDB(getContext());
            mDB.open();
            if (mDB.getSHA1(path) == null) {
                Log.i(TAG, "" + path + " doesn't exist in db, create it");
                mDB.insertNewMap(Encrypt.SHA1_file(mFile), path, 1);
            } else
                Log.i(TAG, "" + path + " already exist, skip SHA-1 calc");

            mFileSHA1 = mDB.getSHA1(path);
            mDefaultPage = mDB.getDefaultPage(path);

            Log.i(TAG, "get sha1 " + mFileSHA1);
            Log.i(TAG, "default page:" + mDefaultPage);

            mDB.createPaintTable(mFileSHA1);
            if (mDefaultPage == -1)
                mDefaultPage = 1;
            
            mDBThreadPool = Executors.newFixedThreadPool(3);
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
        
        mHandler = new Handler() {  
          public void handleMessage(Message msg) {   
               switch (msg.what) {   
                    case GUIUPDATE:
                   		invalidate();  
                        break;   
               }   
               super.handleMessage(msg);   
          }   
        };  
        
        onPageChanged(mCurPage, nbPages); // explicitly load bitmap from db if exists.
        
        // FIXME
        // if (BlueToothManager.getInstance().getRole() == BlueToothManager.ROLE_CLIENT) {
        try {
        	Connection connection = BlueToothManager.getInstance().getConnection();
			if (connection != null) {
				connection.addListener(new INetworkListener() {

					@Override
					public void onNetworkConnected() {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onNetworkDisconnected() {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onNetworkReceivedObj(Object object) {
						if (object instanceof Packet) {
							Packet pack = (Packet) object;
							Message msg = Message.obtain(mPointHandler, POINT_MSG, pack);
							msg.sendToTarget();
						}	
					}
				});
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

    	// FIXME
        Connection connection = null;
		try {
			connection = BlueToothManager.getInstance().getConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (connection != null) {
        //if (BlueToothManager.getInstance().getRole() == BlueToothManager.ROLE_SERVER) {
        	Packet pack = new Packet(action, x, y);
        	connection.send(pack);
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
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {	
        mXoffset = getCurrentXOffset() + (mCurPage - 1) * pageWidth;
        mYoffset = getCurrentYOffset();
        float zoom = getZoom() * mOptimalRatio;

        synchronized (mBitmapLock)  {
        if (mLoadComplete) {
            canvas.save();
            canvas.scale(zoom, zoom);
            canvas.drawColor(mColor);
            canvas.drawPath(mPath, Brush.getPaint());
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.restore();
        }
        }
    }
    
    @Override
    public void onPageChanged(int pageNum, int pageCount) {
    	Log.i(TAG, "current page:" + pageNum + "/" + pageCount);
    	if (pageNum != mCurPage) {
    		if (mLastTask != null && !mLastTask.isDone())
    			mLastTask.cancel(false);

    		mLastTask = new FutureTask<Integer>(new DBSyncTask(pageNum));
    		mDBThreadPool.submit(mLastTask);
    	}
    }
   	
    class DBSyncTask implements Callable<Integer> {
        private int pageNum;

        DBSyncTask(int pageNum) {
            this.pageNum = pageNum;
        }

        public Integer call() {
            mDB.updateDefaultPageNum(mFileSHA1, pageNum);
            if (mDirty == true)
                mDB.insertBitmap(mFileSHA1, mCurPage, mBitmap);
            mCurPage = pageNum;

            synchronized(mBitmapLock) {
            if (mCanvas != null) {
            	mCanvas.drawColor(Color.TRANSPARENT, Mode.SRC);

                Bitmap bm = mDB.getBitmap(mFileSHA1, pageNum);
                if (bm != null) {
                    Log.i(TAG, "draw bitmap from db");
                    mCanvas.drawBitmap(bm, 0, 0, mBitmapPaint);
                   	Message message = new Message();
                   	message.what = GUIUPDATE;
                   	mHandler.sendMessage(message);
                }
                mDirty = false;
            }
            }
            return 0;
        }
    }
 
    public void setDrawMode(boolean on) {
        Log.i(TAG, "draw mode " + on);
        mDrawMode = on;
    }

    public boolean getDrawMode() {
        return mDrawMode;
    }
}
