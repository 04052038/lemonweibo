package com.star.weibo.async;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.RefreshViews;
import com.star.weibo.Sina;
import com.star.weibo.UserInfo;
import com.star.weibo.WeiboToast;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.listview.UserStatusXListViewProxy;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Paging;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.User;
import com.star.weibo4j.model.UserFriendship;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.common.YyProgressDiagFact;
import com.star.yytv.model.OAuthInfoManager;

public class AsyncRefreshUserInfoCallback implements AsyncDataLoader.Callback {

	private Context mContext;
	private UserStatusXListViewProxy userStatusXListViewProxy;
	private ProgressDialog progressDialog = null;
	private boolean isNeedShowProgressDialog;
	private RefreshViews refreshView;
	private User user;
	
	public AsyncRefreshUserInfoCallback(Context context, UserStatusXListViewProxy xListViewProxy, boolean isNeedShowProgress){
		this.mContext = context;
		this.userStatusXListViewProxy = xListViewProxy;
		if (isNeedShowProgress){
			this.progressDialog = YyProgressDiagFact.getYyProgressDiag(context);
		}
		this.isNeedShowProgressDialog = isNeedShowProgress;
		this.refreshView = xListViewProxy.getRefreshView();
	}
	
	@Override
	public void onPrepare() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		userStatusXListViewProxy.setIsRefreshingUserInfo(true);
		try {
			
			if(OAuthInfoManager.getInstance().doBindSinaWeiboUser(mContext))
			{
				String username = userStatusXListViewProxy.getUsername();
				String userid = userStatusXListViewProxy.getUserid();
				if (username != null){
					user = Sina.getInstance().getWeibo().showUserByName(username);
					Log("usernam: " + username);
				}else if (userid != null){
					user = Sina.getInstance().getWeibo().showUser(userid);
					Log("userid: " + userid);
				}else{
					Log("problem: not set user");
				}
				if (user != null){
					UserFriendship friendShip = Sina.getInstance().getWeibo().getUserFriendship("" + OAuthInfoManager.getInstance().getWeiboUserId(), user.getId());
					if (friendShip != null && friendShip.getSource() != null){
						user.setFollowHim(friendShip.getSource().isFollowing());
					}	
				}
			}
		} catch (WeiboException e) {
			e.printStackTrace();
			Log(e.toString());
//			WeiboToast.show(mContext, "用户不存在!");
//			UserInfo.getInstance().finish();
//			refreshView.refreshView(null);
//			userStatusXListViewProxy.setUser(null);
		}
	}
	
	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		userStatusXListViewProxy.setIsRefreshingUserInfo(false);
		if (user != null){
			refreshView.refreshView(user);
			userStatusXListViewProxy.setUser(user);
			AsyncRefreshUserStatusCallback refreshCallback = new AsyncRefreshUserStatusCallback(mContext, userStatusXListViewProxy, isNeedShowProgressDialog, user);
			new AsyncDataLoader(refreshCallback).execute();
		}
		

	}
	
	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "AsyncRefreshUserInfoCallback--" + msg);
	}

}
