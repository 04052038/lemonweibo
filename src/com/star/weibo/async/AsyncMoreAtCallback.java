package com.star.weibo.async;

import android.content.Context;

import java.util.List;
import java.util.ArrayList;

import com.star.weibo4j.model.Comment;
import com.star.weibo4j.model.Paging;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.WeiboException;
import com.star.weibo.AsyncDataLoader;
import com.star.weibo.Sina;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.buf.WeiboBuffer;
import com.star.weibo.listview.AtXListViewProxy;
import com.star.weibo.xlist.XListView;
import com.star.yytv.model.OAuthInfoManager;

public class AsyncMoreAtCallback implements AsyncDataLoader.Callback {
	private Context mContext;
	private XListView atListView;
	private BufferedWeiboItemAdapter weiboItemAdapter;
	private List<Status> moreStatusList;
	
	public AsyncMoreAtCallback(Context context, AtXListViewProxy xListViewProxy){
		this.mContext = context;
		this.atListView = xListViewProxy.getXListView();
		this.weiboItemAdapter = xListViewProxy.getWeiboItemAdapter();
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
					moreStatusList = Sina.getInstance().getWeibo().getMentions(atPaging, 0, 0, 0);
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
		atListView.stopLoadMore();
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
