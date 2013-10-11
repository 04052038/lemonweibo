package com.star.yytv;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.star.weibo.Home;
import com.star.yytv.common.StackTrace;
import com.star.yytv.common.yytvConst;
import com.star.yytv.model.OAuthInfoManager;
import com.star.yytv.model.UserLoginMan;


/**
 * <p>文件名称: FrontTimerHandler.java </p>
 * <p>文件描述: 定时处理器。</p>
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

public class FrontTimerHandler extends Handler{	
	
	private Activity mActivity = null;
	
	public FrontTimerHandler(Activity mActivity)
	{
		this.mActivity = mActivity;
	}

	
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		
		int msgCode = msg.what;

		switch (msgCode) {
        
		case Front.CLOSE_ACT:
		{//启动新窗体
			//1、关闭当前窗体
			SharedPreferences sp = mActivity.getSharedPreferences(yytvConst.SP_PASSWDFILE, Activity.MODE_PRIVATE);
			UserLoginMan.getInstance().prepareLogin(sp);
			
			mActivity.finish();
			if(OAuthInfoManager.getInstance().tokenIsReady()){
				Intent intent = new Intent(mActivity, Home.class);
				mActivity.startActivity(intent);
			}
			else{
			//2、进入主程序窗体
				Intent intent = new Intent(mActivity, LoginActivity.class);
				mActivity.startActivity(intent);
			}
			break;
		}
		
		case Front.DOWNLOAD_ACT:
		{
			break;
		}	
	
		default:
			break;

		}

	}
}
