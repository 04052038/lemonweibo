package com.star.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.star.yytv.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.star.weibo.async.AsyncDelStatusCallback;
import com.star.weibo.listview.TimelineXListViewProxy;
import com.star.weibo.listview.UserStatusXListViewProxy;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.Weibo;
import com.star.weibo4j.model.User;
import com.star.weibo4j.model.WeiboException;
import com.star.weibo4j.org.json.JSONException;
import com.star.weibo4j.org.json.JSONObject;
import com.star.yytv.R;
import com.star.yytv.model.OAuthInfoManager;
/**
 * 显示用户信息
 * @author starry
 *
 */
public class UserInfo extends Activity implements RefreshViews {
	private Button titleBack;
	private TextView titleName;
	private Button titleHome;
	private ProgressBar titleProgressBar;
	private ImageView icon;
	private TextView name;
	private ImageView v;
	private Button attention_bt;
	private Button unattention_bt;
	private Button at_bt;
	private LinearLayout userinfo_atten_layout;
	private LinearLayout sinaVLayout;
	private TextView sinaV;
//	private TextView address;
//	private TextView intro;
	private LinearLayout attentionLayout;
	private TextView attentionCount;
	private LinearLayout userInfoDetailLayout;
	private TextView weiboCount;
	private LinearLayout fansLayout;
	private TextView fansCount;
	private LinearLayout topicLayout;
	private TextView trendCount;
//	private Button refresh;
//	private Button atElse;
//	private Button sendMsg;
//	private Button addToBlacklistl;
	private View headerView;
	private XListView weiboList;
	private UserStatusXListViewProxy userStatusXListViewProxy;

	private User user;
	private Weibo weibo;
	private boolean isLoadUser = false;

	public static final String USER_ID = "user_id";
	//xujun 20120908：为支持按用户的screenname显示用户信息增加USER_SCREENNAME
	public static final String USER_SCREENNAME = "user_screenname";
	public static final int REQUEST_CODE_USERSTATUS = 2000;
	private static UserInfo instance;
	
	public static UserInfo getInstance()
	{
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.userinfotwo);
		weibo = Sina.getInstance().getWeibo();
		getViews();
		setViews();
		setListener();
		userStatusXListViewProxy.loadRefresh(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//isLoadUser flag: prevent repeating loading user
		if (user == null && ! isLoadUser){
			isLoadUser = true;
			userStatusXListViewProxy.loadRefresh(true);
		}
	}
	
	private void getViews() {
		RelativeLayout titlebar=(RelativeLayout)findViewById(R.id.userinfo_titlebar);
		titleBack = (Button) titlebar.findViewById(R.id.titlebar_back);
		titleName=(TextView)titlebar.findViewById(R.id.titlebar_name);
		titleHome = (Button) titlebar.findViewById(R.id.titlebar_home);
		titleProgressBar = (ProgressBar) titlebar.findViewById(R.id.titlebar_progress);
		headerView = View.inflate(this,R.layout.userinfoheader,null);
		icon = (ImageView) headerView.findViewById(R.id.userinfo_icon);
		name = (TextView) headerView.findViewById(R.id.userinfo_name);
		v = (ImageView) headerView.findViewById(R.id.userinfo_v);
		attention_bt = (Button) headerView.findViewById(R.id.userinfo_attention_bt);
		unattention_bt = (Button) headerView.findViewById(R.id.userinfo_unattention_bt);
		at_bt = (Button) headerView.findViewById(R.id.userinfo_at_bt);
		userinfo_atten_layout = (LinearLayout) headerView.findViewById(R.id.userinfo_atten_layout);
		sinaVLayout = (LinearLayout)headerView.findViewById(R.id.userinfo_SinaV_layout);
		sinaV = (TextView) headerView.findViewById(R.id.userinfo_elseSinaV);
		//address = (TextView) findViewById(R.id.userinfo_elseAddress);
		//intro = (TextView) findViewById(R.id.userinfo_elseIntro);
		userInfoDetailLayout = (LinearLayout) headerView.findViewById(R.id.userinfo_detail_layout);
		attentionLayout = (LinearLayout) headerView.findViewById(R.id.userinfo_attention_layout);
		attentionCount = (TextView) headerView.findViewById(R.id.userinfo_attention);
		//weiboLayout = (LinearLayout) findViewById(R.id.userinfo_weibo_layout);
		weiboCount = (TextView) headerView.findViewById(R.id.userinfo_weibonum);
		fansLayout = (LinearLayout) headerView.findViewById(R.id.userinfo_fans_layout);
		fansCount = (TextView) headerView.findViewById(R.id.userinfo_fans);
		topicLayout = (LinearLayout) headerView.findViewById(R.id.userinfo_topic_layout);
		trendCount = (TextView) headerView.findViewById(R.id.userinfo_topic);
//		refresh = (Button) findViewById(R.id.userinfo_refresh);
//		atElse = (Button) findViewById(R.id.userinfo_atElse);
//		sendMsg = (Button) findViewById(R.id.userinfo_senMsg);
		//addToBlacklistl = (Button) findViewById(R.id.userinfo_addToBlacklist);
		weiboList = (XListView) findViewById(R.id.user_weiboList);
	}

