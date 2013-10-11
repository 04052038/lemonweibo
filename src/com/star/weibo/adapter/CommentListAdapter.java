package com.star.weibo.adapter;

import java.util.List;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.star.weibo.util.TextUtil;
import com.star.weibo.util.TimeUtil;
import com.star.weibo4j.model.Comment;
import com.star.yytv.R;

public class CommentListAdapter extends BaseAdapter {
	private Context context;
	private List<Comment> comments;

	public CommentListAdapter(Context context, List<Comment> comments) {
		this.context = context;
		this.comments = comments;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comments == null ? 0 : comments.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CommentItem item;
		if (convertView == null) {
			item = new CommentItem();
			convertView = View.inflate(context, R.layout.comment_list_item, null);
			item.nick = (TextView) convertView.findViewById(R.id.commentListItem_nick);
			item.createTime = (TextView) convertView.findViewById(R.id.commentListItem_createTime);
			item.content = (TextView) convertView.findViewById(R.id.commentListItem_content);
			item.content.setMovementMethod(LinkMovementMethod.getInstance());
			convertView.setTag(item);
		}else {
			item = (CommentItem) convertView.getTag();
		}
		Comment comment=comments.get(position);
		item.nick.setText(comment.getUser().getScreenName());
		item.createTime.setText(TimeUtil.getTimeStr(comment.getCreatedAt()));
		item.content.setText(TextUtil.formatContent(comment.getText(), context));
		return convertView;
	}

}
