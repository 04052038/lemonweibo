package com.star.weibo.buf;

import java.util.ArrayList;
import java.util.List;

import com.star.weibo.db.CommentDBAdapter;
import com.star.weibo4j.model.Comment;

public abstract class CommentBuffer {
	
	//status buffer size
	private int bufSize;
	protected CommentDBAdapter commentDBAdapter;
	protected List<Comment> mCommentList = new ArrayList<Comment>();
	//after adding more status, showing location of listview
	private int selectedIndex = -1;
	private boolean isOverBuffer = false;
	
	public CommentBuffer(int bufSize, CommentDBAdapter commentDBAdapter){
		this.bufSize = bufSize;
		this.commentDBAdapter = commentDBAdapter;
	}
	public void setSelectedIndex(int selIndex){
		selectedIndex = selIndex;
	}
	
	public int getSelectedIndex(){
		return selectedIndex;
	}
	
	public CommentBuffer(int bufferSize){
		bufSize = bufferSize;
	}
	
	public List<Comment> getCommentList(){
		return mCommentList;
	}
	
	public boolean isOverBuffer(){
		return isOverBuffer;
	}
	
	public int commentSize(){
		int size = 0;
		if (mCommentList != null){
			size = mCommentList.size();
		}
		return size;
	}
	
	/**
	 * add items before statusList
	 * @param refreshStatus
	 */
	public void addItemsBefore(List<Comment> refreshComment){
		if (refreshComment != null && refreshComment.size() > 0){
			List<Comment> tempStatusList = new ArrayList<Comment>();
			tempStatusList.addAll(refreshComment);
			tempStatusList.addAll(mCommentList);
			mCommentList.clear();
			mCommentList.addAll(tempStatusList);
			addCommentListDB(refreshComment);
			if (mCommentList.size() > bufSize){
				isOverBuffer = true;
				for (int i = mCommentList.size(); i > bufSize; i --){
					delCommentDB((Comment)mCommentList.get(i-1));
					mCommentList.remove(i-1);
				}
			}
		}
		
	}
	
	/**
	 * add items last
	 * @param moreStatus
	 */
	public void addItemsLast(List<Comment> moreComment){
		if (moreComment != null && moreComment.size() > 0){
			mCommentList.addAll(moreComment);
			addCommentListDB(moreComment);
			if (mCommentList.size() > bufSize){
				isOverBuffer = true;
			}
		}
	}
	
	/*
	 * clear buffer
	 */
	public void clear(){
		clearCommentListDB();
		mCommentList.clear();
		isOverBuffer = false;
	}
	
	public void initializeCommentBuffer(){
		commentDBAdapter.open();
		mCommentList = queryCommentListDB();
	}
	
	public void delItem(int commentIndex){
		Comment comment = mCommentList.get(commentIndex);
		if (comment != null){
			delCommentDB(comment);
			mCommentList.remove(commentIndex);
		}
	}
	
	
	public abstract void delCommentDB(Comment comment);
	
	public abstract void addCommentListDB(List<Comment> commentList);
	
	public abstract void clearCommentListDB();
	
	public abstract List<Comment> queryCommentListDB();
	
	public abstract void setRefreshTimeDB(long refreshTime);
	
	public abstract long getRefreshTimeDB();

}
