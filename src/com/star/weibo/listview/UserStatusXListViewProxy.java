package com.star.weibo.listview;

import java.util.ArrayList;

import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.RefreshViews;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.async.AsyncMoreAtCallback;
import com.star.weibo.async.AsyncMoreUserStatusCallback;
import com.star.weibo.async.AsyncRefreshAtCallback;
import com.star.weibo.async.AsyncRefreshUserInfoCallback;
import com.star.weibo.buf.AtStatusBuffer;
import com.star.weibo.buf.UserStatusBuffer;
import com.star.weibo.buf.WeiboBuffer;
import com.star.weibo.db.WeiboDBAdapter;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.User;

import com.star.weibo.listview.UserStatusXListViewListener;

public class UserStatusXListViewProxy extends StatusXListViewProxy {
	
	private boolean isRefreshingUserInfo;
	private boolean isRefreshingUserStatus;
	private String username = null;
	private String userid = null;
	private RefreshViews refreshView;
	private User user;

	public UserStatusXListViewProxy(Context context, XListView listView, RefreshViews refreshView){
		super(context, listView, refreshView);
		UserStatusBuffer userStatusBuffer = new UserStatusBuffer(WeiboBuffer.WEIBO_STATUSLIST_BUF_MAX);
		weiboItemAdapter = new BufferedWeiboItemAdapter(context, new ArrayList<Status>(), userStatusBuffer);
		weiboItemAdapter.setShowPortraitLayout(false);
		atListView.setXListViewListener(new UserStatusXListViewListener(context, this));
		atListView.setAdapter(weiboItemAdapter);
		WeiboItemClickListener itemClickListener = new WeiboItemClickListener(context, weiboItemAdapter);
		itemClickListener.setHeaderNum((byte)2);
		atListView.setOnItemClickListener(itemClickListener);
	}
	
	public void setIsRefreshingUserInfo(boolean isRereshing){
		isRefreshingUserInfo = isRereshing;
	}
	
	public void setIsRefreshingUserStatus(boolean isRereshing){
		isRefreshingUserStatus = isRereshing;
	}
	
	public boolean isRefreshing(){
		return isRefreshingUserInfo || isRefreshingUserStatus;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public void setUserid(String userid){
		this.userid = userid;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getUserid(){
		return userid;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public User getUser(){
		return user;
	}
	
	@Override
	public void loadFromDB() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadMore() {
		// TODO Auto-generated method stub
		AsyncMoreUserStatusCallback moreCallback = new AsyncMoreUserStatusCallback(mContext, this);
		new AsyncDataLoader(moreCallback).execute();

	}

	@Override
	public void loadRefresh(boolean isNeedShowProgress) {
		// TODO Auto-generated method stub
		if (! isRefreshing()){
			setIsRefreshingUserInfo(true);
			AsyncRefreshUserInfoCallback refreshCallback = new AsyncRefreshUserInfoCallback(mContext, this, isNeedShowProgress);
			new AsyncDataLoader(refreshCallback).execute();
		}
	}

}
