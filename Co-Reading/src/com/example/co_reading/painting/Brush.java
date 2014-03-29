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

import android.graphics.*;
import android.util.Log;

/**
 *
 * @author wenpincui
 *
 */
public class Brush implements ColorPickerDialog.OnColorChangedListener {
    private final String TAG = Brush.class.getSimpleName();

    private Paint mPaint;
    private MaskFilter  mEmboss;
    private MaskFilter  mBlur;

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

        mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
                                       0.4f, 6, 3.5f);

        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
    }

    public void colorChanged(int color) {
        mPaint.setColor(color);
    }

    public static MaskFilter getEmbossFilter() {
        return getInstance().mEmboss;
    }

    public static MaskFilter getBlurFilter() {
        return getInstance().mBlur;
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
