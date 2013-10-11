package com.star.weibo.listview;

import java.util.ArrayList;

import android.content.Context;

import com.star.weibo.RefreshViews;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.async.AsyncDBAtCallback;
import com.star.weibo.buf.AtStatusBuffer;
import com.star.weibo.buf.WeiboBuffer;
import com.star.weibo.db.WeiboDBAdapter;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Status;

public abstract class StatusXListViewProxy {
	protected Context mContext;
	protected XListView atListView;
	protected BufferedWeiboItemAdapter weiboItemAdapter;
	protected boolean loadedFromDB = false;
	protected boolean isRefreshing = false;
	protected boolean isJustLogin = false;
	protected RefreshViews refreshView;
	
	public StatusXListViewProxy(Context context, XListView listView, RefreshViews refreshView){
		this.mContext = context;
		this.atListView = listView;
		this.refreshView = refreshView;
	}
	
	public XListView getXListView(){
		return atListView;
	}
	
	public void setXListView(XListView listView){
		this.atListView = listView;
	}
	
	public boolean isLoadedFromDB(){
		return loadedFromDB;
	}
	
	public void setLoadedFromDB(boolean loadedFromDB){
		this.loadedFromDB = loadedFromDB;
	}
	
	public boolean isRefreshing(){
		return isRefreshing;
	}
	
	public void setIsRefreshing(boolean isRefreshing){
		this.isRefreshing = isRefreshing;
	}
	
	public boolean isJustLogin(){
		return isJustLogin;
	}
	
	public void setJustLogin(boolean justLogin){
		this.isJustLogin = justLogin;
	}
	
	
	public BufferedWeiboItemAdapter getWeiboItemAdapter(){
		return weiboItemAdapter;
	}
	
	public void setRequestCode(int requestCode){
		weiboItemAdapter.setRequestCode(requestCode);
	}
	
	public int getRequestCode(){
		return weiboItemAdapter.getRequestCode();
	}
	
	public RefreshViews getRefreshView(){
		return refreshView;
	}
	
	public abstract void loadFromDB();
	
	public abstract void loadMore();
	
	public abstract void loadRefresh(boolean isNeedShowProgress);

}
