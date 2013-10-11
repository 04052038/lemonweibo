package com.star.weibo.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.User;
import com.star.weibo4j.model.Source;
import com.star.weibo4j.model.Visible;

public class WeiboDBAdapter {
	
	private WeiboDBHelper weiboDBHelper;
	protected SQLiteDatabase weiboDB;
	private Context context;
	
	private String statusTable = "";
	private String retweetedStatusTable = "";
	private String statusUserTable = "";
	private String retweetedStatusUserTable = "";
	
	
	public WeiboDBAdapter(Context context)
	{
		this.context = context;
	}
	
	/**
	 * 初始化数据库连接
	 */
	public WeiboDBAdapter open()
	{
		weiboDBHelper = new WeiboDBHelper(this.context);
		weiboDB = weiboDBHelper.getWritableDatabase();
		return this;
	}
	
	/**
	 * 关闭连接
	 */
	public void close()
	{
		if(weiboDBHelper != null)
		{
			weiboDBHelper.close();
		}
	}
	
	public void insertStatusList(List<Status> statusList){
		for (int i=0; i < statusList.size(); i ++){
			insertStatus((Status)statusList.get(i));
		}
	}
	
	public void insertAtStatusList(List<Status> statusList){
		for (int i=0; i < statusList.size(); i ++){
			insertAtStatus((Status)statusList.get(i));
		}
	}
	
	public void insertStatus(Status status){
		insertStatus(status, WeiboDBHelper.STATUS_TYPE);
	}
	
	public void insertAtStatus(Status status){
		insertStatus(status, WeiboDBHelper.AT_STATUS_TYPE);
	}
	
	private void insertStatus(Status status, int statusType){
		if (statusType == WeiboDBHelper.STATUS_TYPE){
			statusTable = WeiboDBHelper.STATUS_TABLE;
			retweetedStatusTable = WeiboDBHelper.RETWEETEDSTATUS_TABLE;
			statusUserTable = WeiboDBHelper.STATUSUSER_TABLE;
			retweetedStatusUserTable = WeiboDBHelper.RETWEETEDSTATUSUSER_TABLE;
		}else if (statusType == WeiboDBHelper.AT_STATUS_TYPE){
			statusTable = WeiboDBHelper.AT_STATUS_TABLE;
			retweetedStatusTable = WeiboDBHelper.AT_RETWEETEDSTATUS_TABLE;
			statusUserTable = WeiboDBHelper.AT_STATUSUSER_TABLE;
			retweetedStatusUserTable = WeiboDBHelper.AT_RETWEETEDSTATUSUSER_TABLE;
		}
		insertStatusStatus(status);
		if (status.getRetweetedStatus() != null){
			insertRetweetedStatus(status, status.getRetweetedStatus());
		}
		
	}
	
	protected ContentValues getStatusValues(Status status){
		ContentValues values = new ContentValues();
		User user = status.getUser();
		if (user != null){
			values.put(StatusColumn.USERID, user.getId());
		}else{
			values.put(StatusColumn.USERID, "");
		}
		
		values.put(StatusColumn.CREATEDAT, status.getCreatedAtStr());
		values.put(StatusColumn.STATUSID, status.getId());
		values.put(StatusColumn.MID, status.getMid());
		values.put(StatusColumn.IDSTR, status.getIdstr());
		values.put(StatusColumn.TEXT, status.getText());
		Source source = status.getSource();
		if (source != null){
			values.put(StatusColumn.SOURCEURL, status.getSource().getUrl());
			values.put(StatusColumn.SOURCEREL, status.getSource().getRelationship());
			values.put(StatusColumn.SOURCENAME, status.getSource().getName());
		}
		
		if (status.isFavorited()){
			values.put(StatusColumn.FAVORITED, 1);
		}else{
			values.put(StatusColumn.FAVORITED, 0);
		}
		if (status.isTruncated()){
			values.put(StatusColumn.TRUNCATED, 1);
		}else{
			values.put(StatusColumn.TRUNCATED, 0);
		}
		values.put(StatusColumn.INREPLYTOSTATUSID, status.getInReplyToStatusId());
		values.put(StatusColumn.INREPLYTOUSERID, status.getInReplyToUserId());
		values.put(StatusColumn.INREPLYTOSCREENNAME, status.getInReplyToScreenName());
		values.put(StatusColumn.THUMBNAILPIC, status.getThumbnailPic());
		values.put(StatusColumn.BMIDDLEPIC, status.getBmiddlePic());
		values.put(StatusColumn.ORIGINALPIC, status.getOriginalPic());
		if (status.getRetweetedStatus() != null){
			values.put(StatusColumn.RETWEETEDSTATUSFLAG, 1);
			values.put(StatusColumn.RETWEETEDSTATUSID, status.getRetweetedStatus().getId());
		}else{
			values.put(StatusColumn.RETWEETEDSTATUSFLAG, 0);
			values.put(StatusColumn.RETWEETEDSTATUSID, "");
		}
		values.put(StatusColumn.GEO, status.getGeo());
		values.put(StatusColumn.LATITUDE, status.getLatitude());
		values.put(StatusColumn.LONGITUDE, status.getLongitude());
		values.put(StatusColumn.REPOSTSCOUNT, status.getRepostsCount());
		values.put(StatusColumn.COMMENTSCOUNT, status.getCommentsCount());
		values.put(StatusColumn.ANNOTATIONS, status.getAnnotations());
		values.put(StatusColumn.MLEVEL, status.getMlevel());
		Visible visible = status.getVisible();
		if (visible != null){
			values.put(StatusColumn.VISIBLETYPE, status.getVisible().getType());
			values.put(StatusColumn.VISIBLELISTID, status.getVisible().getList_id());
		}
		return values;
	}
	
