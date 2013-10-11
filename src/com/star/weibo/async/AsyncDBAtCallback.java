package com.star.weibo.async;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.Sina;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.listview.AtXListViewProxy;
import com.star.weibo.util.TimeUtil;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Comment;
import com.star.weibo4j.model.Paging;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.common.YyProgressDiagFact;
import com.star.yytv.model.OAuthInfoManager;

public class AsyncDBAtCallback implements AsyncDataLoader.Callback {
	
	private Context mContext;
	private XListView atListView;
	private BufferedWeiboItemAdapter weiboItemAdapter;
	private AtXListViewProxy atXListViewProxy;
	private ProgressDialog progressDialog = null;
	
	public AsyncDBAtCallback(Context context, AtXListViewProxy xListViewProxy){
		this.mContext = context;
		this.atXListViewProxy = xListViewProxy;
		this.atListView = xListViewProxy.getXListView();
		this.weiboItemAdapter = xListViewProxy.getWeiboItemAdapter();
		this.progressDialog = YyProgressDiagFact.getYyProgressDiag(context);
	}
	
	@Override
	public void onStart() {
		try {
			weiboItemAdapter.loadStatusDB();
			atXListViewProxy.setLoadedFromDB(true);
			int statusSize = weiboItemAdapter.statusBufferSize();
			if (statusSize == 0){
				if (OAuthInfoManager.getInstance().tokenIsReady()){
					List<Status> refreshStatusList = Sina.getInstance().getWeibo().getMentions();
					Log("AsyncDBAtCallback: db is null, so refresh");
					if (refreshStatusList != null && refreshStatusList.size() > 0){
						weiboItemAdapter.addItemsBefore(refreshStatusList);
						weiboItemAdapter.setRefreshTime(System.currentTimeMillis());
					}
				}else{
//					OAuthInfoManager.getInstance().doBindSinaWeiboUser(mContext);
					atXListViewProxy.setJustLogin(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log(e.toString());
		}
	}

	@Override
	public void onPrepare() {		
		if(OAuthInfoManager.getInstance().isLogin(mContext)){
			atXListViewProxy.setIsRefreshing(true);
			progressDialog.show();
		}
	}

	@Override
	public void onFinish() {
		
		if(atXListViewProxy.isJustLogin()){
			atXListViewProxy.getRefreshView().refreshView(null);
		}else{
			weiboItemAdapter.syncStatusBuffer();
			atListView.setRefreshTime(TimeUtil.getTimeStr(weiboItemAdapter.getRefreshTime()));
			weiboItemAdapter.notifyDataSetChanged();
			LogStatusList(weiboItemAdapter.getStatusList(), "DBAtStatus");
		}
		atXListViewProxy.setIsRefreshing(false);
		if (progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}
	
	
	
	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "AsyncDBAtCallback--" + msg);
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
