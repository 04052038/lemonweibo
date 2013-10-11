package com.star.weibo.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import com.star.yytv.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.star.weibo.AsyncImageLoader;
import com.star.weibo.Sina;
import com.star.weibo.UserList;
import com.star.weibo.WeiboDetail;
import com.star.weibo.WeiboToast;
import com.star.weibo.lsnr.UserItemAttenClickListener;
import com.star.weibo.lsnr.UserItemUnAttenClickListener;
import com.star.weibo4j.Weibo;
import com.star.weibo4j.model.User;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.R;
import com.star.yytv.model.YyTvActivityManager;

public class UserListAdapter extends BaseAdapter {
	private Context mContext;
	private List<User> users;
	private Weibo weibo;
	private int cate; //显示种类（关注 粉丝 黑名单）
	private AsyncImageLoader asyncImageLoader = AsyncImageLoader.getInstance();
	public int getCate() {
		return cate;
	}

	public void setCate(int cate) {
		this.cate = cate;
	}

	public UserListAdapter(Context context, List<User> users) {
		this.mContext = context;
		this.users = users;
		this.weibo = Sina.getInstance().getWeibo();
	}

	@Override
	public int getCount() {
		return users == null ? 0 : users.size();
	}

	@Override
	public Object getItem(int position) {
		return users.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserItem item = null;
		if (convertView == null) {
			item = new UserItem();
			convertView = View.inflate(mContext, R.layout.useritem, null);
			item.icon = (ImageView) convertView
					.findViewById(R.id.useritem_icon);
			item.v = (ImageView) convertView.findViewById(R.id.useritem_v);
			item.name = (TextView) convertView.findViewById(R.id.useritem_name);
			item.weibo = (TextView) convertView
					.findViewById(R.id.useritem_weibo);
			item.attention = (Button) convertView
					.findViewById(R.id.useritem_attention);
			item.unattention = (Button) convertView
					.findViewById(R.id.useritem_unattention);	
			convertView.setTag(item);
		} else {
			item = (UserItem) convertView.getTag();
		}
		final User user = users.get(position);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Sina.getInstance().goToUserInfo(mContext, Long.parseLong(user.getId()));
			}
		});
		if(user != null){
			asyncImageLoader.loadPortrait(Long.parseLong(user.getId()), user.getProfileImageUrl(), item.icon);
		}
		if (user.isVerified()) {
			item.v.setVisibility(View.VISIBLE);
		} else {
			item.v.setVisibility(View.GONE);
		}
		item.attention.setOnClickListener(new UserItemAttenClickListener(mContext, user, item));
		item.unattention.setOnClickListener(new UserItemUnAttenClickListener(mContext, user, item));
		if (user.isFollowHim()){
			item.attention.setVisibility(View.GONE);
			item.unattention.setVisibility(View.VISIBLE);
		}else{
			item.attention.setVisibility(View.VISIBLE);
			item.unattention.setVisibility(View.GONE);
		}
		item.name.setText(user.getScreenName());
		return convertView;
	}
	
	void log(String msg){
		Log.i("weibo", "UserListAdapter--"+msg);
	}

}
