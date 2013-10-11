package com.star.weibo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import com.star.yytv.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.star.weibo.adapter.UserListAdapter;
import com.star.weibo.async.AsyncMoreFriendshipCallback;
import com.star.weibo4j.model.User;
import com.star.weibo4j.model.UserFriendship;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.R;
import com.star.yytv.common.YyProgressDiagFact;
import com.star.yytv.model.OAuthInfoManager;

/**
 * 显示用户关注列表/用户粉丝列表/用户黑名单列表（暂不支持）
 * @author starry
 *
 */
public class UserList extends Activity{
	private Button titleBack;
	private TextView titleName;
	private Button titleHome;
	private ProgressDialog dialog;
	private List<User> users;
	private ListView userList;
	private boolean needUserFriendship;
	
	private int cate; //显示种类（关注 粉丝 黑名单）
	
	public static final String USER_CATE="user_cate";
	public static final int CATE_ATTENTION=0; //关注
	public static final int CATE_FANS=1; //粉丝
	public static final int CATE_BLACKLIST=3; //黑名单
	 
	private long userId; //传入的用户id
	public static final String USER_ID="user_id";
	public static final short FIRSTSHOW_USERNUM = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.attentionerlist);
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			userId=bundle.getLong(USER_ID,0);
			cate=bundle.getInt(USER_CATE,0);
		}
		
		getViews();
		setViews();
		setListeners();
		
		new AsyncDataLoader(asyncCallback).execute();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void getViews()
	{
		dialog = YyProgressDiagFact.getYyProgressDiag(this);
		RelativeLayout titleBar=(RelativeLayout)findViewById(R.id.attentioner_titlebar);
		titleBack=(Button)titleBar.findViewById(R.id.titlebar_back);
		titleName=(TextView)titleBar.findViewById(R.id.titlebar_name);
		titleHome=(Button)titleBar.findViewById(R.id.titlebar_home);
		
		userList=(ListView)findViewById(R.id.attentioner_listView);
		
	}
	
	private void setViews()
	{
		titleBack.setVisibility(View.VISIBLE);
		titleName.setVisibility(View.VISIBLE);
		titleHome.setVisibility(View.GONE);
		titleBack.setOnClickListener(clickListener);
		titleHome.setOnClickListener(clickListener);
		
		if(cate==CATE_ATTENTION){
			titleName.setText(R.string.attention);
		}else if(cate==CATE_FANS){
			titleName.setText(R.string.fans);
		}else if(cate==CATE_BLACKLIST){
			titleName.setText(R.string.blackList);
		}
	}
	
	private void setListeners() 
	{
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(dialog != null)
		{
			dialog.dismiss();
		}
	}

	
	private AsyncDataLoader.Callback asyncCallback=new AsyncDataLoader.Callback(){
		
		@Override
		public void onStart() {
			try {
				if(cate==CATE_ATTENTION){
					log("user attention");
					users=Sina.getInstance().getWeibo().getFriendsStatuses(String.valueOf(userId));
				}else if(cate==CATE_FANS){
					users=Sina.getInstance().getWeibo().getFollowersStatuses(String.valueOf(userId));
				}else if(cate==CATE_BLACKLIST){
					//users=Sina.getInstance().getWeibo().getBlockingUsers();
				}
				if (users != null){
					int firstUserFriendshipNum = 0;
					if (users.size() <= FIRSTSHOW_USERNUM){
						needUserFriendship = false;
						firstUserFriendshipNum = users.size();
					}else{
						needUserFriendship = true;
						firstUserFriendshipNum = FIRSTSHOW_USERNUM;
					}
					for (int i = 0; i < firstUserFriendshipNum; i ++){
						UserFriendship friendShip = Sina.getInstance().getWeibo().getUserFriendship("" + OAuthInfoManager.getInstance().getWeiboUserId(), users.get(i).getId());
						if (friendShip != null && friendShip.getSource() != null){
							users.get(i).setFollowHim(friendShip.getSource().isFollowing());
						}		
					}
					
				}
				
			} catch (WeiboException e) {
				e.printStackTrace();
				log(e.toString());
			}
		}
		
		@Override
		public void onPrepare() {
			dialog.show();
		}
		
		@Override
		public void onFinish() {
			UserListAdapter adapter=new UserListAdapter(UserList.this, users);
			adapter.setCate(cate);
			userList.setAdapter(adapter);
			if (needUserFriendship){
				AsyncMoreFriendshipCallback moreCallback = new AsyncMoreFriendshipCallback(UserList.this, userList, users);
				new AsyncDataLoader(moreCallback).execute();
			}
			dialog.dismiss();
		}
	};

	private OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.titlebar_back:
				finish();
				break;
			case R.id.titlebar_home:
				backToHome();
				break;
			}
		}
	};
	/**
	 * 返回首页
	 */
	private void backToHome() {
		
	}
	
	void log(String msg){
		Log.i("weibo", "UserList--"+msg);
	}
}
