package com.star.weibo.db;

import android.database.sqlite.SQLiteDatabase;

public class CommentDBHelper {
	
	//comment table
	public static final String COMMENT_TABLE = "comment";
	public static final String REPLYCOMMENT_TABLE = "replyComment";
	public static final String COMMENT_USER_TABLE = "commentUser";
	public static final String REPLYCOMMENT_USER_TABLE = "replyCommentUser";
	public static final String COMMENT_STATUS_TABLE = "commentStatus";
	public static final String COMMENT_RETWEETEDSTATUS_TABLE = "commentRetweetedStatus";
	public static final String COMMENT_STATUSUSER_TABLE = "commentStatusUser";
	public static final String COMMENT_RETWEETEDSTATUSUSER_TABLE = "commentRetweetedStatusUser";
	
	//create comment table
	public static final String CREATE_COMMENT_TABLE = "CREATE TABLE IF NOT EXISTS " + COMMENT_TABLE + CommentColumn.CREATE_COMMENT_COLS;
	public static final String CREATE_REPLYCOMMENT_TABLE = "CREATE TABLE IF NOT EXISTS " + REPLYCOMMENT_TABLE + CommentColumn.CREATE_COMMENT_COLS;
	public static final String CREATE_COMMENT_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + COMMENT_USER_TABLE + UserColumn.CREATE_USER_COLS;
	public static final String CREATE_REPLYCOMMENT_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + REPLYCOMMENT_USER_TABLE + UserColumn.CREATE_USER_COLS;
	public static final String CREATE_COMMENT_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS " + COMMENT_STATUS_TABLE + StatusColumn.CREATE_STATUS_COLS;
	public static final String CREATE_COMMENT_RETWEETEDSTATUS_TABLE = "CREATE TABLE IF NOT EXISTS " + COMMENT_RETWEETEDSTATUS_TABLE + StatusColumn.CREATE_STATUS_COLS;
	public static final String CREATE_COMMENT_STATUSUSER_TABLE = "CREATE TABLE IF NOT EXISTS " + COMMENT_STATUSUSER_TABLE + UserColumn.CREATE_USER_COLS;
	public static final String CREATE_COMMENT_RETWEETEDSTATUSUSER_TABLE = "CREATE TABLE IF NOT EXISTS " + COMMENT_RETWEETEDSTATUSUSER_TABLE + UserColumn.CREATE_USER_COLS;
	
	//drop comment table
	public static final String DROP_COMMENT_TABLE = "DROP TABLE IF EXISTS " + COMMENT_TABLE;
	public static final String DROP_REPLYCOMMENT_TABLE = "DROP TABLE IF EXISTS " + REPLYCOMMENT_TABLE;
	public static final String DROP_COMMENT_USER_TABLE = "DROP TABLE IF EXISTS " + COMMENT_USER_TABLE;
	public static final String DROP_REPLYCOMMENT_USER_TABLE = "DROP TABLE IF EXISTS " + REPLYCOMMENT_USER_TABLE;
	public static final String DROP_COMMENT_STATUS_TABLE = "DROP TABLE IF EXISTS " + COMMENT_STATUS_TABLE;
	public static final String DROP_COMMENT_RETWEETEDSTATUS_TABLE = "DROP TABLE IF EXISTS " + COMMENT_RETWEETEDSTATUS_TABLE;
	public static final String DROP_COMMENT_STATUSUSER_TABLE = "DROP TABLE IF EXISTS " + COMMENT_STATUSUSER_TABLE;
	public static final String DROP_COMMENT_RETWEETEDSTATUSUSER_TABLE = "DROP TABLE IF EXISTS " + COMMENT_RETWEETEDSTATUSUSER_TABLE;

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_COMMENT_TABLE);
		db.execSQL(CREATE_REPLYCOMMENT_TABLE);
		db.execSQL(CREATE_COMMENT_USER_TABLE);
		db.execSQL(CREATE_REPLYCOMMENT_USER_TABLE);
		db.execSQL(CREATE_COMMENT_STATUS_TABLE);
		db.execSQL(CREATE_COMMENT_RETWEETEDSTATUS_TABLE);
		db.execSQL(CREATE_COMMENT_STATUSUSER_TABLE);
		db.execSQL(CREATE_COMMENT_RETWEETEDSTATUSUSER_TABLE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_COMMENT_TABLE);
		db.execSQL(DROP_REPLYCOMMENT_TABLE);
		db.execSQL(DROP_COMMENT_USER_TABLE);
		db.execSQL(DROP_REPLYCOMMENT_USER_TABLE);
		db.execSQL(DROP_COMMENT_STATUS_TABLE);
		db.execSQL(DROP_COMMENT_RETWEETEDSTATUS_TABLE);
		db.execSQL(DROP_COMMENT_STATUSUSER_TABLE);
		db.execSQL(DROP_COMMENT_RETWEETEDSTATUSUSER_TABLE);
	}
}
