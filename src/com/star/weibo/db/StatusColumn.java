package com.star.weibo.db;

import android.provider.BaseColumns;

public class StatusColumn implements BaseColumns {
	//status column name
	public static final String USERID = "userID";
	public static final String CREATEDAT = "createdAt";
	public static final String STATUSID = "statusId";
	public static final String MID = "mid";
	public static final String IDSTR = "idstr";
	public static final String TEXT = "text";
	public static final String SOURCEURL = "sourceUrl";
	public static final String SOURCEREL = "sourceRel";
	public static final String SOURCENAME = "sourceName";
	public static final String FAVORITED = "favorited";
	public static final String TRUNCATED = "truncated";
	public static final String INREPLYTOSTATUSID = "inReplyToStatusId";
	public static final String INREPLYTOUSERID = "inReplyToUserId";
	public static final String INREPLYTOSCREENNAME = "inReplyToScreenName";
	public static final String THUMBNAILPIC = "thumbnailPic";
	public static final String BMIDDLEPIC = "bmiddlePic";
	public static final String ORIGINALPIC = "originalPic";
	public static final String RETWEETEDSTATUSFLAG = "retweetedStatusFlag";
	public static final String RETWEETEDSTATUSID = "retweetedStatusId";
	public static final String GEO = "geo";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String REPOSTSCOUNT = "repostsCount";
	public static final String COMMENTSCOUNT = "commentsCount";
	public static final String ANNOTATIONS = "annotations";
	public static final String MLEVEL = "mlevel";
	public static final String VISIBLETYPE = "visibleType";
	public static final String VISIBLELISTID = "visibleListId";
	public static final String COMMENTID = "commentId";
	
	//index value
	public static final int USERID_COL = 0;
	public static final int CREATEDAT_COL = 1;
	public static final int ID_COL = 2;
	public static final int MID_COL = 3;
	public static final int IDSTR_COL = 4;
	public static final int TEXT_COL = 5;
	public static final int SOURCEURL_COL = 6;
	public static final int SOURCEREL_COL = 7;
	public static final int SOURCENAME_COL = 8;
	public static final int FAVORITED_COL = 9;
	public static final int TRUNCATED_COL = 10;
	public static final int INREPLYTOSTATUSID_COL = 11;
	public static final int INREPLYTOUSERID_COL = 12;
	public static final int INREPLYTOSCREENNAME_COL = 13;
	public static final int THUMBNAILPIC_COL = 14;
	public static final int BMIDDLEPIC_COL = 15;
	public static final int ORIGINALPIC_COL = 16;
	public static final int RETWEETEDSTATUSFLAG_COL = 17;
	public static final int RETWEETEDSTATUSID_COL = 18;
	public static final int GEO_COL = 19;
	public static final int LATITUDE_COL = 20;
	public static final int LONGITUDE_COL = 21;
	public static final int REPOSTSCOUNT_COL = 22;
	public static final int COMMENTSCOUNT_COL = 23;
	public static final int ANNOTATIONS_COL = 24;
	public static final int MLEVEL_COL = 25;
	public static final int VISIBLETYPE_COL = 26;
	public static final int VISIBLELISTID_COL = 27;
	public static final int COMMENTID_COL = 28;
	
	public static final String[] PROJECTION = {
		USERID,
		CREATEDAT,
		STATUSID,
		MID,
		IDSTR,
		TEXT,
		SOURCEURL,
		SOURCEREL,
		SOURCENAME,
		FAVORITED,
		TRUNCATED,
		INREPLYTOSTATUSID,
		INREPLYTOUSERID,
		INREPLYTOSCREENNAME,
		THUMBNAILPIC,
		BMIDDLEPIC,
		ORIGINALPIC,
		RETWEETEDSTATUSFLAG,
		RETWEETEDSTATUSID,
		GEO,
		LATITUDE,
		LONGITUDE,
		REPOSTSCOUNT,
		COMMENTSCOUNT,
		ANNOTATIONS,
		MLEVEL,
		VISIBLETYPE,
		VISIBLELISTID,
		COMMENTID
	};
	
	public static final String CREATE_STATUS_COLS = " ("
		    +StatusColumn._ID + " integer primary key autoincrement,"
		    +StatusColumn.USERID + " integer,"
			+StatusColumn.CREATEDAT + " text,"
			+StatusColumn.STATUSID + " text,"
			+StatusColumn.MID + " text,"
			+StatusColumn.IDSTR + " integer,"
			+StatusColumn.TEXT + " text,"
			+StatusColumn.SOURCEURL + " text,"
			+StatusColumn.SOURCEREL + " text,"
			+StatusColumn.SOURCENAME + " text,"
			+StatusColumn.FAVORITED + " integer,"
			+StatusColumn.TRUNCATED + " integer,"
			+StatusColumn.INREPLYTOSTATUSID + " integer,"
			+StatusColumn.INREPLYTOUSERID + " integer,"
			+StatusColumn.INREPLYTOSCREENNAME + " text,"
			+StatusColumn.THUMBNAILPIC + " text,"
			+StatusColumn.BMIDDLEPIC + " text,"
			+StatusColumn.ORIGINALPIC + " text,"
			+StatusColumn.RETWEETEDSTATUSFLAG + " integer,"
			+StatusColumn.RETWEETEDSTATUSID + " text,"
			+StatusColumn.GEO + " text,"
			+StatusColumn.LATITUDE + " double,"
			+StatusColumn.LONGITUDE + " double,"
			+StatusColumn.REPOSTSCOUNT + " integer,"
			+StatusColumn.COMMENTSCOUNT + " integer,"
			+StatusColumn.ANNOTATIONS + " text,"
			+StatusColumn.MLEVEL + " integer,"
			+StatusColumn.VISIBLETYPE + " integer,"
			+StatusColumn.VISIBLELISTID + " integer,"
			+StatusColumn.COMMENTID + " integer)";
}
