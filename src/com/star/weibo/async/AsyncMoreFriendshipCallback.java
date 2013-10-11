package com.star.weibo.async;

import java.util.List;

import android.content.Context;
import com.star.yytv.Log;
import android.widget.ListView;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.OAuthConstant;
import com.star.weibo.Sina;
import com.star.weibo.UserList;
import com.star.weibo.adapter.BufferedWeiboItemAdapter;
import com.star.weibo.adapter.UserListAdapter;
import com.star.weibo.listview.AtXListViewProxy;
import com.star.weibo.xlist.XListView;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.User;
import com.star.weibo4j.model.UserFriendship;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.model.OAuthInfoManager;

public class AsyncMoreFriendshipCallback implements AsyncDataLoader.Callback {
	
	private Context mContext;
	private ListView userListView;
	private List<User> users;
	
	public AsyncMoreFriendshipCallback(Context context, ListView userListView, List<User> users){
		this.mContext = context;
		this.userListView = userListView;
		this.users = users;
	}

	
	@Override
	public void onPrepare() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		try{
			for (int i = UserList.FIRSTSHOW_USERNUM; i < users.size(); i ++){
				UserFriendship friendShip = Sina.getInstance().getWeibo().getUserFriendship("" + OAuthInfoManager.getInstance().getWeiboUserId(), users.get(i).getId());
				if (friendShip != null && friendShip.getSource() != null){
					users.get(i).setFollowHim(friendShip.getSource().isFollowing());
				}		
			}
		}catch (WeiboException e) {
			e.printStackTrace();
			log(e.toString());
		}
		

	}
	
	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		((UserListAdapter)(userListView.getAdapter())).notifyDataSetChanged();

	}
	
	void log(String msg){
		Log.i("weibo", "AsyncMoreFriendshipCallback--"+msg);
	}


}
