package com.star.weibo.buf;

import java.util.List;

import com.star.weibo.db.WeiboDBAdapter;
import com.star.weibo4j.model.Status;

public class TimelineStatusBuffer extends StatusBuffer {
	
	public TimelineStatusBuffer(int bufSize, WeiboDBAdapter weiboDBAdapter){
		super(bufSize, weiboDBAdapter);
	}

	@Override
	public void addStatusListDB(List<Status> statusList) {
		// TODO Auto-generated method stub
		weiboDBAdapter.insertStatusList(statusList);

	}

	@Override
	public void clearStatusListDB() {
		// TODO Auto-generated method stub
		weiboDBAdapter.clearStatus();
	}

	@Override
	public void delStatusDB(Status status) {
		// TODO Auto-generated method stub
		weiboDBAdapter.deleteStatus(status);

	}
	
	@Override
	public List<Status> queryStatusListDB(){
		return weiboDBAdapter.getAllStatus();
	}
	
	@Override
	public void setRefreshTimeDB(long refreshTime){
		weiboDBAdapter.updateStatusRefreshTime(refreshTime);
	}
	
	@Override
	public long getRefreshTimeDB(){
		return weiboDBAdapter.getStatusRefreshTime();
	}

}
