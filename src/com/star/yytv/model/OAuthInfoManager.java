package com.star.yytv.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.star.weibo.WeiboToast;
import com.star.weibo4j.Weibo;
import com.star.weibo4j.http.AccessToken;
import com.star.yytv.R;
import com.star.yytv.SplashActivity;
import com.star.yytv.common.yytvConst;

public class OAuthInfoManager {
	public static final String BASE_URL = "https://github.com/04052038";
	public static final String CONSUMER_KEY = "2609369401";
	public static final String CONSUMER_SECRET = "77c59c45340133fa1e710b820916e10b";
	
//	public static final String BASE_URL = "http://www.yaoyaotv.cn";
//	public static final String CONSUMER_KEY = "2919078884";
//	public static final String CONSUMER_SECRET = "52823dcea72668ea36aef78efbe89f63";

	private static OAuthInfoManager instance = null;
	
	private static Weibo weibo = null;
	
	private AccessToken accessToken = null;
	private String weiboUserId = null;
	private String token = null;
	private String tokenSecret = null;
	
	private OAuthInfoManager() {
	};

	public static synchronized OAuthInfoManager getInstance() {
		if (instance == null)
			instance = new OAuthInfoManager();
		return instance;
	}
	
	public String getWeiboUserId() {
		return weiboUserId;
	}

	public void setWeiboUserId(String weiboUserId) {
		this.weiboUserId = weiboUserId;
	}

	

	public Weibo getWeibo() {
		if (weibo == null)
			weibo = new Weibo();
		weibo.setToken(token);
		return weibo;
	}

	

	public boolean doBindSinaWeiboUser(Context context) {
		if (token == null || token.length() <= 0) {
			Intent intent = new Intent(context, SplashActivity.class);			
			context.startActivity(intent);
			return false;
		}

		else {

			return true;
		}

	}
	/**
	 * xujun 20121018
	 * token是否准备好
	 * @return
	 */
	public boolean tokenIsReady(){
		if (token == null || token.length() <= 0) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * 判断是否已登录微博
	 * @param mContext
	 * @return
	 */
	public boolean isLogin(Context mContext){
		if(token == null){
			SharedPreferences sp = mContext.getSharedPreferences(yytvConst.SP_PASSWDFILE, Context.MODE_PRIVATE);
			this.token = sp.getString(yytvConst.SP_TOKEN,"");
		}
		if (tokenIsReady()){
			return true;
		}else{
			WeiboToast.show(mContext, mContext.getString(R.string.login_tip));
			return false;
		}	
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

}
