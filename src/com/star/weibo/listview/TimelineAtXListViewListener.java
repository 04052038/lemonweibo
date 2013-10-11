package com.star.weibo.listview;

import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.WeiboToast;
import com.star.weibo.async.AsyncMoreAtCallback;
import com.star.weibo.async.AsyncMoreTimelineCallback;
import com.star.weibo.async.AsyncRefreshAtCallback;
import com.star.weibo.async.AsyncRefreshTimelineCallback;
import com.star.weibo.xlist.XListView.IXListViewListener;

public class TimelineAtXListViewListener implements IXListViewListener {
	
	private Context mContext;
	private TimelineXListViewProxy timelineXListViewProxy;
	
	public TimelineAtXListViewListener(Context context, TimelineXListViewProxy xListViewProxy){
		this.mContext = context;
		this.timelineXListViewProxy = xListViewProxy;
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (timelineXListViewProxy.getWeiboItemAdapter().isOverBuffer()){
			WeiboToast.show(mContext, "缓存已满！");
		}else{
			AsyncMoreTimelineCallback moreCallback = new AsyncMoreTimelineCallback(mContext, timelineXListViewProxy);
			new AsyncDataLoader(moreCallback).execute();
		}
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (! timelineXListViewProxy.isRefreshing()){
			AsyncRefreshTimelineCallback refreshCallback = new AsyncRefreshTimelineCallback(mContext, timelineXListViewProxy, false);
			new AsyncDataLoader(refreshCallback).execute();
		}
	}

}
