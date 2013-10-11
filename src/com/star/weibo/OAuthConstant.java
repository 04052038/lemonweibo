package com.star.weibo;

import com.star.weibo4j.Weibo;
import com.star.weibo4j.http.AccessToken;

public class OAuthConstant {
	private static Weibo weibo=null;
	private static OAuthConstant instance=null;
	
	private AccessToken accessToken;
	private String token;
	private String tokenSecret;
	private long userId;
	private OAuthConstant(){};
	public static synchronized OAuthConstant getInstance(){
		if(instance==null)
			instance= new OAuthConstant();
		return instance;
	}
	public Weibo getWeibo(){
		if(weibo==null)
			weibo= new Weibo();
		return weibo;
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
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
}
