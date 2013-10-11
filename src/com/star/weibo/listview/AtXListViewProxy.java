package com.star.weibo.listview;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.RefreshViews;
import com.star.weibo.util.LocalMemory;
import com.star.weibo.xlist.XListView;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.async.AsyncDBAtCallback;
import com.star.weibo.async.AsyncMoreAtCallback;
import com.star.weibo.async.AsyncRefreshAtCallback;
import com.star.weibo.db.WeiboDBAdapter;
import com.star.weibo.buf.AtStatusBuffer;
import com.star.weibo.buf.WeiboBuffer;
import com.star.weibo4j.model.Status;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>文件名称: AtXListViewProxy.java </p>
 * <p>文件描述: at me XListView proxy</p>
 * <p>版权所有: 版权所有(C)2012-2016</p>
 * <p>公   司: 上海曜众信息科技有限公司</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>完成日期：2012-11-04</p>
 * <p>修改记录1: </p>
 * <pre>
 *    修改日期：
 *    版 本 号：
 *    修 改 人：
 *    修改内容：
 * </pre>
 * <p></p>
 * @version 1.0
 * @author 
 */

public class AtXListViewProxy extends StatusXListViewProxy {
	
	public AtXListViewProxy(Context context, XListView listView, RefreshViews refreshView){
		super(context, listView, refreshView);
		AtStatusBuffer atStatusBuffer = new AtStatusBuffer(WeiboBuffer.MSG_ATSTATUS_BUF_MAX, new WeiboDBAdapter(context));
		weiboItemAdapter = new BufferedWeiboItemAdapter(context, new ArrayList<Status>(), atStatusBuffer);
		weiboItemAdapter.setWeiboType(LocalMemory.WEIBOTYPE_ATME);
		atListView.setXListViewListener(new AtXListViewListener(context, this));
		atListView.setAdapter(weiboItemAdapter);
		atListView.setOnItemClickListener(new WeiboItemClickListener(context, weiboItemAdapter));
		atListView.setOnItemLongClickListener(new AtWeiboItemLongClickListener(context, weiboItemAdapter));
	}
	
	public void loadFromDB(){
		if (! isLoadedFromDB()){
			AsyncDBAtCallback dbAtCallback = new AsyncDBAtCallback(mContext, this);
			new AsyncDataLoader(dbAtCallback).execute();
		}	
	}
	
	public void loadMore(){
		AsyncMoreAtCallback moreAtCallback = new AsyncMoreAtCallback(mContext, this);
		new AsyncDataLoader(moreAtCallback).execute();
	}
	
	public void loadRefresh(boolean isNeedShowProgress){
		if (! isRefreshing()){
			setIsRefreshing(true);
			AsyncRefreshAtCallback refreshCallback = new AsyncRefreshAtCallback(mContext, this, isNeedShowProgress);
			new AsyncDataLoader(refreshCallback).execute();
		}
		
	}
}
