package com.star.weibo.adapter;


import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.star.weibo4j.model.UserTrend;
import com.star.yytv.R;

public class TrendListAdapter extends BaseAdapter {
	private Context mContext;
	private List<UserTrend> trends;
	private TextView name;

	public TrendListAdapter(Context context, List<UserTrend> trends) {
		this.mContext = context;
		this.trends = trends;
	}

	@Override
	public int getCount() {
		return trends == null ? 0 : trends.size();
	}

	@Override
	public Object getItem(int position) {
		return trends.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.trendsitem, null);
			name = (TextView) convertView.findViewById(R.id.trend_name);
		}
		
		final UserTrend trend = trends.get(position);
		name.setText(trend.getHotword());
		
		return convertView;
	}

}
