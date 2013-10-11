package com.star.weibo.async;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.Sina;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.listview.AtXListViewProxy;
import com.star.weibo.listview.UserStatusXListViewProxy;
import com.star.weibo.util.TimeUtil;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Comment;
import com.star.weibo4j.model.Paging;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.User;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.common.YyProgressDiagFact;
import com.star.yytv.model.OAuthInfoManager;

public class AsyncRefreshUserStatusCallback implements AsyncDataLoader.Callback {
	
	private Context mContext;
	private XListView userStatusListView;
	private BufferedWeiboItemAdapter weiboItemAdapter;
	private UserStatusXListViewProxy userStatusXListViewProxy;
	private List<Status> refreshStatusList;
	private ProgressDialog progressDialog = null;
	private boolean isNeedShowProgressDialog;
	private User user;
	
	public AsyncRefreshUserStatusCallback(Context context, UserStatusXListViewProxy xListViewProxy, boolean isNeedShowProgress, User user){
		this.mContext = context;
		this.userStatusListView = xListViewProxy.getXListView();
		this.weiboItemAdapter = xListViewProxy.getWeiboItemAdapter();
		this.userStatusXListViewProxy = xListViewProxy;
		if (isNeedShowProgress){
			this.progressDialog = YyProgressDiagFact.getYyProgressDiag(context);
		}
		this.isNeedShowProgressDialog = isNeedShowProgress;
		this.user = user;
	}
	
	@Override
	public void onPrepare() {
		// TODO Auto-generated method stub
		if (isNeedShowProgressDialog){
			progressDialog.show();
		}

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		userStatusXListViewProxy.setIsRefreshingUserStatus(true);
		try {
			
			if(OAuthInfoManager.getInstance().doBindSinaWeiboUser(mContext))
			{
				if (user != null){
					int statusSize = weiboItemAdapter.statusSize();
					if (statusSize > 0){
						weiboItemAdapter.setSelectedIndex(0);
						String sinceId = weiboItemAdapter.getSelectedStatus().getId();
						Paging atPaging = new Paging();
						atPaging.setSinceId(Long.valueOf(sinceId));
						refreshStatusList = Sina.getInstance().getWeibo().getUserTimeline(user.getId(), atPaging, 0, 0);
						Log("refresh buffer at since");
					}else{
						refreshStatusList = Sina.getInstance().getWeibo().getUserTimeline(user.getId());
						Log("refresh buffer null at default");
					}
					if (refreshStatusList != null && refreshStatusList.size() > 0){
						if (refreshStatusList.size() >= Sina.USER_WEIBO_COUNT){
							weiboItemAdapter.clearDB();
						}
						weiboItemAdapter.addItemsBefore(refreshStatusList);
						weiboItemAdapter.setRefreshTime(System.currentTimeMillis());
					}
				}
				
			}
		} catch (WeiboException e) {
			e.printStackTrace();
			Log(e.toString());
		}

	}
	
	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		if(refreshStatusList != null  && refreshStatusList.size() > 0)
		{
			LogStatusList(refreshStatusList, "refreshAtStatus");
			weiboItemAdapter.syncStatusBuffer();
			userStatusListView.setRefreshTime(TimeUtil.getTimeStr(weiboItemAdapter.getRefreshTime()));
			weiboItemAdapter.notifyDataSetChanged();
		}
		userStatusListView.stopRefresh();
		refreshStatusList = null;
		userStatusXListViewProxy.setIsRefreshingUserStatus(false);
		if (isNeedShowProgressDialog && progressDialog.isShowing()){
			progressDialog.dismiss();
		}

	}
	
	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "AsyncRefreshUserStatusCallback--" + msg);
	}
	
	void Log(List<Comment> slist, String slistName){
		Log(slistName + " size: " + slist.size());
		for (int i=0; i < slist.size(); i++){
			Log(slistName + " " + i + " : " + slist.get(i).getId());
		}
	}
	
	void LogStatusList(List<Status> slist, String slistName){
		Log(slistName + " size: " + slist.size());
		for (int i=0; i < slist.size(); i++){
			Log(slistName + " " + i + " : " + slist.get(i).getId());
		}
	}

}
