package com.star.yytv.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class yytvSysUtil {
	private final static String sDate = "";
    
	public static String getDate(long dateTime )
	{
		SimpleDateFormat df = new SimpleDateFormat("MM-dd kk:mm");
		String retDate = null;
		
		try
		{
			retDate = df.format(new Date(dateTime));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		 
		return retDate;
	}
	public static Date getDate(String sDate)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date retDate = null;
		
		try
		{
			retDate = df.parse(sDate);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		 
		return retDate;
	}
    
    public static Date getDateTime(String sDate)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyMMddkkmm");
		Date retDate = null;
		
		try
		{
			retDate = df.parse(sDate);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		 
		return retDate;
	}
    
	public static Date getSysDate()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date retDate = null;
		
		if(sDate.trim().length()>0)
		{
			try
			{
				retDate = df.parse(sDate);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			retDate = new Date();
		}
		
		 
		return retDate;
	}
	
	public static String getSysDateStr()
	{
		SimpleDateFormat df = new SimpleDateFormat("MM-dd");
		String retDate = "";
		
		if(sDate.trim().length()<=0)
		{
			try
			{
				retDate = df.format(new Date());
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			retDate = sDate;
		}
		
		return retDate;
		
	}
	
	public static String getSysDateTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmm");
		String retDate = "";
		
		try
		{
			retDate = df.format(new Date());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return retDate;
		
	}
	
	public static String getDateStr(int index)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		
		java.util.Calendar calendar = new java.util.GregorianCalendar();

		calendar.add(Calendar.DATE, index);
		
		String retDate = "";
		try
		{
			retDate = df.format(calendar.getTime());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return retDate;
		
	}
	
	public static int getDayNum(int index) {
		java.util.Calendar calendar = new java.util.GregorianCalendar();

		calendar.add(Calendar.DATE, index);//
		int dWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int realDay = (dWeek - 1) == 0 ? 7 : (dWeek - 1);

		return realDay;
	}

	public static String getDayLabel(int dWeek) {
		String strDay = "";
		switch (dWeek) {
		case 1:
			strDay = "周一";
			break;
		case 2:
			strDay = "周二";
			break;
		case 3:
			strDay = "周三";
			break;
		case 4:
			strDay = "周四";
			break;
		case 5:
			strDay = "周五";
			break;
		case 6:
			strDay = "周六";
			break;
		case 7:
			strDay = "周日";
			break;
		default:
			break;
		}
		
		return strDay;
	}
}
