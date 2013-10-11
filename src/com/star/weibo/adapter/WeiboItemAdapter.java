package com.star.weibo.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.drawable.Drawable;

import com.star.weibo.AsyncImageLoader;
import com.star.weibo.Sina;
import com.star.weibo.WeiboList;
import com.star.weibo.util.TextUtil;
import com.star.weibo.util.TimeUtil;
import com.star.weibo4j.model.Count;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.User;
import com.star.yytv.R;

public class WeiboItemAdapter extends BaseAdapter {
	protected Context mContext;
	protected List<Status> mStatusList;
	//private HashMap<Long, Count> counts;
	private AsyncImageLoader asyncImageLoader = AsyncImageLoader.getInstance();
	//xujun 20121105
	private Drawable defaultUserIcon;
	private Drawable defaultPreviewLoadingPic;
	private boolean isShowPortraitLayout = true;
	private boolean isShowRetweetedStatus = true;
	private String weiboType = null;

	public WeiboItemAdapter(Context context, List<Status> status
			) {
		mContext = context;
		mStatusList = status;
		//this.counts = counts;
		Resources resource = mContext.getResources();
		defaultUserIcon = resource.getDrawable(R.drawable.portrait);
		defaultPreviewLoadingPic = resource.getDrawable(R.drawable.preview_pic_loading);
	}
	
	public void setShowPortraitLayout(boolean isShow){
		isShowPortraitLayout = isShow;
	}
	
	public boolean isShowRetweetedStatus() {
		return isShowRetweetedStatus;
	}

	public void setShowRetweetedStatus(boolean isShowRetweetedStatus) {
		this.isShowRetweetedStatus = isShowRetweetedStatus;
	}

	public String getWeiboType() {
		return weiboType;
	}

	public void setWeiboType(String weiboType) {
		this.weiboType = weiboType;
	}
 
	@Override
	public int getCount() {
		return mStatusList == null ? 0 : mStatusList.size();
	}

	@Override
	public Object getItem(int position) {
		return mStatusList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WeiboItem item = null;
		if (convertView == null) {
			item = new WeiboItem();
			convertView = View.inflate(mContext, R.layout.weibo_item, null);
			item.portraitLayout = (FrameLayout)convertView
			.findViewById(R.id.weibo_item_portrait_layout);
			item.icon = (ImageView) convertView
					.findViewById(R.id.weibo_item_icon);
			item.v = (ImageView) convertView.findViewById(R.id.weibo_item_v);
			item.name = (TextView) convertView
					.findViewById(R.id.weibo_item_name);
			item.pic = (ImageView) convertView
					.findViewById(R.id.weibo_item_pic);
			item.createTime = (TextView) convertView
					.findViewById(R.id.weibo_item_createTime);
			item.content = (TextView) convertView
					.findViewById(R.id.weibo_item_content);
			item.content_pic = (ImageView) convertView
					.findViewById(R.id.weibo_item_content_pic);
			item.sub = (LinearLayout) convertView
					.findViewById(R.id.weibo_item_sub);
			item.subContent = (TextView) convertView
					.findViewById(R.id.weibo_item_subContent);
			item.subPic = (ImageView) convertView
					.findViewById(R.id.weibo_item_subPic);
			item.source = (TextView) convertView
					.findViewById(R.id.weibo_item_source);
			item.redirectPic = (ImageView) convertView
					.findViewById(R.id.weibo_item_redirectPic);
			item.redirectNum = (TextView) convertView
					.findViewById(R.id.weibo_item_redirectNum);
			item.commentPic = (ImageView) convertView
					.findViewById(R.id.weibo_item_commentPic);
			item.commentNum = (TextView) convertView
					.findViewById(R.id.weibo_item_commentNum);

			convertView.setTag(item);
		}
		item = (WeiboItem) convertView.getTag();
		final Status status = mStatusList.get(position);
		//xujun 20121105
		item.icon.setImageDrawable(defaultUserIcon);
		if(status.getUser() != null)
		{
			if (isShowPortraitLayout){
				if (weiboType != null){
					asyncImageLoader.loadPortrait(Long.parseLong(status.getUser().getId()), status
							.getUser().getProfileImageUrl(), item.icon, weiboType);
				} else {
					asyncImageLoader.loadPortrait(Long.parseLong(status.getUser().getId()), status
							.getUser().getProfileImageUrl(), item.icon);
				}
				if (status.getUser().isVerified()) {
					item.v.setVisibility(View.VISIBLE);
				} else {
					item.v.setVisibility(View.GONE);
				}
			}else{
				item.portraitLayout.setVisibility(View.GONE);
			}
			item.name.setText(status.getUser().getScreenName());
		}else{  
			if (isShowPortraitLayout){
				item.v.setVisibility(View.GONE);
			}else{
				item.portraitLayout.setVisibility(View.GONE);
			}
			item.name.setText("");
		}
		item.content_pic.setImageDrawable(defaultPreviewLoadingPic);
		if (weiboType != null){
			asyncImageLoader.loadPre(Long.parseLong(status.getId()), status.getThumbnailPic(),
					item.content_pic, weiboType);
		} else {
			asyncImageLoader.loadPre(Long.parseLong(status.getId()), status.getThumbnailPic(),
					item.content_pic);
		}
		item.createTime.setText(TimeUtil
				.getTimeStr(status.getCreatedAt()));
		item.content.setText(TextUtil.formatContent(status.getText(),mContext));
		if (status.getRetweetedStatus() != null && isShowRetweetedStatus) {
			item.sub.setVisibility(View.VISIBLE);
			item.subPic.setImageDrawable(defaultPreviewLoadingPic);
			User user = status.getRetweetedStatus().getUser();
			String subContent="";
			if(user != null)
			{
				subContent="@"
					+ status.getRetweetedStatus().getUser().getScreenName()
					+ ":" + status.getRetweetedStatus().getText();
				
				/*
				item.subContent.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						Sina.getInstance().goToDetail(mContext, status);
					}
				});
				*/
			}else{
				subContent = status.getRetweetedStatus().getText();
			}
			item.subContent.setText(TextUtil.formatContent(subContent, mContext));
			if (weiboType != null){
				asyncImageLoader.loadPre(Long.parseLong(status.getRetweetedStatus().getId()),
						status.getRetweetedStatus().getThumbnailPic(),
						item.subPic, weiboType);
			} else {
				asyncImageLoader.loadPre(Long.parseLong(status.getRetweetedStatus().getId()),
						status.getRetweetedStatus().getThumbnailPic(),
						item.subPic);
			}
		} else {
			item.sub.setVisibility(View.GONE);
		}
		
		if(status.getSource() != null)
		{
			item.source.setText(mContext.getString(R.string.from)
					+ status.getSource().getName());	
		}else{
			item.source.setText("");
		}
		
		item.redirectNum.setText("" + status.getRepostsCount());
		item.commentNum.setText("" + status.getCommentsCount());
		
		item.redirectPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Sina.getInstance().redirectWeibo(mContext, Long.parseLong(status.getId()), status);
			}
		});
		item.redirectNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Sina.getInstance().redirectWeibo(mContext, Long.parseLong(status.getId()), status);
			}
		});
		item.commentPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Sina.getInstance().commentWeibo(mContext, Long.parseLong(status.getId()));
			}
		});
		item.commentNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Sina.getInstance().commentWeibo(mContext, Long.parseLong(status.getId()));
			}
		});
		return convertView;
	}

	
	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "itemAdapter---" + msg);
	}
	
	public List<Status> getStatusList(){
		return mStatusList;
	}

}
