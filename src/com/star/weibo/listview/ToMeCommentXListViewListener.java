package com.star.weibo.listview;

import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.WeiboToast;
import com.star.weibo.async.AsyncMoreTimelineCallback;
import com.star.weibo.async.AsyncMoreToMeCommentCallback;
import com.star.weibo.async.AsyncRefreshTimelineCallback;
import com.star.weibo.async.AsyncRefreshToMeCommentCallback;
import com.star.weibo.xlist.XListView.IXListViewListener;

public class ToMeCommentXListViewListener implements IXListViewListener {
	
	private Context mContext;
	private ToMeCommentXListViewProxy toMeCommentXListViewProxy;
	
	public ToMeCommentXListViewListener(Context context, ToMeCommentXListViewProxy xListViewProxy){
		this.mContext = context;
		this.toMeCommentXListViewProxy = xListViewProxy;
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (toMeCommentXListViewProxy.getCommentAdapter().isOverBuffer()){
			WeiboToast.show(mContext, "缓存已满！");
		}else{
			AsyncMoreToMeCommentCallback moreCallback = new AsyncMoreToMeCommentCallback(mContext, toMeCommentXListViewProxy);
			new AsyncDataLoader(moreCallback).execute();
		}
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (! toMeCommentXListViewProxy.isRefreshing()){
			AsyncRefreshToMeCommentCallback refreshCallback = new AsyncRefreshToMeCommentCallback(mContext, toMeCommentXListViewProxy, false);
			new AsyncDataLoader(refreshCallback).execute();
		}
	}

}
