package com.example.co_reading.painting;

import com.joanzapata.pdfview.DragPinchManager;
import com.joanzapata.pdfview.PDFView;

public class CoDragPinchManager extends DragPinchManager {
    private CoDragPinchListener mCoDragPinchListener;

    public CoDragPinchManager(PDFView pdfView) {
    	super(pdfView);
    	mCoDragPinchListener = new CoDragPinchListener();
        mCoDragPinchListener.setOnDragListener(this);
        mCoDragPinchListener.setOnPinchListener(this);
        mCoDragPinchListener.setOnDoubleTapListener(this);
    	pdfView.setOnTouchListener(mCoDragPinchListener);
    }
}
