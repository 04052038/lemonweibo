package com.star.weibo.async;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.buf.WeiboBuffer;

public class AsyncClearBufferCallback implements AsyncDataLoader.Callback {

	@Override
	public void onPrepare() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		if (WeiboBuffer.HOME_TIMELINE_WEIBOITEMADAPTER != null){
			WeiboBuffer.HOME_TIMELINE_WEIBOITEMADAPTER.clearDB();
		}
		if (WeiboBuffer.MSG_ATME_WEIBOITEMADAPTER != null){
			WeiboBuffer.MSG_ATME_WEIBOITEMADAPTER.clearDB();
		}
		if (WeiboBuffer.MSG_TOMECOMMENT_COMMENTITEMADAPTER != null){
			WeiboBuffer.MSG_TOMECOMMENT_COMMENTITEMADAPTER.clearDB();
		}
	}
	
	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		if (WeiboBuffer.HOME_TIMELINE_WEIBOITEMADAPTER != null){
			WeiboBuffer.HOME_TIMELINE_WEIBOITEMADAPTER.syncStatusBuffer();
			WeiboBuffer.HOME_TIMELINE_WEIBOITEMADAPTER.notifyDataSetChanged();
		}
		if (WeiboBuffer.MSG_ATME_WEIBOITEMADAPTER != null){
			WeiboBuffer.MSG_ATME_WEIBOITEMADAPTER.syncStatusBuffer();
			WeiboBuffer.MSG_ATME_WEIBOITEMADAPTER.notifyDataSetChanged();
		}
		if (WeiboBuffer.MSG_TOMECOMMENT_COMMENTITEMADAPTER != null){
			WeiboBuffer.MSG_TOMECOMMENT_COMMENTITEMADAPTER.syncCommentBuffer();
			WeiboBuffer.MSG_TOMECOMMENT_COMMENTITEMADAPTER.notifyDataSetChanged();
		}
	}

}
