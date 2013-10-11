package com.star.weibo;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

import com.star.weibo.adapter.CommentListAdapter;
import com.star.weibo.buf.WeiboBuffer;
import com.star.weibo4j.model.Comment;
import com.star.weibo4j.model.Paging;
import com.star.weibo4j.model.WeiboException;
import com.star.weibo.util.TimeUtil;
import com.star.weibo.xlist.XListView;
import com.star.weibo.xlist.XListView.IXListViewListener;
import com.star.yytv.R;
import com.star.yytv.model.OAuthInfoManager;
import com.star.yytv.ui.lsn.MyDialogWin;
import com.star.yytv.common.YyProgressDiagFact;

public class CommentList extends Activity implements InitViews {
	private Button titleBack;
	private Button titleComment;
	private XListView cList;
	
	private List<Comment> commentList;
	private List<Comment> refreshCommentList;
	private List<Comment> moreCommentList;
	private ProgressDialog dialog;
	
	//评论缓存
	private List<Comment> weiboCommentBuf;
	//是否正在刷新的标志
	private boolean isRefreshing;
	//是否已进行过第一次加载，第一次加载时要显示进度条
	private boolean isFirstLoadFinished = false;
	//当上拉加载更多时，使界面上显示上次的最后一条微博
	private int selectedIndex = -1;
	//上拉加载更多时，是否超过了缓冲区大小，当超过缓冲区大小时，需要重置selectedIndex
	private boolean isOverBuffer = false;
	
	private int clickLocation;
	private CommentListAdapter adapter;

	public long weiboId;
	public static final String WEIBO_ID = "weiboId";
	
	private MyDialogWin mydialog;
	
