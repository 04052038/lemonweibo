package com.star.weibo.listview;

import com.star.weibo.Sina;
import com.star.weibo.WeiboToast;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.ui.lsn.MyDialogWin;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class AtWeiboItemLongClickListener implements OnItemLongClickListener {

	private Context mContext;
	private BufferedWeiboItemAdapter weiboItemAdapter;
	private Status status;
    private MyDialogWin mydialog;
	
	public AtWeiboItemLongClickListener(Context context, BufferedWeiboItemAdapter weiboItemAdapter){
		this.mContext = context;
		this.weiboItemAdapter = weiboItemAdapter;
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		weiboItemAdapter.setClickedStatusIndex(arg2 - 1);
        status = weiboItemAdapter.getClickedStatus();
        
		mydialog = new MyDialogWin(mContext);
		mydialog.show();
		mydialog.setMsgPopShow();
		mydialog.setPopItem3Show();
		mydialog.setPopText("微博功能");
		mydialog.setPopItem1Text("转     发");
		mydialog.setPopItem2Text("评     论");
		mydialog.setPopItem3Text("收     藏");
		mydialog.setPopItem1(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Sina.getInstance().redirectWeibo(mContext, Long.parseLong(status.getId()), 
						status);
				mydialog.cancel();
			}
		});
		mydialog.setPopItem2(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Sina.getInstance().commentWeibo(mContext, Long.parseLong(status.getId()));
				mydialog.cancel();
			}
		});
		mydialog.setPopItem3(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Sina.getInstance().getWeibo().createFavorite(Long.parseLong(status.getId()));
					WeiboToast.show(mContext, "加入收藏");
				} catch (WeiboException e) {
					e.printStackTrace();
					WeiboToast.show(mContext, "收藏失败");
				}
				mydialog.cancel();
			}
		});
		return true;
	}

}