	protected ContentValues getUserValues(User user){
		ContentValues values = new ContentValues();
		values.put(UserColumn.USERID, user.getId());
		values.put(UserColumn.SCREENNAME,user.getScreenName());
		values.put(UserColumn.NAME,user.getName());
		values.put(UserColumn.PROVINCE,user.getProvince());
		values.put(UserColumn.CITY,user.getCity());
		values.put(UserColumn.LOCATION,user.getLocation());
		values.put(UserColumn.DESCRIPTION,user.getDescription());
		values.put(UserColumn.URL,user.getUrl());
		values.put(UserColumn.PROFILEIMAGEURL,user.getProfileImageUrl());
		values.put(UserColumn.USERDOMAIN,user.getUserDomain());
		values.put(UserColumn.GENDER,user.getGender());
		values.put(UserColumn.FOLLOWERSCOUNT,user.getFollowersCount());
		values.put(UserColumn.FRIENDSCOUNT,user.getFriendsCount());
		values.put(UserColumn.STATUSESCOUNT,user.getStatusesCount());
		values.put(UserColumn.FAVOURITESCOUNT,user.getFavouritesCount());
		values.put(UserColumn.CREATEDAT,user.getCreatedAtStr());
		if (user.isFollowing()){
			values.put(UserColumn.FOLLOWING,1);
		}else{
			values.put(UserColumn.FOLLOWING,0);
		}
		if (user.isVerified()){
			values.put(UserColumn.VERIFIED, 1);
		}else{
			values.put(UserColumn.VERIFIED, 0);
		}
		values.put(UserColumn.VERIFIEDTYPE,user.getVerifiedType());
		if (user.isAllowAllActMsg()){
			values.put(UserColumn.ALLOWALLACTMSG, 1);
		}else{
			values.put(UserColumn.ALLOWALLACTMSG, 0);
		}
		if (user.isAllowAllComment()){
			values.put(UserColumn.ALLOWALLCOMMENT, 1);
		}else{
			values.put(UserColumn.ALLOWALLCOMMENT, 0);
		}
		if (user.isFollowMe()){
			values.put(UserColumn.FOLLOWME, 1);
		}else{
			values.put(UserColumn.FOLLOWME, 0);
		}
		values.put(UserColumn.AVATARLARGE,user.getAvatarLarge());
		values.put(UserColumn.ONLINESTATUS,user.getOnlineStatus());
		values.put(UserColumn.BIFOLLOWERSCOUNT,user.getBiFollowersCount());
		values.put(UserColumn.REMARK,user.getRemark());
		values.put(UserColumn.LANG,user.getLang());
		values.put(UserColumn.VERIFIEDREASON,user.getVerifiedReason());
		values.put(UserColumn.WEIHAO,user.getWeihao());
		return values;
	}
	
	private void insertStatusStatus(Status status){
		ContentValues values = getStatusValues(status);
		weiboDB.insert(statusTable, null, values);
		insertStatusUser(status);
	}
	
