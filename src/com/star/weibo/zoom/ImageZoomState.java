package com.star.weibo.zoom;

import java.util.Observable;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

public class ImageZoomState extends Observable {
	private final static String TAG = "ImageZoomState";
	private final static float FLOAT_ERROR = (float)0.00005;
	
	private final static float DELTA_ZOOM = (float) 0.2;
	
	private int canvasWidth = 0;
	private int canvasHeight = 0;
	private int imageWidth = 0;
	private int imageHeight = 0;
	
	private Matrix currMatrix = new Matrix();
	RectF imageRect = new RectF();
	RectF dstRect = new RectF();
	private float mScale = 1;
	private float minScale = 1;
	private float maxScale = 1;
	private float mTransX = 0;
	private float mTransY = 0;
	
	// below 3 variables for free zoom
	private float mDistance = 0;
	private float mCenterX = 0;
	private float mCenterY = 0;

	public Matrix getMatrix() {
		return currMatrix;
	}
	
	public void setCanvasSize(int width, int height) {
		Log.d(TAG, "canvas size: "+width+", "+height);
		
		if (canvasWidth != width || canvasHeight != height) {
			canvasWidth = width;
			canvasHeight = height;
			init();
		}
	}
	
	public void setImageSize(int width, int height) {
		Log.d(TAG, "image size: "+width+", "+height);
		imageWidth = width;
		imageHeight = height;
		imageRect.set((float)0, (float)0, (float)imageWidth, (float)imageHeight);
		init();
	}
	
	private void init() {
		// initialization
		resetMatrix();
		if (canvasWidth == 0 ||
				canvasHeight == 0 ||
				imageWidth == 0 ||
				imageHeight == 0)
			return;
		
		calculateScaleLimitation();
		// to make easy to read long text in image, show image fill screen width first
		doZoom(canvasWidth/(float)imageWidth, 0, 0, 0, 0);
	}
	
	private void calculateScaleLimitation() {
		// 计算最大最小缩放比例
		// 最小：宽高皆小于屏幕的，可以缩小一倍；否则长边可缩小为屏幕的一半
		if (imageWidth<canvasWidth && imageHeight<canvasHeight) {
			minScale = (float) 0.5;
		} else {
			if (imageWidth/(float)imageHeight > canvasWidth/(float)canvasHeight) {
				minScale = (float) ((canvasWidth/2.0)/imageWidth);
			} else {
				minScale = (float) ((canvasHeight/2.0)/imageHeight);
			}
		}
		// 最大：宽高皆大于屏幕的，可以放大四倍；否则短边可放大为屏幕的四倍
		if (imageWidth<canvasWidth && imageHeight<canvasHeight) {
			if (imageWidth/(float)imageHeight > canvasWidth/(float)canvasHeight) {
				maxScale = (float) ((canvasHeight*4.0)/imageHeight);
			} else {
				maxScale = (float) ((canvasWidth*4.0)/imageWidth);
			}
		} else {
			maxScale = (float) 4.0;
		}
		
		assert(maxScale > minScale);
		Log.d(TAG, "maxScale: "+maxScale+", minScale: "+minScale);
	}
	
	private float checkScale(float scale) {
		if (scale > maxScale)
			return maxScale;
		if (scale < minScale)
			return minScale;
		return scale;
	}
	
	public void zoomIn() {
		doZoom(1+DELTA_ZOOM, 0, 0, canvasWidth/(float)2, canvasHeight/(float)2);
	}
	
	public void zoomOut() {
		doZoom(1/(1+DELTA_ZOOM), 0, 0, canvasWidth/(float)2, canvasHeight/(float)2);
	}
	
