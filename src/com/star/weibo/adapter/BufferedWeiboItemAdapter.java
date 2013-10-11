package com.star.weibo.adapter;

import java.util.List;
import java.util.ArrayList;

import android.content.Context;

import com.star.weibo.buf.StatusBuffer;
import com.star.weibo4j.model.Status;

/**
 * 20121122:add weibo buffer
 * @author Administrator
 *
 */
public class BufferedWeiboItemAdapter extends WeiboItemAdapter {
	
	//after adding more status, showing location of listview
	private int selectedIndex = -1;
	private StatusBuffer statusBuffer;
	private long refreshTime;
	//index of status that is showing in WeiboDetail activity, used for weibo delete
	private int clickedStatusIndex;
	private int requestCode = -1;
	
	public BufferedWeiboItemAdapter(Context context, List<Status> statusList, StatusBuffer statusBuf){
		super(context, statusList);
		this.statusBuffer = statusBuf;
	}
	
	public void setSelectedIndex(int selIndex){
		selectedIndex = selIndex;
	}
	
	public int getSelectedIndex(){
		return selectedIndex;
	}
	
	public void setRequestCode(int requestCode){
		this.requestCode = requestCode;
	}
	
	public int getRequestCode(){
		return requestCode;
	}
	
	public void addItemsBefore(List<Status> refreshStatus){
		statusBuffer.addItemsBefore(refreshStatus);
	}
	
	public void addItemsLast(List<Status> moreStatus){
		statusBuffer.addItemsLast(moreStatus);
	}
	
	public boolean isOverBuffer(){
		return statusBuffer.isOverBuffer();
	}
	
	public void clear(){
		mStatusList.clear();
	}
	
	public void syncStatusBuffer(){
		clear();
		mStatusList.addAll(statusBuffer.getStatusList());
	}
	
	public int statusBufferSize(){
		if (statusBuffer != null){
			return statusBuffer.statusSize();
		}else{
			return 0;
		}
	}
	
	public void clearDB(){
		statusBuffer.clear();
	}
	
	public int statusSize(){
		if (mStatusList == null){
			return 0;
		}else{
			return mStatusList.size();
		}
	}
	
	public Status getSelectedStatus(){
		return mStatusList.get(selectedIndex);
	}
	
	public long getRefreshTime(){
		return refreshTime;
	}
	
	public void setRefreshTime(long refreshTime){
		this.refreshTime = refreshTime;
		statusBuffer.setRefreshTimeDB(refreshTime);
	}
	
	public void loadStatusDB(){
		statusBuffer.initializeStatusBuffer();
		refreshTime = statusBuffer.getRefreshTimeDB();
	}
	
	public int getClickedStatusIndex(){
		return clickedStatusIndex;
	}
	
	public void setClickedStatusIndex(int clickedStatusIndex){
		this.clickedStatusIndex = clickedStatusIndex;
	}
	
	public Status getClickedStatus(){
		return mStatusList.get(clickedStatusIndex);
	}
	
	public void removeClickedStatus(){
		statusBuffer.delItem(clickedStatusIndex);
	}

}
