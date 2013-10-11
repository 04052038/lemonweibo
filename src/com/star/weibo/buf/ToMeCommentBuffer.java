package com.star.weibo.buf;

import java.util.List;

import com.star.weibo.db.CommentDBAdapter;
import com.star.weibo4j.model.Comment;

public class ToMeCommentBuffer extends CommentBuffer {
	
	public ToMeCommentBuffer(int bufSize, CommentDBAdapter commentDBAdapter){
		super(bufSize, commentDBAdapter);
	}

	@Override
	public void addCommentListDB(List<Comment> commentList) {
		// TODO Auto-generated method stub
		commentDBAdapter.insertCommentList(commentList);
	}

	@Override
	public void clearCommentListDB() {
		// TODO Auto-generated method stub
		commentDBAdapter.clearComment();
	}

	@Override
	public void delCommentDB(Comment comment) {
		// TODO Auto-generated method stub
		commentDBAdapter.deleteComment(comment);
	}

	@Override
	public long getRefreshTimeDB() {
		// TODO Auto-generated method stub
		return commentDBAdapter.getCommentRefreshTime();
	}

	@Override
	public List<Comment> queryCommentListDB() {
		// TODO Auto-generated method stub
		return commentDBAdapter.getAllComment();
	}

	@Override
	public void setRefreshTimeDB(long refreshTime) {
		// TODO Auto-generated method stub
		commentDBAdapter.updateCommentRefreshTime(refreshTime);
	}

}