	private void insertRetweetedStatus(Status srcStatus, Status retweetedStatus){
		ContentValues values = getStatusValues(retweetedStatus);
		values.put(StatusColumn.STATUSID, srcStatus.getId());
		values.put(StatusColumn.RETWEETEDSTATUSID, retweetedStatus.getId());
		weiboDB.insert(retweetedStatusTable, null, values);
		insertRetweetedStatusUser(retweetedStatus);
	}
	
	private void insertStatusUser(Status status){
		User user = status.getUser();
		if (user != null){
			ContentValues values = getUserValues(user);
			values.put(UserColumn.STATUSID,status.getId());
			weiboDB.insert(statusUserTable, null, values);
		}
	}
	
	private void insertRetweetedStatusUser(Status retweetedStatus){
		User user = retweetedStatus.getUser();
		if (user != null){
			ContentValues values = getUserValues(user);
			values.put(UserColumn.STATUSID,retweetedStatus.getId());
			weiboDB.insert(retweetedStatusUserTable, null, values);
		}
	}
	
	public void deleteStatus(Status status){
		deleteStatus(status, WeiboDBHelper.STATUS_TYPE);
	}
	
	public void deleteAtStatus(Status status){
		deleteStatus(status, WeiboDBHelper.AT_STATUS_TYPE);
	}
	
	private void deleteStatus(Status status, int statusType){
		if (statusType == WeiboDBHelper.STATUS_TYPE){
			statusTable = WeiboDBHelper.STATUS_TABLE;
			retweetedStatusTable = WeiboDBHelper.RETWEETEDSTATUS_TABLE;
			statusUserTable = WeiboDBHelper.STATUSUSER_TABLE;
			retweetedStatusUserTable = WeiboDBHelper.RETWEETEDSTATUSUSER_TABLE;
		}else if (statusType == WeiboDBHelper.AT_STATUS_TYPE){
			statusTable = WeiboDBHelper.AT_STATUS_TABLE;
			retweetedStatusTable = WeiboDBHelper.AT_RETWEETEDSTATUS_TABLE;
			statusUserTable = WeiboDBHelper.AT_STATUSUSER_TABLE;
			retweetedStatusUserTable = WeiboDBHelper.AT_RETWEETEDSTATUSUSER_TABLE;
		}
		if (status.getRetweetedStatus() != null){
			deleteRetweetedStatus(status, status.getRetweetedStatus());
		}
		deleteStatusStatus(status);
		
	}
	
	private void deleteStatusStatus(Status status){
		if (status.getUser() != null){
			deleteStatusUser(status, status.getUser());
		}
		weiboDB.delete(statusTable, StatusColumn.STATUSID + "=?", new String[]{status.getId()});
	}
	
	private void deleteStatusUser(Status srcStatus, User user){
		weiboDB.delete(statusUserTable, UserColumn.STATUSID + "=? and " + UserColumn.USERID + "=?", 
				new String[]{srcStatus.getId(), user.getId()});
	}
	
	private void deleteRetweetedStatus(Status srcStatus, Status retweetedStatus){
		if (retweetedStatus.getUser() != null){
			deleteRetweetedStatusUser(retweetedStatus, retweetedStatus.getUser());
		}
		weiboDB.delete(retweetedStatusTable, StatusColumn.STATUSID + "=? and " + StatusColumn.RETWEETEDSTATUSID + "=?",
				new String[]{srcStatus.getId(), retweetedStatus.getId()});
	}
	
	private void deleteRetweetedStatusUser(Status retweetedStatus, User user){
		weiboDB.delete(retweetedStatusUserTable, UserColumn.STATUSID + "=? and " + UserColumn.USERID + "=?", 
				new String[]{retweetedStatus.getId(), user.getId()});
	}
	
	public void clearStatus(){
		clearStatus(WeiboDBHelper.STATUS_TYPE);
	}
	
	public void clearAtStatus(){
		clearStatus(WeiboDBHelper.AT_STATUS_TYPE);
	}
	
