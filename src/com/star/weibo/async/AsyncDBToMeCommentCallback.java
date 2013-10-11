package com.star.weibo.async;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.Sina;
import com.star.weibo.adapter.BufferedCommentAdapter;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.listview.TimelineXListViewProxy;
import com.star.weibo.listview.ToMeCommentXListViewProxy;
import com.star.weibo.util.TimeUtil;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Comment;
import com.star.weibo4j.model.Status;
import com.star.yytv.common.YyProgressDiagFact;
import com.star.yytv.model.OAuthInfoManager;

public class AsyncDBToMeCommentCallback implements AsyncDataLoader.Callback {

	private Context mContext;
	private XListView commentListView;
	private BufferedCommentAdapter commentAdapter;
	private ToMeCommentXListViewProxy toMeCommentXListViewProxy;
	private ProgressDialog progressDialog = null;
	
	public AsyncDBToMeCommentCallback(Context context, ToMeCommentXListViewProxy xListViewProxy){
		this.mContext = context;
		this.toMeCommentXListViewProxy = xListViewProxy;
		this.commentListView = xListViewProxy.getXListView();
		this.commentAdapter = xListViewProxy.getCommentAdapter();
		this.progressDialog = YyProgressDiagFact.getYyProgressDiag(context);
	}
	
	@Override
	public void onStart() {
		try {
			commentAdapter.loadStatusDB();
			toMeCommentXListViewProxy.setLoadedFromDB(true);
			int commentSize = commentAdapter.commentBufferSize();
			if (commentSize == 0){
				if (OAuthInfoManager.getInstance().tokenIsReady()){
					List<Comment> refreshCommentList = Sina.getInstance().getWeibo().getCommentsToMe();
					Log("AsyncDBToMeCommentCallback: db is null, so refresh");
					if (refreshCommentList != null && refreshCommentList.size() > 0){
						commentAdapter.addItemsBefore(refreshCommentList);
						commentAdapter.setRefreshTime(System.currentTimeMillis());
					}
				}else{
					toMeCommentXListViewProxy.setJustLogin(true);
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
			toMeCommentXListViewProxy.setIsRefreshing(true);
			progressDialog.show();
		}
	}

	@Override
	public void onFinish() {
		if (toMeCommentXListViewProxy.isJustLogin()){
			toMeCommentXListViewProxy.getRefreshView().refreshView(null);
		}else{
			commentAdapter.syncCommentBuffer();
			commentListView.setRefreshTime(TimeUtil.getTimeStr(commentAdapter.getRefreshTime()));
			commentAdapter.notifyDataSetChanged();
			Log(commentAdapter.getCommentList(), "DBComment");
		}
		toMeCommentXListViewProxy.setIsRefreshing(false);
		if (progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}
	
	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "AsyncDBToMeCommentCallback--" + msg);
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
