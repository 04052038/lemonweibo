package com.star.weibo.buf;

import java.util.ArrayList;
import java.util.List;

import com.star.weibo.db.WeiboDBAdapter;

import com.star.weibo4j.model.Status;

/**
 * xujun 20121122: add weibo buffer
 * @author Administrator
 *
 */
public abstract class StatusBuffer {
	//status buffer size
	private int bufSize;
	protected WeiboDBAdapter weiboDBAdapter;
	protected List<Status> mStatusList = new ArrayList<Status>();
	//after adding more status, showing location of listview
	private int selectedIndex = -1;
	private boolean isOverBuffer = false;
	
	public StatusBuffer(int bufSize, WeiboDBAdapter weiboDBAdapter){
		this.bufSize = bufSize;
		this.weiboDBAdapter = weiboDBAdapter;
	}
	public void setSelectedIndex(int selIndex){
		selectedIndex = selIndex;
	}
	
	public int getSelectedIndex(){
		return selectedIndex;
	}
	
	public StatusBuffer(int bufferSize){
		bufSize = bufferSize;
	}
	
	public List<Status> getStatusList(){
		return mStatusList;
	}
	
	public boolean isOverBuffer(){
		return isOverBuffer;
	}
	
	public int statusSize(){
		int size = 0;
		if (mStatusList != null){
			size = mStatusList.size();
		}
		return size;
	}
	
	/**
	 * add items before statusList
	 * @param refreshStatus
	 */
	public void addItemsBefore(List<Status> refreshStatus){
		if (refreshStatus != null && refreshStatus.size() > 0){
			List<Status> tempStatusList = new ArrayList<Status>();
			tempStatusList.addAll(refreshStatus);
			tempStatusList.addAll(mStatusList);
			mStatusList.clear();
			mStatusList.addAll(tempStatusList);
			addStatusListDB(refreshStatus);
			if (mStatusList.size() > bufSize){
				isOverBuffer = true;
				for (int i = mStatusList.size(); i > bufSize; i --){
					delStatusDB((Status)mStatusList.get(i-1));
					mStatusList.remove(i-1);
				}
			}
		}
		
	}
	
	/**
	 * add items last
	 * @param moreStatus
	 */
	public void addItemsLast(List<Status> moreStatus){
		if (moreStatus != null && moreStatus.size() > 0){
			mStatusList.addAll(moreStatus);
			addStatusListDB(moreStatus);
			if (mStatusList.size() > bufSize){
				isOverBuffer = true;
			}
		}
	}
	
	/*
	 * clear buffer
	 */
	public void clear(){
		clearStatusListDB();
		mStatusList.clear();
		isOverBuffer = false;
	}
	
	public void initializeStatusBuffer(){
		if (weiboDBAdapter != null){
			weiboDBAdapter.open();
		}
		mStatusList = queryStatusListDB();
	}
	
	public void delItem(int statusIndex){
		Status status = mStatusList.get(statusIndex);
		if (status != null){
			delStatusDB(status);
			mStatusList.remove(statusIndex);
		}
	}
	
	
	public abstract void delStatusDB(Status status);
	
	public abstract void addStatusListDB(List<Status> statusList);
	
	public abstract void clearStatusListDB();
	
	public abstract List<Status> queryStatusListDB();
	
	public abstract void setRefreshTimeDB(long refreshTime);
	
	public abstract long getRefreshTimeDB();
	
}
