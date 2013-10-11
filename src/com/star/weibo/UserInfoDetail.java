package com.star.weibo;

import com.star.weibo.async.AsyncDelStatusCallback;
import com.star.weibo.listview.UserStatusXListViewProxy;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.Weibo;
import com.star.weibo4j.model.User;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.star.yytv.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserInfoDetail extends Activity implements InitViews {
	
	private Button titleBack;
	private TextView titleName;
	private Button titleHome;
	private ProgressBar titleProgressBar;
	private TextView verify;
	private TextView screenName;
	private TextView gender;
	private TextView location;
	private TextView description;
	private TextView blog;
	private TextView remark;
	private LinearLayout userinfo_verify_layout;

	private User user;
	
	public static String USER = "user";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.userinfo_detail);
		getViews();
		setViews();
		setListeners();
		setUserInfo();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void getViews() {
		RelativeLayout titlebar=(RelativeLayout)findViewById(R.id.userinfo_titlebar);
		titleBack = (Button) titlebar.findViewById(R.id.titlebar_back);
		titleName=(TextView)titlebar.findViewById(R.id.titlebar_name);
		titleHome = (Button) titlebar.findViewById(R.id.titlebar_home);
		titleProgressBar = (ProgressBar) titlebar.findViewById(R.id.titlebar_progress);
		verify = (TextView)findViewById(R.id.userinfo_verify);
		screenName = (TextView)findViewById(R.id.userinfo_screenname);
		gender = (TextView)findViewById(R.id.userinfo_gender);
		location = (TextView)findViewById(R.id.userinfo_loc);
		description = (TextView)findViewById(R.id.userinfo_desc);
		blog = (TextView)findViewById(R.id.userinfo_blog);
		remark = (TextView)findViewById(R.id.userinfo_remark);
		userinfo_verify_layout = (LinearLayout)findViewById(R.id.userinfo_verify_layout);
	}

	public void setViews() {
		titleBack.setVisibility(View.VISIBLE);
		titleName.setVisibility(View.VISIBLE);
		titleName.setText(R.string.user_detail_titlename);
		titleHome.setVisibility(View.VISIBLE);
	}

	public void setListeners() {
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
	
	private void setUserInfo(){
		Bundle bundle = getIntent().getExtras();
		if (bundle!=null) {
				if (bundle.containsKey(USER)){
					user = (User)bundle.getSerializable(USER);
					verify.setText(user.getVerifiedReason());
					if(user.getVerifiedReason().equals("")){
						userinfo_verify_layout.setVisibility(View.GONE);
					}
					screenName.setText(user.getScreenName());
					String gengderStr = user.getGender();
					if (gengderStr.equalsIgnoreCase("m")){
						gender.setText("男");
					}else if (gengderStr.equalsIgnoreCase("f")){
						gender.setText("女");
					}else{
						gender.setText("未知");
					}
					location.setText(user.getLocation());
					description.setText(user.getDescription());
					blog.setText("");
					remark.setText(user.getRemark());
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

	void Log(String msg) {
		Log.i("weibo", "UserInfo--" + msg);
	}

}
