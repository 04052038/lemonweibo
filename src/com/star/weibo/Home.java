package com.star.weibo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import com.star.yytv.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.adapter.WeiboItemAdapter;
import com.star.weibo.async.AsyncDelStatusCallback;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.WeiboException;
import com.star.weibo.xlist.XListView;
import com.star.weibo.xlist.XListView.IXListViewListener;
import com.star.weibo.listview.AtXListViewProxy;
import com.star.weibo.listview.TimelineXListViewProxy;
import com.star.weibo.listview.ToMeCommentXListViewProxy;
import com.star.weibo.util.TimeUtil;
import com.star.weibo.buf.WeiboBuffer;
import com.star.yytv.BackTaskConst;
import com.star.yytv.BasicActivity;
import com.star.yytv.R;
import com.star.yytv.common.MyAnimations;
import com.star.yytv.common.YyProgressDiagFact;
import com.star.yytv.common.yytvConst;
import com.star.yytv.model.OAuthInfoManager;
import com.star.yytv.model.YyTvActivityManager;
import com.star.yytv.ui.lsn.ShakeListener;
import com.star.yytv.ui.lsn.ShakeListener.OnShakeListener;
/**
 * 首页
 * @author starry
 *
 */
public class Home extends BasicActivity implements InitViews, RefreshViews {
	public static final int REQUEST_CODE_WBDETAIL = 1;
	private Button titleCreateWeibo;
	private Button titleRefresh;
	private LinearLayout login_layout;
	private Button login_on;
	private ImageView lemonweibo_logo;

	//xujun 20121011：采用XListView，屏蔽掉list_footer
	private ProgressDialog dialog;
	private PopupWindow popupWindow;
	private View popupWindowLayout;
	private TextView popupInfoView;
	private RelativeLayout titleLayout;
	
	private Handler handler;
	
	private static Home instance;
	//private ProgressBar titleProgress;
	//xujun 20121011：采用XListView，屏蔽掉list_footer
	//private LinearLayout list_footer;
	//private TextView list_footer_more;
	//private LinearLayout list_footer_loading;
	private XListView weiboList;
	private TimelineXListViewProxy timelineXListViewProxy;
	private XListView atList;
	private AtXListViewProxy atXListViewProxy;
	private XListView commentList;	
	private ToMeCommentXListViewProxy toMeCommentXListViewProxy;
	public static boolean justLogin = false;
	private ViewFlipper mViewFlipper = null;
	private List<View> listViews = null;
	private int currIndex = 0;
	
	private RelativeLayout composerButtonsWrapper = null;
	private RelativeLayout composerButtonsShowHideButton = null;
	private LinearLayout home_shade = null;
	private ImageView composerButtonsShowHideButtonIcon = null;
	private boolean areButtonsShowing = false;
	
