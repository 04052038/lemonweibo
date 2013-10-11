package com.star.weibo.lsnr;

import com.star.weibo.Sina;
import com.star.weibo.WeiboToast;
import com.star.weibo.adapter.UserItem;
import com.star.weibo4j.model.User;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.R;

import android.content.Context;
import com.star.yytv.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class UserItemUnAttenClickListener implements OnClickListener {
	
	private Context mContext;
	private User user;
	private UserItem item;
	
	public UserItemUnAttenClickListener(Context mContext, User user, UserItem item){
		this.mContext = mContext;
		this.user = user;
		this.item = item;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		try {
			Sina.getInstance().getWeibo().destroyFriendship(String.valueOf(user.getId()));
			item.unattention.setVisibility(View.GONE);
			item.attention.setVisibility(View.VISIBLE);
			user.setFollowHim(false);
			WeiboToast.show(mContext, mContext.getString(R.string.user_unattention_success));
		} catch (WeiboException e) {
			e.printStackTrace();
			WeiboToast.show(mContext, mContext.getString(R.string.user_unattention_fail));
			log(e.toString());
		}

	}
	
	void log(String msg){
		Log.i("weibo", "UserItemUnAttenClickListener--"+msg);
	}

}
