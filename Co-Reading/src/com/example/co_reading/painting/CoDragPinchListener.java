package com.example.co_reading.painting;

import android.view.MotionEvent;
import android.view.View;

import com.joanzapata.pdfview.util.DragPinchListener;

public class CoDragPinchListener extends DragPinchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PaintingView pdfView = (PaintingView)v;
        if (pdfView.getDrawMode())
            return pdfView.onTouchEvent(event);
        return super.onTouch(v, event);
    }
}
