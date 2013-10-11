package com.star.weibo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.star.weibo4j.Weibo;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.User;
import com.star.yytv.SplashActivity;
import com.star.yytv.common.yytvConst;
import com.star.yytv.model.OAuthInfoManager;

public class Sina {	
	private static Sina sina;
	private static Weibo weibo;
	
	//weibo count, default value for statuses/friends_timeline
	public static final int WEIBO_COUNT = 2;
	//weibo count, default value for statuses/mentions
	public static final int AT_WEIBO_COUNT = 20;
	//comment count, default value for comments/to_me
	public static final int COMMENT_COUNT = 50;
	//user weibo count, default value for statuses/user_timeline
	public static final int USER_WEIBO_COUNT = 20;
	
	public synchronized static Sina getInstance() {
		if (sina == null) {
			sina = new Sina();
		}
		return sina;
	}
	
	public Weibo getWeibo(){
//		if(weibo==null){
//			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
//			System.setProperty("weibo4j.oauth.consumerSecret",
//					Weibo.CONSUMER_SECRET);
//			weibo=new Weibo();
//			weibo.setOAuthAccessToken(OAuthConstant.getInstance().getToken(),
//					OAuthConstant.getInstance().getTokenSecret());
//			
//			//for test
//		}
//		return weibo;
		if(weibo==null)
		{
			weibo = OAuthInfoManager.getInstance().getWeibo();
			//weibo.setOAuthAccessToken(OAuthConstant.getInstance().getAccessToken());
			
		}
		//weibo.setToken(OAuthConstant.getInstance().getToken(), OAuthConstant.getInstance().getTokenSecret());
//		/weibo.setOAuthAccessToken(OAuthConstant.getInstance().getAccessToken());

		return weibo;
		
	}
	
