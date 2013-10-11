package com.star.yytv;

public class BackTaskConst {
	/** 打点记录后台线程 **/
	public static final int UPLOAD_USER_ACTIVITY  = 1000;
	public static final int RECORD_USER_ACTIVITY  = 1010;
	/** APP异常记录线程 **/
	public static final int RECORD_EXCEPTION_INFO = 1040;
	public static final int UPLOAD_EXCEPTION_INFO = 1050;
	
	/** 启动应用 **/
	public static final int LOGIN_APP             = 10;
	/** 退出应用 **/
	public static final int LOGOFF_APP            = 20;
	
	/** 节目指南 **/
	public static final int PROGRAM_GUIDES        = 1100;
	/** 节目指南-左右滑屏 **/
	public static final int SLIDE_LEFT_RIGHT      = 1110;
	/** 节目指南-当前节目弹出窗口 **/
	public static final int CURPROGRAM_POP_WIN    = 1120;
	/** 当前节目遥控换台 **/
	public static final int SWITCH_CHANNEL        = 1130;
	/** 节目详情 **/
	public static final int PROGRAM_DETAILS       = 1140;
	/** 节目详情-踩 **/
	public static final int PROGRAM_STAMP         = 1150;
	/** 节目详情-顶 **/
	public static final int PROGRAM_SUPP          = 1160;
	/** 节目详情-关注 **/
	public static final int PROGRAM_ATTENTION     = 1170;
	/** 节目详情-预约 **/
	public static final int PROGRAM_SUBSCRIBE     = 1180;
	/** 节目详情-详情 **/
	public static final int PROGRAM_DETAIL        = 1190;
	/** 节目详情-评论 **/
	public static final int PROGRAM_COMMENT       = 1200;
	/** 节目详情-刷新 **/
	public static final int PROGRAM_REFRESH       = 1210;
	/** 节目详情-查看评论 **/
	public static final int PROGRAM_BROWSECOMMENT = 1220;
	
	/** 摇摇遥控器 **/
	public static final int ROCK_RC               = 1500;
	/** 标题栏打开遥控器 **/
	public static final int CLICK_RC              = 1510;
	
	/** 节目预告 **/
	public static final int PROGRAM_PARADES       = 1300;
	/** 节目预告-左右滑屏 **/
	public static final int PROGRAM_PARADES_SLIDE = 1310;
	/** 节目预告-收藏频道 **/
	public static final int PR_COLLECT_CHANNEL    = 1320;	
	/** 节目预告-预约节目 **/
	public static final int SCHEDULE_PROGRAM      = 1330;	
	/** 弹出窗口-收藏频道 **/
	public static final int COLLECT_CHANNEL       = 1400;
	/** 弹出窗口-取消收藏频道 **/
	public static final int UNCOLLECT_CHANNEL     = 1410;
	/** 弹出窗口-换台 **/
	public static final int POP_SWITCH_CHANNEL    = 1420;
	
	/** 信息 **/
	public static final int INFORMATION           = 1600;
	
	/** 微博 **/
	public static final int WEIBO                 = 1700;
	/** 转发微博 **/
	public static final int REDIRECT_WEIBO        = 1710;
	/** 评论微博 **/
	public static final int COMMENT_WEIBO         = 1720;
	/** 发布微博 **/
	public static final int SHARE_WEIBO           = 1730;
	/** 回复评论 **/
	public static final int RECOMMENT_WEIBO       = 1740;
	
	/** 我的摇摇 **/
	public static final int MY_YAOYAO             = 1800;
	public static final int MY_ATTENTION          = 1810;
	public static final int MY_WEIBO              = 1820;
	public static final int MY_FANS               = 1830;	
}