	private void doZoom(float deltaScale, float deltaTransX, float deltaTransY, float zoomCenterX, float zoomCenterY) {
		//Log.d(TAG, "doZoom: "+deltaScale+", "+deltaTransX+", "+deltaTransY+", "+zoomCenterX+", "+zoomCenterY);

		if (canvasWidth == 0 ||
				canvasHeight == 0 ||
				imageWidth == 0 ||
				imageHeight == 0)
			return;
		// below 3 variables to prevent unchanged repaint
		float oldScale = mScale;
		float oldTransX = mTransX;
		float oldTransY = mTransY;

		if (Math.abs(deltaScale-1) > FLOAT_ERROR) {
			currMatrix.mapRect(dstRect, imageRect);
			float centerOffsetX = zoomCenterX-dstRect.centerX();
			float centerOffsetY = zoomCenterY-dstRect.centerY();
			
			// check zoom limitation and recalculate deltaScale
			float newScale = checkScale(mScale*deltaScale);
			deltaScale = (newScale-mScale)/mScale;
			
			// 以按键来缩放，以图像中心点为中心计算偏移
			mTransX -= imageWidth/2.0*mScale*deltaScale;
			mTransY -= imageHeight/2.0*mScale*deltaScale;
			mTransX -= centerOffsetX*deltaScale;
			mTransY -= centerOffsetY*deltaScale;
			mScale = newScale;
		}
		mTransX += deltaTransX;
		mTransY += deltaTransY;
		
		ensureImageInScreen(); // note that after setScale called, matrix is reset and then set scale
		currMatrix.setScale(mScale, mScale);
		currMatrix.postTranslate(mTransX, mTransY);
		
		if ((Math.abs(mScale-oldScale)>FLOAT_ERROR) ||
				(Math.abs(mTransX-oldTransX)>FLOAT_ERROR) ||
				(Math.abs(mTransY-oldTransY)>FLOAT_ERROR)) {
			setChanged();
		}
		notifyObservers();
	}
	
	public void freeZoom(float x0, float y0, float x1, float y1) {
		
		if (mDistance == 0) {
			// initialization, will drop the first move event
			mDistance = getDistance(x0, y0, x1, y1);
			mCenterX = (x0+x1)/2;
			mCenterY = (y0+y1)/2;
			return;
		}
		float newDistance = getDistance(x0, y0, x1, y1);
		float newCenterX = (x0+x1)/2;
		float newCenterY = (y0+y1)/2;
		//Log.d(TAG, "newDistance: "+newDistance);

		doZoom(newDistance/mDistance, newCenterX-mCenterX, newCenterY-mCenterY,
				newCenterX, newCenterY);
		
		mDistance = newDistance;
		mCenterX = newCenterX;
		mCenterY = newCenterY;
	}
	
	public void clearZoomEnv() {

		//Log.d(TAG, "clearZoomEnv");
		mDistance = 0;
		mCenterX = 0;
		mCenterY = 0;
	}
	
	private void resetMatrix() {
		currMatrix.reset();
		mScale = 1;
		mTransX = 0;
		mTransY = 0;
		setChanged();
	}
	
	public void move(float x, float y) {
		doZoom(1, -x, -y, 0, 0);
	}
	
	public void fillOrFull(float x, float y) {
		float initDeltaScale = canvasWidth/(float)imageWidth;

		float oldTransY = mTransY;
		if ((Math.abs(mScale-initDeltaScale)>FLOAT_ERROR) ||
				(Math.abs(mTransX)>FLOAT_ERROR)) {
			float oldScale = mScale;
			resetMatrix();
			// match width, try not to change Y
			//Log.d(TAG, "fill width");
			float tempTransY = y-(y-oldTransY)/oldScale*initDeltaScale;
			doZoom(initDeltaScale, 0, tempTransY, 0, 0);
		} else {
			resetMatrix();
			//Log.d(TAG, "full size");
			doZoom(1, x-x/initDeltaScale, y-(y-oldTransY)/initDeltaScale, 0, 0);
		}
	}
	
	private void ensureImageInScreen() {
		float scaledImageWidth = mScale*imageWidth;
		float scaledImageHeight = mScale*imageHeight;
		
		// 横边小于屏幕则纵向居中
		if (scaledImageWidth < canvasWidth) {
			mTransX = (canvasWidth-scaledImageWidth)/2;
		}
		// 竖边小于屏幕则横向居中
		if (scaledImageHeight < canvasHeight) {
			mTransY = (canvasHeight-scaledImageHeight)/2;
		}
		// 图片显示大于屏幕时不允许移动出界
		// X
		if (scaledImageWidth >= canvasWidth) {
			if (mTransX > 0)
				mTransX = 0;
			else if (scaledImageWidth+mTransX < canvasWidth)
				mTransX = canvasWidth - scaledImageWidth;
		}
		// Y
		if (scaledImageHeight >= canvasHeight) {
			if (mTransY > 0)
				mTransY = 0;
			else if (scaledImageHeight+mTransY < canvasHeight)
				mTransY = canvasHeight - scaledImageHeight;
		}
	}
	
	private float getDistance(float x0, float y0, float x1, float y1) {
		double dX2 = Math.pow(x0 - x1, 2);// 两点横坐标差的平法
		double dY2 = Math.pow(y0 - y1, 2);// 两点纵坐标差的平法
		return (float) Math.pow(dX2 + dY2, 0.5);
	}
}