	/**
	 * 分享给好友界面
	 * @param context
	 */
	public void gotoShare(Context context) {
		Intent i = new Intent(context, WeiboUpdater.class);
		Bundle bundle = new Bundle();
		bundle.putInt(WeiboUpdater.WEIBO_CATE, WeiboUpdater.SHARE);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	
	/**
	 * 进入发布微博界面
	 * @param context
	 */
	public void updateWeibo(Context context) {
		Intent i = new Intent(context, WeiboUpdater.class);
		Bundle bundle = new Bundle();
		bundle.putInt(WeiboUpdater.WEIBO_CATE, WeiboUpdater.UPDATE);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	
	/**
	 * 进入转发微博界面
	 * @param context
	 * @param id 要转发的微博id
	 * xujun 20121118: add param status
	 */
	public void redirectWeibo(Context context, long id, Status status) {
		Intent i = new Intent(context, WeiboUpdater.class);
		Bundle bundle = new Bundle();
		bundle.putInt(WeiboUpdater.WEIBO_CATE, WeiboUpdater.REDIRECT);
		bundle.putLong(WeiboUpdater.WEIBO_ID, id);
		bundle.putSerializable(WeiboUpdater.WEIBO_REDIRECT, status);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	
	/**
	 * 进入@他界面
	 * @param context
	 */
	public void weiboAtHim(Context context,User atUser) {
		Intent i = new Intent(context, WeiboUpdater.class);
		Bundle bundle = new Bundle();
		bundle.putInt(WeiboUpdater.WEIBO_CATE, WeiboUpdater.UPDATE);
		bundle.putSerializable(WeiboUpdater.WEIBO_ATHIM, atUser);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	
	/**
	 * 进入评论列表界面
	 * @param context context
	 * @param id
	 */
	public void goToCommentList(Context context,long id){
		Intent i =new Intent(context,CommentList.class);
		Bundle bundle=new Bundle();
		bundle.putLong(CommentList.WEIBO_ID, id);
		i.putExtras(bundle);
		context.startActivity(i);		
	}
	/**
	 * 进入评论微博界面
	 * @param context
	 * @param id
	 */
	public void commentWeibo(Context context, long id) {
		Intent i = new Intent(context, WeiboUpdater.class);
		Bundle bundle = new Bundle();
		bundle.putInt(WeiboUpdater.WEIBO_CATE, WeiboUpdater.COMMENT);
		bundle.putLong(WeiboUpdater.WEIBO_ID, id);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * added by yunlong, 2012-11-26
	 * go to comment-comment view
	 * @param context
	 * @param cid
	 * @param id
	 */
	public void commentCom(Context context, long cid, long id){
		Intent i = new Intent(context,WeiboUpdater.class);
		Bundle bundle = new Bundle();
		bundle.putInt(WeiboUpdater.WEIBO_CATE, WeiboUpdater.COMMENT2);
		bundle.putLong(WeiboUpdater.WEIBO_ID, id);
		bundle.putLong(WeiboUpdater.COMMENT_ID, cid);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 返回首页/Home
	 * @param context
	 */
	public void backToHome(Context context) {
		Intent i=new Intent(context, Home.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);
	}
	/**
	 * 进入微博详细信息界面
	 * @param context
	 * @param status 对应微博的Status
	 */
	public void goToDetail(Context context, Status status) {
		Intent i = new Intent(context, WeiboDetail.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(WeiboDetail.STATUS, status);		
		i.putExtras(bundle);
		context.startActivity(i);
	}
	
	/**
	 * enter weibo detail GUI, return result
	 * @param context
	 * @param status 
	 * xujun 20121119: add param requestCode
	 */
	public void goToDetailForResult(Context context, Status status, int requestCode) {
		Intent i = new Intent(context, WeiboDetail.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(WeiboDetail.STATUS, status);		
		i.putExtras(bundle);
		((Activity)context).startActivityForResult(i, requestCode);
	}
	
	/**
	 * 进入用户资料界面
	 * @param context
	 * @param userId 对应用户id
	 */
	public void goToUserInfo(Context context, long userId) {
		Intent i = new Intent(context, UserInfo.class);
		Bundle bundle = new Bundle();
		bundle.putLong(UserInfo.USER_ID, userId);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * xujun 20120908
	 * 进入用户资料界面
	 * @param context
	 * @param userScreenname 对应用户的screenname
	 */
	public void goToUserInfoByScreenname(Context context, String userScreenname) {
		Intent i = new Intent(context, UserInfo.class);
		Bundle bundle = new Bundle();
		bundle.putString(UserInfo.USER_SCREENNAME, userScreenname);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 进入用户资料编辑界面（仅支持编辑自己的资料）
	 * @param context
	 * @param user
	 */
	public void goToInfoEditor(Context context,User user){
		Intent i = new Intent(context, UserInfoEditor.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(UserInfoEditor.USER, user);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 显示关注界面
	 * @param context
	 * @param userId 对应的用户id
	 */
	public void showAttention(Context context, long userId) {
		Intent i=new Intent(context,UserList.class);
		Bundle bundle=new Bundle();
		bundle.putInt(UserList.USER_CATE, UserList.CATE_ATTENTION);
		bundle.putLong(UserList.USER_ID, userId);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	
	public void bindAccount(Context context) {
		
		
		Intent intent = new Intent(context, 
				SplashActivity.class); 
    
		context.startActivity(intent);
	}
	
	
	/**
	 * 显示发表的微博界面
	 * @param context
	 * @param userId 对应的用户id
	 */
	public void showWeibo(Context context,long userId){
		Intent i=new Intent(context,WeiboList.class);
		Bundle bundle=new Bundle();
		bundle.putInt(WeiboList.WEIBO_CATE, WeiboList.CATE_ALL);
		bundle.putLong(WeiboList.USER_ID, userId);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 显示粉丝界面
	 * @param context
	 * @param userId 对应的用户id
	 */
	public void showFans(Context context, long userId) {
		Intent i=new Intent(context,UserList.class);
		Bundle bundle=new Bundle();
		bundle.putInt(UserList.USER_CATE, UserList.CATE_FANS);
		bundle.putLong(UserList.USER_ID, userId);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 显示话题界面（）
	 */
	public void ShowTopic(Context context, long userId) {
		Intent i=new Intent(context,TrendList.class);
		Bundle bundle=new Bundle();
		bundle.putLong(TrendList.USER_ID, userId);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 显示收藏界面（仅支持显示自己的收藏）
	 * @param context
	 */
	public void showFavorite(Context context) {
		Intent i=new Intent(context,WeiboList.class);
		Bundle bundle=new Bundle();
		bundle.putInt(WeiboList.WEIBO_CATE, WeiboList.CATE_FAVORITE);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	/**
	 * 显示黑名单界面（暂不支持）（SDK有误，尚未修改）
	 * @param context
	 * @param userId
	 */
	public void showBlacklist(Context context,long userId) {
		Intent i=new Intent(context,UserList.class);
		Bundle bundle=new Bundle();
		bundle.putInt(UserList.USER_CATE, UserList.CATE_BLACKLIST);
		bundle.putLong(UserList.USER_ID, userId);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	
	/**
	 * xujun 20120908
	 * 显示微博图片放大界面
	 * @param context
	 * @param statusId
	 * @param url
	 * @param picType 图片类型，取值为 thumb、mid、ori
	 */
	public void showImageViewZoom(Context context,long statusId, String url, String picType) {
		Intent i=new Intent(context,ImageViewZoomActivity.class);
		Bundle bundle=new Bundle();
		bundle.putLong(ImageViewZoomActivity.STATUSID, statusId);
		bundle.putString(ImageViewZoomActivity.URL, url);
		bundle.putString(ImageViewZoomActivity.PICTYPE, picType);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	
	/**
	 * xujun 20120912  增加微博网页浏览功能
	 * @param context
	 * @param url 网页地址
	 */
	public void showWebView(Context context, String url){
		Intent i=new Intent(context,WebViewActivity.class);
		Bundle bundle=new Bundle();
		bundle.putString(WebViewActivity.WEBURL, url);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	
	/**
	 * xujun 20130106
	 * @param context
	 * @param user
	 */
	public void showUserInfoDetail(Context context, User user){
		Intent i=new Intent(context,UserInfoDetail.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable(UserInfoDetail.USER, user);
		i.putExtras(bundle);
		context.startActivity(i);
	}
	
	/**
	 * 显示微博转发列表
	 * @param context
	 * @param srcStatus
	 */
	public void showRepostWeibo(Context context, Status srcStatus){
		Intent i=new Intent(context,WeiboRepost.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable(WeiboRepost.SRCSTATUS, srcStatus);
		i.putExtras(bundle);
		context.startActivity(i);
	}

}
