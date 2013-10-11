package com.star.weibo.db;

import android.provider.BaseColumns;

public class CommentColumn implements BaseColumns {
	
	//comment column name
	public static final String CREATEDAT = "createdAt";
	public static final String COMMENTID = "commentId";
	public static final String MID = "mid";
	public static final String IDSTR = "idstr";
	public static final String TEXT = "text";
	public static final String SOURCEURL = "sourceUrl";
	public static final String SOURCEREL = "sourceRel";
	public static final String SOURCENAME = "sourceName";
	public static final String REPLYCOMMENTID = "replyCommentId";
	public static final String USERID = "userId";
	public static final String STATUSID = "statusId";
	
	//index name
	public static final int CREATEDAT_COL = 0;
	public static final int COMMENTID_COL = 1;
	public static final int MID_COL = 2;
	public static final int IDSTR_COL = 3;
	public static final int TEXT_COL = 4;
	public static final int SOURCEURL_COL = 5;
	public static final int SOURCEREL_COL = 6;
	public static final int SOURCENAME_COL = 7;
	public static final int REPLYCOMMENTID_COL = 8;
	public static final int USERID_COL = 9;
	public static final int STATUSID_COL = 10;
	
	public static final String[] PROJECTION = {
		CREATEDAT,
		COMMENTID,
		MID,
		IDSTR,
		TEXT,
		SOURCEURL,
		SOURCEREL,
		SOURCENAME,
		REPLYCOMMENTID,
		USERID,
		STATUSID
	};
	
	public static final String CREATE_COMMENT_COLS = " ("
		+CommentColumn._ID + " integer primary key autoincrement,"
		+CommentColumn.CREATEDAT + " text,"
		+CommentColumn.COMMENTID + " integer,"
		+CommentColumn.MID + " text,"
		+CommentColumn.IDSTR + " text,"
		+CommentColumn.TEXT + " text,"
		+CommentColumn.SOURCEURL + " text,"
		+CommentColumn.SOURCEREL + " text,"
		+CommentColumn.SOURCENAME + " text,"
		+CommentColumn.REPLYCOMMENTID + " integer,"
		+CommentColumn.USERID + " text,"
		+CommentColumn.STATUSID + " text)";
	
}