	//cList的IXListViewListener，处理下拉刷新、上拉加载更多
	private IXListViewListener cListXListViewListener = new IXListViewListener(){
		
		@Override
		/**
		 * xujun 20121022：采用XListView，实现IXListViewListener接口
		 */
		public void onRefresh() {
			refresh();
			Log("onRefresh");
		}

		@Override
		/**
		 * xujun 20121022：采用XListView，实现IXListViewListener接口
		 */
		public void onLoadMore() {
			new AsyncDataLoader(asyncMoreCallback).execute();
			Log("onLoadMore");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.comment_list);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			weiboId = bundle.getLong(WEIBO_ID, 0);
		}
		getViews();
		setViews();
		setListeners();
		//xujun 20121022 第一次加载调整到onCreate中
		refresh();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		weiboCommentBuf = null;
		Log("home destory");
	}
	
	//xujun 20121022  注释掉asyncCallback，用新增的asyncRefreshCallback代替
	/*
	private AsyncDataLoader.Callback asyncCallback = new AsyncDataLoader.Callback() {
		 CommentListAdapter adapter;
		
		@Override
		public void onStart() {
			try {
				commentList=Sina.getInstance().getWeibo().getComments(String.valueOf(weiboId));
				adapter = new CommentListAdapter(CommentList.this, commentList);
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
			cList.setAdapter(adapter);			
		}
	};
	*/
	
	/**
	 * xujun 20121022
	 * 刷新评论的Callback
	 */
	private AsyncDataLoader.Callback asyncRefreshCallback = new AsyncDataLoader.Callback() {

		@Override
		public void onStart() {
			try {
				
				if(OAuthInfoManager.getInstance().doBindSinaWeiboUser(CommentList.this))
				{
					//如果缓存不为空，则加载id大于第一条评论id的评论，即加载比第一条评论新的评论
					if (weiboCommentBuf != null && weiboCommentBuf.size() > 0){
						long sinceId = weiboCommentBuf.get(0).getId();
						Paging paging = new Paging();
						paging.setSinceId(sinceId);
						refreshCommentList = Sina.getInstance().getWeibo().getComments(String.valueOf(weiboId), paging, 0);
						Log("refresh buffer since");
					}else{
						//如果缓存为空，则加载最新的评论
						refreshCommentList = Sina.getInstance().getWeibo().getComments(String.valueOf(weiboId));
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
			//如果是第一次加载，则显示进度条
			if (!isFirstLoadFinished){
				dialog.show();
			}
		}

		@Override
		public void onFinish() {
			
			if(refreshCommentList != null)
			{
				Log(refreshCommentList, "refreshCommentList");
				if (weiboCommentBuf != null && weiboCommentBuf.size() > 0){
					Log(weiboCommentBuf, "weiboCommentBuf");
					refreshCommentList.addAll(weiboCommentBuf);
					Log("refreshCommentList.addAll(weiboCommentBuf");
					
				}
				if (refreshCommentList.size() > WeiboBuffer.WEIBO_COMMENTLIST_BUF_MAX){
					refreshCommentList = refreshCommentList.subList(0, WeiboBuffer.WEIBO_COMMENTLIST_BUF_MAX);
					Log("refreshCommentList.size() > WeiboBuffer.WEIBO_COMMENTLIST_BUF_MAX");
				}
				commentList = refreshCommentList;
				
				weiboCommentBuf = refreshCommentList;
				//WeiboBuffer.weiboCommentRefreshTime = System.currentTimeMillis();
				cList.setRefreshTime(TimeUtil.getTimeStr(System.currentTimeMillis()));
			}else
			{
				if (weiboCommentBuf != null && weiboCommentBuf.size() > 0){
					commentList = weiboCommentBuf;
					//cList.setRefreshTime(TimeUtil.getTimeStr(WeiboBuffer.weiboCommentRefreshTime));
					Log("refreshCommentList = null & weiboCommentBuf != null");
					Log(weiboCommentBuf, "weiboCommentBuf");
				}
			}
			//dialog.dismiss();
			if (commentList != null){
				adapter = new CommentListAdapter(CommentList.this,
						commentList);
				cList.setAdapter(adapter);
				Log("setAdapter");
			}
			cList.stopRefresh();
			refreshCommentList = null;
			isRefreshing = false;
			if (dialog.isShowing()){
				dialog.dismiss();
			}
		}
	};
	
	/**
	 * xujun 20121022：采用XListView，增加 上拉加载更多 的处理
	 */
	private AsyncDataLoader.Callback asyncMoreCallback = new AsyncDataLoader.Callback() {

		@Override
		public void onStart() {
			try {
				
				if(OAuthInfoManager.getInstance().doBindSinaWeiboUser(CommentList.this))
				{
					if (commentList != null && commentList.size() > 0){
						selectedIndex = commentList.size()-1;
						long maxId = commentList.get(commentList.size()-1).getId();
						Log("onLoadMore maxid=" + maxId);
						Paging paging = new Paging();
						paging.setMaxId(maxId - 1);
						moreCommentList = Sina.getInstance().getWeibo().getComments(String.valueOf(weiboId), paging, 0);
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
				Log(moreCommentList, "moreCommentList");
				
				commentList.addAll(moreCommentList);
				//超过缓冲区大小时，删除多余部分
				if (commentList.size() > WeiboBuffer.WEIBO_COMMENTLIST_BUF_MAX){
					//删除前面多余缓冲区的微博
					int statusNum = commentList.size();
					for (int i=0; i < statusNum - WeiboBuffer.WEIBO_COMMENTLIST_BUF_MAX; i++){
						commentList.remove(0);
					}
					selectedIndex = selectedIndex - (statusNum - WeiboBuffer.WEIBO_COMMENTLIST_BUF_MAX);
					isOverBuffer = true;
				}
				Log(commentList, "commentList");
				weiboCommentBuf = commentList;
				Log("load more:commentList.size = " + commentList.size());
				adapter.notifyDataSetChanged();	
				if (isOverBuffer){
					if (selectedIndex >= 0){
						cList.setSelection(selectedIndex + 1);
						selectedIndex = -1;
					}
					isOverBuffer = false;
				}
			}
			cList.stopLoadMore();
			//dialog.dismiss();
		}
	};

	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.commentList_title_back:
				back();
				break;
			case R.id.commentList_title_comment:
				commentWeibo(weiboId);
				break;
			default:
				break;
			}
		}
	};

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg2 > 0 && arg2 <= commentList.size()){
				clickLocation = arg2-1;
				
				mydialog = new MyDialogWin(CommentList.this);
				mydialog.show();
				mydialog.setMsgPopShow();
				if(commentList.get(clickLocation).getUser().getId().equals(OAuthInfoManager.getInstance().getWeiboUserId())){
					mydialog.setPopItem3Show();
					mydialog.setPopItem3Text("删除评论");
				}				
				mydialog.setPopText("评论功能");
				mydialog.setPopItem1Text("回复评论");
				mydialog.setPopItem2Text("查看个人资料");				
				mydialog.setPopItem1(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						replyComment(commentList.get(clickLocation).getId(), weiboId);
						mydialog.cancel();
					}
				});
				mydialog.setPopItem2(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
///						viewProfile(Long.parseLong(commentList.get(clickLocation).getUser().getId()));
						mydialog.cancel();
						
					}
				});
				mydialog.setPopItem3(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						delComment(commentList.get(clickLocation).getId());
						mydialog.cancel();
					}
				});
			}
		}
	};
	
	/**
	 * 返回
	 */
	private void back() {
		finish();
	}
	/**
	 * 转到评论界面
	 * @param id
	 */
	private void commentWeibo(long id) {
		Sina.getInstance().commentWeibo(this, id);
	}
	/**
	 * edited by yunlong, 2012-11-26
	 * reply comment
	 * @param cid:comment id
	 * @param id:weibo id
	 */
	private void replyComment(long cid, long id) {
		
		Sina.getInstance().commentCom(this, cid, id);
//		Log("replyComment");		
	}
	/**
	 * added by yunlong, 2012-11-26
	 * delete comment (only yourself)
	 * @param comment id
	 */
	private void delComment(long cid){
		try {
			Sina.getInstance().getWeibo().destroyComment(String.valueOf(cid));
			WeiboToast.show(getApplicationContext(), "删除成功");
		} catch (WeiboException e) {
			e.printStackTrace();
			WeiboToast.show(getApplicationContext(), "删除失败");
		}
	}
	/**
	 * 查看个人资料
	 */
	private void viewProfile(long uId) {
		Sina.getInstance().goToUserInfo(this, uId);
	}

	@Override
	public void getViews() {
		dialog = YyProgressDiagFact.getYyProgressDiag(CommentList.this);
		titleBack = (Button) findViewById(R.id.commentList_title_back);
		titleComment = (Button) findViewById(R.id.commentList_title_comment);
		cList = (XListView) findViewById(R.id.commentList_list);
		cList.setPullLoadEnable(true);
	}

	@Override
	public void setListeners() {
		titleBack.setOnClickListener(clickListener);
		titleComment.setOnClickListener(clickListener);
		cList.setOnItemClickListener(itemClickListener);
		cList.setXListViewListener(cListXListViewListener);
	}

	@Override
	public void setViews() {
		//xujun 20121022 第一次加载调整到onCreate中，故屏蔽下述调用
		// new AsyncDataLoader(asyncCallback).execute();
	}
	
	/**
	 * xujun 20121022 
	 * 刷新comment
	 */
	private void refresh(){
		if (! isRefreshing){
			new AsyncDataLoader(asyncRefreshCallback).execute();
		}
	}

	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "CommentList--" + msg);
	}
	
	void Log(List<Comment> slist, String slistName){
		Log(slistName + " size: " + slist.size());
		for (int i=0; i < slist.size(); i++){
			Log(slistName + " " + i + " : " + slist.get(i).getId());
		}
	}

}
