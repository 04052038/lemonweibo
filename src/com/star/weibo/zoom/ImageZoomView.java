package com.star.weibo.zoom;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ImageZoomView extends View implements Observer {
	private final static String TAG = "ImageZoomView";
    private Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
    private Bitmap mBitmap;
	private ImageZoomState mZoomState;
	RectF recttest;
	RectF restresult;

	public ImageZoomView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
 
    @Override
    protected void onDraw(Canvas canvas) {
    	if (mBitmap != null && mZoomState != null) {
    		Matrix matrix = mZoomState.getMatrix();

    		matrix.mapRect(restresult, recttest);
    		//Log.d(TAG, "onDraw: [" + (int)restresult.left + ", " + (int)restresult.top
    		//		 + ", " + (int)restresult.right + ", " + (int)restresult.bottom + "]");
    		
    		canvas.drawBitmap(mBitmap, matrix, mPaint);
    	}
    }
 
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
           int bottom) {
    	// TODO Auto-generated method stub
    	super.onLayout(changed, left, top, right, bottom);
    	if (null != mZoomState) {
    		mZoomState.setCanvasSize(getWidth(), getHeight());
    	}
    }

	@Override
	public void update(Observable arg0, Object arg1) {
		this.invalidate();
	}
	
	public void setImageZoomState(ImageZoomState zoomState) {
    	if (mZoomState == zoomState)
    		return;
    	
    	if (mZoomState != null) {
    		mZoomState.deleteObserver(this);
    	}
    	mZoomState = zoomState;
    	
    	if (zoomState == null)
    		return;

    	Log.d(TAG, "setImageZoomState: "+zoomState);
    	mZoomState.addObserver(this);
    }

	 
    public void setImage(Bitmap bitmap) {
    	if (mBitmap != null)
    		mBitmap.recycle();
    	mBitmap = bitmap;
    	Log.d(TAG, "setImage: "+bitmap);
    	
    	if (bitmap == null) {
    		invalidate();
    		return;
    	}
    	
    	int bitmapWidth = mBitmap.getWidth();
    	int bitmapHeight = mBitmap.getHeight();
    	mZoomState.setImageSize(bitmapWidth, bitmapHeight);
    	
    	recttest = new RectF((float)0, (float)0, (float)bitmapWidth, (float)bitmapHeight);
    	restresult = new RectF();
    }
}