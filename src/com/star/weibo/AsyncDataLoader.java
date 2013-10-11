package com.star.weibo;

import android.app.Activity;
import android.os.AsyncTask;
import com.star.yytv.Log;
import android.widget.Toast;

import com.star.yytv.model.NetWorkManager;
/**
 * 异步数据加载
 * 
 * @author starry
 * 
 */
public class AsyncDataLoader extends AsyncTask<Void, Long, Object> {
	private AsyncDataLoader.Callback mCallback;
	private Activity mActivity ;
	

	public AsyncDataLoader(AsyncDataLoader.Callback callback,Activity activity) {
		setCallback(callback);
		mActivity = activity;
	}
	
	public AsyncDataLoader(AsyncDataLoader.Callback callback) {
		setCallback(callback);
		
	}

	@Override
	protected Object doInBackground(Void... voids) {
		if (mCallback != null) {
			mCallback.onStart();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (mCallback != null) {
			mCallback.onFinish();
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mCallback != null) {
			if(NetWorkManager.getInstance().isNetworkAvailable(mActivity))
			{
				mCallback.onPrepare();
			}
			else
			{
				Toast.makeText(mActivity, "网络异常！", Toast.LENGTH_SHORT).show();
			}
			
		}
	}

	public void setCallback(AsyncDataLoader.Callback callback) {
		this.mCallback = callback;
	}

	public interface Callback {
		public void onPrepare();

		public void onStart();

		public void onFinish();
	}

	void Log(String msg) {
		Log.i("weibo", "Asyc---" + msg);
	}

}
