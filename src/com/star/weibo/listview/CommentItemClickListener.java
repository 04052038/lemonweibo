package com.star.weibo.listview;

import com.star.weibo.adapter.BufferedCommentAdapter;
import com.star.weibo4j.model.Comment;
import com.star.weibo.Sina;
import com.star.yytv.ui.lsn.MyDialogWin;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class CommentItemClickListener implements OnItemClickListener {
	
	private Context mContext;
	private BufferedCommentAdapter commentAdapter;
	private Comment clickedComment;
    private MyDialogWin mydialog;
	
	public CommentItemClickListener(Context context, BufferedCommentAdapter commentAdapter){
		this.mContext = context;
		this.commentAdapter = commentAdapter;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		commentAdapter.setClickedCommentIndex(arg2 - 1);
		clickedComment = commentAdapter.getClickedComment();
		mydialog = new MyDialogWin(mContext);
		mydialog.show();
		mydialog.setMsgPopShow();
		mydialog.setPopText("微博功能");
		mydialog.setPopItem1Text("回复评论");
		mydialog.setPopItem2Text("查看原微博");
		mydialog.setPopItem1(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Sina.getInstance().commentCom(mContext, clickedComment.getId(), 
    					Long.parseLong(clickedComment.getStatus().getId()));
				mydialog.cancel();
			}
		});
		mydialog.setPopItem2(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Sina.getInstance().goToDetail(mContext, clickedComment.getStatus());
				mydialog.cancel();
			}
		});	
	}

}
