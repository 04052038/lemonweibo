package com.star.weibo.db;

import java.util.ArrayList;
import java.util.List;

import com.star.weibo4j.model.Comment;
import com.star.weibo4j.model.Source;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CommentDBAdapter extends WeiboDBAdapter {
	
	public CommentDBAdapter(Context context){
		super(context);
	}
	
	public void insertComment(Comment comment){
		insertCommentComment(comment);
		Comment replyComment = comment.getReplycomment();
		if (replyComment != null){
			insertReplyComment(comment, replyComment);
		}
		Status status = comment.getStatus();
		if (status != null){
			insertCommentStatus(comment, status);
		}
	}
	
	private ContentValues getCommentValues(Comment comment){
		ContentValues values = new ContentValues();
		values.put(CommentColumn.CREATEDAT, comment.getCreatedAtStr());
		values.put(CommentColumn.COMMENTID, comment.getId());
		values.put(CommentColumn.MID, comment.getMid());
		values.put(CommentColumn.IDSTR, comment.getIdstr());
		values.put(CommentColumn.TEXT, comment.getText());
		Source source = comment.getSource();
		if (source != null){
			values.put(CommentColumn.SOURCEURL, source.getUrl());
			values.put(CommentColumn.SOURCEREL, source.getRelationship());
			values.put(CommentColumn.SOURCENAME, source.getName());
		}
		Comment replyComment = comment.getReplycomment();
		if (replyComment != null){
			values.put(CommentColumn.REPLYCOMMENTID, replyComment.getId());
		}else{
			values.put(CommentColumn.REPLYCOMMENTID, 0);
		}
		User user = comment.getUser();
		if (user != null){
			values.put(CommentColumn.USERID, user.getId());
		}
		Status status = comment.getStatus();
		if (status != null){
			values.put(CommentColumn.STATUSID, status.getId());
		}
		return values;
	}
	
	private void insertCommentComment(Comment comment){
		ContentValues values = getCommentValues(comment);
		weiboDB.insert(CommentDBHelper.COMMENT_TABLE, null, values);
		if (comment.getUser() != null){
			insertCommentUser(comment);
		}
	}
	
	private void insertCommentUser(Comment comment){
		ContentValues values = getUserValues(comment.getUser());
		values.put(UserColumn.COMMENTID, comment.getId());
		weiboDB.insert(CommentDBHelper.COMMENT_USER_TABLE, null, values);
	}
	
	private void insertReplyComment(Comment comment, Comment replyComment){
		ContentValues values = getCommentValues(replyComment);
		values.put(CommentColumn.COMMENTID, comment.getId());
		values.put(CommentColumn.REPLYCOMMENTID, replyComment.getId());
		weiboDB.insert(CommentDBHelper.REPLYCOMMENT_TABLE, null, values);
		if (replyComment.getUser() != null){
			insertReplyCommentUser(comment, replyComment);
		}
	}
	
	private void insertReplyCommentUser(Comment comment, Comment replyComment){
		ContentValues values = getUserValues(replyComment.getUser());
		values.put(UserColumn.COMMENTID, comment.getId());
		values.put(UserColumn.REPLYCOMMENTID, replyComment.getId());
		weiboDB.insert(CommentDBHelper.REPLYCOMMENT_USER_TABLE, null, values);
	}
	
	private void insertCommentStatus(Comment comment, Status status){
		insertCommentStatusStatus(comment, status);
		if (status.getRetweetedStatus() != null){
			insertCommentRetweetedStatusStatus(comment, status);
		}
	}
	
	private void insertCommentStatusStatus(Comment comment, Status status){
		ContentValues values = getStatusValues(status);
		values.put(StatusColumn.COMMENTID, comment.getId());
		weiboDB.insert(CommentDBHelper.COMMENT_STATUS_TABLE, null, values);
		if (status.getUser() != null){
			insertCommentStatusUser(comment, status);
		}
	}
	
	private void insertCommentStatusUser(Comment comment, Status status){
		ContentValues values = getUserValues(status.getUser());
		values.put(UserColumn.COMMENTID, comment.getId());
		values.put(UserColumn.STATUSID, status.getId());
		weiboDB.insert(CommentDBHelper.COMMENT_STATUSUSER_TABLE, null, values);
	}
	
	private void insertCommentRetweetedStatusStatus(Comment comment, Status status){
		ContentValues values = getStatusValues(status.getRetweetedStatus());
		values.put(StatusColumn.COMMENTID, comment.getId());
		values.put(StatusColumn.STATUSID, status.getId());
		values.put(StatusColumn.RETWEETEDSTATUSID, status.getRetweetedStatus().getId());
		weiboDB.insert(CommentDBHelper.COMMENT_RETWEETEDSTATUS_TABLE, null, values);
		if (status.getRetweetedStatus().getUser() != null){
			insertCommentRetweetedStatusUser(comment, status);
		}
	}
	
	private void insertCommentRetweetedStatusUser(Comment comment, Status status){
		Status retweetedStatus = status.getRetweetedStatus();
		ContentValues values = getUserValues(retweetedStatus.getUser());
		values.put(UserColumn.COMMENTID, comment.getId());
		values.put(UserColumn.STATUSID, status.getId());
		values.put(UserColumn.RETWEETEDSTATUSID, retweetedStatus.getId());
		weiboDB.insert(CommentDBHelper.COMMENT_RETWEETEDSTATUSUSER_TABLE, null, values);
	}
	
	private Comment convertToCommentBase(Cursor cursor){
		Comment comment = new Comment();
		comment.setCreatedAtStr(cursor.getString(CommentColumn.CREATEDAT_COL));
		comment.setId(cursor.getLong(CommentColumn.COMMENTID_COL));
		comment.setMid(cursor.getString(CommentColumn.MID_COL));
		comment.setIdstr(cursor.getString(CommentColumn.IDSTR_COL));
		comment.setText(cursor.getString(CommentColumn.TEXT_COL));
		Source source = new Source();
		source.setUrl(cursor.getString(CommentColumn.SOURCEURL_COL));
		source.setRelationship(cursor.getString(CommentColumn.SOURCEREL_COL));
		source.setName(cursor.getString(CommentColumn.SOURCENAME_COL));
		comment.setSource(source);
		return comment;
	}
	
	private Comment convertToComment(Cursor cursor){
		Comment comment = convertToCommentBase(cursor);
		String userId = cursor.getString(CommentColumn.USERID_COL);
		if (userId != null){
			comment.setUser(convertToCommentUser(comment, userId));
		}
		long replyCommentId = cursor.getLong(CommentColumn.REPLYCOMMENTID_COL);
		if (replyCommentId > 0){
			comment.setReplycomment(convertToReplyComment(comment, replyCommentId));
		}
		String statusId = cursor.getString(CommentColumn.STATUSID_COL);
		if (statusId != null){
			comment.setStatus(convertToCommentStatus(comment, statusId));
		}
		return comment;
	}
	
	private User convertToCommentUser(Comment comment, String userId){
		Cursor cursor = weiboDB.query(CommentDBHelper.COMMENT_USER_TABLE, UserColumn.PROJECTION, UserColumn.COMMENTID + "=? and " + UserColumn.USERID + "=?", 
				new String[]{""+comment.getId(), userId}, null, null, null);
		User user = new User();
		if (cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			user = convertToUser(cursor);	
		}
		if (cursor != null){
			cursor.close();
		}
		return user;
	}
	
	private Comment convertToReplyComment(Comment comment, long replyCommentId){
		Cursor cursor = weiboDB.query(CommentDBHelper.REPLYCOMMENT_TABLE, CommentColumn.PROJECTION, CommentColumn.COMMENTID + "=? and " + CommentColumn.REPLYCOMMENTID + "=?", 
				new String[]{"" + comment.getId(), "" + replyCommentId}, null, null, null);
		Comment replyComment = new Comment(); 
		if (cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			replyComment = convertToCommentBase(cursor);
			replyComment.setId(replyCommentId);
			String userId = cursor.getString(CommentColumn.USERID_COL);
			if (userId != null){
				replyComment.setUser(convertToReplyCommentUser(comment, replyCommentId, userId));
			}
		}
		if (cursor != null){
			cursor.close();
		}
		return replyComment;
	}
	
	private User convertToReplyCommentUser(Comment comment, long replyCommentId, String userId){
		Cursor cursor = weiboDB.query(CommentDBHelper.REPLYCOMMENT_USER_TABLE, UserColumn.PROJECTION, UserColumn.COMMENTID + "=? and " + UserColumn.REPLYCOMMENTID + "=? and " + UserColumn.USERID + "=?", 
				new String[]{""+comment.getId(), "" + replyCommentId, userId}, null, null, null);
		User user = new User();
		if (cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			user = convertToUser(cursor);	
		}
		if (cursor != null){
			cursor.close();
		}
		return user;
	}
	
	private Status convertToCommentStatus(Comment comment, String statusId){
		Cursor cursor = weiboDB.query(CommentDBHelper.COMMENT_STATUS_TABLE, StatusColumn.PROJECTION, StatusColumn.COMMENTID + "=? and " + StatusColumn.STATUSID + "=?", 
				new String[]{""+comment.getId(), statusId}, null, null, null);
		Status status = new Status();
		if (cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			status = convertToStatusBase(cursor);
			String userId = cursor.getString(StatusColumn.USERID_COL);
			if (userId != null){
				status.setUser(convertToCommentStatusUser(comment, statusId, userId));
			}
			int retweetedStatusFlag = cursor.getInt(StatusColumn.RETWEETEDSTATUSFLAG_COL);
			if (retweetedStatusFlag == 1){
				String retweetedStatusId = cursor.getString(StatusColumn.RETWEETEDSTATUSID_COL);
				status.setRetweetedStatus(convertToCommentRetweetedStatus(comment, statusId, retweetedStatusId));
			}	
		}
		if (cursor != null){
			cursor.close();
		}
		return status;
	}
	
	private User convertToCommentStatusUser(Comment comment, String statusId, String userId){
		Cursor cursor = weiboDB.query(CommentDBHelper.COMMENT_STATUSUSER_TABLE, UserColumn.PROJECTION, UserColumn.COMMENTID + "=? and " + UserColumn.STATUSID + "=? and " + UserColumn.USERID + "=?", 
				new String[]{""+comment.getId(), statusId, userId}, null, null, null);
		User user = new User();
		if (cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			user = convertToUser(cursor);	
		}
		if (cursor != null){
			cursor.close();
		}
		return user;
	}
	
	private Status convertToCommentRetweetedStatus(Comment comment, String statusId, String retweetedStatusId){
		Cursor cursor = weiboDB.query(CommentDBHelper.COMMENT_RETWEETEDSTATUS_TABLE, StatusColumn.PROJECTION, StatusColumn.COMMENTID + "=? and " + StatusColumn.STATUSID + "=? and " + StatusColumn.RETWEETEDSTATUSID + "=?", 
				new String[]{""+comment.getId(), statusId, retweetedStatusId}, null, null, null);
		Status retweetedStatus = new Status();
		if (cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			retweetedStatus = convertToStatusBase(cursor);
			retweetedStatus.setId(retweetedStatusId);
			String userId = cursor.getString(StatusColumn.USERID_COL);
			if (userId != null){
				retweetedStatus.setUser(convertToCommentRetweetedStatusUser(comment, statusId, retweetedStatusId, userId));
			}
		}
		if (cursor != null){
			cursor.close();
		}
		return retweetedStatus;
	}
	
	private User convertToCommentRetweetedStatusUser(Comment comment, String statusId, String retweetedStatusId, String userId){
		Cursor cursor = weiboDB.query(CommentDBHelper.COMMENT_RETWEETEDSTATUSUSER_TABLE, UserColumn.PROJECTION, UserColumn.COMMENTID + "=? and " + UserColumn.STATUSID + "=? and " + UserColumn.RETWEETEDSTATUSID + "=? and " + UserColumn.USERID + "=?", 
				new String[]{""+comment.getId(), statusId, retweetedStatusId, userId}, null, null, null);
		User user = new User();
		if (cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			user = convertToUser(cursor);	
		}
		if (cursor != null){
			cursor.close();
		}
		return user;
	}
	
	public List<Comment> getAllComment(){
		Cursor cursor = weiboDB.query(CommentDBHelper.COMMENT_TABLE, CommentColumn.PROJECTION, null, null, null, null, CommentColumn.COMMENTID + " desc");
		ArrayList<Comment> commentList = new ArrayList<Comment>();
		if (cursor != null && cursor.getCount() > 0){
			Log("" + cursor.getCount());
			cursor.moveToFirst();
			while (!cursor.isAfterLast()){
				commentList.add(convertToComment(cursor));
				cursor.moveToNext();
			}
		}
		if (cursor != null){
			cursor.close();
		}
		return commentList;
	}
	
	public void insertCommentList(List<Comment> commentList){
		for (int i=0; i < commentList.size(); i ++){
			insertComment((Comment)commentList.get(i));
		}
	}
	
	public void deleteComment(Comment comment){
		User user = comment.getUser();
		if (user != null){
			deleteCommentUser(comment, user.getId());
		}
		Comment replyComment = comment.getReplycomment();
		if (replyComment != null){
			deleteReplyComment(comment, replyComment);
		}
		Status status = comment.getStatus();
		if (status != null){
			deleteCommentStatus(comment, status);
		}
	}
	
	private void deleteCommentUser(Comment comment, String userId){
		weiboDB.delete(CommentDBHelper.COMMENT_USER_TABLE, UserColumn.COMMENTID + "=? and " + UserColumn.USERID + "=?", new String[]{"" + comment.getId(), userId});
	}
	
	private void deleteReplyComment(Comment comment, Comment replyComment){
		User user = replyComment.getUser();
		if (user != null){
			deleteReplyCommentUser(comment, replyComment.getId(), user.getId());
		}
		weiboDB.delete(CommentDBHelper.REPLYCOMMENT_TABLE, CommentColumn.COMMENTID + "=? and " + CommentColumn.REPLYCOMMENTID + "=?", 
				new String[]{"" + comment.getId(), "" + replyComment.getId()});
	}
	
	private void deleteReplyCommentUser(Comment comment, long replyCommentId, String userId){
		weiboDB.delete(CommentDBHelper.REPLYCOMMENT_USER_TABLE, UserColumn.COMMENTID + "=? and " + UserColumn.REPLYCOMMENTID + "=? and " + UserColumn.USERID + "=?", new String[]{"" + comment.getId(), "" + replyCommentId, userId});
	}
	
	private void deleteCommentStatus(Comment comment, Status status){
		User user = status.getUser();
		if (user != null){
			deleteCommentStatusUser(comment, status, user.getId());
		}
		Status retweetedStatus = status.getRetweetedStatus();
		if (retweetedStatus != null){
			deleteCommentRetweetedStatus(comment, status, retweetedStatus);
		}
		weiboDB.delete(CommentDBHelper.COMMENT_STATUS_TABLE, StatusColumn.COMMENTID + "=? and " + StatusColumn.STATUSID + "=?", 
				new String[]{"" + comment.getId(), status.getId()});
	}
	
	private void deleteCommentStatusUser(Comment comment, Status status, String userId){
		weiboDB.delete(CommentDBHelper.COMMENT_STATUSUSER_TABLE, UserColumn.COMMENTID + "=? and " + UserColumn.STATUSID + "=? and " + UserColumn.USERID + "=?", 
				new String[]{"" + comment.getId(), status.getId(), userId});
	}
	
	private void deleteCommentRetweetedStatus(Comment comment, Status status, Status retweetedStatus){
		User user = retweetedStatus.getUser();
		if (user != null){
			deleteCommentRetweetedStatusUser(comment, status, retweetedStatus, user.getId());
		}
		weiboDB.delete(CommentDBHelper.COMMENT_RETWEETEDSTATUS_TABLE, StatusColumn.COMMENTID + "=? and " + StatusColumn.STATUSID + "=? and " + StatusColumn.RETWEETEDSTATUSID + "=?", 
				new String[]{"" + comment.getId(), status.getId(), retweetedStatus.getId()});
	}
	
	private void deleteCommentRetweetedStatusUser(Comment comment, Status status, Status retweetedStatus, String userId){
		weiboDB.delete(CommentDBHelper.COMMENT_RETWEETEDSTATUSUSER_TABLE, UserColumn.COMMENTID + "=? and " + UserColumn.STATUSID + "=? and " + UserColumn.RETWEETEDSTATUSID + "=? and " + UserColumn.USERID + "=?", 
				new String[]{"" + comment.getId(), status.getId(), retweetedStatus.getId(), userId});
	}
	
	public void clearComment(){
		if (weiboDB != null){
			weiboDB.delete(CommentDBHelper.REPLYCOMMENT_USER_TABLE, null, null);
			weiboDB.delete(CommentDBHelper.REPLYCOMMENT_TABLE, null, null);
			weiboDB.delete(CommentDBHelper.COMMENT_RETWEETEDSTATUSUSER_TABLE, null, null);
			weiboDB.delete(CommentDBHelper.COMMENT_RETWEETEDSTATUS_TABLE, null, null);
			weiboDB.delete(CommentDBHelper.COMMENT_STATUSUSER_TABLE, null, null);
			weiboDB.delete(CommentDBHelper.COMMENT_STATUS_TABLE, null, null);
			weiboDB.delete(CommentDBHelper.COMMENT_USER_TABLE, null, null);
			weiboDB.delete(CommentDBHelper.COMMENT_TABLE, null, null);
		}
	}
	

}
