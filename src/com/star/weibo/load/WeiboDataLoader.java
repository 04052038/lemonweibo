package com.star.weibo.load;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.widget.ListView;

import com.star.weibo.AsyncDataLoader;
import com.star.weibo.Sina;
import com.star.weibo.adapter.WeiboItemAdapter;
import com.star.weibo.data.DB;
import com.star.weibo.data.DBHandler;
import com.star.weibo4j.model.Count;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.WeiboException;

public class WeiboDataLoader extends DataLoader{
	private Context mContext;
	private List<Status> sList;
	private Handler handler;
	private ListView lv;
	
	public WeiboDataLoader(Context context, ListView listView){
		mContext=context;
		lv=listView;
		handler = new Handler(context.getMainLooper());
	}

	@Override
	protected void asyncLoad() {
		// TODO Auto-generated method stub
		super.asyncLoad();
		new AsyncDataLoader(asyncRemoteLoadListener).execute();
	}
	/**
	 * 异步加载服务器上的微博数据的监听器
	 */
	private AsyncDataLoader.Callback asyncRemoteLoadListener = new AsyncDataLoader.Callback() {
		List<Status> statusList;
		private HashMap<Long, Count> counts;
		@Override
		public void onStart() {
			try {
				statusList = Sina.getInstance().getWeibo().getFriendsTimeline();
				//counts=getCounts(statusList);
				//saveData(statusList, counts);
			} catch (WeiboException e) {
				e.printStackTrace();
				Log(e.toString());
			}
		}

		@Override
		public void onPrepare() {
		}

		@Override
		public void onFinish() {
			WeiboItemAdapter adapter = new WeiboItemAdapter(mContext,
					statusList);
			lv.setAdapter(adapter);
		}
	};
	/**
	 * 获取一个HashMap，以微博ID为key，对应微博的Count为值
	 * @param sList
	 * @return
	 */
//	private HashMap<Long,Count> getCounts(List<Status> sList){
//		HashMap<Long, Count> counts = new HashMap<Long, Count>();
//		StringBuffer buffer=new StringBuffer();
//		for (int i = 0; i < sList.size(); i++) {
//			buffer.append(sList.get(i).getId() + ",");
//		}
//		try {
//			List<Count> count = Sina.getInstance().getWeibo()
//					.getCounts(buffer.substring(0, buffer.length() - 1));
//			for (int i = 0; i < count.size(); i++) {
//				counts.put(count.get(i).getId(), count.get(i));
//			}
//		} catch (WeiboException e) {
//			e.printStackTrace();
//		}
//		return counts;
//	}
	/**
	 * 保存微博数据
	 * @param status
	 * @param counts
	 */
	private void saveData(List<Status> status, HashMap<Long, Count> counts) {
		DBHandler dbHandler = new DBHandler(mContext);
		dbHandler.clearTable(DB.HOME_TABLE);
		if(status.size()>20){ //只保存前20项
			status=status.subList(0, 19);
		}
		for (int i = 0; i < status.size(); i++) {
			dbHandler.save(status.get(i), counts.get(status.get(i)
					.getId()));
		}
	}

	void Log(String msg){
		com.star.yytv.Log.i("weibo", "load--WeiboDataLoader--"+msg);
	}

}
