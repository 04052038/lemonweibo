package com.star.weibo;

import java.util.List;

import com.star.weibo.adapter.TrendListAdapter;

import com.star.weibo4j.model.UserTrend;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.Log;
import com.star.yytv.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TrendList extends Activity{
	private Button titleBack;
	private TextView titleName;
	private Button titleHome;
	private List<UserTrend> trends;
	private long userId; //传入的用户id
	private ListView userList;
	
	public static final String USER_ID="user_id";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.attentionerlist);
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			userId=bundle.getLong(USER_ID,0);
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
		
		titleName.setText("话题列表");
	}
	
	private void setListeners() 
	{
		
	}
	
	private AsyncDataLoader.Callback asyncCallback=new AsyncDataLoader.Callback(){
		
		@Override
		public void onStart() {
			try {

				trends = Sina.getInstance().getWeibo().getTrends(String.valueOf(userId));
				
			} catch (WeiboException e) {
				e.printStackTrace();
				log(e.toString());
			}
		}
		
		@Override
		public void onPrepare() {
			//
		}
		
		@Override
		public void onFinish() {
			TrendListAdapter adapter=new TrendListAdapter(TrendList.this, trends);

			userList.setAdapter(adapter);
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
		Log.i("weibo", "TrendList--"+msg);
	}
}
