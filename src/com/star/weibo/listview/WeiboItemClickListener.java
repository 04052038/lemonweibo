package com.star.weibo.listview;

import com.star.weibo.Sina;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo4j.model.Status;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class WeiboItemClickListener implements OnItemClickListener {
	
	private Context mContext;
	private BufferedWeiboItemAdapter weiboItemAdapter;
	//header num of XListView
	private byte headerNum = 1;
	
	public WeiboItemClickListener(Context context, BufferedWeiboItemAdapter weiboItemAdapter){
		this.mContext = context;
		this.weiboItemAdapter = weiboItemAdapter;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (weiboItemAdapter.statusSize() > 0) {
			if (arg2 > headerNum - 1 && arg2 < weiboItemAdapter.statusSize() + headerNum){
				weiboItemAdapter.setClickedStatusIndex(arg2 - headerNum);
				Sina.getInstance().goToDetailForResult(mContext, (Status)weiboItemAdapter.getItem(arg2 - headerNum), weiboItemAdapter.getRequestCode());
			}
		}

	}
	
	public void setHeaderNum(byte headerNum){
		this.headerNum = headerNum;
	}

}