	private void clearStatus(int statusType){
		if (statusType == WeiboDBHelper.STATUS_TYPE){
			statusTable = WeiboDBHelper.STATUS_TABLE;
			retweetedStatusTable = WeiboDBHelper.RETWEETEDSTATUS_TABLE;
			statusUserTable = WeiboDBHelper.STATUSUSER_TABLE;
			retweetedStatusUserTable = WeiboDBHelper.RETWEETEDSTATUSUSER_TABLE;
		}else if (statusType == WeiboDBHelper.AT_STATUS_TYPE){
			statusTable = WeiboDBHelper.AT_STATUS_TABLE;
			retweetedStatusTable = WeiboDBHelper.AT_RETWEETEDSTATUS_TABLE;
			statusUserTable = WeiboDBHelper.AT_STATUSUSER_TABLE;
			retweetedStatusUserTable = WeiboDBHelper.AT_RETWEETEDSTATUSUSER_TABLE;
		}
		if (weiboDB != null){
			weiboDB.delete(retweetedStatusUserTable, null, null);
			weiboDB.delete(retweetedStatusTable, null, null);
			weiboDB.delete(statusUserTable, null, null);
			weiboDB.delete(statusTable, null, null);
		}
	}
	
	public List<Status> getAllStatus(){
		return getAllStatus(WeiboDBHelper.STATUS_TYPE);
	}
	
	public List<Status> getAllAtStatus(){
		return getAllStatus(WeiboDBHelper.AT_STATUS_TYPE);
	}
	
	private List<Status> getAllStatus(int statusType){
		if (statusType == WeiboDBHelper.STATUS_TYPE){
			statusTable = WeiboDBHelper.STATUS_TABLE;
			retweetedStatusTable = WeiboDBHelper.RETWEETEDSTATUS_TABLE;
			statusUserTable = WeiboDBHelper.STATUSUSER_TABLE;
			retweetedStatusUserTable = WeiboDBHelper.RETWEETEDSTATUSUSER_TABLE;
		}else if (statusType == WeiboDBHelper.AT_STATUS_TYPE){
			statusTable = WeiboDBHelper.AT_STATUS_TABLE;
			retweetedStatusTable = WeiboDBHelper.AT_RETWEETEDSTATUS_TABLE;
			statusUserTable = WeiboDBHelper.AT_STATUSUSER_TABLE;
			retweetedStatusUserTable = WeiboDBHelper.AT_RETWEETEDSTATUSUSER_TABLE;
		}
		Cursor cursor = weiboDB.query(statusTable, StatusColumn.PROJECTION, null, null, null, null, StatusColumn.STATUSID + " desc");
		ArrayList<Status> statusList = new ArrayList<Status>();
		if (cursor != null && cursor.getCount() > 0){
			Log("" + cursor.getCount());
			cursor.moveToFirst();
			while (!cursor.isAfterLast()){
				statusList.add(convertToStatus(cursor));
				cursor.moveToNext();
			}
		}
		if (cursor != null){
			cursor.close();
		}
		return statusList;
	}
	
	private Status convertToStatus(Cursor cursor){
		Status status = convertToStatusStatus(cursor);
		if (cursor.getInt(StatusColumn.RETWEETEDSTATUSFLAG_COL) == 1){
			status.setRetweetedStatus(convertToRetweetedStatus(status, cursor.getString(StatusColumn.RETWEETEDSTATUSID_COL)));
		}
		return status;
	}
	
	private Status convertToStatusStatus(Cursor cursor){
		Status status = convertToStatusBase(cursor);
		String userid = cursor.getString(StatusColumn.USERID_COL);
		if (userid != null && userid.trim().length() > 0){
			status.setUser(getStatusUser(status, userid));
		}
		return status;
	}
	
