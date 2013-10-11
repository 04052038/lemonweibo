package com.star.yytv.model;

import android.app.Activity;

import android.view.View;
import android.view.View.OnClickListener;
import com.star.yytv.ui.lsn.MyDialogWin;

public class YyTvActivityManager {

	private static YyTvActivityManager instance;
	private MyDialogWin mydialog;
	 
	
	public synchronized static YyTvActivityManager getInstance() {
		if (instance == null) {
			instance = new YyTvActivityManager();
		}
		return instance;
	}
	
	public boolean SysExitCallBack(final Activity mActivity)
	{
		boolean retAck = true;
		mydialog = new MyDialogWin(mActivity);
		mydialog.show();
		mydialog.setMsgPopShow();
		mydialog.setPopText("你确定退出吗？");
		mydialog.setPopItem1Text("确     定");
		mydialog.setPopItem2Text("返     回");
		mydialog.setPopItem1(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				mActivity.finish();
				
				android.os.Process
						.killProcess(android.os.Process
								.myPid());
				android.os.Process
						.killProcess(android.os.Process
								.myTid());
				android.os.Process
						.killProcess(android.os.Process
								.myUid());
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
		
		return retAck;
	}
	
}
