package com.star.weibo;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.star.yytv.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.star.weibo.adapter.WeiboItemAdapter;
import com.star.weibo.buf.WeiboBuffer;
import com.star.weibo.util.TimeUtil;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.WeiboException;
import com.star.weibo4j.model.Paging;
import com.star.weibo.xlist.XListView;
import com.star.weibo.xlist.XListView.IXListViewListener;
import com.star.yytv.R;
import com.star.yytv.model.OAuthInfoManager;
import com.star.yytv.common.YyProgressDiagFact;
/**
 * 微博列表
 * @author starry
 *
 */
public class WeiboList extends Activity {
	public static final int REQUEST_CODE_WBDETAIL = 1;
	
	private Button titleBack;
	private TextView titleName;
	private Button titleHome;

	private long userId;
	private XListView weiboList;
	private List<Status> statusList;
	private List<Status> refreshStatusList;
	private List<Status> moreStatusList;
	private int cate;
	private ProgressDialog dialog;
	private WeiboItemAdapter adapter;
	
	
	//微博缓存
	private List<Status> weiboStatusBuf;
	
	//是否正在刷新的标志
	private boolean isRefreshing;
	//是否已进行过第一次加载，第一次加载时要显示进度条
	private boolean isFirstLoadFinished = false;
	//当上拉加载更多时，使界面上显示上次的最后一条微博
	private int selectedIndex = -1;
	//上拉加载更多时，是否超过了缓冲区大小，当超过缓冲区大小时，需要重置selectedIndex
	private boolean isOverBuffer = false;
	//index of status that is showing in WeiboDetail activity, used for weibo delete
	private int curStatusIndex = 0;
	
	public static final String USER_ID="user_id";
	
	public static final String WEIBO_CATE="weibo_cate";
	public static final int CATE_ALL=0; //显示所有微博
	public static final int CATE_FAVORITE=1; //显示收藏微博
	
