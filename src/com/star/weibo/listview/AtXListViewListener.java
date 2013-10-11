package com.star.weibo.listview;

import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.WeiboToast;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.async.AsyncMoreAtCallback;
import com.star.weibo.async.AsyncRefreshAtCallback;
import com.star.weibo.xlist.XListView;
import com.star.weibo.xlist.XListView.IXListViewListener;

public class AtXListViewListener implements IXListViewListener {
	
	private Context mContext;
	private AtXListViewProxy atXListViewProxy;
	
	public AtXListViewListener(Context context, AtXListViewProxy xListViewProxy){
		this.mContext = context;
		this.atXListViewProxy = xListViewProxy;
	}
	
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (atXListViewProxy.getWeiboItemAdapter().isOverBuffer()){
			WeiboToast.show(mContext, "缓存已满！");
		}else{
			AsyncMoreAtCallback moreCallback = new AsyncMoreAtCallback(mContext, atXListViewProxy);
			new AsyncDataLoader(moreCallback).execute();
		}
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (! atXListViewProxy.isRefreshing()){
			AsyncRefreshAtCallback refreshCallback = new AsyncRefreshAtCallback(mContext, atXListViewProxy, false);
			new AsyncDataLoader(refreshCallback).execute();
		}

	}

}
