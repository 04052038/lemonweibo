package com.star.weibo.listview;

import java.util.ArrayList;

import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.RefreshViews;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.async.AsyncDBAtCallback;
import com.star.weibo.async.AsyncDBTimelineCallback;
import com.star.weibo.async.AsyncMoreAtCallback;
import com.star.weibo.async.AsyncMoreTimelineCallback;
import com.star.weibo.async.AsyncRefreshAtCallback;
import com.star.weibo.async.AsyncRefreshTimelineCallback;
import com.star.weibo.buf.AtStatusBuffer;
import com.star.weibo.buf.TimelineStatusBuffer;
import com.star.weibo.buf.WeiboBuffer;
import com.star.weibo.db.WeiboDBAdapter;
import com.star.weibo.util.LocalMemory;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Status;

public class TimelineXListViewProxy extends StatusXListViewProxy {
	
	public TimelineXListViewProxy(Context context, XListView listView, RefreshViews refreshView){
		super(context, listView, refreshView);
		TimelineStatusBuffer timelineStatusBuffer = new TimelineStatusBuffer(WeiboBuffer.HOME_STATUSLIST_BUF_MAX, new WeiboDBAdapter(context));
		weiboItemAdapter = new BufferedWeiboItemAdapter(context, new ArrayList<Status>(), timelineStatusBuffer);
		weiboItemAdapter.setWeiboType(LocalMemory.WEIBOTYPE_STATUS);
		atListView.setXListViewListener(new TimelineAtXListViewListener(context, this));
		atListView.setAdapter(weiboItemAdapter);
		atListView.setOnItemClickListener(new WeiboItemClickListener(context, weiboItemAdapter));
	}

	@Override
	public void loadFromDB() {
		// TODO Auto-generated method stub
		if (! isLoadedFromDB()){
			AsyncDBTimelineCallback dbAtCallback = new AsyncDBTimelineCallback(mContext, this);
			new AsyncDataLoader(dbAtCallback).execute();
		}	

	}

	@Override
	public void loadMore() {
		// TODO Auto-generated method stub
		AsyncMoreTimelineCallback moreAtCallback = new AsyncMoreTimelineCallback(mContext, this);
		new AsyncDataLoader(moreAtCallback).execute();
	}

	@Override
	public void loadRefresh(boolean isNeedShowProgress) {
		// TODO Auto-generated method stub
		if (! isRefreshing()){
			setIsRefreshing(true);
			AsyncRefreshTimelineCallback refreshCallback = new AsyncRefreshTimelineCallback(mContext, this, isNeedShowProgress);
			new AsyncDataLoader(refreshCallback).execute();
		}
	}

}
