package com.star.weibo.listview;

import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.WeiboToast;
import com.star.weibo.async.AsyncMoreUserStatusCallback;
import com.star.weibo.async.AsyncRefreshUserInfoCallback;
import com.star.weibo.xlist.XListView.IXListViewListener;
import com.star.weibo4j.model.User;

public class UserStatusXListViewListener implements IXListViewListener {

	private Context mContext;
	private UserStatusXListViewProxy userStatusXListViewProxy;
	
	public UserStatusXListViewListener(Context context, UserStatusXListViewProxy xListViewProxy){
		this.mContext = context;
		this.userStatusXListViewProxy = xListViewProxy;
	}
	
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (userStatusXListViewProxy.getWeiboItemAdapter().isOverBuffer()){
			WeiboToast.show(mContext, "缓存已满！");
		}else{
			User user = userStatusXListViewProxy.getUser();
			if (user != null){
				AsyncMoreUserStatusCallback moreCallback = new AsyncMoreUserStatusCallback(mContext, userStatusXListViewProxy);
				new AsyncDataLoader(moreCallback).execute();
			}else{
				Log("problem: user is null");
			}
		}
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		User user = userStatusXListViewProxy.getUser();
		if(user != null){
		if (! userStatusXListViewProxy.isRefreshing()){
			AsyncRefreshUserInfoCallback refreshCallback = new AsyncRefreshUserInfoCallback(mContext, userStatusXListViewProxy, false);
			new AsyncDataLoader(refreshCallback).execute();
		}}else{
			Log("problem: user is null");
		}

	}
	
	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "UserStatusXListViewListener--" + msg);
	}

}
