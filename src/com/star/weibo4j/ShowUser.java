package com.star.weibo4j;

import com.star.weibo4j.model.User;
import com.star.weibo4j.model.WeiboException;

public class ShowUser {
	/** * @param args */
	public static void main(String[] args) {
		String access_token = args[0];
		Weibo weibo = new Weibo();
		weibo.setToken(access_token);
		String uid = args[1];
		Users um = new Users();
		try {
			User user = um.showUserById(uid);
			//Log.logInfo(user.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}