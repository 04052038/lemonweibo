package com.star.weibo;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ZoomControls;
import android.app.Activity;
import android.app.ProgressDialog;

import com.star.weibo.zoom.ImageZoomState;
import com.star.weibo.zoom.ImageZoomView;
import com.star.yytv.R;
import com.star.yytv.common.YyProgressDiagFact;

public class ImageViewZoomActivity extends Activity implements OnGestureListener, OnDoubleTapListener {
	private final static String TAG = "ImageViewZoomActivity";
	public static final String STATUSID = "statusid";
	public static final String URL = "url";
	public static final String PICTYPE = "picType";

	private ImageZoomView mZoomView;
	private ZoomControls mZoomCtrl;
	
	private ImageZoomState mZoomState;
	private GestureDetector mGesture;

	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_zoom_layout);
		dialog = YyProgressDiagFact.getYyProgressDiag(this);
		mZoomState = new ImageZoomState();
		mZoomView = (ImageZoomView) findViewById(R.id.zoomView);
		mZoomView.setImageZoomState(mZoomState);
		mZoomView.setImage(null);
		mZoomView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (!mGesture.onTouchEvent(arg1)) {
					// handle 2 touch points
					if (arg1.getPointerCount() >= 2) {
						return onGestureZoom(arg1);
					}
					return false;
				}
				return true;
			}
		});
		mZoomView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			}
		});
		
		mZoomCtrl = (ZoomControls) findViewById(R.id.zoomCtrl);
		setImageController();
		
		mGesture = new GestureDetector(this, this);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null){
			long id = bundle.getLong(STATUSID, 0);
			String url = bundle.getString(URL);
			String picType = bundle.getString(PICTYPE);
			AsyncImageLoader.getInstance().loadZoomPic(id, url, mZoomView, picType, dialog);
		}
	}
	
	@Override
	protected void onDestroy() {
		mZoomView.setImage(null);
		
		super.onDestroy();
	}

	private void setImageController() {
		mZoomCtrl.setOnZoomInClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Zoom in
				Log.d(TAG, "Zoom In");
				mZoomState.zoomIn();
			}
		});
		mZoomCtrl.setOnZoomOutClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Zoom out
				Log.d(TAG, "Zoom Out");
				mZoomState.zoomOut();
			}
		});
	}

	/**
	 * 隐藏处ImageZoomView外地其他组件，全屏显示
	 */
	private void setFullScreen() {
		if (mZoomCtrl != null) {
			if (mZoomCtrl.getVisibility() == View.VISIBLE) {
				// mZoomCtrl.setVisibility(View.GONE);
				mZoomCtrl.hide(); // 有过度效果
			} else if (mZoomCtrl.getVisibility() == View.GONE) {
				// mZoomCtrl.setVisibility(View.VISIBLE);
				mZoomCtrl.show();// 有过渡效果
			}
		}
	}

	private boolean onGestureZoom(MotionEvent e) {
		if (e.getAction() != MotionEvent.ACTION_MOVE)
			Log.d(TAG, "onGestureZoom. Action: 0x"+Integer.toHexString(e.getAction())+", pointer count: "+e.getPointerCount());
		int action = e.getAction();
		if (MotionEvent.ACTION_MOVE == action) {
			mZoomState.freeZoom(e.getX(e.getPointerId(0)), e.getY(e.getPointerId(0)),
					e.getX(e.getPointerId(1)), e.getY(e.getPointerId(1)));
		} else if (//(MotionEvent.ACTION_POINTER_DOWN == e.getActionMasked()) ||
				(MotionEvent.ACTION_POINTER_UP == e.getActionMasked())) {
			mZoomState.clearZoomEnv();
		}
		return true;
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "onFling. animation.");
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2,
			float distanceX, float distanceY) {
		//Log.d(TAG, "onScroll. move. "+distanceX+", "+distanceY);
		mZoomState.move(distanceX, distanceY);
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	
	//////
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		Log.d(TAG, "onDoubleTap. show original size or fill screen by width.");
		mZoomState.fillOrFull(e.getX(), e.getY());
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		//Log.d(TAG, "onSingleTapConfirmed. show/hide zoombar.");
		setFullScreen();
		return true;
	}
}
