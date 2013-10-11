package com.star.weibo4j.model;

import com.star.yytv.Log;

import com.star.weibo4j.org.json.JSONException;
import com.star.weibo4j.org.json.JSONObject;

public class UserFriendship {
	private UserFriendshipInfo target;
	private UserFriendshipInfo source;
	
	public UserFriendshipInfo getTarget() {
		return target;
	}
	public void setTarget(UserFriendshipInfo target) {
		this.target = target;
	}
	public UserFriendshipInfo getSource() {
		return source;
	}
	public void setSource(UserFriendshipInfo source) {
		this.source = source;
	}
	
	public UserFriendship(JSONObject json) throws WeiboException {
		super();
		init(json);
	}

	private void init(JSONObject json) throws WeiboException {
		if(json!=null){
			try {
				if (!json.isNull("target")) {
					target = new UserFriendshipInfo(json.getJSONObject("target"));
				}
				if (!json.isNull("source")) {
					source = new UserFriendshipInfo(json.getJSONObject("source"));
				}
				Log.i("weibo", "UserFriendship-- " + json.toString());
			} catch (JSONException jsone) {
				jsone.printStackTrace();
				throw new WeiboException(jsone.getMessage() + ":" + json.toString(), jsone);
			}
		}
	}

}