	//weiboList的IXListViewListener，处理下拉刷新、上拉加载更多
	private IXListViewListener weiboListXListViewListener = new IXListViewListener(){
		
		@Override
		/**
		 * xujun 20121022：采用XListView，实现IXListViewListener接口
		 */
		public void onRefresh() {
			if(cate==CATE_ALL){
				refreshWeibo();
			}else if(cate==CATE_FAVORITE){
				//待补充
			}
			Log("onRefreshWeibo");
		}

		@Override
		/**
		 * xujun 20121022：采用XListView，实现IXListViewListener接口
		 */
		public void onLoadMore() {
			if(cate==CATE_ALL){
				new AsyncDataLoader(asyncMoreWeiboCallback).execute();
			}else if(cate==CATE_FAVORITE){
				//待补充
			}
			Log("onLoadMoreWeibo");
		}
	};
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weibolist);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			cate=bundle.getInt(WEIBO_CATE,CATE_ALL);
			userId=bundle.getLong(USER_ID,0);
		}

		getViews();
		setViews();
		setLisneter();
		if(cate==CATE_ALL){
			refreshWeibo();
		}else if(cate==CATE_FAVORITE){
			new AsyncDataLoader(asyncCallback).execute();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		weiboStatusBuf = null;
		Log("home destory");
	}
	
	private AsyncDataLoader.Callback asyncCallback=new AsyncDataLoader.Callback() {
		
		@Override
		public void onStart() {
			try {
				if(cate==CATE_ALL){
					//log("weibo all");
					//status=Sina.getInstance().getWeibo().getUserTimeline(String.valueOf(userId));
				}else if(cate==CATE_FAVORITE){
					Log("weibo fav");
					statusList=Sina.getInstance().getWeibo().getFavorites(1);
				}
			} catch (WeiboException e) {
				e.printStackTrace();
				Log(e.toString());
			}
		}
		
		@Override
		public void onPrepare() {
			
		}
		
		@Override
		public void onFinish() {
			adapter=new WeiboItemAdapter(WeiboList.this, statusList);
			weiboList.setAdapter(adapter);
		}
	};
	
	/**
	 * 异步加载服务器上的微博数据的监听
	 */
	private AsyncDataLoader.Callback asyncRefreshWeiboCallback = new AsyncDataLoader.Callback() {

		@Override
		public void onStart() {
			try {
				
				if(OAuthInfoManager.getInstance().doBindSinaWeiboUser(WeiboList.this))
				{
					if (weiboStatusBuf != null && weiboStatusBuf.size() > 0){
						String sinceId = weiboStatusBuf.get(0).getId();
						Paging paging = new Paging();
						paging.setSinceId(Long.valueOf(sinceId));
						refreshStatusList = Sina.getInstance().getWeibo().getUserTimeline(String.valueOf(userId), paging, 0, 0);
						Log("refresh buffer since");
					}else{
						refreshStatusList = Sina.getInstance().getWeibo().getUserTimeline(String.valueOf(userId));
						Log("refresh buffer null default");
					}
					isFirstLoadFinished = true;
				}
			} catch (WeiboException e) {
				e.printStackTrace();
				Log(e.toString());
			}
		
		}

		@Override
		public void onPrepare() {
			isRefreshing = true;
			if (!isFirstLoadFinished){
				dialog.show();
			}
		}

		@Override
		public void onFinish() {
			
			if(refreshStatusList != null)
			{
				Log(refreshStatusList, "refreshStatusList");
				if (weiboStatusBuf != null && weiboStatusBuf.size() > 0){
					Log(weiboStatusBuf, "weiboStatusBuf");
					refreshStatusList.addAll(weiboStatusBuf);
					Log("refreshStatusList.addAll(weiboStatusBuf)");
					
				}
				if (refreshStatusList.size() > WeiboBuffer.WEIBO_STATUSLIST_BUF_MAX){
					refreshStatusList = refreshStatusList.subList(0, WeiboBuffer.WEIBO_STATUSLIST_BUF_MAX);
					Log("refreshStatusList.size() > WeiboBuffer.WEIBO_STATUSLIST_BUF_MAX");
				}
				statusList = refreshStatusList;
				
				weiboStatusBuf = refreshStatusList;
				//WeiboBuffer.weiboRefreshTime = System.currentTimeMillis();
				weiboList.setRefreshTime(TimeUtil.getTimeStr(System.currentTimeMillis()));
			}else
			{
				if (weiboStatusBuf != null && weiboStatusBuf.size() > 0){
					statusList = weiboStatusBuf;
					//weiboList.setRefreshTime(TimeUtil.getTimeStr(WeiboBuffer.weiboRefreshTime));
					Log("refreshStatusList = null & weiboStatusBuf != null");
					Log(weiboStatusBuf, "weiboStatusBuf");
				}
			}
			//dialog.dismiss();
			if (statusList != null){
				adapter = new WeiboItemAdapter(WeiboList.this,
						statusList);
				weiboList.setAdapter(adapter);
				Log("setAdapter");
				Log(statusList, "statusList");
			}
			weiboList.stopRefresh();
			refreshStatusList = null;
			isRefreshing = false;
			if (dialog.isShowing()){
				dialog.dismiss();
			}
		}
	};
	
	/**
	 * xujun 20121011：采用XListView，增加 上拉加载更多 的处理
	 */
	private AsyncDataLoader.Callback asyncMoreWeiboCallback = new AsyncDataLoader.Callback() {

		@Override
		public void onStart() {
			try {
				
				if(OAuthInfoManager.getInstance().doBindSinaWeiboUser(WeiboList.this))
				{
					if (statusList != null && statusList.size() > 0){
						selectedIndex = statusList.size()-1;
						String maxId = statusList.get(statusList.size()-1).getId();
						Log("onLoadMore maxid=" + maxId);
						Paging paging = new Paging();
						paging.setMaxId(Long.valueOf(maxId) - 1);
						moreStatusList = Sina.getInstance().getWeibo().getUserTimeline(String.valueOf(userId), paging, 0, 0);
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
			
			if(moreStatusList != null)
			{
				Log(moreStatusList, "moreStatusList");
				
				statusList.addAll(moreStatusList);
				//超过缓冲区大小时，删除多余部分
				if (statusList.size() > WeiboBuffer.WEIBO_STATUSLIST_BUF_MAX){
					//删除前面多余缓冲区的微博
					int statusNum = statusList.size();
					for (int i=0; i < statusNum - WeiboBuffer.WEIBO_STATUSLIST_BUF_MAX; i++){
						statusList.remove(0);
					}
					selectedIndex = selectedIndex - (statusNum - WeiboBuffer.WEIBO_STATUSLIST_BUF_MAX);
					isOverBuffer = true;
				}
				Log(statusList, "statusList");
				weiboStatusBuf = statusList;
				Log("load more:statusList.size = " + statusList.size());
				adapter.notifyDataSetChanged();	
				if (isOverBuffer){
					if (selectedIndex >= 0){
						weiboList.setSelection(selectedIndex + 1);
						selectedIndex = -1;
					}
					isOverBuffer = false;
				}
			}
			weiboList.stopLoadMore();
			//dialog.dismiss();
		}
	};

	private void getViews() {
		dialog = YyProgressDiagFact.getYyProgressDiag(WeiboList.this);
		RelativeLayout titlebar = (RelativeLayout) findViewById(R.id.weibolist_titlebar);
		titleBack = (Button) titlebar.findViewById(R.id.titlebar_back);
		titleName = (TextView) titlebar.findViewById(R.id.titlebar_name);
		titleHome = (Button) titlebar.findViewById(R.id.titlebar_home);

		weiboList = (XListView) findViewById(R.id.weibolist_weiboList);
		weiboList.setPullLoadEnable(true);
	}

	private void setViews() {
		titleBack.setVisibility(View.VISIBLE);
		titleName.setVisibility(View.VISIBLE);
		titleHome.setVisibility(View.GONE);
		if(cate==CATE_ALL){
			titleName.setText(R.string.weibo);
		}else if(cate==CATE_FAVORITE){
			titleName.setText(R.string.favorite);
		}
	}

	private void setLisneter() {
		titleBack.setOnClickListener(clickListener);
		titleHome.setOnClickListener(clickListener);
		weiboList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log("weiboList.setOnItemClickListener called");
				if (arg2 > 0 && arg2 <= statusList.size()){
					Sina.getInstance().goToDetailForResult(WeiboList.this, statusList.get(arg2 - 1), REQUEST_CODE_WBDETAIL);
					curStatusIndex = arg2-1;
					Log("status is selected: " + curStatusIndex);
				}
			}
		});
		weiboList.setXListViewListener(weiboListXListViewListener);
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.titlebar_back:
				back();
				break;
			case R.id.titlebar_home:
				backToHome();
				break;
			default:
				break;
			}
		}
	};	

	private void back() {
		finish();
	}

	private void backToHome() {
		Sina.getInstance().backToHome(this);
	}
	
	/**
	 * 刷新
	 */
	private void refreshWeibo() {
		if (!isRefreshing){
			new AsyncDataLoader(asyncRefreshWeiboCallback).execute();
		}
		Log("refresh weibo");
	}
	
	@Override
	/**
	 * delete weibo
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_WBDETAIL){
			if (resultCode == RESULT_OK){
				if (data != null){
					boolean delFlag = data.getBooleanExtra(WeiboDetail.DELFLAG, false);
					if (delFlag){
						statusList.remove(curStatusIndex);
						adapter.notifyDataSetChanged();
						Log("status is deleted: " + curStatusIndex);
					}
				}
			}
		}
	}

	void Log(String msg) {
		Log.i("weibo", "WeiboList--" + msg);
	}
	
	void Log(List<Status> slist, String slistName){
		Log(slistName + " size: " + slist.size());
		for (int i=0; i < slist.size(); i++){
			Log(slistName + " " + i + " : " + slist.get(i).getId());
		}
	}

}