	public static Home getInstance()
	{
		return instance;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		setContentView(R.layout.home);
//		setGuideResId(R.drawable.guide_img2);//添加引导页
        
		getViews();
		setViews();
		setListeners();
		
		if (! timelineXListViewProxy.isLoadedFromDB()){
			timelineXListViewProxy.loadFromDB();
		}
		handler = new Handler();
		Log("onCreate");
		logPhoneVer();
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (timelineXListViewProxy.isLoadedFromDB() && (timelineXListViewProxy.isJustLogin() || justLogin)
				&& OAuthInfoManager.getInstance().tokenIsReady()){
			
///			login_layout.setVisibility(View.GONE);
///			weiboList.setVisibility(View.VISIBLE);
			timelineXListViewProxy.setJustLogin(false);
			justLogin = false;
			timelineXListViewProxy.loadRefresh(true);
            Log("onResume: just login, timelineXListViewProxy.loadRefresh");
		}
		if(!OAuthInfoManager.getInstance().tokenIsReady()){
///			login_layout.setVisibility(View.VISIBLE);
///			weiboList.setVisibility(View.GONE);
			timelineXListViewProxy.setJustLogin(true);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log("home destory");
	}
	@Override
	protected void onStop() {		
		super.onStop();
	}

	@Override
	protected void onPause() {	
		super.onPause();

	}
	
	public void setCheckboxState(boolean flag){
//		login_checkbox.setChecked(flag);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			return YyTvActivityManager.getInstance().SysExitCallBack(Home.this);
		}

		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void getViews() {
		dialog = YyProgressDiagFact.getYyProgressDiag(Home.this);
		popupWindowLayout = View.inflate(this, R.layout.popupwindow, null);
		popupInfoView = (TextView)popupWindowLayout.findViewById(R.id.pop_info);
		titleLayout = (RelativeLayout) findViewById(R.id.home_title);
		titleCreateWeibo = (Button) findViewById(R.id.weibo_createWeibo);
				
		titleRefresh = (Button) findViewById(R.id.weibo_refresh);
		lemonweibo_logo = (ImageView) findViewById(R.id.lemonweibo_logo);
//		weiboList = (XListView) findViewById(R.id.home_weiboList);
//		weiboList.setPullLoadEnable(true);
		
///		login_layout = (LinearLayout) findViewById(R.id.login_layout_1);
///		login_on = (Button) login_layout.findViewById(R.id.login_on_1);

		//xujun 20121011：采用XListView，屏蔽掉list_footer
		//list_footer=(LinearLayout) getLayoutInflater().inflate(R.layout.list_footer, null);
		//list_footer_more=(TextView)list_footer.findViewById(R.id.footer_more);
		//list_footer_loading=(LinearLayout)list_footer.findViewById(R.id.footer_loading);
		MyAnimations.initOffset(this);
        RelativeLayout prog_composer_buttons = (RelativeLayout) findViewById(R.id.weibo_composer_buttons);
		composerButtonsWrapper = (RelativeLayout) prog_composer_buttons.findViewById(R.id.composer_buttons_wrapper);
		composerButtonsShowHideButton = (RelativeLayout) prog_composer_buttons.findViewById(R.id.composer_buttons_show_hide_button);
		composerButtonsShowHideButtonIcon = (ImageView) prog_composer_buttons.findViewById(R.id.composer_buttons_show_hide_button_icon);
		home_shade = (LinearLayout) findViewById(R.id.home_shade);
		

		mViewFlipper = (ViewFlipper) findViewById(R.id.weibo_viewflipper);
		listViews = new ArrayList<View>();
	}
	@Override
	public void setViews() {
		
		weiboList = new XListView(this);
		weiboList.setPullLoadEnable(true);
		weiboList.setCacheColorHint(Color.TRANSPARENT);
		weiboList.setDivider(new ColorDrawable(0x00000000));
		weiboList.setVerticalScrollBarEnabled(false);
		atList = new XListView(this);
		atList.setPullLoadEnable(true);
		atList.setCacheColorHint(Color.TRANSPARENT);
		atList.setDivider(new ColorDrawable(0x00000000));
		atList.setVerticalScrollBarEnabled(false);
		commentList = new XListView(this);
		commentList.setPullLoadEnable(true);
		commentList.setCacheColorHint(Color.TRANSPARENT);
		commentList.setDivider(new ColorDrawable(0x00000000));
		commentList.setVerticalScrollBarEnabled(false);
		
		listViews.add(weiboList);
		listViews.add(atList);
		listViews.add(commentList);
		for(int i=0; i<listViews.size(); i++){
			mViewFlipper.addView(listViews.get(i));
		}
		
		timelineXListViewProxy = new TimelineXListViewProxy(this, weiboList, this);
		timelineXListViewProxy.setRequestCode(REQUEST_CODE_WBDETAIL);
		WeiboBuffer.HOME_TIMELINE_XLISTVIEWPROXY = timelineXListViewProxy;
		WeiboBuffer.HOME_TIMELINE_WEIBOITEMADAPTER = timelineXListViewProxy.getWeiboItemAdapter();
		atXListViewProxy = new AtXListViewProxy(this, atList, this);
		atXListViewProxy.setRequestCode(REQUEST_CODE_WBDETAIL);
		toMeCommentXListViewProxy = new ToMeCommentXListViewProxy(this, commentList, this);
		WeiboBuffer.MSG_ATME_XLISTVIEWPROXY = atXListViewProxy;
		WeiboBuffer.MSG_ATME_WEIBOITEMADAPTER = atXListViewProxy.getWeiboItemAdapter();
		WeiboBuffer.MSG_TOMECOMMENT_COMMENTITEMADAPTER = toMeCommentXListViewProxy.getCommentAdapter();
		WeiboBuffer.MSG_TOMECOMMENT_XLISTVIEWPROXY = toMeCommentXListViewProxy;
		
		setCurTabIndex(currIndex);
		
	}
	@Override
	public void setListeners() {
		titleCreateWeibo.setOnClickListener(clickListener);
		titleRefresh.setOnClickListener(clickListener);
		lemonweibo_logo.setOnClickListener(clickListener);
///		login_on.setOnClickListener(clickListener);
		composerButtonsShowHideButton.setOnClickListener(clickListener);
		home_shade.setOnClickListener(clickListener);
		// 给小图标设置点击事件
		for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) {
			final ImageView smallIcon = (ImageView) composerButtonsWrapper.getChildAt(i);
			final int position = i;
			smallIcon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					home_shade.setVisibility(View.GONE);
					home_shade.setClickable(false);
					setCurTabIndex(position);
					// 这里写各个item的点击事件
					// 1.加号按钮缩小后消失 缩小的animation
					// 2.其他按钮缩小后消失 缩小的animation
					// 3.被点击按钮放大后消失 透明度渐变 放大渐变的animation
					//composerButtonsShowHideButton.startAnimation(MyAnimations.getMiniAnimation(300));
					composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(-225, 0, 300));
					areButtonsShowing = !areButtonsShowing;
					smallIcon.startAnimation(MyAnimations.getMaxAnimation(300));
					for (int j = 0; j < composerButtonsWrapper.getChildCount(); j++) {
						if (j != position) {
							final ImageView smallIcon = (ImageView) composerButtonsWrapper.getChildAt(j);
							smallIcon.startAnimation(MyAnimations.getMiniAnimation(300));
						}
					}
				}
			});
		}
		composerButtonsShowHideButton.startAnimation(MyAnimations.getRotateAnimation(0, 360, 200));
	}
	
	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.weibo_createWeibo:
				createWeibo();
				break;
			case R.id.weibo_refresh:		
				refresh();
				break;
			case R.id.lemonweibo_logo:
				gotoSelfAbout();
				break;
			case R.id.composer_buttons_show_hide_button:		
				if (!areButtonsShowing) {
					home_shade.setVisibility(View.VISIBLE);
					home_shade.setBackgroundColor(0x66ffe7cc);
					home_shade.setClickable(true);
					// 图标的动画
					MyAnimations.startAnimationsIn(composerButtonsWrapper, 300);
					// 加号的动画
					composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(0, -225, 300));
				} else {
					home_shade.setVisibility(View.GONE);
					home_shade.setClickable(false);
					// 图标的动画
					MyAnimations.startAnimationsOut(composerButtonsWrapper, 300);
					// 加号的动画
					composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(-225, 0, 300));
				}
				areButtonsShowing = !areButtonsShowing;
				break;
			case R.id.home_shade:
				if(!areButtonsShowing){
					//
				}else{
					home_shade.setVisibility(View.GONE);
					home_shade.setClickable(false);
					// 图标的动画
					MyAnimations.startAnimationsOut(composerButtonsWrapper, 300);
					// 加号的动画
					composerButtonsShowHideButtonIcon.startAnimation(MyAnimations.getRotateAnimation(-225, 0, 300));
				}
				break;
			default:
				break;
			}
		}
	};
	
	
	
	private void login_on(){
		justLogin = true;
		timelineXListViewProxy.setJustLogin(true);
		OAuthInfoManager.getInstance().doBindSinaWeiboUser(Home.this);
	}
	
	private void showWeibo()
	{
		if(OAuthInfoManager.getInstance().doBindSinaWeiboUser(this))
		{
			
			weiboList.setVisibility(View.VISIBLE);	
			/*
			if (weiboList.getAdapter() == null || weiboList.getAdapter().getCount()<=0) {
				new AsyncDataLoader(asyncRemoteCallback).execute();
			}
			*/
		}		
		//xujun 20121011：采用XListView，屏蔽掉list_footer
		//list_footer_more.setVisibility(View.VISIBLE);
		
		
	}
	/**
	 * 发表新微�?	 */
	private void createWeibo() {
		if(OAuthInfoManager.getInstance().tokenIsReady()){
			Sina.getInstance().updateWeibo(this);
		}else{
			WeiboToast.show(Home.this, "请先登陆微博账户！");
		}
	}
	/**
	 * 刷新
	 */
	private void refresh() {
		if(OAuthInfoManager.getInstance().isLogin(Home.this)){
			timelineXListViewProxy.loadRefresh(true);
			Log("refresh");
		}
	}
	
	private void gotoSelfAbout(){
		Intent intent = new Intent(this, SelfAbout.class);
		this.startActivity(intent);
	}
	
