package com.star.weibo4j.model;

import com.star.weibo4j.org.json.JSONException;
import com.star.weibo4j.org.json.JSONObject;

public class UserFriendshipInfo {
	private String id;
	private String screenName;
	private boolean followed_by;
	private boolean following;
	private boolean notifications_enabled;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public boolean isFollowed_by() {
		return followed_by;
	}
	public void setFollowed_by(boolean followedBy) {
		followed_by = followedBy;
	}
	public boolean isFollowing() {
		return following;
	}
	public void setFollowing(boolean following) {
		this.following = following;
	}
	public boolean isNotifications_enabled() {
		return notifications_enabled;
	}
	public void setNotifications_enabled(boolean notificationsEnabled) {
		notifications_enabled = notificationsEnabled;
	}
	
	public UserFriendshipInfo(JSONObject json) throws WeiboException {
		super();
		init(json);
	}

	private void init(JSONObject json) throws WeiboException {
		if(json!=null){
			try {
				id = json.getString("id");
				screenName = json.getString("screen_name");
				followed_by = getBoolean("followed_by", json);
				following = getBoolean("following", json);
				notifications_enabled = getBoolean("notifications_enabled", json);
			} catch (JSONException jsone) {
				jsone.printStackTrace();
				throw new WeiboException(jsone.getMessage() + ":" + json.toString(), jsone);
			}
		}
	}
	
	private boolean getBoolean(String key, JSONObject json) throws JSONException {
        String str = json.getString(key);
        if(null == str || "".equals(str)||"null".equals(str)){
            return false;
        }
        return Boolean.valueOf(str);
    }
	
}
