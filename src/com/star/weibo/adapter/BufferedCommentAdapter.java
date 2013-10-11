package com.star.weibo.adapter;

import java.util.List;

import android.content.Context;

import com.star.weibo.buf.CommentBuffer;
import com.star.weibo4j.model.Comment;

public class BufferedCommentAdapter extends CommentAdapter {
	
	//after adding more status, showing location of listview
	private int selectedIndex = -1;
	private CommentBuffer commentBuffer;
	private long refreshTime;
	//index of status that is showing in WeiboDetail activity, used for weibo delete
	private int clickedCommentIndex;
	private int requestCode = -1;
	
	public BufferedCommentAdapter(Context context, List<Comment> commentList, CommentBuffer commentBuf){
		super(context, commentList);
		this.commentBuffer = commentBuf;
	}
	
	public void setSelectedIndex(int selIndex){
		selectedIndex = selIndex;
	}
	
	public int getSelectedIndex(){
		return selectedIndex;
	}
	
	public void setRequestCode(int requestCode){
		this.requestCode = requestCode;
	}
	
	public int getRequestCode(){
		return requestCode;
	}
	
	public void addItemsBefore(List<Comment> refreshComment){
		commentBuffer.addItemsBefore(refreshComment);
	}
	
	public void addItemsLast(List<Comment> moreComment){
		commentBuffer.addItemsLast(moreComment);
	}
	
	public boolean isOverBuffer(){
		return commentBuffer.isOverBuffer();
	}
	
	public void clear(){
		mCommentList.clear();
	}
	
	public void syncCommentBuffer(){
		clear();
		mCommentList.addAll(commentBuffer.getCommentList());
	}
	
	public void clearDB(){
		commentBuffer.clear();
	}
	
	public int commentSize(){
		if (mCommentList == null){
			return 0;
		}else{
			return mCommentList.size();
		}
	}
	
	public int commentBufferSize(){
		if (commentBuffer != null){
			return commentBuffer.commentSize();
		}else{
			return 0;
		}
	}
	
	public Comment getSelectedComment(){
		return mCommentList.get(selectedIndex);
	}
	
	public long getRefreshTime(){
		return refreshTime;
	}
	
	public void setRefreshTime(long refreshTime){
		this.refreshTime = refreshTime;
		commentBuffer.setRefreshTimeDB(refreshTime);
	}
	
	public void loadStatusDB(){
		commentBuffer.initializeCommentBuffer();
		refreshTime = commentBuffer.getRefreshTimeDB();
	}
	
	public int getClickedCommentIndex(){
		return clickedCommentIndex;
	}
	
	public void setClickedCommentIndex(int clickedCommentIndex){
		this.clickedCommentIndex = clickedCommentIndex;
	}
	
	public Comment getClickedComment(){
		return mCommentList.get(clickedCommentIndex);
	}
	
	public void removeClickedComment(){
		commentBuffer.delItem(clickedCommentIndex);
	}

}
