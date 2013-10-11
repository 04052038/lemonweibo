package com.star.weibo;

import com.star.weibo.buf.WeiboBuffer;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.Log;
import com.star.yytv.LoginActivity;
import com.star.yytv.R;
import com.star.yytv.common.yytvConst;
import com.star.yytv.model.OAuthInfoManager;
import com.star.yytv.ui.lsn.MyDialogWin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebViewDatabase;
import android.widget.Button;
import android.widget.TextView;

public class SelfAbout extends Activity{
	
	private TextView titlename;
	private Button back;
	private Button yytv_userdeal;
	private MyDialogWin mydialog;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);		
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN ,  
		              WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		setContentView(R.layout.yytv_about);
		init();

	}	
	private void init(){
		titlename = (TextView)findViewById(R.id.titlebar_name);
		titlename.setVisibility(View.VISIBLE);
		titlename.setText("关于");
	
		back = (Button)findViewById(R.id.titlebar_back);
		back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				SelfAbout.this.finish();
			}
        });
        
        yytv_userdeal = (Button)findViewById(R.id.yytv_about_userdeal);
        yytv_userdeal.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				endContect();
			}
        });      
	}
	
	private void gotoLoginActivity(){
		Intent intent = new Intent(this, LoginActivity.class);
		this.startActivity(intent);
	}
	
	private void endContect(){
		if(OAuthInfoManager.getInstance().tokenIsReady()){
    	mydialog = new MyDialogWin(this);
    	mydialog.show();
		mydialog.setMsgPopShow();
		mydialog.setPopText("是否退出微博账户？");
		mydialog.setPopItem1Text("确     定");
		mydialog.setPopItem2Text("取     消");
		mydialog.setPopItem1(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {			
					Sina.getInstance().getWeibo().endSession();					
					WebViewDatabase.getInstance(SelfAbout.this).clearUsernamePassword();
					WebViewDatabase.getInstance(SelfAbout.this).clearHttpAuthUsernamePassword();
					WeiboBuffer.clearWebViewCache(SelfAbout.this);
					WeiboBuffer.beforeLogOut(OAuthInfoManager.getInstance().getWeiboUserId(), SelfAbout.this);

					OAuthInfoManager.getInstance().setToken(null);
					sp = SelfAbout.this.getSharedPreferences(yytvConst.SP_PASSWDFILE, Context.MODE_PRIVATE);
					sp.edit().putString(yytvConst.SP_TOKEN, "").commit();
					WeiboToast.show(getApplicationContext(), "微博账户已退出。");
					gotoLoginActivity();
				} catch (WeiboException e) {
					e.printStackTrace();
					Log.i("endSession", e.toString());
					WeiboToast.show(getApplicationContext(), "请重新尝试。");
				}
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
	}
}
