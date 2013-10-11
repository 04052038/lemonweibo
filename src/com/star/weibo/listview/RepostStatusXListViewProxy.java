package com.star.weibo.listview;

import java.util.ArrayList;

import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.RefreshViews;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.async.AsyncMoreRepostCallback;
import com.star.weibo.async.AsyncRefreshRepostCallback;
import com.star.weibo.buf.RepostStatusBuffer;
import com.star.weibo.buf.WeiboBuffer;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Status;

public class RepostStatusXListViewProxy extends StatusXListViewProxy {
	
	private String srcStatusId;
	
	public RepostStatusXListViewProxy(Context context, XListView listView, RefreshViews refreshView){
		super(context, listView, refreshView);
		RepostStatusBuffer repostStatusBuffer = new RepostStatusBuffer(WeiboBuffer.WEIBO_STATUSLIST_BUF_MAX);
		weiboItemAdapter = new BufferedWeiboItemAdapter(context, new ArrayList<Status>(), repostStatusBuffer);
		weiboItemAdapter.setShowRetweetedStatus(false);
		atListView.setXListViewListener(new RepostXListViewListener(context, this));
		atListView.setAdapter(weiboItemAdapter);
		WeiboItemClickListener itemClickListener = new WeiboItemClickListener(context, weiboItemAdapter);
		atListView.setOnItemClickListener(itemClickListener);
	}
	
	public String getSrcStatusId() {
		return srcStatusId;
	}

	public void setSrcStatusId(String srcStatusId) {
		this.srcStatusId = srcStatusId;
	}

	@Override
	public void loadFromDB() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadMore() {
		// TODO Auto-generated method stub
		AsyncMoreRepostCallback moreCallback = new AsyncMoreRepostCallback(mContext, this);
		new AsyncDataLoader(moreCallback).execute();
	}

	@Override
	public void loadRefresh(boolean isNeedShowProgress) {
		// TODO Auto-generated method stub
		if (! isRefreshing()){
			setIsRefreshing(true);
			AsyncRefreshRepostCallback refreshCallback = new AsyncRefreshRepostCallback(mContext, this, isNeedShowProgress);
			new AsyncDataLoader(refreshCallback).execute();
		}
	}

}
