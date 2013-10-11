package com.star.weibo.listview;

import android.content.Context;

import com.star.weibo.RefreshViews;
import com.star.weibo.adapter.BufferedCommentAdapter;
import com.star.weibo.xlist.XListView;

public abstract class CommentXListViewProxy {
	
	protected Context mContext;
	protected XListView commentListView;
	protected BufferedCommentAdapter commentAdapter;
	protected boolean loadedFromDB = false;
	protected boolean isRefreshing = false;
	protected boolean isJustLogin = false;
	protected RefreshViews refreshView;
	
	public CommentXListViewProxy(Context context, XListView listView, RefreshViews refreshView){
		this.mContext = context;
		this.commentListView = listView;
		this.refreshView = refreshView;
	}
	
	public XListView getXListView(){
		return commentListView;
	}
	
	public void setXListView(XListView listView){
		this.commentListView = listView;
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
	
	public BufferedCommentAdapter getCommentAdapter(){
		return commentAdapter;
	}
	
	public void setRequestCode(int requestCode){
		commentAdapter.setRequestCode(requestCode);
	}
	
	public int getRequestCode(){
		return commentAdapter.getRequestCode();
	}
	
	public boolean isJustLogin(){
		return isJustLogin;
	}
	
	public void setJustLogin(boolean justLogin){
		this.isJustLogin = justLogin;
	}
	
	public RefreshViews getRefreshView(){
		return refreshView;
	}
	
	public abstract void loadFromDB();
	
	public abstract void loadMore();
	
	public abstract void loadRefresh(boolean isNeedShowProgress);

}
