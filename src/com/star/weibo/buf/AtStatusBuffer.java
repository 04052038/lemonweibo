package com.star.weibo.buf;

import java.util.List;

import com.star.weibo.db.WeiboDBAdapter;
import com.star.weibo4j.model.Status;

public class AtStatusBuffer extends StatusBuffer {
	
	public AtStatusBuffer(int bufSize, WeiboDBAdapter weiboDBAdapter){
		super(bufSize, weiboDBAdapter);
	}

	@Override
	public void addStatusListDB(List<Status> statusList) {
		// TODO Auto-generated method stub
		weiboDBAdapter.insertAtStatusList(statusList);

	}

	@Override
	public void clearStatusListDB() {
		// TODO Auto-generated method stub
		weiboDBAdapter.clearAtStatus();

	}

	@Override
	public void delStatusDB(Status status) {
		// TODO Auto-generated method stub
		weiboDBAdapter.deleteAtStatus(status);

	}
	
	@Override
	public List<Status> queryStatusListDB(){
		return weiboDBAdapter.getAllAtStatus();
	}
	
	@Override
	public void setRefreshTimeDB(long refreshTime){
		weiboDBAdapter.updateAtStatusRefreshTime(refreshTime);
	}
	
	@Override
	public long getRefreshTimeDB(){
		return weiboDBAdapter.getAtStatusRefreshTime();
	}


}
