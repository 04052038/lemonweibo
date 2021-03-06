package com.star.yytv;

public class Log {
	static final boolean isDebug = true;
	
	public static void i(String tag, String msg){
		if (isDebug)
			android.util.Log.i(tag, msg);
	}
	
	public static void e(String tag, String msg){
		if (isDebug)
			android.util.Log.e(tag, msg);
	}
	
	public static void d(String tag, String msg){
		if (isDebug)
			android.util.Log.d(tag, msg);
	}
	
	public static void v(String tag, String msg){
		if (isDebug)
			android.util.Log.v(tag, msg);
	}
}
