package com.star.weibo.async;

import java.util.List;

import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.Sina;
import com.star.weibo.adapter.BufferedCommentAdapter;
import com.star.weibo.listview.ToMeCommentXListViewProxy;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Comment;
import com.star.weibo4j.model.Paging;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.model.OAuthInfoManager;

public class AsyncMoreToMeCommentCallback implements AsyncDataLoader.Callback {

	private Context mContext;
	private XListView commentListView;
	private BufferedCommentAdapter commentAdapter;
	private List<Comment> moreCommentList;
	
	public AsyncMoreToMeCommentCallback(Context context, ToMeCommentXListViewProxy xListViewProxy){
		this.mContext = context;
		this.commentListView = xListViewProxy.getXListView();
		this.commentAdapter = xListViewProxy.getCommentAdapter();
	}
	
	@Override
	public void onStart() {
		try {
			
			if(OAuthInfoManager.getInstance().doBindSinaWeiboUser(mContext))
			{
				int commentSize = commentAdapter.commentSize();
				if (commentSize > 0){
					commentAdapter.setSelectedIndex(commentSize - 1);
					long maxId = commentAdapter.getSelectedComment().getId();
					Log("onLoadMore maxid=" + maxId);
					Paging commentPaging = new Paging();
					commentPaging.setMaxId(maxId - 1);
					moreCommentList = Sina.getInstance().getWeibo().getCommentsToMe(commentPaging, 0, 0);
					if (moreCommentList != null && moreCommentList.size() > 0){
						commentAdapter.addItemsLast(moreCommentList);
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
		
		if(moreCommentList != null)
		{
			Log(moreCommentList, "moreToMeComment");
			commentAdapter.syncCommentBuffer();
			List<Comment> syncCommentList = commentAdapter.getCommentList();
			Log(syncCommentList, "syncCommentList");
			Log("load more:syncCommentList.size = " + syncCommentList.size());
			commentAdapter.notifyDataSetChanged();	
		}
		commentListView.stopLoadMore();
	}
	
	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "AsyncMoreToMeCommentCallback--" + msg);
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