	protected Status convertToStatusBase(Cursor cursor){
			Status status = new Status();
			String createdAt = cursor.getString(StatusColumn.CREATEDAT_COL);
			if (createdAt != null && createdAt.trim().length() > 0){
				status.setCreatedAt(createdAt);
			}
			status.setId(cursor.getString(StatusColumn.ID_COL));
			status.setMid(cursor.getString(StatusColumn.MID_COL));
			status.setIdstr(cursor.getLong(StatusColumn.IDSTR_COL));
			status.setText(cursor.getString(StatusColumn.TEXT_COL));
			Source source = new Source();
			source.setUrl(cursor.getString(StatusColumn.SOURCEURL_COL));
			source.setRelationship(cursor.getString(StatusColumn.SOURCEREL_COL));
			source.setName(cursor.getString(StatusColumn.SOURCENAME_COL));
			status.setSource(source);
			int favorited = cursor.getInt(StatusColumn.FAVORITED_COL);
			if (favorited == 1){
				status.setFavorited(true);
			}else{
				status.setFavorited(false);
			}
			int truncated = cursor.getInt(StatusColumn.TRUNCATED_COL);
			if (truncated == 1){
				status.setTruncated(true);
			}else{
				status.setTruncated(false);
			}
			status.setInReplyToStatusId(cursor.getLong(StatusColumn.INREPLYTOSTATUSID_COL));
			status.setInReplyToUserId(cursor.getLong(StatusColumn.INREPLYTOUSERID_COL));
			status.setInReplyToScreenName(cursor.getString(StatusColumn.INREPLYTOSCREENNAME_COL));
			status.setThumbnailPic(cursor.getString(StatusColumn.THUMBNAILPIC_COL));
			status.setBmiddlePic(cursor.getString(StatusColumn.BMIDDLEPIC_COL));
			status.setOriginalPic(cursor.getString(StatusColumn.ORIGINALPIC_COL));
			status.setGeo(cursor.getString(StatusColumn.GEO_COL));
			status.setLatitude(cursor.getDouble(StatusColumn.LATITUDE_COL));
			status.setLongitude(cursor.getDouble(StatusColumn.LONGITUDE_COL));
			status.setRepostsCount(cursor.getInt(StatusColumn.REPOSTSCOUNT_COL));
			status.setCommentsCount(cursor.getInt(StatusColumn.COMMENTSCOUNT_COL));
			status.setAnnotations(cursor.getString(StatusColumn.ANNOTATIONS_COL));
			status.setMlevel(cursor.getInt(StatusColumn.MLEVEL_COL));
			Visible visible = new Visible();
			visible.setType(cursor.getInt(StatusColumn.VISIBLETYPE_COL));
			visible.setList_id(cursor.getInt(StatusColumn.VISIBLELISTID_COL));
			status.setVisible(visible);
			return status;
		}
	
	protected User convertToUser(Cursor cursor){
		User user = new User();
		user.setId(cursor.getString(UserColumn.USERID_COL));
		user.setScreenName(cursor.getString(UserColumn.SCREENNAME_COL));
		user.setName(cursor.getString(UserColumn.NAME_COL));
		user.setProvince(cursor.getInt(UserColumn.PROVINCE_COL));
		user.setCity(cursor.getInt(UserColumn.CITY_COL));
		user.setLocation(cursor.getString(UserColumn.LOCATION_COL));
		user.setDescription(cursor.getString(UserColumn.DESCRIPTION_COL));
		user.setUrl(cursor.getString(UserColumn.URL_COL));
		user.setProfileImageUrl(cursor.getString(UserColumn.PROFILEIMAGEURL_COL));
		user.setUserDomain(cursor.getString(UserColumn.USERDOMAIN_COL));
		user.setGender(cursor.getString(UserColumn.GENDER_COL));
		user.setFollowersCount(cursor.getInt(UserColumn.FOLLOWERSCOUNT_COL));
		user.setFriendsCount(cursor.getInt(UserColumn.FRIENDSCOUNT_COL));
		user.setStatusesCount(cursor.getInt(UserColumn.STATUSESCOUNT_COL));
		user.setFavouritesCount(cursor.getInt(UserColumn.FAVOURITESCOUNT_COL));
		String createdAt = cursor.getString(UserColumn.CREATEDAT_COL);
		if (createdAt != null && createdAt.trim().length() > 0){
			user.setCreatedAt(createdAt);
		}
		if (cursor.getInt(UserColumn.FOLLOWING_COL) == 1){
			user.setFollowing(true);
		}else{
			user.setFollowing(false);
		}
		if (cursor.getInt(UserColumn.VERIFIED_COL) == 1){
			user.setVerified(true);
		}else{
			user.setVerified(false);
		}
		user.setVerifiedType(cursor.getInt(UserColumn.VERIFIEDTYPE_COL));
		if (cursor.getInt(UserColumn.ALLOWALLACTMSG_COL) == 1){
			user.setAllowAllActMsg(true);
		}else{
			user.setAllowAllActMsg(false);
		}
		if (cursor.getInt(UserColumn.ALLOWALLCOMMENT_COL) == 1){
			user.setAllowAllComment(true);
		}else{
			user.setAllowAllComment(false);
		}
		if (cursor.getInt(UserColumn.FOLLOWME_COL) == 1){
			user.setFollowMe(true);
		}else{
			user.setFollowMe(false);
		}
		user.setAvatarLarge(cursor.getString(UserColumn.AVATARLARGE_COL));
		user.setOnlineStatus(cursor.getInt(UserColumn.ONLINESTATUS_COL));
		user.setBiFollowersCount(cursor.getInt(UserColumn.BIFOLLOWERSCOUNT_COL));
		user.setRemark(cursor.getString(UserColumn.REMARK_COL));
		user.setLang(cursor.getString(UserColumn.LANG_COL));
		user.setVerifiedReason(cursor.getString(UserColumn.VERIFIEDREASON_COL));
		user.setWeihao(cursor.getString(UserColumn.WEIHAO_COL));
		return user;
	}
	
