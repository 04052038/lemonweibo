package com.star.weibo.listview;

import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.WeiboToast;
import com.star.weibo.async.AsyncMoreRepostCallback;
import com.star.weibo.async.AsyncMoreTimelineCallback;
import com.star.weibo.async.AsyncRefreshRepostCallback;
import com.star.weibo.async.AsyncRefreshTimelineCallback;
import com.star.weibo.xlist.XListView.IXListViewListener;

public class RepostXListViewListener implements IXListViewListener {

	private Context mContext;
	private RepostStatusXListViewProxy repostXListViewProxy;
	
	public RepostXListViewListener(Context context, RepostStatusXListViewProxy xListViewProxy){
		this.mContext = context;
		this.repostXListViewProxy = xListViewProxy;
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (repostXListViewProxy.getWeiboItemAdapter().isOverBuffer()){
			WeiboToast.show(mContext, "缓存已满！");
		}else{
			AsyncMoreRepostCallback moreCallback = new AsyncMoreRepostCallback(mContext, repostXListViewProxy);
			new AsyncDataLoader(moreCallback).execute();
		}
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (! repostXListViewProxy.isRefreshing()){
			AsyncRefreshRepostCallback refreshCallback = new AsyncRefreshRepostCallback(mContext, repostXListViewProxy, false);
			new AsyncDataLoader(refreshCallback).execute();
		}
	}

}