private void setCurTabIndex(int index){
		
		this.currIndex  = index;
		if(index == 0){
			showWeiboHome();
		}
		else if(index == 1){
			showAtMe();
		}
		else if(index == 2){
			showComment();
		}else{
			//
		}
		mViewFlipper.setDisplayedChild(index);
		
	}
	private void showWeiboHome(){
		if (! timelineXListViewProxy.isLoadedFromDB()){
			timelineXListViewProxy.loadFromDB();
		}
	}	
	/**
	 * 显示@me 的litstview*/
	private void showAtMe() {
		
		if (! atXListViewProxy.isLoadedFromDB()){
			atXListViewProxy.loadFromDB();
		}
		if(atXListViewProxy.isJustLogin() && OAuthInfoManager.getInstance().tokenIsReady()){
			atXListViewProxy.setJustLogin(false);
			atXListViewProxy.loadRefresh(true);
		}

	}

	/**
	 * 显示评论me的listview*/
	private void showComment() {

		if (! toMeCommentXListViewProxy.isLoadedFromDB()){
			toMeCommentXListViewProxy.loadFromDB();
		}
		if(toMeCommentXListViewProxy.isJustLogin() && OAuthInfoManager.getInstance().tokenIsReady() ){
			toMeCommentXListViewProxy.setJustLogin(false);
			toMeCommentXListViewProxy.loadRefresh(true);
		}

	}
