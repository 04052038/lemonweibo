package com.star.weibo;

import android.content.Context;
import android.widget.Toast;

public class WeiboToast {

	public static void show(Context context,String text){
		Toast.makeText(context, text, 1000).show();
	}

}
