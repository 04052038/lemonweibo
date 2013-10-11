package com.star.weibo.async;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;

import com.star.weibo.listview.AtXListViewProxy;
import com.star.weibo.AsyncDataLoader;
import com.star.weibo.Sina;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.adapter.WeiboItemAdapter;
import com.star.weibo.buf.WeiboBuffer;
import com.star.weibo.util.LocalMemory;
import com.star.weibo.util.TimeUtil;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Comment;
import com.star.weibo4j.model.Paging;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.common.YyProgressDiagFact;
import com.star.yytv.model.OAuthInfoManager;

public class AsyncRefreshAtCallback implements AsyncDataLoader.Callback  {
	
	private Context mContext;
	private XListView atListView;
	private BufferedWeiboItemAdapter weiboItemAdapter;
	private AtXListViewProxy atXListViewProxy;
	private List<Status> refreshStatusList;
	private ProgressDialog progressDialog = null;
	private boolean isNeedShowProgressDialog;
	
	public AsyncRefreshAtCallback(Context context, AtXListViewProxy xListViewProxy, boolean isNeedShowProgress){
		this.mContext = context;
		this.atListView = xListViewProxy.getXListView();
		this.weiboItemAdapter = xListViewProxy.getWeiboItemAdapter();
		this.atXListViewProxy = xListViewProxy;
		if (isNeedShowProgress){
			this.progressDialog = YyProgressDiagFact.getYyProgressDiag(context);
		}
		this.isNeedShowProgressDialog = isNeedShowProgress;
	}
	
	@Override
	public void onStart() {
		try {
			
			if(OAuthInfoManager.getInstance().doBindSinaWeiboUser(mContext))
			{
				int statusSize = weiboItemAdapter.statusSize();
				if (statusSize > 0){
					weiboItemAdapter.setSelectedIndex(0);
					String sinceId = weiboItemAdapter.getSelectedStatus().getId();
					Paging atPaging = new Paging();
					atPaging.setSinceId(Long.valueOf(sinceId));
					refreshStatusList = Sina.getInstance().getWeibo().getMentions(atPaging, 0, 0, 0);
					Log("refresh buffer at since");
				}else{
					refreshStatusList = Sina.getInstance().getWeibo().getMentions();
					Log("refresh buffer null at default");
				}
				if (refreshStatusList != null && refreshStatusList.size() > 0){
					if (refreshStatusList.size() >= Sina.AT_WEIBO_COUNT){
						weiboItemAdapter.clearDB();
						WeiboBuffer.executorService.submit(new Runnable(){
							@Override
							public void run() {
								WeiboBuffer.localMemory.clearDrawable(LocalMemory.WEIBOTYPE_ATME);
							}
						} );
					}
					weiboItemAdapter.addItemsBefore(refreshStatusList);
					weiboItemAdapter.setRefreshTime(System.currentTimeMillis());
				}
			}else{
				atXListViewProxy.setJustLogin(true);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
			Log(e.toString());
		}
	
	}

	@Override
	public void onPrepare() {
		atXListViewProxy.setIsRefreshing(true);
		if (isNeedShowProgressDialog){
			progressDialog.show();
		}
	}

	@Override
	public void onFinish() {
		StringBuffer info = new StringBuffer();
		boolean tipFlag = false;
		if(refreshStatusList != null  && refreshStatusList.size() > 0)
		{
			LogStatusList(refreshStatusList, "refreshAtStatus");
			weiboItemAdapter.syncStatusBuffer();
			atListView.setRefreshTime(TimeUtil.getTimeStr(weiboItemAdapter.getRefreshTime()));
			weiboItemAdapter.notifyDataSetChanged();
			info.append(refreshStatusList.size()).append("条@我微博更新");
			tipFlag = true;
		} else {
			if (! atXListViewProxy.isJustLogin()){
				info.append("没有@我的微博");
				tipFlag = true;
			}
		}
		atListView.stopRefresh();
		refreshStatusList = null;
		atXListViewProxy.setIsRefreshing(false);
		if (isNeedShowProgressDialog && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		if (tipFlag){
			tipFlag = false;
			atXListViewProxy.getRefreshView().showPopupWindow(info.toString());
		}
	}
	
	
	
	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "AsyncRefreshAtCallback--" + msg);
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
