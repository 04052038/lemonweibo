package com.star.weibo;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.LinkMovementMethod;
import com.star.yytv.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.star.weibo.AsyncDataLoader.Callback;
import com.star.weibo.util.TextUtil;
import com.star.weibo.util.TimeUtil;
import com.star.weibo4j.model.Count;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.User;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.R;
import com.star.yytv.model.OAuthInfoManager;
import com.star.yytv.ui.lsn.MyDialogWin;

/**
 * 微博详细信息
 * 
 * @author starry
 * 
 */
public class WeiboDetail extends Activity implements InitViews{
	private Button titleBack;
	private TextView titleName;
	private Button titleHome;
	private LinearLayout userinfo;
	private ImageView icon;
	private ImageView v;
	private TextView name;
	private TextView content;
	private ImageView pic;
	private LinearLayout sub;
	private TextView subContent;
	private ImageView subPic;
	private LinearLayout subRedirect;
	private TextView subRedirectNum;
	private LinearLayout subComment;
	private TextView subCommentNum;
	private Button redirect_bt;
	private Button comment_bt;
	private TextView time;
	private TextView source;
	private Button refresh;
	private Button comment;
	private Button redirect;
	private Button favorite;
	private Button weibodel;
	//private TextView more;
	private String userId;
	private MyDialogWin mydialog;

	private Status mStatus;
	private boolean isExitSubStatus;