	private void setViews() {
		titleBack.setVisibility(View.VISIBLE);
		titleName.setVisibility(View.VISIBLE);
		titleName.setText(R.string.info);
		titleHome.setVisibility(View.VISIBLE);
		weiboList.setPullLoadEnable(true);
		weiboList.addHeaderView(headerView);
		userStatusXListViewProxy = new UserStatusXListViewProxy(this, weiboList, this);
		userStatusXListViewProxy.setRequestCode(REQUEST_CODE_USERSTATUS);
		setUserInfo();	
	}

	private void setListener() {
		titleBack.setOnClickListener(clickListener);
		titleHome.setOnClickListener(clickListener);
		attention_bt.setOnClickListener(clickListener);
		unattention_bt.setOnClickListener(clickListener);
		at_bt.setOnClickListener(clickListener);
		attentionLayout.setOnClickListener(clickListener);
		
		sinaVLayout.setOnClickListener(clickListener);
//		weiboLayout.setOnClickListener(clickListener);
		fansLayout.setOnClickListener(clickListener);
		topicLayout.setOnClickListener(clickListener);
		userInfoDetailLayout.setOnClickListener(clickListener);
//		refresh.setOnClickListener(clickListener);
//		atElse.setOnClickListener(clickListener);
//		sendMsg.setOnClickListener(clickListener);
//		addToBlacklistl.setOnClickListener(clickListener);
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
			case R.id.userinfo_attention_bt:
				attention();
				break;
			case R.id.userinfo_unattention_bt:
				unattention();
				break;
			case R.id.userinfo_at_bt:
				atHim();
				break;
			case R.id.userinfo_attention_layout:
				showAttention();
				break;
//			case R.id.userinfo_weibo_layout:
//				showWeibo();
//				break;
			case R.id.userinfo_fans_layout:
				showFans();
				break;
			case R.id.userinfo_topic_layout:
				showTopic();
				break;
			case R.id.userinfo_SinaV_layout:
				showUserInfoDetail();
				break;	
			case R.id.userinfo_detail_layout:
				showUserInfoDetail();
				break;	
//			case R.id.userinfo_refresh:
//				refresh();
//				break;
//			case R.id.userinfo_atElse:
//				atElse();
//				break;
//			case R.id.userinfo_senMsg:
//				sendMsg();
//				break;
//			case R.id.userinfo_addToBlacklist:
//				addToBlacklist();
//				break;
			default:
				break;
			}
		}
	};
	
	private void setUserInfo(){
		Bundle bundle = getIntent().getExtras();
		if (bundle!=null) {
				//xujun 20120908：为支持按用户的screenname显示用户信息，增加对USER_SCREENNAME的处理
				if (bundle.containsKey(USER_ID)){
					long userId = bundle.getLong(USER_ID,0);
					userStatusXListViewProxy.setUserid(String.valueOf(userId));
				}else if (bundle.containsKey(USER_SCREENNAME)){
					String userScreenname = bundle.getString(USER_SCREENNAME);
					userStatusXListViewProxy.setUsername(userScreenname);
				}
				//js = weibo.showFriendships(String.valueOf(user.getId()));
		}
	}
	/**
	 * 返回
	 */
	private void back() {
		finish();
	}
	/**
	 * 显示主页
	 */
	private void backToHome() {

	}
	/**
	 * 关注之 （未做异步处理）
	 */
	private void attention() {
		if (user != null){
			try {
				weibo.createFriendshipByScreenName(user.getScreenName());
				WeiboToast.show(this, "关注成功");
				attention_bt.setVisibility(View.GONE);
				unattention_bt.setVisibility(View.VISIBLE);
			} catch (WeiboException e) {
				e.printStackTrace();
				WeiboToast.show(this, "关注失败");
				Log(e.toString());
			}
		}	
	}
	/**
	 * 解除关注 (未做异步处理)
	 */
	private void unattention() {
		if (user != null){
			try {
				weibo.destroyFriendship(
						String.valueOf(user.getId()));
				WeiboToast.show(this, "取消对" + user.getScreenName() + "的关注");
				unattention_bt.setVisibility(View.GONE);
				attention_bt.setVisibility(View.VISIBLE);
			} catch (WeiboException e) {
				e.printStackTrace();
				WeiboToast.show(this, "取消对"+user.getScreenName()+"的关注失败");
			}
		}
	}
	
	/**
	 * 
	 */
	private void atHim(){
		if (user != null){
			Sina.getInstance().weiboAtHim(UserInfo.this, user);
		}	
	}
	/**
	 * 显示该用户的关注列表 (未完成)
	 */
	private void showAttention() {
		if (user != null){
			Sina.getInstance().showAttention(this, Long.parseLong(user.getId()));
		}
	}
	/**
	 * 显示该用户的微博列表
	 */
	private void showWeibo() {
		if (user != null){
			Sina.getInstance().showWeibo(this, Long.parseLong(user.getId()));
		}	
	}
	/**
	 * 显示该用户的粉丝列表
	 */
	private void showFans() {
		if (user != null){
			Sina.getInstance().showFans(this, Long.parseLong(user.getId()));
		}	
	}
	/**
	 *  显示该用户的话题列表
	 */
	private void showTopic() {
		if (user != null){
			Sina.getInstance().ShowTopic(this, Long.parseLong(user.getId()));
		}
	}
	/**
	 * 刷新
	 */
	private void refresh() {

	}
	/**
	 * @该用户
	 */
	private void atElse() {

	}
	/**
	 * 对该用户发私信
	 */
	private void sendMsg() {

	}
	/**
	 * 将该用户添加到黑名单
	 */
	private void addToBlacklist() {
//		try {
//			weibo.createBlock(
//					String.valueOf(user.getId()));
//			WeiboToast.show(this, "已将 "+user.getScreenName()+" 加入黑名单");
//		} catch (WeiboException e) {
//			e.printStackTrace();
//			Log(e.toString());
//			WeiboToast.show(this, "操作失败，未能将 "+user.getScreenName()+" 加入黑名单");
//		}
	}
	
	private void showUserInfoDetail(){
		if (user != null){
			Sina.getInstance().showUserInfoDetail(UserInfo.this, user);
		}
	}
	
	@Override
	/**
	 * delete weibo
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_USERSTATUS){
			if (resultCode == RESULT_OK){
				if (data != null){
					boolean delFlag = data.getBooleanExtra(WeiboDetail.DELFLAG, false);
					if (delFlag){
						AsyncDelStatusCallback delStatusCallback = new AsyncDelStatusCallback(UserInfo.this, userStatusXListViewProxy);
						new AsyncDataLoader(delStatusCallback).execute();
					}
				}
			}
		}
	}

	void Log(String msg) {
		Log.i("weibo", "UserInfo--" + msg);
	}
	
	public void refreshView(Object data){
		user = (User)data;
		if(user != null){
			isLoadUser = true;
			name.setText(user.getScreenName());
			AsyncImageLoader.getInstance().loadPortrait(Long.parseLong(user.getId()), user.getAvatarLarge(), icon);
			//address.setText(user.getLocation());
			sinaV.setText(user.getVerifiedReason());
			attentionCount.setText("" + user.getFriendsCount());
			weiboCount.setText("" + user.getStatusesCount());
			fansCount.setText("" + user.getFollowersCount());
			trendCount.setText("话题数");
			if(user.isVerified()){
				v.setVisibility(View.VISIBLE);
			}else{
				v.setVisibility(View.GONE);
			}
			if(user.getId().equals(OAuthInfoManager.getInstance().getWeiboUserId())){
				userinfo_atten_layout.setVisibility(View.GONE);
			}else{
				userinfo_atten_layout.setVisibility(View.VISIBLE);
				if (user.isFollowHim()){
					attention_bt.setVisibility(View.GONE);
					unattention_bt.setVisibility(View.VISIBLE);
				} else {
					unattention_bt.setVisibility(View.GONE);	
					attention_bt.setVisibility(View.VISIBLE);	
				}
			}
		}else{
			WeiboToast.show(UserInfo.this, "用户不存在!");
			UserInfo.this.finish();
//			weiboList.setPullRefreshEnable(false);
//			weiboList.setPullLoadEnable(false);
		}
	}
	
	public void showPopupWindow(String info){
		
	}
	
	public void hidePopupWindow(){
		
	}
}
