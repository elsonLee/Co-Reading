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

import android.graphics.Paint;
import android.util.Log;

/**
 * 
 * @author wenpincui
 *
 */
public class Brush {
    private final String TAG = Brush.class.getSimpleName();

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
