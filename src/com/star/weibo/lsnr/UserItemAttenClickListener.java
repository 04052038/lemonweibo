package com.star.weibo.lsnr;

import com.star.weibo.Sina;
import com.star.weibo.WeiboToast;
import com.star.weibo.adapter.UserListAdapter;
import com.star.weibo.adapter.UserItem;
import com.star.weibo4j.model.User;
import com.star.weibo4j.Weibo;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.R;

import android.content.Context;
import com.star.yytv.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class UserItemAttenClickListener implements OnClickListener {
	
	private Context mContext;
	private User user;
	private UserItem item;
	
	public UserItemAttenClickListener(Context mContext, User user, UserItem item){
		this.mContext = mContext;
		this.user = user;
		this.item = item;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		try {
			Sina.getInstance().getWeibo().createFriendshipByUserid(String.valueOf(user.getId()));
			item.attention.setVisibility(View.GONE);
			item.unattention.setVisibility(View.VISIBLE);
			user.setFollowHim(true);
			WeiboToast.show(mContext, mContext.getString(R.string.user_attention_success));
		} catch (WeiboException e) {
			e.printStackTrace();
			WeiboToast.show(mContext, mContext.getString(R.string.user_attention_fail));
			log(e.toString());
		}
	}
	
	void log(String msg){
		Log.i("weibo", "UserItemAttenClickListener--"+msg);
	}

}
