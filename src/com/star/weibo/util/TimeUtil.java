package com.star.weibo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.star.yytv.Log;

public class TimeUtil {

	public static String getTimeStr(Date date) {
		Date today = new Date();
		long interval = today.getTime() - date.getTime();
		today.setHours(23);
		today.setMinutes(59);
		today.setSeconds(59);
		boolean isSameDay = (interval / 86400000) == 0 ? true : false;
		if (isSameDay) {
			if(interval<60*1000){
				return (interval/1000)+"秒前";
			}else if(interval<60*60*1000){
				return (interval/(60*1000))+"分前";
			}else{
				return (interval/(60*60*1000))+"小时前";
			}
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm",
					Locale.ENGLISH);
			return dateFormat.format(date);
		}
	}

	public static String getTimeStr(long date) {
		return getTimeStr(new Date(date));
	}

	void log(String msg) {
		Log.i("weibo", "TimeCalculator--" + msg);
	}
}
