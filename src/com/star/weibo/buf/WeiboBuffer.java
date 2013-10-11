package com.star.weibo.buf;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import com.star.yytv.Log;
import android.webkit.CacheManager;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.AsyncImageLoader;
import com.star.weibo.adapter.BufferedCommentAdapter;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.async.AsyncClearBufferCallback;
import com.star.weibo.async.AsyncRefreshAtCallback;
import com.star.weibo.listview.AtXListViewProxy;
import com.star.weibo.listview.TimelineXListViewProxy;
import com.star.weibo.listview.ToMeCommentXListViewProxy;
import com.star.weibo.util.LocalMemory;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.Comment;

public class WeiboBuffer {
	//Home Activity Status buffer
	public static final int HOME_STATUSLIST_BUF_MAX = 500;
	
	//Msg Activity Comment buffer
	public static final int MSG_COMMENTLIST_BUF_MAX = 500;
	
	//Msg Activity At buffer
	public static final int MSG_ATSTATUS_BUF_MAX = 500;
	
	//WeiboList Activity weibo buffer
	public static final int WEIBO_STATUSLIST_BUF_MAX = 500;
	
	//CommentList Activity buffer
	public static final int WEIBO_COMMENTLIST_BUF_MAX = 500;
	
	public static LocalMemory localMemory = new LocalMemory();
	
	public static ExecutorService executorService = Executors.newFixedThreadPool(3);
	
	//Home Activity Status BufferedWeiboItemAdapter
	public static BufferedWeiboItemAdapter HOME_TIMELINE_WEIBOITEMADAPTER = null;
	//Msg Activity At BufferedWeiboItemAdapter
	public static BufferedWeiboItemAdapter MSG_ATME_WEIBOITEMADAPTER = null;
	//Msg Activity to me Comment BufferedCommentAdapter
	public static BufferedCommentAdapter MSG_TOMECOMMENT_COMMENTITEMADAPTER = null;
	
	//Home Activity Status BufferedWeiboItemAdapter
	public static TimelineXListViewProxy HOME_TIMELINE_XLISTVIEWPROXY = null;
	//Msg Activity At BufferedWeiboItemAdapter
	public static AtXListViewProxy MSG_ATME_XLISTVIEWPROXY = null;
	//Msg Activity to me Comment BufferedCommentAdapter
	public static ToMeCommentXListViewProxy MSG_TOMECOMMENT_XLISTVIEWPROXY = null;
	
	
	public static void clearUserBuffer(final String userId){
		//clear adapter of xListView
		AsyncClearBufferCallback clearCallback = new AsyncClearBufferCallback();
		new AsyncDataLoader(clearCallback).execute();
		//clear image buffer
		AsyncImageLoader.getInstance().clearUserBuffer();
		//clear image files at memory or sd card
		WeiboBuffer.executorService.submit(new Runnable(){
			@Override
			public void run() {
				WeiboBuffer.localMemory.clearDrawableUser(userId);
			}
		} );
	}
	
	/**
	 * clear buffer before logout
	 * @param userId
	 */
	public static void beforeLogOut(final String userId, final Context mContext){
		clearUserBuffer(userId);
	}
	
	/**
	 * need do something after login
	 * @param userId
	 */
	public static void afterLogin(final String userId){
		if (HOME_TIMELINE_XLISTVIEWPROXY != null){
			HOME_TIMELINE_XLISTVIEWPROXY.setJustLogin(true);
		}
		if (MSG_ATME_XLISTVIEWPROXY != null){
			MSG_ATME_XLISTVIEWPROXY.setJustLogin(true);
		}
		if (MSG_TOMECOMMENT_XLISTVIEWPROXY != null){
			MSG_TOMECOMMENT_XLISTVIEWPROXY.setJustLogin(true);
		}
	}
	
	/**
	 * clear WebView Cache
	 */
	private static void clearWebViewCacheFunc(Context mContext){
		File file = CacheManager.getCacheFileBaseDir();
		WeiboBuffer.localMemory.deleteFile(file);
		mContext.deleteDatabase("webview.db");
		mContext.deleteDatabase("webviewCache.db");
	}
	
	/**
	 * clear WebView Cache
	 * @param mContext
	 */
	public static void clearWebViewCache(final Context mContext){
		WeiboBuffer.executorService.submit(new Runnable(){
			@Override
			public void run() {
				clearWebViewCacheFunc(mContext);
				Log.i("weibo", "clearWebViewCache success");
			}
		} );
	}
		
}
