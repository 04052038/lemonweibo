package com.star.weibo.listview;

import java.util.ArrayList;

import android.content.Context;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.RefreshViews;
import com.star.weibo.adapter.BufferedCommentAdapter;
import com.star.weibo.async.AsyncDBTimelineCallback;
import com.star.weibo.async.AsyncDBToMeCommentCallback;
import com.star.weibo.async.AsyncMoreTimelineCallback;
import com.star.weibo.async.AsyncMoreToMeCommentCallback;
import com.star.weibo.async.AsyncRefreshTimelineCallback;
import com.star.weibo.async.AsyncRefreshToMeCommentCallback;
import com.star.weibo.buf.ToMeCommentBuffer;
import com.star.weibo.buf.WeiboBuffer;
import com.star.weibo.db.CommentDBAdapter;
import com.star.weibo.util.LocalMemory;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Comment;

public class ToMeCommentXListViewProxy extends CommentXListViewProxy {
	
	public ToMeCommentXListViewProxy(Context context, XListView listView, RefreshViews refreshView){
		super(context, listView, refreshView);
		ToMeCommentBuffer toMeCommentBuffer = new ToMeCommentBuffer(WeiboBuffer.MSG_COMMENTLIST_BUF_MAX, new CommentDBAdapter(context));
		commentAdapter = new BufferedCommentAdapter(context, new ArrayList<Comment>(), toMeCommentBuffer);
		commentAdapter.setWeiboType(LocalMemory.WEIBOTYPE_COMMENT);
		commentListView.setXListViewListener(new ToMeCommentXListViewListener(context, this));
		commentListView.setAdapter(commentAdapter);
		commentListView.setOnItemClickListener(new CommentItemClickListener(context, commentAdapter));
	}

	@Override
	public void loadFromDB() {
		// TODO Auto-generated method stub
		if (! isLoadedFromDB()){
			AsyncDBToMeCommentCallback dbCommentCallback = new AsyncDBToMeCommentCallback(mContext, this);
			new AsyncDataLoader(dbCommentCallback).execute();
		}	

	}

	@Override
	public void loadMore() {
		// TODO Auto-generated method stub
		AsyncMoreToMeCommentCallback moreCommentCallback = new AsyncMoreToMeCommentCallback(mContext, this);
		new AsyncDataLoader(moreCommentCallback).execute();
	}

	@Override
	public void loadRefresh(boolean isNeedShowProgress) {
		// TODO Auto-generated method stub
		if (! isRefreshing()){
			setIsRefreshing(true);
			AsyncRefreshToMeCommentCallback refreshCallback = new AsyncRefreshToMeCommentCallback(mContext, this, isNeedShowProgress);
			new AsyncDataLoader(refreshCallback).execute();
		}
	}

}
