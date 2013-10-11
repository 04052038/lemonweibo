package com.star.weibo.adapter;

import java.util.List;

import android.content.Context;
import com.star.yytv.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.star.weibo.AsyncImageLoader;
import com.star.weibo.util.TextUtil;
import com.star.weibo.util.TimeUtil;
import com.star.weibo4j.model.Comment;
import com.star.yytv.R;

public class CommentAdapter extends BaseAdapter {
	private Context mContext;
	protected List<Comment> mCommentList;
	private String weiboType;
		
	public CommentAdapter(Context context, List<Comment> comments) {
		mContext = context;
		mCommentList = comments;
	}
	
	public List<Comment> getCommentList(){
		return mCommentList;
	}
	
	public String getWeiboType() {
		return weiboType;
	}

	public void setWeiboType(String weiboType) {
		this.weiboType = weiboType;
	}

	@Override
	public int getCount() {
		return mCommentList == null ? 0 : mCommentList.size();
	}

	@Override
	public Object getItem(int position) {
		return mCommentList.get(position);
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
			item.icon = (ImageView) convertView
					.findViewById(R.id.weibo_item_icon);
			item.v = (ImageView) convertView.findViewById(R.id.weibo_item_v);
			item.name = (TextView) convertView
					.findViewById(R.id.weibo_item_name);
			item.pic = (ImageView) convertView
					.findViewById(R.id.weibo_item_pic);
			item.pic.setVisibility(View.GONE);
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
			item.redirectPic.setVisibility(View.GONE);
			item.redirectNum = (TextView) convertView
					.findViewById(R.id.weibo_item_redirectNum);
			item.redirectNum.setVisibility(View.GONE);
			item.commentPic = (ImageView) convertView
					.findViewById(R.id.weibo_item_commentPic);
			item.commentPic.setVisibility(View.GONE);
			item.commentNum = (TextView) convertView
					.findViewById(R.id.weibo_item_commentNum);
			item.commentNum.setVisibility(View.GONE);
			convertView.setTag(item);
		} else {
			item = (WeiboItem) convertView.getTag();
		}
		Comment comment = mCommentList.get(position);
		item.name.setText(comment.getUser().getScreenName());
		if (weiboType != null){
			AsyncImageLoader.getInstance().loadPortrait(Long.parseLong(comment.getUser().getId()),
					comment.getUser().getProfileImageUrl(), item.icon, weiboType);
		} else {
			AsyncImageLoader.getInstance().loadPortrait(Long.parseLong(comment.getUser().getId()),
					comment.getUser().getProfileImageUrl(), item.icon);
		}
		item.createTime.setText(TimeUtil.getTimeStr(comment
				.getCreatedAt()));
		item.content.setText(TextUtil.formatContent(comment.getText(), mContext));
		if(comment.getSource() != null)
		{
			item.source.setText(mContext.getString(R.string.from)
					+ comment.getSource().getName());
		}else{
			//xujun 20121024:当source为空时，需要置为空，否则会显示上一条评论的source，因为convertView会复用
			item.source.setText(mContext.getString(R.string.from));
		}
		
		if (comment.getReplycomment() != null) {
			item.sub.setVisibility(View.VISIBLE);
			item.subContent.setText(TextUtil.formatContent(comment.getReplycomment().getText(), mContext));
		}else{
			//xujun 20121024:当Replycomment为空时，需要隐藏，否则会显示上一条评论的Replycomment，因为convertView会复用
			item.sub.setVisibility(View.GONE);
		}

		return convertView;
	}

	void Log(String msg) {
		Log.i("weibo", "CommentAdapter--" + msg);
	}

}
