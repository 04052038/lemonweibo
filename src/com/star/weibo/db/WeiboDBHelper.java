package com.star.weibo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.star.yytv.Log;

public class WeiboDBHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "weibotest003.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final int STATUS_TYPE = 1;
	public static final int AT_STATUS_TYPE = 2;
	public static final int COMMENT_TOME_TYPE = 3;
	
	//TimeLine Status table
	public static final String STATUS_TABLE = "status";
	public static final String RETWEETEDSTATUS_TABLE = "retweetedStatus";
	public static final String STATUSUSER_TABLE = "statusUser";
	public static final String RETWEETEDSTATUSUSER_TABLE = "retweetedStatusUser";
	
	//AtMe status table
	public static final String AT_STATUS_TABLE = "atStatus";
	public static final String AT_RETWEETEDSTATUS_TABLE = "atRetweetedStatus";
	public static final String AT_STATUSUSER_TABLE = "atStatusUser";
	public static final String AT_RETWEETEDSTATUSUSER_TABLE = "atRetweetedStatusUser";
	
	//refresh time table
	public static final String REFRESHTIME_TABLE = "refreshTime";
	
	//create TimeLine Status table
	public static final String CREATE_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS " + STATUS_TABLE + StatusColumn.CREATE_STATUS_COLS;
	public static final String CREATE_RETWEETEDSTATUS_TABLE = "CREATE TABLE IF NOT EXISTS " + RETWEETEDSTATUS_TABLE + StatusColumn.CREATE_STATUS_COLS;
	public static final String CREATE_STATUSUSER_TABLE = "CREATE TABLE IF NOT EXISTS " + STATUSUSER_TABLE + UserColumn.CREATE_USER_COLS;
	public static final String CREATE_RETWEETEDSTATUSUSER_TABLE = "CREATE TABLE IF NOT EXISTS " + RETWEETEDSTATUSUSER_TABLE + UserColumn.CREATE_USER_COLS;
	
	//create AtMe Status table
	public static final String CREATE_AT_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS " + AT_STATUS_TABLE + StatusColumn.CREATE_STATUS_COLS;
	public static final String CREATE_AT_RETWEETEDSTATUS_TABLE = "CREATE TABLE IF NOT EXISTS " + AT_RETWEETEDSTATUS_TABLE + StatusColumn.CREATE_STATUS_COLS;
	public static final String CREATE_AT_STATUSUSER_TABLE = "CREATE TABLE IF NOT EXISTS " + AT_STATUSUSER_TABLE + UserColumn.CREATE_USER_COLS;
	public static final String CREATE_AT_RETWEETEDSTATUSUSER_TABLE = "CREATE TABLE IF NOT EXISTS " + AT_RETWEETEDSTATUSUSER_TABLE + UserColumn.CREATE_USER_COLS;
	
	//create refresh time table
	public static final String CREATE_REFRESHTIME_TABLE = "CREATE TABLE IF NOT EXISTS " + REFRESHTIME_TABLE + RefreshTimeColumn.CREATE_REFRESHTIME_COLS;
	
	//drop  TimeLineStutus table
	public static final String DROP_STATUS_TABLE = "DROP TABLE IF EXISTS " + STATUS_TABLE;
	public static final String DROP_RETWEETEDSTATUS_TABLE = "DROP TABLE IF EXISTS " + RETWEETEDSTATUS_TABLE;
	public static final String DROP_STATUSUSER_TABLE = "DROP TABLE IF EXISTS " + STATUSUSER_TABLE;
	public static final String DROP_RETWEETEDSTATUSUSER_TABLE = "DROP TABLE IF EXISTS " + RETWEETEDSTATUSUSER_TABLE;
	
	//drop AtMe table
	public static final String DROP_AT_STATUS_TABLE = "DROP TABLE IF EXISTS " + AT_STATUS_TABLE;
	public static final String DROP_AT_RETWEETEDSTATUS_TABLE = "DROP TABLE IF EXISTS " + AT_RETWEETEDSTATUS_TABLE;
	public static final String DROP_AT_STATUSUSER_TABLE = "DROP TABLE IF EXISTS " + AT_STATUSUSER_TABLE;
	public static final String DROP_AT_RETWEETEDSTATUSUSER_TABLE = "DROP TABLE IF EXISTS " + AT_RETWEETEDSTATUSUSER_TABLE;
	
	//drop refresh time table
	public static final String DROP_REFRESHTIME_TABLE = "DROP TABLE IF EXISTS " + REFRESHTIME_TABLE;
	
	public WeiboDBHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_STATUS_TABLE);
		db.execSQL(CREATE_RETWEETEDSTATUS_TABLE);
		db.execSQL(CREATE_STATUSUSER_TABLE);
		db.execSQL(CREATE_RETWEETEDSTATUSUSER_TABLE);
		
		db.execSQL(CREATE_AT_STATUS_TABLE);
		db.execSQL(CREATE_AT_RETWEETEDSTATUS_TABLE);
		db.execSQL(CREATE_AT_STATUSUSER_TABLE);
		db.execSQL(CREATE_AT_RETWEETEDSTATUSUSER_TABLE);
		
		db.execSQL(CREATE_REFRESHTIME_TABLE);
		
		CommentDBHelper.onCreate(db);
		initializeRefreshTimeTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("WeiboDBHelper", "onUpgrade to newVersion " + newVersion);
		db.execSQL(DROP_STATUS_TABLE);
		db.execSQL(DROP_RETWEETEDSTATUS_TABLE);
		db.execSQL(DROP_STATUSUSER_TABLE);
		db.execSQL(DROP_RETWEETEDSTATUSUSER_TABLE);
		
		db.execSQL(DROP_AT_STATUS_TABLE);
		db.execSQL(DROP_AT_RETWEETEDSTATUS_TABLE);
		db.execSQL(DROP_AT_STATUSUSER_TABLE);
		db.execSQL(DROP_AT_RETWEETEDSTATUSUSER_TABLE);
		
		db.execSQL(DROP_REFRESHTIME_TABLE);
		
		CommentDBHelper.onUpgrade(db, oldVersion, newVersion);
		
		onCreate(db);
	}
	
	private void initializeRefreshTimeTable(SQLiteDatabase db){
		long nowtime = System.currentTimeMillis();
		ContentValues values = new ContentValues();
		values.put(RefreshTimeColumn.REFRESH_TYPE, WeiboDBHelper.STATUS_TYPE);
		values.put(RefreshTimeColumn.REFRESH_TIME, nowtime);
		db.insert(REFRESHTIME_TABLE, null, values);
		
		values = new ContentValues();
		values.put(RefreshTimeColumn.REFRESH_TYPE, WeiboDBHelper.AT_STATUS_TYPE);
		values.put(RefreshTimeColumn.REFRESH_TIME, nowtime);
		db.insert(REFRESHTIME_TABLE, null, values);
		
		values = new ContentValues();
		values.put(RefreshTimeColumn.REFRESH_TYPE, WeiboDBHelper.COMMENT_TOME_TYPE);
		values.put(RefreshTimeColumn.REFRESH_TIME, nowtime);
		db.insert(REFRESHTIME_TABLE, null, values);
		
	}

}
