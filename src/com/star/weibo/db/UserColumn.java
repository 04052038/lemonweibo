package com.star.weibo.db;

import android.provider.BaseColumns;

public class UserColumn implements BaseColumns {
	
	//user column name
	public static final String USERID = "userID";
	public static final String SCREENNAME = "screenName";
	public static final String NAME = "name";
	public static final String PROVINCE = "province";
	public static final String CITY = "city";
	public static final String LOCATION = "location";
	public static final String DESCRIPTION = "description";
	public static final String URL = "url";
	public static final String PROFILEIMAGEURL = "profileImageUrl";
	public static final String USERDOMAIN = "userDomain";
	public static final String GENDER = "gender";
	public static final String FOLLOWERSCOUNT = "followersCount";
	public static final String FRIENDSCOUNT = "friendsCount";
	public static final String STATUSESCOUNT = "statusesCount";
	public static final String FAVOURITESCOUNT = "favouritesCount";
	public static final String CREATEDAT = "createdAt";
	public static final String FOLLOWING = "following";
	public static final String VERIFIED = "verified";
	public static final String VERIFIEDTYPE = "verifiedType";
	public static final String ALLOWALLACTMSG = "allowAllActMsg";
	public static final String ALLOWALLCOMMENT = "allowAllComment";
	public static final String FOLLOWME = "followMe";
	public static final String AVATARLARGE = "avatarLarge";
	public static final String ONLINESTATUS = "onlineStatus";
	public static final String BIFOLLOWERSCOUNT = "biFollowersCount";
	public static final String REMARK = "remark";
	public static final String LANG = "lang";
	public static final String VERIFIEDREASON = "verifiedReason";
	public static final String WEIHAO = "weihao";
	public static final String STATUSID = "statusId";
	public static final String COMMENTID = "commentId";
	public static final String REPLYCOMMENTID = "replyCommentId";
	public static final String RETWEETEDSTATUSID = "retweetedStatusId";

	//index name
	public static final int USERID_COL = 0;
	public static final int SCREENNAME_COL = 1;
	public static final int NAME_COL = 2;
	public static final int PROVINCE_COL = 3;
	public static final int CITY_COL = 4;
	public static final int LOCATION_COL = 5;
	public static final int DESCRIPTION_COL = 6;
	public static final int URL_COL = 7;
	public static final int PROFILEIMAGEURL_COL = 8;
	public static final int USERDOMAIN_COL = 9;
	public static final int GENDER_COL = 10;
	public static final int FOLLOWERSCOUNT_COL = 11;
	public static final int FRIENDSCOUNT_COL = 12;
	public static final int STATUSESCOUNT_COL = 13;
	public static final int FAVOURITESCOUNT_COL = 14;
	public static final int CREATEDAT_COL = 15;
	public static final int FOLLOWING_COL = 16;
	public static final int VERIFIED_COL = 17;
	public static final int VERIFIEDTYPE_COL = 18;
	public static final int ALLOWALLACTMSG_COL = 19;
	public static final int ALLOWALLCOMMENT_COL = 20;
	public static final int FOLLOWME_COL = 21;
	public static final int AVATARLARGE_COL = 22;
	public static final int ONLINESTATUS_COL = 23;
	public static final int BIFOLLOWERSCOUNT_COL = 24;
	public static final int REMARK_COL = 25;
	public static final int LANG_COL = 26;
	public static final int VERIFIEDREASON_COL = 27;
	public static final int WEIHAO_COL = 28;
	public static final int STATUSID_COL = 29;
	public static final int COMMENTID_COL = 30;
	public static final int REPLYCOMMENTID_COL = 31;
	public static final int RETWEETEDSTATUSID_COL = 32;
	
	public static final String[] PROJECTION = {
		USERID,
		SCREENNAME,
		NAME,
		PROVINCE,
		CITY,
		LOCATION,
		DESCRIPTION,
		URL,
		PROFILEIMAGEURL,
		USERDOMAIN,
		GENDER,
		FOLLOWERSCOUNT,
		FRIENDSCOUNT,
		STATUSESCOUNT,
		FAVOURITESCOUNT,
		CREATEDAT,
		FOLLOWING,
		VERIFIED,
		VERIFIEDTYPE,
		ALLOWALLACTMSG,
		ALLOWALLCOMMENT,
		FOLLOWME,
		AVATARLARGE,
		ONLINESTATUS,
		BIFOLLOWERSCOUNT,
		REMARK,
		LANG,
		VERIFIEDREASON,
		WEIHAO,
		STATUSID,
		COMMENTID,
		REPLYCOMMENTID,
		RETWEETEDSTATUSID
	};
	public static final String CREATE_USER_COLS = " ("
		+UserColumn._ID + " integer primary key autoincrement,"
		+UserColumn.USERID + " text,"
		+UserColumn.SCREENNAME + " text,"
		+UserColumn.NAME + " text,"
		+UserColumn.PROVINCE + " integer,"
		+UserColumn.CITY + " integer,"
		+UserColumn.LOCATION + " text,"
		+UserColumn.DESCRIPTION + " text,"
		+UserColumn.URL + " text,"
		+UserColumn.PROFILEIMAGEURL + " text,"
		+UserColumn.USERDOMAIN + " text,"
		+UserColumn.GENDER + " text,"
		+UserColumn.FOLLOWERSCOUNT + " integer,"
		+UserColumn.FRIENDSCOUNT + " integer,"
		+UserColumn.STATUSESCOUNT + " integer,"
		+UserColumn.FAVOURITESCOUNT + " integer,"
		+UserColumn.CREATEDAT + " text,"
		+UserColumn.FOLLOWING + " integer,"
		+UserColumn.VERIFIED + " integer,"
		+UserColumn.VERIFIEDTYPE + " integer,"
		+UserColumn.ALLOWALLACTMSG + " integer,"
		+UserColumn.ALLOWALLCOMMENT + " integer,"
		+UserColumn.FOLLOWME + " integer,"
		+UserColumn.AVATARLARGE + " text,"
		+UserColumn.ONLINESTATUS + " integer,"
		+UserColumn.BIFOLLOWERSCOUNT + " integer,"
		+UserColumn.REMARK + " text,"
		+UserColumn.LANG + " text,"
		+UserColumn.VERIFIEDREASON + " text,"
		+UserColumn.WEIHAO + " text,"
		+UserColumn.STATUSID + " text,"
		+UserColumn.COMMENTID + " integer,"
		+UserColumn.REPLYCOMMENTID + " integer,"
		+UserColumn.RETWEETEDSTATUSID + " text)";
	
}
