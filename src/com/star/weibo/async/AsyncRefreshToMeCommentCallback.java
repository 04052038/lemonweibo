package com.star.weibo.async;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.Sina;
import com.star.weibo.adapter.BufferedCommentAdapter;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.buf.WeiboBuffer;
import com.star.weibo.listview.TimelineXListViewProxy;
import com.star.weibo.listview.ToMeCommentXListViewProxy;
import com.star.weibo.util.LocalMemory;
import com.star.weibo.util.TimeUtil;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Comment;
import com.star.weibo4j.model.Paging;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.common.YyProgressDiagFact;
import com.star.yytv.model.OAuthInfoManager;

public class AsyncRefreshToMeCommentCallback implements AsyncDataLoader.Callback {

	private Context mContext;
	private XListView commentListView;
	private BufferedCommentAdapter commentAdapter;
	private ToMeCommentXListViewProxy toMeCommentXListViewProxy;
	private List<Comment> refreshCommentList;
	private ProgressDialog progressDialog = null;
	private boolean isNeedShowProgressDialog;
	
	public AsyncRefreshToMeCommentCallback(Context context, ToMeCommentXListViewProxy xListViewProxy, boolean isNeedShowProgress){
		this.mContext = context;
		this.commentListView = xListViewProxy.getXListView();
		this.commentAdapter = xListViewProxy.getCommentAdapter();
		this.toMeCommentXListViewProxy = xListViewProxy;
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
				int commentSize = commentAdapter.commentSize();
				if (commentSize > 0){
					long sinceId = ((Comment)commentAdapter.getItem(0)).getId();
					Paging commentPaging = new Paging();
					commentPaging.setSinceId(sinceId);
					refreshCommentList = Sina.getInstance().getWeibo().getCommentsToMe(commentPaging, 0, 0);
					Log("refresh comment buffer since");
				}else{
					//如果缓存为空，则加载最新的微博
					refreshCommentList = Sina.getInstance().getWeibo().getCommentsToMe();
					Log("refresh comment buffer null default");
				}
				if (refreshCommentList != null && refreshCommentList.size() > 0){
					if (refreshCommentList.size() >= Sina.COMMENT_COUNT){
						commentAdapter.clearDB();
						WeiboBuffer.executorService.submit(new Runnable(){
							@Override
							public void run() {
								WeiboBuffer.localMemory.clearDrawable(LocalMemory.WEIBOTYPE_COMMENT);
							}
						} );
					}
					commentAdapter.addItemsBefore(refreshCommentList);
					commentAdapter.setRefreshTime(System.currentTimeMillis());
				}
			}else{
				toMeCommentXListViewProxy.setJustLogin(true);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
			Log(e.toString());
		}

	}
	
	@Override
	public void onPrepare() {
		// TODO Auto-generated method stub
		toMeCommentXListViewProxy.setIsRefreshing(true);
		if (isNeedShowProgressDialog){
			progressDialog.show();
		}
	}
	
	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		StringBuffer info = new StringBuffer();
		boolean tipFlag = false;
		if(refreshCommentList != null  && refreshCommentList.size() > 0)
		{
			Log(refreshCommentList, "refreshCommentList");
			commentAdapter.syncCommentBuffer();
			commentListView.setRefreshTime(TimeUtil.getTimeStr(commentAdapter.getRefreshTime()));
			commentAdapter.notifyDataSetChanged();
			info.append(refreshCommentList.size()).append("条评论更新");
			tipFlag = true;
		} else {
			if (! toMeCommentXListViewProxy.isJustLogin()){
				info.append("没有更新的评论");
				tipFlag = true;
			}
		}
		commentListView.stopRefresh();
		refreshCommentList = null;
		toMeCommentXListViewProxy.setIsRefreshing(false);
		if (isNeedShowProgressDialog && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		if (tipFlag){
			tipFlag = false;
			toMeCommentXListViewProxy.getRefreshView().showPopupWindow(info.toString());
		}

	}
	
	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "AsyncRefreshToMeCommentCallback--" + msg);
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
