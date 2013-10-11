package com.star.weibo.buf;

import java.util.ArrayList;
import java.util.List;

import com.star.weibo4j.model.Status;

public class UserStatusBuffer extends StatusBuffer {
	
	private long refreshTime;
	
	public UserStatusBuffer(int bufSize){
		super(bufSize);
	}

	@Override
	public void addStatusListDB(List<Status> statusList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearStatusListDB() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delStatusDB(Status status) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getRefreshTimeDB() {
		// TODO Auto-generated method stub
		return refreshTime;
	}

	@Override
	public List<Status> queryStatusListDB() {
		// TODO Auto-generated method stub
		return new ArrayList<Status>();
	}

	@Override
	public void setRefreshTimeDB(long refreshTime) {
		// TODO Auto-generated method stub
		this.refreshTime = refreshTime;
	}

}
