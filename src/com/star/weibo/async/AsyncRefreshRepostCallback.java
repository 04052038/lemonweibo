package com.star.weibo.async;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.Sina;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.buf.WeiboBuffer;
import com.star.weibo.listview.RepostStatusXListViewProxy;
import com.star.weibo.listview.TimelineXListViewProxy;
import com.star.weibo.util.LocalMemory;
import com.star.weibo.util.TimeUtil;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Comment;
import com.star.weibo4j.model.Paging;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.common.YyProgressDiagFact;
import com.star.yytv.model.OAuthInfoManager;

public class AsyncRefreshRepostCallback implements AsyncDataLoader.Callback {

	private Context mContext;
	private XListView atListView;
	private BufferedWeiboItemAdapter weiboItemAdapter;
	private RepostStatusXListViewProxy repostXListViewProxy;
	private List<Status> refreshStatusList;
	private ProgressDialog progressDialog = null;
	private boolean isNeedShowProgressDialog;
	
	public AsyncRefreshRepostCallback(Context context, RepostStatusXListViewProxy xListViewProxy, boolean isNeedShowProgress){
		this.mContext = context;
		this.atListView = xListViewProxy.getXListView();
		this.weiboItemAdapter = xListViewProxy.getWeiboItemAdapter();
		this.repostXListViewProxy = xListViewProxy;
		if (isNeedShowProgress){
			this.progressDialog = YyProgressDiagFact.getYyProgressDiag(context);
		}
		this.isNeedShowProgressDialog = isNeedShowProgress;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		try {
			
			if(OAuthInfoManager.getInstance().doBindSinaWeiboUser(mContext))
			{
				//如果缓存不为空，则加载id大于第一条微博id的评论，即加载比第一条微博新的微博
				int statusSize = weiboItemAdapter.statusSize();
				if (statusSize > 0){
					String sinceId = ((Status)weiboItemAdapter.getItem(0)).getId();
					Paging paging = new Paging();
					paging.setSinceId(Long.valueOf(sinceId));
					refreshStatusList = Sina.getInstance().getWeibo().getRepostTimeline(repostXListViewProxy.getSrcStatusId(), paging);
					Log("refresh buffer since");
				}else{
					//如果缓存为空，则加载最新的微博
					refreshStatusList = Sina.getInstance().getWeibo().getRepostTimeline(repostXListViewProxy.getSrcStatusId());
					Log("refresh buffer null default");
				}
				if (refreshStatusList != null && refreshStatusList.size() > 0){
					if (refreshStatusList.size() >= Sina.WEIBO_COUNT){
						weiboItemAdapter.clearDB();
					}
					weiboItemAdapter.addItemsBefore(refreshStatusList);
					weiboItemAdapter.setRefreshTime(System.currentTimeMillis());
				}
			}else{
				repostXListViewProxy.setJustLogin(true);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
			Log(e.toString());
		}

	}
	
	@Override
	public void onPrepare() {
		// TODO Auto-generated method stub
		repostXListViewProxy.setIsRefreshing(true);
		if (isNeedShowProgressDialog){
			progressDialog.show();
		}
	}
	
	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		StringBuffer info = new StringBuffer();
		boolean tipFlag = false;
		if(refreshStatusList != null  && refreshStatusList.size() > 0)
		{
			LogStatusList(refreshStatusList, "refreshAtStatus");
			weiboItemAdapter.syncStatusBuffer();
			atListView.setRefreshTime(TimeUtil.getTimeStr(weiboItemAdapter.getRefreshTime()));
			weiboItemAdapter.notifyDataSetChanged();
			info.append(refreshStatusList.size()).append("条微博更新");
			tipFlag = true;
		} else {
			if (! repostXListViewProxy.isJustLogin()){
				info.append("没有更新的微博");
				tipFlag = true;
			}
		}
		atListView.stopRefresh();
		refreshStatusList = null;
		repostXListViewProxy.setIsRefreshing(false);
		if (isNeedShowProgressDialog && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		if (tipFlag){
			tipFlag = false;
			repostXListViewProxy.getRefreshView().showPopupWindow(info.toString());
		}
	}
	
	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "AsyncRefreshTimelineCallback--" + msg);
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