	private User getStatusUser(Status status, String userid){
		Cursor cursor = weiboDB.query(statusUserTable, UserColumn.PROJECTION, 
				UserColumn.STATUSID + "=? and " + UserColumn.USERID + "=?", new String[]{ status.getId(), userid }, null, null, null);
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
	
	private User getRetweetedStatusUser(Status retweetedStatus, String userid){
		Cursor cursor = weiboDB.query(retweetedStatusUserTable, UserColumn.PROJECTION, 
				UserColumn.STATUSID + "=? and " + UserColumn.USERID + "=?", new String[]{retweetedStatus.getId(), userid}, null, null, null);
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
	
	private Status convertToRetweetedStatus(Status srcStatus, String retweetedStatusId){
		Cursor cursor = weiboDB.query(retweetedStatusTable, StatusColumn.PROJECTION, StatusColumn.STATUSID + "=? and " + StatusColumn.RETWEETEDSTATUSID + "=?", 
				new String[]{srcStatus.getId(), retweetedStatusId}, null, null, null);
		if (cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			Status status = convertToStatusBase(cursor);
			status.setId(retweetedStatusId);
			status.setRetweetedStatus(null);
			String userid = cursor.getString(StatusColumn.USERID_COL);
			if (userid != null && userid.trim().length() > 0){
				status.setUser(getRetweetedStatusUser(status, userid));
			}
			cursor.close();
			return status;
		}
		if (cursor != null){
			cursor.close();
		}
		return null;
	}
	
	public void updateStatusRefreshTime(long refreshTime){
		ContentValues values = new ContentValues();
		values.put(RefreshTimeColumn.REFRESH_TIME, refreshTime);
		weiboDB.update(WeiboDBHelper.REFRESHTIME_TABLE, values, RefreshTimeColumn.REFRESH_TYPE + "=?", new String[]{ "" + WeiboDBHelper.STATUS_TYPE });
	}
	
	public void updateAtStatusRefreshTime(long refreshTime){
		ContentValues values = new ContentValues();
		values.put(RefreshTimeColumn.REFRESH_TIME, refreshTime);
		weiboDB.update(WeiboDBHelper.REFRESHTIME_TABLE, values, RefreshTimeColumn.REFRESH_TYPE + "=?", new String[]{ "" + WeiboDBHelper.AT_STATUS_TYPE });
	}
	
	public void updateCommentRefreshTime(long refreshTime){
		ContentValues values = new ContentValues();
		values.put(RefreshTimeColumn.REFRESH_TIME, refreshTime);
		weiboDB.update(WeiboDBHelper.REFRESHTIME_TABLE, values, RefreshTimeColumn.REFRESH_TYPE + "=?", new String[]{ "" + WeiboDBHelper.COMMENT_TOME_TYPE });
	}
	
	public long getStatusRefreshTime(){
		return getRefreshTime(WeiboDBHelper.STATUS_TYPE);
	}
	
	public long getAtStatusRefreshTime(){
		return getRefreshTime(WeiboDBHelper.AT_STATUS_TYPE);
	}
	
	public long getCommentRefreshTime(){
		return getRefreshTime(WeiboDBHelper.COMMENT_TOME_TYPE);
	}
	
	private long getRefreshTime(int refreshTimeType){
		long refreshTime = 0;
		Cursor cursor = weiboDB.query(WeiboDBHelper.REFRESHTIME_TABLE, RefreshTimeColumn.PROJECTION, RefreshTimeColumn.REFRESH_TYPE + "=?", 
				new String[]{ "" + refreshTimeType }, null, null, null);
		if (cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			refreshTime = cursor.getLong(RefreshTimeColumn.REFRESH_TIME_COL);
		}
		if (cursor != null){
			cursor.close();
		}
		return refreshTime;
	}
	
	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "WeiboDBAdapter--" + msg);
	}
}
