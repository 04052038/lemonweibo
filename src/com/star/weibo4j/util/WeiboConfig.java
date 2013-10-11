package com.star.weibo4j.util;


public class WeiboConfig {
	public WeiboConfig(){}
//	private static Properties props = new Properties(); 
//	static{
//		try {
//			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	private static String client_ID ="1437404342";        
	private static String client_SERCRET ="11648f542ec5be5c079428af57ed2cf0";
	private static String redirect_URI="http://www.yaoyaotv.com/yytv";
	private static String baseURL="https://api.weibo.com/2/";
	private static String accessTokenURL="https://api.weibo.com/oauth2/access_token";
	private static String authorizeURL="https://api.weibo.com/oauth2/authorize";
	
	public static String getValue(String key){
		if(key.equalsIgnoreCase("client_ID"))
		{
			return client_ID;
		}
		else if(key.equalsIgnoreCase("client_SERCRET"))
		{
			return client_SERCRET;
		}
		else if(key.equalsIgnoreCase("redirect_URI"))
		{
			return redirect_URI;
		}
		else if(key.equalsIgnoreCase("baseURL"))
		{
			return baseURL;
		}
		else if(key.equalsIgnoreCase("accessTokenURL"))
		{
			return accessTokenURL;
		}
		else if(key.equalsIgnoreCase("authorizeURL"))
		{
			return authorizeURL;
		}
		//return props.getProperty(key);
		return "";
	}

//    public static void updateProperties(String key,String value) {    
//            props.setProperty(key, value); 
//    } 
}
