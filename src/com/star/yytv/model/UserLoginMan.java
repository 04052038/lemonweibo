package com.star.yytv.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.star.weibo.net.AccessToken;
import com.star.weibo4j.Users;
import com.star.yytv.common.StackTrace;
import com.star.yytv.common.yytvConst;

/**
 * <p>文件名称: UserLoginMan.java </p>
 * <p>文件描述: 用户帐号模型管理。</p>
 * <p>版权所有: 版权所有(C)2012-2016</p>
 * <p>公   司: 上海曜众信息科技有限公司</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>完成日期：2012-09-22</p>
 * <p>修改记录1: </p>
 * <pre>
 *    修改日期：
 *    版 本 号：
 *    修 改 人：
 *    修改内容：
 * </pre>
 * <p></p>
 * @version 1.0
 * @author 
 */

public class UserLoginMan {
	
	private static UserLoginMan instance=null;
	
	/**
	 * 构造函数
	 */
	private UserLoginMan(){
		
	}
	
	/**
	 * 单件
	 * @return
	 */
	public static synchronized UserLoginMan getInstance(){
		if(instance==null)
			instance= new UserLoginMan();
		return instance;
	}
	
	
	/**
	 * 用户登陆前，本地信息设置
	 * @param sp
	 */
	public void prepareLogin(SharedPreferences sp)
	{		
		
		String token = sp.getString(yytvConst.SP_TOKEN,"");		
		String weiboUserId = sp.getString(yytvConst.SP_WEIBOUSERID,"");			
		String expires_in = sp.getString(yytvConst.SP_EXPIRTES_IN,"");
        //之前登陆过
        if(token!=null && token.trim().length() > 0)
        {   
			OAuthInfoManager.getInstance().setToken(token);			
			OAuthInfoManager.getInstance().setWeiboUserId(weiboUserId);
			
			AccessToken accessToken = new AccessToken(token, OAuthInfoManager.CONSUMER_SECRET);
			accessToken.setExpiresIn(expires_in);
			com.star.weibo.net.Weibo.getInstance().setAccessToken(accessToken);
		
			com.star.weibo4j.Weibo weibo1 = new com.star.weibo4j.Weibo();
			weibo1.setToken(token);			
						
        }
	
	}
		
	
	public void storeWeibInfo(Context context,Bundle values)
	{
		String token = values.getString("access_token");
		String expires_in = values.getString("expires_in");
		String uid = values.getString("uid");
		
		SharedPreferences sp = context.getSharedPreferences(yytvConst.SP_PASSWDFILE, Context.MODE_PRIVATE);
		sp.edit().putString(yytvConst.SP_TOKEN,token).commit();				
		sp.edit().putString(yytvConst.SP_WEIBOUSERID,uid).commit();				
		sp.edit().putString(yytvConst.SP_EXPIRTES_IN,expires_in).commit();
		
		
		OAuthInfoManager.getInstance().setToken(token);          
        OAuthInfoManager.getInstance().setWeiboUserId(uid);
	}
	
	
}
