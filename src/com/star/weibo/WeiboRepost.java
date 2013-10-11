package com.star.weibo;

import com.star.weibo.async.AsyncDelStatusCallback;
import com.star.weibo.listview.RepostStatusXListViewProxy;
import com.star.weibo.listview.TimelineXListViewProxy;
import com.star.weibo.listview.UserStatusXListViewProxy;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.Weibo;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.User;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.star.yytv.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeiboRepost extends Activity implements RefreshViews {
	private Button titleBack;
	private TextView titleName;
	private Button titleHome;
	private ProgressBar titleProgressBar;
	
	private PopupWindow popupWindow;
	private View popupWindowLayout;
	private TextView popupInfoView;
	private RelativeLayout titleLayout;
	private Handler handler;
	private Status srcStatus;
	private Weibo weibo;
	private XListView repostList;
	private RepostStatusXListViewProxy repostStatusXListViewProxy;

	public static final String SRCSTATUS = "srcStatus";
	public static final int REQUEST_CODE_REPOST = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weibo_repost);
		weibo = Sina.getInstance().getWeibo();
		getViews();
		setViews();
		setListener();
		handler = new Handler();
		repostStatusXListViewProxy.loadRefresh(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void getViews() {
		RelativeLayout titlebar=(RelativeLayout)findViewById(R.id.repost_titlebar);
		titleBack = (Button) titlebar.findViewById(R.id.titlebar_back);
		titleName=(TextView)titlebar.findViewById(R.id.titlebar_name);
		titleHome = (Button) titlebar.findViewById(R.id.titlebar_home);
		titleProgressBar = (ProgressBar) titlebar.findViewById(R.id.titlebar_progress);
		repostList = (XListView) findViewById(R.id.repost_weiboList);
		popupWindowLayout = View.inflate(this, R.layout.popupwindow, null);
		popupInfoView = (TextView)popupWindowLayout.findViewById(R.id.pop_info);
		titleLayout = (RelativeLayout) findViewById(R.id.repost_titlebar);
	}

	private void setViews() {
		titleBack.setVisibility(View.VISIBLE);
		titleName.setVisibility(View.VISIBLE);
		titleName.setText(R.string.repost_title);
		titleHome.setVisibility(View.INVISIBLE);
		repostList.setPullLoadEnable(true);
		repostStatusXListViewProxy = new RepostStatusXListViewProxy(this, repostList, this);
		repostStatusXListViewProxy.setRequestCode(REQUEST_CODE_REPOST);
		setSrcStatus();
	}

	private void setListener() {
		titleBack.setOnClickListener(clickListener);
		titleHome.setOnClickListener(clickListener);
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
	
	private void setSrcStatus(){
		Bundle bundle = getIntent().getExtras();
		if (bundle!=null) {
				if (bundle.containsKey(SRCSTATUS)){
					srcStatus = (Status)bundle.getSerializable(SRCSTATUS);
					repostStatusXListViewProxy.setSrcStatusId(srcStatus.getId());
				}
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
	
	@Override
	/**
	 * delete weibo
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_REPOST){
			if (resultCode == RESULT_OK){
				if (data != null){
					boolean delFlag = data.getBooleanExtra(WeiboDetail.DELFLAG, false);
					if (delFlag){
						AsyncDelStatusCallback delStatusCallback = new AsyncDelStatusCallback(WeiboRepost.this, repostStatusXListViewProxy);
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

}
