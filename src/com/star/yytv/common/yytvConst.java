package com.star.yytv.common;

public interface yytvConst {
	int USER_STAE_BOOK = 1;//预约
	int USER_STAE_SIGN = 2;//签到
	int USER_STAE_CONCERN = 3;//关注
	
	int MOVIE_PROGRAM = 1;
	int SERIES_PROGRAM = 2;
	int COLUMN_PROGRAM = 3;
	
	int CCTV_CHANNEL = 1;
	int SATELLITE_CHANNEL = 2;
	int LOCAL_CHANNEL = 3;
	int PAYED_CHANNEL = 4;
	
	boolean isOnline = true;
	
	boolean isVibrate = true;
	
	boolean isShakeTo = true;
	
	boolean isFirstUse = true;
	
	String PROGRAMID = "programid";
	
	String CHANNELID = "channelid";
	
	String PRID = "provinceid";
	
	String YYTVTAG = "yytv";
	
	String KEY_GUIDE_ACTIVITY = "guide_activity";
	
	//持久化到文件中数据的标识
	String SP_PASSWDFILE = "yytvpasswordFile";
	
	String SP_TOKEN = "token";
	
	String SP_WEIBOUSERID = "weiboUserId";
	
	String SP_EXPIRTES_IN = "expires_in";
	
	String SP_YYTVNICKNAME = "yytvNickName";
	
	String SP_YYTVUSERID = "yytvUserId";
	
	String SP_AUTOSWITCH_CHANNEL = "yytvautoswitch";
	
	String SP_SETTING = "yytvSetting";
	
	String WHAT_IS_NEW_PRE_NAME = "what_is_new_pre_name";
}