//	/**
//	 * 获取一个HashMap，以微博ID为key，对应微博的Count为�?	 * @param sList
//	 * @return
//	 */
//	private HashMap<Long,Count> getCounts(List<Status> sList){
//		HashMap<Long, Count> counts = new HashMap<Long, Count>();
//		StringBuffer buffer=new StringBuffer();
//		for (int i = 0; i < sList.size(); i++) {
//			buffer.append(sList.get(i).getId() + ",");
//		}
//		try {
//			List<Count> count = new ArrayList<Count>(sList.size());
//			for (int i = 0; i < count.size(); i++) {
//				Status tmpStatus = sList.get(i);
//				Count tmpCount = new Count();
//				tmpCount.setId(Long.parseLong(tmpStatus.getId()));
//				tmpCount.setRt(tmpStatus.getRepostsCount());
//				tmpCount.setComments(tmpStatus.getCommentsCount());
//				//tmpCount.setFollowers(tmpStatus.g)
//				counts.put(Long.parseLong(sList.get(i).getId()), tmpCount);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return counts;
//	}
//	
//	private HashMap<Long,Count> appendCounts(List<Status> sList,HashMap<Long,Count> counts){
//		StringBuffer buffer=new StringBuffer();
//		for (int i = 0; i < sList.size(); i++) {
//			buffer.append(sList.get(i).getId() + ",");
//		}
//		try {
//			List<Count> count = new ArrayList<Count>(sList.size());
//			for (int i = 0; i < count.size(); i++) {
//				Status tmpStatus = sList.get(i);
//				Count tmpCount = new Count();
//				tmpCount.setId(Long.parseLong(tmpStatus.getId()));
//				tmpCount.setRt(tmpStatus.getRepostsCount());
//				tmpCount.setComments(tmpStatus.getCommentsCount());
//				//tmpCount.setFollowers(tmpStatus.g)
//				counts.put(Long.parseLong(sList.get(i).getId()), tmpCount);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log(e.toString());
//		}
//		return counts;
//	}
	/**
	 * 获取比指定微博id发表时间早的微博（包括此id代表的微博）
	 * xujun 20121011：采用XListView，屏蔽掉该方法
	 * @param id
	 */
	/*
	private void showMoreWeiboMax(String id){
		list_footer_more.setVisibility(View.GONE);
		list_footer_loading.setVisibility(View.VISIBLE);
		
		try {
			int foot=weiboList.getCount();
			List<Status> moreWeibo=Sina.getInstance().getWeibo().getFriendsTimelineMax(id);
			//counts=appendCounts(moreWeibo, counts);
			statusList.addAll(moreWeibo);
			WeiboItemAdapter adapter=new WeiboItemAdapter(this, statusList);
			weiboList.setAdapter(adapter);
			weiboList.setSelection(foot);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			Log(e.toString());
		}
		list_footer_loading.setVisibility(View.GONE);
		list_footer_more.setVisibility(View.VISIBLE);
		
	}
	*/
	
	public void refreshView(Object data){
///		login_layout.setVisibility(View.VISIBLE);
///		mViewFlipper.setVisibility(View.GONE);
	}
	
	public void showPopupWindow(String info){
		initPopupWindow(info);
		popupWindow.showAsDropDown(titleLayout, 0, 10);
		handler.postDelayed(new Runnable(){
			public void run(){
				if (popupWindow != null){
					popupWindow.dismiss();
				}
			}
		}, 2000);
	}
	public void hidePopupWindow(){
		
	}
	
	private void initPopupWindow(String info){
		if (popupWindow != null){
			popupWindow.dismiss();
		}
		popupWindow = new PopupWindow(popupWindowLayout, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		popupInfoView.setText(info);
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
						AsyncDelStatusCallback delStatusCallback = new AsyncDelStatusCallback(Home.this, timelineXListViewProxy);
						new AsyncDataLoader(delStatusCallback).execute();
					}
				}
			}
		}
	}

	void Log(String msg) {
		Log.i("weibo", "home--" + msg);
	}
	
	void Log(List<Status> slist, String slistName){
		Log(slistName + " size: " + slist.size());
		for (int i=0; i < slist.size(); i++){
			Log(slistName + " " + i + " : " + slist.get(i).getId());
		}
	}
	
	void logPhoneVer(){
		String phoneInfo = "Product: " + android.os.Build.PRODUCT;
        phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI;
        phoneInfo += ", TAGS: " + android.os.Build.TAGS;
        phoneInfo += ", VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE;
        phoneInfo += ", MODEL: " + android.os.Build.MODEL;
        phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK;
        phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
        phoneInfo += ", DEVICE: " + android.os.Build.DEVICE;
        phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY;
        phoneInfo += ", BRAND: " + android.os.Build.BRAND;
        phoneInfo += ", BOARD: " + android.os.Build.BOARD;
        phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT;
        phoneInfo += ", ID: " + android.os.Build.ID;
        phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER;
        phoneInfo += ", USER: " + android.os.Build.USER;
        Log(phoneInfo);
	}
	

}
