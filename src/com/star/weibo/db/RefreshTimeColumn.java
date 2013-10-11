package com.star.weibo.db;

import android.provider.BaseColumns;

public class RefreshTimeColumn implements BaseColumns {
	
	public static final String REFRESH_TYPE = "refreshType";
	public static final String REFRESH_TIME = "refreshTime";
	
	public static final int REFRESH_TYPE_COL = 0;
	public static final int REFRESH_TIME_COL = 1;
	
	public static final String[] PROJECTION = {
		REFRESH_TYPE,
		REFRESH_TIME
	};
	
	public static final String CREATE_REFRESHTIME_COLS = " ("
		 +RefreshTimeColumn._ID + " integer primary key autoincrement,"
		 +RefreshTimeColumn.REFRESH_TYPE + " integer,"
	     +RefreshTimeColumn.REFRESH_TIME + " integer)";

}