	public static final String STATUS = "status";
	public static final String DELFLAG = "delFlag";
	//xujun 20121117: handler for weibo delete
	private Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 1:
					handler.post(weiboDelRunnable);
				break;
				default:
					break;
			}	
		}
	};
	
	private Runnable weiboDelRunnable = new Runnable() {

		@Override
		public void run() {
			delWeiboDialog();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weibo_detail);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mStatus = (Status) bundle.getSerializable(STATUS);
			if (mStatus != null && mStatus.getRetweetedStatus() != null) {
				isExitSubStatus = true;
			}
		}

		getViews();
		setViews();
		setListeners();
	}
	@Override
	public void getViews() {
		RelativeLayout titlebar = (RelativeLayout) findViewById(R.id.detail_titlebar);
		titleBack = (Button) titlebar.findViewById(R.id.titlebar_back);
		titleName = (TextView) titlebar.findViewById(R.id.titlebar_name);
		titleHome = (Button) titlebar.findViewById(R.id.titlebar_home);
		userinfo = (LinearLayout) findViewById(R.id.detail_userinfo);
		icon = (ImageView) findViewById(R.id.detail_icon);
		v = (ImageView) findViewById(R.id.detail_v);
		name = (TextView) findViewById(R.id.detail_name);
		content = (TextView) findViewById(R.id.detail_content);
		pic = (ImageView) findViewById(R.id.detail_pic);
		sub = (LinearLayout) findViewById(R.id.detail_sub);
		subContent = (TextView) findViewById(R.id.detail_subContent);
		subPic = (ImageView) findViewById(R.id.detail_subPic);
		subRedirect = (LinearLayout) findViewById(R.id.detail_subRedirect);
		subRedirectNum = (TextView) findViewById(R.id.detail_sub_redirectNum);
		subComment = (LinearLayout) findViewById(R.id.detail_subComent);
		subCommentNum = (TextView) findViewById(R.id.detail_sub_commentNum);
		redirect_bt = (Button) findViewById(R.id.detail_redirect_bt);
		comment_bt = (Button) findViewById(R.id.detail_comment_bt);
		time = (TextView) findViewById(R.id.detail_time);
		source = (TextView) findViewById(R.id.detail_from);
		refresh = (Button) findViewById(R.id.detail_refresh);
		comment = (Button) findViewById(R.id.detail_comment);
		redirect = (Button) findViewById(R.id.detail_redirect);
		favorite = (Button) findViewById(R.id.detail_favorite);
		weibodel = (Button) findViewById(R.id.detail_del);
	}
	@Override
	public void setListeners() {
		titleBack.setOnClickListener(clickListener);
		titleHome.setOnClickListener(clickListener);
		userinfo.setOnClickListener(clickListener);
		pic.setOnClickListener(clickListener);
		subPic.setOnClickListener(clickListener);
		subRedirect.setOnClickListener(clickListener);
		subComment.setOnClickListener(clickListener);
		redirect_bt.setOnClickListener(clickListener);
		comment_bt.setOnClickListener(clickListener);
		refresh.setOnClickListener(clickListener);
		comment.setOnClickListener(clickListener);
		redirect.setOnClickListener(clickListener);
		favorite.setOnClickListener(clickListener);
		weibodel.setOnClickListener(clickListener);
		//more.setOnClickListener(clickListener);
	}
	@Override
	public void setViews() {
		titleBack.setVisibility(View.VISIBLE);
		titleName.setVisibility(View.VISIBLE);
		titleHome.setVisibility(View.GONE);
		titleName.setText(R.string.weiboBody);
		
        if (mStatus != null){
        	User user = mStatus.getUser();
        	if (user != null){
        		AsyncImageLoader.getInstance().loadPortrait(Long.parseLong(mStatus.getUser().getId()),
        				mStatus.getUser().getProfileImageUrl(), icon);
        		name.setText(user.getScreenName());
        		userId = user.getId();
        		String selfUserId = OAuthInfoManager.getInstance().getWeiboUserId();
        		//20121112 xujun: for weibo delete
        		if (userId.equalsIgnoreCase(selfUserId)){
        			weibodel.setVisibility(View.VISIBLE);
        		}else{
        			weibodel.setVisibility(View.GONE);
        		}
        		if (user.isVerified()) {
        			v.setVisibility(View.VISIBLE);
        		} else {
        			v.setVisibility(View.GONE);
        		}
        	}
    		content.setMovementMethod(LinkMovementMethod.getInstance());
    		content.setText(TextUtil.formatContent(mStatus.getText(), this));
    		time.setText(TimeUtil.getTimeStr(mStatus.getCreatedAt()));
    		if(mStatus.getSource() != null)
    		{
    			source.setText(getString(R.string.from) + mStatus.getSource().getName());
    		}
    		

    		// 加载图片
    		AsyncImageLoader.getInstance().loadPre(Long.parseLong(mStatus.getId()),
    				mStatus.getBmiddlePic(), pic);
    		if (isExitSubStatus) { // 若存在转发、评论的微博
    			sub.setVisibility(View.VISIBLE);
    			Status subStatus = mStatus.getRetweetedStatus();
    			AsyncImageLoader.getInstance().loadPre(Long.parseLong(subStatus.getId()), subStatus.getBmiddlePic(), subPic);
    			String sContent = "";
    			if (subStatus.getUser() != null){
    				sContent = "@" + subStatus.getUser().getScreenName() + ":" + subStatus.getText();
    			}else{
    				sContent = subStatus.getText();
    			}
    			subContent.setMovementMethod(LinkMovementMethod.getInstance());
    			subContent.setText(TextUtil.formatContent(sContent, this));
    		} else {
    			sub.setVisibility(View.GONE);
    		}

    		// 更新微博及转发微博的转发数、评论数
    		new AsyncDataLoader(countCallback).execute();
        }
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	private AsyncDataLoader.Callback countCallback = new Callback() {
		boolean isRefresh = false;
		
		@Override
		public void onStart() {
			try {
				if (mStatus != null && OAuthInfoManager.getInstance().tokenIsReady()){
					mStatus = Sina.getInstance().getWeibo().getOneUserTimeline(mStatus.getId());
					isRefresh = true;
				}
			} catch (WeiboException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void onPrepare() {

		}

		@Override
		public void onFinish() {
			if (isRefresh && mStatus != null) {
				redirect_bt.setText(mStatus.getRepostsCount() + "");
				comment_bt.setText(mStatus.getCommentsCount() + "");
				if (mStatus.getRetweetedStatus() != null ) {
					subRedirectNum.setText(mStatus.getRetweetedStatus().getRepostsCount() + "");
					subCommentNum.setText(mStatus.getRetweetedStatus().getCommentsCount() + "");
				}
			}	
		}
	};

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
			case R.id.detail_userinfo:
//				goToUserinfo();
				break;
			case R.id.detail_pic:
				showPic(mStatus);
				break;
			case R.id.detail_subPic:
				showPic(mStatus.getRetweetedStatus());
				break;
			case R.id.detail_subRedirect:
				goToRepostList(mStatus.getRetweetedStatus());
				break;
			case R.id.detail_subComent:
				goToCommentList(Long.parseLong(mStatus.getRetweetedStatus().getId()));
				break;
			case R.id.detail_redirect_bt:
				goToRepostList(mStatus);
				break;
			case R.id.detail_comment_bt:
				goToCommentList(Long.parseLong(mStatus.getId()));
				break;
			case R.id.detail_refresh:
				refresh();
				break;
			case R.id.detail_comment:
				commentWeibo(Long.parseLong(mStatus.getId()));
				break;
			case R.id.detail_redirect:
				redirectWeibo(Long.parseLong(mStatus.getId()), mStatus);
				break;
			case R.id.detail_favorite:
				favWeibo();
				break;
			case R.id.detail_del:
				if (OAuthInfoManager.getInstance().isLogin(WeiboDetail.this)){
					handler.sendEmptyMessage(1);
				}
				break;
			default:
				break;
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
	 * 返回首页
	 */
	private void backToHome() {
		Sina.getInstance().backToHome(this);
	}

	/**
	 * 转入用户信息界面
	 */
	private void goToUserinfo() {
		if (OAuthInfoManager.getInstance().isLogin(WeiboDetail.this)){
			if (mStatus != null && mStatus.getUser() != null){
				Sina.getInstance().goToUserInfo(this, Long.parseLong(mStatus.getUser().getId()));
			}	
		}
	}

	/**
	 * xujun 20120908
	 * 显示图片
	 */
	private void showPic(Status status) {
		if (status == null)
			return;
		String url = "";
		String picType = "";
		if (status.getOriginalPic() != null){
			url = status.getOriginalPic();
			picType = "ori";
			Log("showPic ori");
		}else if (status.getBmiddlePic() != null){
			url = status.getBmiddlePic();
			picType = "mid";
			Log("showPic mid");
		}else if (status.getThumbnailPic() != null){
			url = status.getThumbnailPic();
			picType = "thumb";
			Log("showPic thumb");
		}else{
			Log("no pic url");
			return;
		}
		Sina.getInstance().showImageViewZoom(WeiboDetail.this, Long.parseLong(status.getId()), url, picType);
	}

	/**
	 * 刷新
	 */
	private void refresh() {
		new AsyncDataLoader(countCallback).execute();
	}

	/**
	 * 转到评论列表界面
	 * 
	 * @param id
	 */
	private void goToCommentList(long id) {
		if (OAuthInfoManager.getInstance().isLogin(WeiboDetail.this)){
			Sina.getInstance().goToCommentList(this, id);
		}	
	}

	/**
	 * 评论微博
	 */
	private void commentWeibo(long id) {
		if (OAuthInfoManager.getInstance().isLogin(WeiboDetail.this)){
			Sina.getInstance().commentWeibo(WeiboDetail.this, id);
		}	
	}

	/**
	 * 转发微博
	 * 
	 * @param id
	 */
	private void redirectWeibo(long id, Status status) {
		if (OAuthInfoManager.getInstance().isLogin(WeiboDetail.this)){
			Sina.getInstance().redirectWeibo(WeiboDetail.this, id, status);
		}	
	}

	/**
	 * 收藏微博
	 */
	private void favWeibo() {
		if (OAuthInfoManager.getInstance().isLogin(WeiboDetail.this)){
			try {
				Sina.getInstance().getWeibo().createFavorite(Long.parseLong(mStatus.getId()));
				WeiboToast.show(this, "加入收藏");
			} catch (WeiboException e) {
				e.printStackTrace();
				WeiboToast.show(this, "收藏失败");
			}
		}
	}
	
	/**
	 * xujun 20121112: destroy status
	 * 
	 * @param id
	 */
	private void delWeiboDialog() {
		
		mydialog = new MyDialogWin(WeiboDetail.this);
		mydialog.show();
		mydialog.setMsgPopShow();
		mydialog.setPopText(WeiboDetail.this.getString(R.string.weibo_del_confirm));
		mydialog.setPopItem1Text(WeiboDetail.this.getString(R.string.weibo_del_confirm_ok));
		mydialog.setPopItem2Text(WeiboDetail.this.getString(R.string.weibo_del_confirm_cancel));
		mydialog.setPopItem1(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delWeibo();
				mydialog.cancel();
			}
		});
		mydialog.setPopItem2(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mydialog.cancel();
			}
		});	
	}
	
	/**
	 * xujun 20121117: weibo delete
	 */
	private void delWeibo(){
		try{
			Sina.getInstance().getWeibo().destroyStatus(mStatus.getId());
			Intent i = new Intent();
			i.putExtra(DELFLAG, true);
			setResult(RESULT_OK, i);
			finish();
			WeiboToast.show(getApplicationContext(), WeiboDetail.this.getString(R.string.weibo_del_success));
		}catch(WeiboException e){
			Log(e.getMessage());
			WeiboToast.show(getApplicationContext(), WeiboDetail.this.getString(R.string.weibo_del_fail));
		}
	}

	/**
	 * 更多
	 */
	private void showMore() {

	}
	
	private void goToRepostList(Status srcStatus){
		if (OAuthInfoManager.getInstance().isLogin(WeiboDetail.this)){
			Sina.getInstance().showRepostWeibo(WeiboDetail.this, srcStatus);
		}
	}

	void Log(String msg) {
		Log.i("weibo", "WeiboDetail--" + msg);
	}

}
