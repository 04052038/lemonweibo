package com.star.weibo.async;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.Sina;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.listview.AtXListViewProxy;
import com.star.weibo.listview.UserStatusXListViewProxy;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Comment;
import com.star.weibo4j.model.Paging;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.User;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.model.OAuthInfoManager;

public class AsyncMoreUserStatusCallback implements AsyncDataLoader.Callback {

	private Context mContext;
	private XListView userStatusListView;
	private BufferedWeiboItemAdapter weiboItemAdapter;
	private List<Status> moreStatusList;
	private User user;
	
	public AsyncMoreUserStatusCallback(Context context, UserStatusXListViewProxy xListViewProxy){
		this.mContext = context;
		this.userStatusListView = xListViewProxy.getXListView();
		this.weiboItemAdapter = xListViewProxy.getWeiboItemAdapter();
		this.user = xListViewProxy.getUser();
	}
	
	@Override
	public void onStart() {
		try {
			
			if(OAuthInfoManager.getInstance().doBindSinaWeiboUser(mContext))
			{
				int statusSize = weiboItemAdapter.statusSize();
				if (statusSize > 0){
					weiboItemAdapter.setSelectedIndex(statusSize - 1);
					String maxId = weiboItemAdapter.getSelectedStatus().getId();
					Log("onLoadMore maxid=" + maxId);
					Paging atPaging = new Paging();
					atPaging.setMaxId(Long.valueOf(maxId) - 1);
					moreStatusList = Sina.getInstance().getWeibo().getUserTimeline(user.getId(), atPaging, 0, 0);
					if (moreStatusList != null && moreStatusList.size() > 0){
						weiboItemAdapter.addItemsLast(moreStatusList);
					}
				}
	
			}
		} catch (WeiboException e) {
			e.printStackTrace();
			Log(e.toString());
		}
	}

	@Override
	public void onPrepare() {

		//dialog.show();
	}

	@Override
	public void onFinish() {
		
		if(moreStatusList != null)
		{
			LogStatusList(moreStatusList, "moreAtStatus");
			weiboItemAdapter.syncStatusBuffer();
			List<Status> syncStatusList = weiboItemAdapter.getStatusList();
			LogStatusList(syncStatusList, "syncStatusList");
			Log("load more:syncStatusList.size = " + syncStatusList.size());
			weiboItemAdapter.notifyDataSetChanged();	
		}
		userStatusListView.stopLoadMore();
	}
	
	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "AsyncMoreAtCallback--" + msg);
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
