package com.star.weibo.async;

import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.listview.AtXListViewProxy;
import com.star.weibo.listview.StatusXListViewProxy;

public class AsyncDelStatusCallback implements AsyncDataLoader.Callback {
	
	private Context mContext;
	private StatusXListViewProxy xListViewProxy;
	private boolean delSuccess;
	
	public AsyncDelStatusCallback(Context context, StatusXListViewProxy xListViewProxy){
		this.mContext = context;
		this.xListViewProxy = xListViewProxy;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		delSuccess = false;
		try{
			xListViewProxy.getWeiboItemAdapter().removeClickedStatus();
			delSuccess = true;
		}catch(Exception e){
			Log(e.getMessage());
			Log(e.getStackTrace().toString());
		}

	}
	
	@Override
	public void onPrepare() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		if (delSuccess){
			xListViewProxy.getWeiboItemAdapter().syncStatusBuffer();
			xListViewProxy.getWeiboItemAdapter().notifyDataSetChanged();
		}

	}
	
	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "AsyncDelStatusCallback--" + msg);
	}


}
