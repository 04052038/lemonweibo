package com.star.yytv;

import android.app.Activity;
import android.os.Bundle;
import com.star.yytv.Log;
import android.view.Window;
import android.widget.Toast;

import com.star.weibo.net.AccessToken;
import com.star.weibo.net.DialogError;
import com.star.weibo.net.Weibo;
import com.star.weibo.net.WeiboDialogListener;
import com.star.weibo.net.WeiboException;
import com.star.weibo4j.Users;
import com.star.weibo4j.model.User;
import com.star.yytv.model.OAuthInfoManager;
import com.star.yytv.model.UserLoginMan;

public class SplashActivity extends Activity
{
	
	public static SplashActivity instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{   
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		super.onCreate(savedInstanceState);		 
		setContentView( R.layout.splash );
		
		instance = this;
	
		Weibo weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(OAuthInfoManager.CONSUMER_KEY, 
				OAuthInfoManager.CONSUMER_SECRET);
		weibo.setRedirectUrl(OAuthInfoManager.BASE_URL);	
							
		weibo.authorize(SplashActivity.this,new AuthDialogListener());
	

	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	

	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			String uid = values.getString("uid");
			Log.i("access_token",token);
			Log.i("expires_in",expires_in);
			Log.i("uid",uid);
			
			try 
			{
				AccessToken accessToken = new AccessToken(token, OAuthInfoManager.CONSUMER_SECRET);
				accessToken.setExpiresIn(expires_in);
				com.star.weibo.net.Weibo.getInstance().setAccessToken(accessToken);
			
				new com.star.weibo4j.Weibo().setToken(token);
				
				//validate
				Users um = new Users();
				User user = um.showUserById(uid);
				
				//process
				UserLoginMan.getInstance().storeWeibInfo(SplashActivity.this,values);  
				
				Log.i("SplashActivity.this.finish()","SplashActivity.this.finish()");
		        SplashActivity.this.finish();
		
			} catch (Exception e) {
				
				Log.d("onComplete Exception",e.getMessage());
								
				Toast.makeText(getApplicationContext(),
						"Auth onComplete Exception token:" + token + " uid:"+uid, Toast.LENGTH_LONG).show();
				SplashActivity.this.finish();
			}
		}

		@Override
		public void onError(DialogError e) {
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
			SplashActivity.this.finish();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
			SplashActivity.this.finish();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
			SplashActivity.this.finish();
		}

	}
}