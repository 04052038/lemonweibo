package com.star.yytv.model;

import com.star.weibo.WeiboToast;
import com.star.yytv.R;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkManager {
	public static NetWorkManager instance = new NetWorkManager();
	public static String networkError = "网络连接不给力哦，建议使用WIFI。";

	private NetWorkManager() {

	}

	public synchronized static NetWorkManager getInstance() {
		return instance;
	}

	public synchronized boolean isNetworkAvailable( Activity mActivity ) { 
		if(mActivity == null)
		{
			return true;
		}
		Context context = mActivity.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {    
		   return false;
		 } else {  
		    NetworkInfo[] info = connectivity.getAllNetworkInfo();    
		     if (info != null) {        
		          for (int i = 0; i < info.length; i++) {           
		              if (info[i].getState() == NetworkInfo.State.CONNECTED) {              
		                 return true; 
		             }        
		          }     
		       } 
		   }   
		   return false;
		}
	
	public boolean isNetworkAvailableWithToast(Activity mActivity){
		if (isNetworkAvailable(mActivity)){
			return true;
		}else{
			WeiboToast.show(mActivity, networkError);
			return false;
		}
	}
	
	/**
	 * getActiveNetwork function
	 * <p>get active network type.
	 * @param cnt Activity Context
	 * @return int 1: WIFI, 2: MOBILE, 3: OTHER, 0: no connect.
	 */
	public int getActiveNetwork(Context cnt){
		ConnectivityManager connMgr = (ConnectivityManager) cnt.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		if (activeInfo != null && activeInfo.isConnected()){
			if (activeInfo.getType() == ConnectivityManager.TYPE_WIFI)
				return 1;
			else if (activeInfo.getType() == ConnectivityManager.TYPE_MOBILE)
				return 2;
			else 
				return 3;
		} else {
			return 0;
		}
	}

}