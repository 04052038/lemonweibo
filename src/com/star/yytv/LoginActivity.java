package com.star.yytv;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.star.weibo.Home;
import com.star.weibo.WeiboToast;
import com.star.yytv.common.yytvConst;
import com.star.yytv.model.NetWorkManager;
import com.star.yytv.model.OAuthInfoManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.star.yytv.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;


public class LoginActivity extends Activity {
	private static final int ISNEW = 0x001;
	private SharedPreferences sp;
	private Context mContext = this;
	private DotMarks mDotMarks;
	private Button login_in = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
        setContentView(R.layout.login);
		
		init0();
	}
	@Override
	public void onResume(){
		super.onResume();
		
		if(OAuthInfoManager.getInstance().tokenIsReady()){
			WeiboToast.show(LoginActivity.this, "您已经绑定微博账户，谢谢！");
			gotoFront();
		}	
	}
	private void init0(){
		
		login_in = (Button)findViewById(R.id.login_in);
		login_in.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetWorkManager.getInstance().isNetworkAvailableWithToast(LoginActivity.this)){
					OAuthInfoManager.getInstance().doBindSinaWeiboUser(LoginActivity.this);
				}
			}
		});	
	}

	
	private void gotoFront(){
		Intent intent = new Intent(LoginActivity.this, Home.class);
		LoginActivity.this.startActivity(intent);
		LoginActivity.this.finish();
	}
	
}