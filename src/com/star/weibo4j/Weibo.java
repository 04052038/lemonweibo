package com.star.weibo4j;

import java.util.ArrayList;
import java.util.List;
import com.star.weibo4j.http.NewHttpClient;
import com.star.weibo4j.http.ImageItem;
import com.star.weibo4j.model.Comment;
import com.star.weibo4j.model.Favorites;
import com.star.weibo4j.model.Paging;
import com.star.weibo4j.model.PostParameter;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.Trends;
import com.star.weibo4j.model.User;
import com.star.weibo4j.model.UserFriendship;
import com.star.weibo4j.model.UserTrend;
import com.star.weibo4j.model.WeiboException;
import com.star.weibo4j.util.WeiboConfig;


/**
 * @author sinaWeibo
 * 
 */

public class Weibo implements java.io.Serializable {

	private static final long serialVersionUID = 4282616848978535016L;

	public static NewHttpClient client = new NewHttpClient();

	public Weibo()
	{
		
	}
	/**
	 * Sets token information
	 * 
	 * @param token
	 */
	public synchronized void setToken(String token) {
		client.setToken(token);
	}

    //----------------------------微博搜索 API------------------------------------
    /**
     * 搜索微博用户 (仅对新浪合作开发者开放)
     * @param query
     * @return a list of User
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Users/search">users/search </a>
     */
//	public List<User> searchUser(Query query) throws WeiboException {
//		return User.constructUsers(get(searchBaseURL + "users/search.json",
//				query.getParameters(), false));
//	}
    /**
     * 搜索微博文章 (仅对新浪合作开发者开放)
     * @param query
     * @return
     * @throws WeiboException
     */
    
//    public List<SearchResult> search(Query query) throws WeiboException {
//        return SearchResult.constructResults(get(searchBaseURL + "search.json", query.getParameters(), true));
//    }
    /**
     * 搜索微博(多条件组合) (仅对合作开发者开放)
     * @param query
     * @return a list of statuses
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/search">Statuses/search
     */
//    public List<Status> statussearch(Query query) throws WeiboException{
//    	return Status.constructStatuses(get(searchBaseURL + "statuses/search.json", query.getParameters(), true));
//    }
  
    //----------------------------收藏接口----------------------------------------
 
    /**
     * 获取当前用户的收藏列表
     * @param page the number of page
     * @return List<Status>
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites">favorites </a>
     * @since Weibo4J 1.2.0
     */
    public List<Status> getFavorites(int page) throws WeiboException {
    	
    	List<Favorites> favorites = new Favorite().getFavorites(new Paging(page));
    	ArrayList<Status> retStatus = new ArrayList<Status>();
    	for(int i=0; i < favorites.size(); i++)
    	{
    		retStatus.add(favorites.get(i).getStatus());
    	}
    	return retStatus;
    	
    }
    


    /**
     * 收藏一条微博消息
     * @param id the ID of the status to favorite
     * @return Status
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites/create">favorites/create </a>
     */
    public Favorites createFavorite(long id) throws WeiboException {
    	return new Favorite().createFavorites(String.valueOf(id));
    }

    /**
     * 删除微博收藏.注意：只能删除自己收藏的信息
     * @param id the ID of the status to un-favorite
     * @return Status
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites/destroy">favorites/destroy </a>
     */
    public Favorites destroyFavorite(long id) throws WeiboException {
    	return new Favorite().destroyFavorites(String.valueOf(id));
    }

    /**
     * 批量删除微博收藏
     * @param ids 要删除的一组已收藏的微博消息ID，用半角逗号隔开，一次提交最多提供20个ID
     * @return Status
     * @throws WeiboException
     * @Ricky
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites/destroy_batch">favorites/destroy_batch</a>
     * @since Weibo4J 1.2.0
     */
    public boolean destroyFavorites(String ids)throws WeiboException{
    	return new Favorite().destroyFavoritesBatch(ids);
    }
    
    
    /**
	 * Sends a new direct message to the specified user from the authenticating
	 * user. Requires both the user and text parameters below. The text will be
	 * trimed if the length of the text is exceeding 140 characters. <br>
	 * This method calls http://api.t.sina.com.cn/direct_messages/new.format
	 * 
	 * @param id
	 *            the ID or screen name of the user to whom send the direct
	 *            message
	 * @param text
	 *            String
	 * @return DirectMessage
	 * @throws WeiboException
	 *             when Weibo service or network is unavailable
	 * @see <a
	 *      href="http://open.t.sina.com.cn/wiki/index.php/Direct_messages/new">发�?私信
	 *      </a>
	 */
	public DirectMessage sendDirectMessage(String id, String text) throws WeiboException
	{		
		return new DirectMessages().createDirectMessage(id, text);
	}

//    public List<Status> destroyFavorites(String...ids)throws WeiboException{
//    	 StringBuilder sb = new StringBuilder();
//	  	 for(String id : ids) {
//	  		 sb.append(id).append(',');
//	  	 }
//	  	 sb.deleteCharAt(sb.length() - 1);
//    	return Status.constructStatuses(http.post(getBaseURL()+"favorites/destroy_batch.json",
//    			new PostParameter[]{new PostParameter("ids",sb.toString())},true));
//    }
    //----------------------------账号接口 ----------------------------------------
    

    /**
     * 验证当前用户身份是否合法
     * @return user
     * @since Weibo4J 1.2.0
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/verify_credentials">account/verify_credentials </a>
     */
//    public User verifyCredentials() throws WeiboException {
//        /*return new User(get(getBaseURL() + "account/verify_credentials.xml"
//                , true), this);*/
//    	return new User(get(getBaseURL() + "account/verify_credentials.json"
//                , true).asJSONObject());
//    }

    /**
     * 更改资料
     * @param name Optional. Maximum of 20 characters.
     * @param gender Optional. Must be m or f
     * @param url Optional. Maximum of 100 characters. Will be prepended with "http://" if not present.
     * @param location Optional. Maximum of 30 characters. The contents are not normalized or geocoded in any way.
     * @param description Optional. Maximum of 160 characters.
     * @return the updated user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/update_profile">account/update_profile </a>
     */
//    public User updateProfile(String name, String gender, String url
//            , String location, String description) throws WeiboException {
//        List<PostParameter> profile = new ArrayList<PostParameter>(5);
//        addParameterToList(profile, "name", name);
//        addParameterToList(profile, "gender", gender);
//        addParameterToList(profile, "url", url);
//        addParameterToList(profile, "location", location);
//        addParameterToList(profile, "description", description);
//        return new User(http.post(getBaseURL() + "account/update_profile.json"
//                , profile.toArray(new PostParameter[profile.size()]), true).asJSONObject());
//    }
    /**
     * 更改资料
     * @param name Optional. Maximum of 20 characters.
     * @param gender Optional. Must be m or f
     * @param location Optional. Maximum of 30 characters. The contents are not normalized or geocoded in any way.
     * @param description Optional. Maximum of 160 characters.
     * @return the updated user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since pWeibo
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/update_profile">account/update_profile </a>
     */
//    public User updateProfile(String name, String gender, String location, String description) throws WeiboException {
//    	List<PostParameter> profile = new ArrayList<PostParameter>(5);
//    	addParameterToList(profile, "name", name);
//    	addParameterToList(profile, "gender", gender);
//    	addParameterToList(profile, "location", location);
//    	addParameterToList(profile, "description", description);
//    	return new User(http.post(getBaseURL() + "account/update_profile.json"
//    			, profile.toArray(new PostParameter[profile.size()]), true).asJSONObject());
//    }
    /**
     * 更改资料
     * @param name Optional. Maximum of 20 characters.
     * @param gender Optional. Must be m or f
     * @param description Optional. Maximum of 160 characters.
     * @return the updated user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since pWeibo
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/update_profile">account/update_profile </a>
     */
//    public User updateProfile(String name, String gender,String description) throws WeiboException {
//    	List<PostParameter> profile = new ArrayList<PostParameter>(5);
//    	addParameterToList(profile, "name", name);
//    	addParameterToList(profile, "gender", gender);
//    	addParameterToList(profile, "description", description);
//    	return new User(http.post(getBaseURL() + "account/update_profile.json"
//    			, profile.toArray(new PostParameter[profile.size()]), true).asJSONObject());
//    }

    /**
     * 更改头像
     * @param image
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/update_profile_image">account/update_profile_image</a>
     */
//    public User updateProfileImage(File image)throws WeiboException {
//    	return new User(http.multPartURL("image",getBaseURL() + "account/update_profile_image.json",
//    			new PostParameter[]{new PostParameter("source",CONSUMER_KEY)},image, true).asJSONObject());
//    }

    /**
     *获取当前用户API访问频率限制<br>
     * @return the rate limit status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/rate_limit_status">account/rate_limit_status </a>
     */
//    public RateLimitStatus rateLimitStatus() throws WeiboException {
//    	 return new RateLimitStatus(http.get(getBaseURL() + "account/rate_limit_status.json" , true),this);
//    }

    
    /**
     * 当前用户退出登录
     * @return a user's object
     * @throws WeiboException when Weibo service or network is unavailable
     */
   public User endSession() throws WeiboException {
	   
       return new User(Weibo.client.get(WeiboConfig.getValue("baseURL") + "account/end_session.json").asJSONObject());
    }
    //----------------------------Tags接口 ----------------------------------------
//    
//    /**
//     * 返回用户的标签信息
//     * @param user_id 用户id
//     * @param paging 分页数据
//     * @return tags
//     * @throws WeiboException
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags">Tags </a>
//     */
//    public List<Tag> getTags(String userId,Paging paging) throws WeiboException{
//    	return Tag.constructTags(get(getBaseURL()+"tags.json",
//    			new PostParameter[]{new PostParameter("user_id",userId)},
//    			paging,true));
//    }
//    /**
//     * 为当前登录用户添加新的用户标签
//     * @param tags
//     * @return tagid
//     * @throws WeiboException
//     * @throws JSONException
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/create">Tags/create </a>
//     */
//     public List<Tag> createTags(String ...tags)throws WeiboException{
//    	 StringBuffer sb= new StringBuffer();
//    	 for(String tag:tags){
//    		 sb.append(tag).append(",");
//    	 }
//    	 sb.deleteCharAt(sb.length()-1);
//    	 return createTags(sb.toString());
//       }
//    
//    /**
//    * 为当前登录用户添加新的用户标签
//    * @param tags 要创建的一组标签，用半角逗号隔开。
//    * @return tagid
//    * @throws WeiboException
//    * @throws JSONException
//    * @since Weibo4J 1.2.0
//    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/create">Tags/create </a>
//    */
//    
//    public List<Tag> createTags(String tags)throws WeiboException{
//        return Tag.constructTags(http.post(getBaseURL()+"tags/create.json", 
//        new PostParameter[]{new PostParameter("tags",tags)},true));
//       
//       }
//    /**
//     * 返回用户感兴趣的标签
//     * @return a list of tags
//     * @throws WeiboException
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/suggestions">Tags/suggestions </a>
//     */
//    
//    public List<Tag> getSuggestionsTags()throws WeiboException{
//    	return Tag.constructTags(get(getBaseURL()+"tags/suggestions.json",true));
//    }
//    /**
//     * 删除标签
//     * @param tag_id
//     * @return 
//     * @throws WeiboException
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/destroy">Tags/destroy </a>
//     */
//    
////    public  boolean destoryTag(String tag_id)throws WeiboException{
////        try {
////			return  http.post(getBaseURL()+"tags/destroy.json",
////			new PostParameter[]{new PostParameter("tag_id",tag_id)},true).asJSONObject().getInt("result") ==0;
////		} catch (JSONException e) {
////			throw new WeiboException(e);
////		}
////       }
//    /**
//     * 删除一组标签
//     * @param ids
//     * @return tagid
//     * @throws WeiboException
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/destroy_batch">Tags/destroy_batch </a>
//     */
//    
////    public List<Tag> destory_batchTags(String ids)throws WeiboException{
////        return Tag.constructTags(http.post(getBaseURL()+"tags/destroy_batch.json",
////        new PostParameter[]{new PostParameter("ids",ids)},true));
////       }
//    
//  
//    
//    //----------------------------黑名单接口 ---------------------------------------
//
//    /**
//     * 将某用户加入登录用户的黑名单
//     * @param weiboId 用户id
//     * @return the blocked user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see<a href="http://open.t.sina.com.cn/wiki/index.php/Blocks/create">Blocks/create</a>
//     */
//    public User createBlock(String userid) throws WeiboException {
//        return new Friendships().(http.post(getBaseURL() + "blocks/create.json",
//    			new PostParameter[]{new PostParameter("user_id", userid)}, true).asJSONObject());
//    }
//    /**
//     * 将某用户加入登录用户的黑名单
//     * @param screenName 用户昵称
//     * @return the blocked user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//      * @see<a href="http://open.t.sina.com.cn/wiki/index.php/Blocks/create">Blocks/create</a>
//     */
////    public User createBlockByScreenName(String screenName) throws WeiboException {
////    	return new User(http.post(getBaseURL() + "blocks/create.json",
////    			new PostParameter[]{new PostParameter("screen_name", screenName)}, true).asJSONObject());
////    }
//
//
//    /**
//     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
//     * @param id the ID or screen_name of the user to block
//     * @return the unblocked user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
////    public User destroyBlock(String id) throws WeiboException {
////    	return new User(http.post(getBaseURL() + "blocks/destroy.json",
////    			new PostParameter[]{new PostParameter("id",id)}, true).asJSONObject());
////    }
//
//
//    /**
//     * Tests if a friendship exists between two users.
//     * @param id The ID or screen_name of the potentially blocked user.
//     * @return  if the authenticating user is blocking a target user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
////    public boolean existsBlock(String id) throws WeiboException {
////        try{
////            return -1 == get(getBaseURL() + "blocks/exists.json?user_id="+id, true).
////                    asString().indexOf("<error>You are not blocking this user.</error>");
////        }catch(WeiboException te){
////            if(te.getStatusCode() == 404){
////                return false;
////            }
////            throw te;
////        }
////    }
//
//    /**
//     * Returns a list of user objects that the authenticating user is blocking.
//     * @return a list of user objects that the authenticating user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
////    public List<User> getBlockingUsers() throws
////            WeiboException {
////        return User.constructUsers(get(getBaseURL() +
////                "blocks/blocking.xml", true), this);
////    }
//
//    /**
//     * Returns a list of user objects that the authenticating user is blocking.
//     * @param page the number of page
//     * @return a list of user objects that the authenticating user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
////    public List<User> getBlockingUsers(int page) throws
////            WeiboException {
////        /*return User.constructUsers(get(getBaseURL() +
////                "blocks/blocking.xml?page=" + page, true), this);*/
////    	return User.constructUsers(get(getBaseURL() +
////                "blocks/blocking.json?page=" + page, true));
////    }
//
//    /**
//     * Returns an array of numeric user ids the authenticating user is blocking.
//     * @return Returns an array of numeric user ids the authenticating user is blocking.
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
////    public IDs getBlockingUsersIDs() throws WeiboException {
//////        return new IDs(get(getBaseURL() + "blocks/blocking/ids.xml", true));
////    	return new IDs(get(getBaseURL() + "blocks/blocking/ids.json", true),this);
////    }
//
//    //----------------------------Social Graph接口-----------------------------------
//    
//    /**
//     * 返回用户关注的一组用户的ID列表
//     * @param userid 用户id
//     * @param paging
//     * @return
//     * @throws WeiboException 
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">Friends/ids</a>
//     */
////    public IDs getFriendsIDSByUserId(String userid,Paging paging) throws WeiboException{
////    	return new IDs(get(baseURL+"friends/ids.json",
////    			new PostParameter[]{new PostParameter("user_id", userid)},paging, true),this);
////    }
//    /**
//     * 返回用户关注的一组用户的ID列表
//     * @param userid 用户昵称
//     * @param paging
//     * @return
//     * @throws WeiboException 
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">Friends/ids</a>
//     */
//    public IDs getFriendsIDSByScreenName(String screenName,Paging paging) throws WeiboException{
//    	return new IDs(get(baseURL+"friends/ids.json",
//    			new PostParameter[]{new PostParameter("screen_name", screenName)},paging, true),this);
//    }
//    /**
//     * 返回用户的粉丝用户ID列表
//     * @param userid 用户id
//     * @param paging
//     * @return
//     * @throws WeiboException 
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">Followers/ids</a>
//     */
//    public IDs getFollowersIDSByUserId(String userid,Paging paging) throws WeiboException{
//    	return new IDs(get(baseURL+"followers/ids.json",
//    			new PostParameter[]{new PostParameter("user_id", userid)},paging, true),this);
//    }
//    /**
//     * 返回用户的粉丝用户ID列表
//     * @param userid 用户昵称
//     * @param paging
//     * @return
//     * @throws WeiboException 
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">Followers/ids</a>
//     */
//    public IDs getFollowersIDSByScreenName(String screenName,Paging paging) throws WeiboException{
//    	return new IDs(get(baseURL+"followers/ids.json",
//    			new PostParameter[]{new PostParameter("screen_name", screenName)},paging, true),this);
//    }
//    
//    
//    
//    //----------------------------话题接口-------------------------------------------
//    /**
//     * 获取用户的话题。
//     * @return the result
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since  Weibo4J 1.2.0 
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends">Trends </a>
//     */
//    public List<UserTrend> getTrends(String userid,Paging paging) throws WeiboException {
//        return UserTrend.constructTrendList(get(baseURL + "trends.json",
//        		new PostParameter[]{new PostParameter("user_id", userid)},paging,true));
//    }
//    /**
//     * 获取某一话题下的微博
//     * @param trendName 话题名称
//     * @return list status
//     * @throws WeiboException
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/statuses">Trends/statuses </a>
//     */
//    public List<Status> getTrendStatus(String trendName,Paging paging) throws WeiboException {
//    	return Status.constructStatuses(get(baseURL + "trends/statuses.json",
//    			new PostParameter[]{new PostParameter("trend_name", trendName)},
//    			paging,true));
//    }
//    /**关注某一个话题
//     * @param treandName 话题名称
//     * @return
//     * @throws WeiboException
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/follow">trends/follow</a>
//     */
//    public UserTrend trendsFollow(String treandName) throws WeiboException {
//			return  new UserTrend(http.post(baseURL+"trends/follow.json",new PostParameter[]{new PostParameter("trend_name", treandName)},true));
//    }
//    /**取消对某话题的关注。
//     * @param trendId 话题id
//     * @return result
//     * @throws WeiboException
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/destroy">Trends/destroy</a>
//     */
//    public boolean trendsDestroy(String trendId) throws WeiboException{
//    	JSONObject obj= http.delete(baseURL+"trends/destroy.json?trend_id="+trendId+"&source="+Weibo.CONSUMER_KEY, true).asJSONObject();
//    	try {
//			return obj.getBoolean("result");
//		} catch (JSONException e) {
//			throw new WeiboException("e");
//		}
//    }
//    /**
//     * 按小时返回热门话题
//     * @param baseApp 是否基于当前应用来获取数据。1表示基于当前应用来获取数据。
//     * @return
//     * @throws WeiboException
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/hourly">Trends/hourly</a>
//     */
//    public List<Trends> getTrendsHourly(Integer baseApp)throws WeiboException{
//    	if(baseApp==null)
//    		baseApp=0;
//    	return Trends.constructTrendsList(get(baseURL+"trends/hourly.json","base_app",baseApp.toString() ,true));
//    }
//    /**
//     * 返回最近一天内的热门话题。
//     * @param baseApp 是否基于当前应用来获取数据。1表示基于当前应用来获取数据。
//     * @return
//     * @throws WeiboException
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/daily">Trends/daily</a>
//     */
//    public List<Trends> getTrendsDaily(Integer baseApp)throws WeiboException{
//    	if(baseApp==null)
//    		baseApp=0;
//    	return Trends.constructTrendsList(get(baseURL+"trends/daily.json","base_app",baseApp.toString() ,true));
//    }
//    /**
//     * 返回最近一周内的热门话题。
//     * @param baseApp 是否基于当前应用来获取数据。1表示基于当前应用来获取数据。
//     * @return
//     * @throws WeiboException
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/weekly">Trends/weekly</a>
//     */
    public List<Trends> getTrendsWeekly(Integer baseApp)throws WeiboException{
    	if(baseApp==null)
    		baseApp=0;
    	return new Trend().getTrendsWeekly(baseApp);
    }
//    
//    
//    private String toDateStr(Date date){
//        if(null == date){
//            date = new Date();
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        return sdf.format(date);
//    }
//
//  
//    //----------------------------关注接口-------------------------------------------
//    
//    /**
//     * 关注一个用户。关注成功则返回关注人的资料
//     * @param id 要关注的用户ID 或者微博昵称 
//     * @return the befriended user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/create">friendships/create </a>
//     * @deprecated
//     */
//    //错误代码？使用报40307错误，应该是post？
//    public User createFriendship(String id) throws WeiboException {
//    	 return new User(get(getBaseURL() + "friendships/create.json", "id",id, true).asJSONObject());
//    }
//    /**
//     * 关注一个用户。关注成功则返回关注人的资料
//     * @param weiboId 微博昵称 
//     * @return the befriended user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/create">friendships/create </a>
//     */
    public User createFriendshipByScreenName(String screenName) throws WeiboException {
    	return new Friendships().createFriendshipsByName(screenName);
    }
//    /**
//     * 关注一个用户。关注成功则返回关注人的资料
//     * @param weiboId 要关注的用户ID 
//     * @return the befriended user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/create">friendships/create </a>
//     */
    public User createFriendshipByUserid(String userid) throws WeiboException {
    	return new Friendships().createFriendshipsById(userid);
    }
//    /**
//     * 取消对某用户的关注
//     * @param id 要取消关注的用户ID 或者微博昵称 
//     * @return User
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/destroy">friendships/destroy </a>
//     */
    public User destroyFriendship(String id) throws WeiboException {
    	 return new Friendships().destroyFriendshipsDestroyById(id);
    }
//    /**
//     * 取消对某用户的关注
//     * @param weiboId 要取消关注的用户ID 
//     * @return User
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/destroy">friendships/destroy </a>
//     */
//    public User destroyFriendshipByUserid(int userid) throws WeiboException {
//    	return new User(http.post(getBaseURL() + "friendships/destroy.json", "user_id",""+userid, true).asJSONObject());
//    }
//    /**
//     * 取消对某用户的关注
//     * @param weiboId 要取消关注的用户昵称
//     * @return User
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/destroy">friendships/destroy </a>
//     */
//    public User destroyFriendshipByScreenName(String screenName) throws WeiboException {
//    	return new User(http.post(getBaseURL() + "friendships/destroy.json", "screen_name",screenName, true).asJSONObject());
//    }
//    
    //----------------------------用户接口-------------------------------------------
    /**
     * 按用户ID或昵称返回用户资料以及用户的最新发布的一条微博消息。
     * @param weiboId 用户ID或者昵称(string)
     * @return User
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Users/show">users/show </a>
     * @since Weibo4J 1.2.0
     */
    public User showUser(String uid) throws WeiboException {
    	 return new Users().showUserById(uid);
    }
    
    public User showUserByName(String name) throws WeiboException {
   	 return new Users().showUserByScreenName(name);
   }
//    
//
//    /**
//     * 获取当前用户关注列表及每个关注用户的最新一条微博，返回结果按关注时间倒序排列，最新关注的用户排在最前面。
//     * @return the list of friends
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
//     * @since Weibo4J 1.2.0
//     */
//    public List<User> getFriendsStatuses() throws WeiboException {
//    	return new Friendships()User.constructResult(get(getBaseURL() + "statuses/friends.json", true));
//    }
//    /**
//     * 获取当前用户关注列表及每个关注用户的最新一条微博，返回结果按关注时间倒序排列，最新关注的用户排在最前面。
//     * @return the list of friends
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
//     * @since Weibo4J 1.2.0
//     */
//    public List<User>getFriendsStatuses(int cursor) throws WeiboException {
//    	return User.constructUser(get(getBaseURL()+"statuses/friends.json"
//    			,new PostParameter[]{new PostParameter("cursor", cursor)},true));
//    }
//    /**
//     * 获取指定用户关注列表及每个关注用户的最新一条微博，返回结果按关注时间倒序排列，最新关注的用户排在最前面。
//     * @param id 用户ID 或者昵称 
//     * @param paging 分页数据
//     * @return the list of friends
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
//     */
//    public List<User> getFriendsStatuses(String id, Paging paging) throws WeiboException {
//    	return User.constructUsers(get(getBaseURL() + "statuses/friends.json",
//    			new PostParameter[]{new PostParameter("id", id)},paging,
//                 true));
//    }
//    /**
//     * 获取当前用户关注列表及每个关注用户的最新一条微博，返回结果按关注时间倒序排列，最新关注的用户排在最前面。
//     * @param paging controls pagination
//     * @return the list of friends
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
//     */
//    public List<User> getFriendsStatuses(Paging paging) throws WeiboException {
//        /*return User.constructUsers(get(getBaseURL() + "statuses/friends.xml", null,
//                paging, true), this);*/
//    	return User.constructUsers(get(getBaseURL() + "statuses/friends.json", null,
//                paging, true));
//    }
//
//
//    /**
//     * 获取指定用户关注列表及每个关注用户的最新一条微博，返回结果按关注时间倒序排列，最新关注的用户排在最前面。
//     * @param id id 用户ID 或者昵称 
//     * @return the list of friends
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
//     * @since Weibo4J 1.2.0
//     */
    public List<User> getFriendsStatuses(String id) throws WeiboException {
        /*return User.constructUsers(get(getBaseURL() + "statuses/friends/" + id + ".xml"
                , false), this);*/
//    	return User.constructUsers(get(getBaseURL() + "statuses/friends.json"
//    			,new PostParameter[]{new PostParameter("id", id)}
//                , false));
    	return new Friendships().getFriendsByID(id).getUsers();
    	
    	
    }
    
    public List<UserTrend> getTrends(String id) throws WeiboException {
        /*return User.constructUsers(get(getBaseURL() + "statuses/friends/" + id + ".xml"
                , false), this);*/
//    	return User.constructUsers(get(getBaseURL() + "statuses/friends.json"
//    			,new PostParameter[]{new PostParameter("id", id)}
//                , false));
    	return new Trend().getTrends(id);
    	
    	
    }
//
//    /**
//     * 获取当前用户粉丝列表及及每个粉丝用户最新一条微博，返回结果按关注时间倒序排列，最新关注的用户排在最前面。
//     * @return List
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
//     * @since Weibo4J 1.2.0
//     */
//    public List<User> getFollowersStatuses() throws WeiboException {
////        return User.constructUsers(get(getBaseURL() + "statuses/followers.xml", true), this);
//    	return User.constructResult(get(getBaseURL() + "statuses/followers.json", true));
//    }
// 
//    /**
//     * 获取指定用户粉丝列表及及每个粉丝用户最新一条微博，返回结果按关注时间倒序排列，最新关注的用户排在最前面。
//     * @param id   用户ID 或者昵称 
//     * @param paging controls pagination
//     * @return List
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
//     */
//    public List<User> getFollowersStatuses(String id, Paging paging) throws WeiboException {
//    	return User.constructUsers(get(getBaseURL() + "statuses/followers.json", 
//    			new PostParameter[]{new PostParameter("id", id)},
//    			paging, true));
//    }
//
//    /**
//     * 获取当前用户粉丝列表及及每个粉丝用户最新一条微博，返回结果按关注时间倒序排列，最新关注的用户排在最前面。
//     * @param paging  分页数据
//     * @return List
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
//     */
//    public List<User> getFollowersStatuses(Paging paging) throws WeiboException {
//    	return User.constructUsers(get(getBaseURL() + "statuses/followers.json", null
//                , paging, true));
//    }
//    /**
//     * 获取当前用户粉丝列表及及每个粉丝用户最新一条微博，返回结果按关注时间倒序排列，最新关注的用户排在最前面。
//     * @param cursor 分页数据
//     * @return List
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
//     */
//    public List<User> getFollowersStatuses(int cursor) throws WeiboException {
//    	return User.constructUser(get(getBaseURL()+"statuses/followers.json",
//    			new PostParameter[]{new PostParameter("cursor", cursor)},true));
//    }
//   
//    /**
//     * 获取指定用户前20 粉丝列表及及每个粉丝用户最新一条微博，返回结果按关注时间倒序排列，最新关注的用户排在最前面。
//     * @return List
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
//     */
    public List<User> getFollowersStatuses(String id) throws WeiboException {
    	return new Friendships().getFollowersById(id).getUsers();
    }
//
//    /**
//     * 获取系统推荐用户
//     * @param category 返回某一类别的推荐用户  具体目录参见文档
//     * @return User
//     * @throws WeiboException
//     * @see<a href="http://open.t.sina.com.cn/wiki/index.php/Users/hot">users/hot</a> 
//     * @since Weibo4J 1.2.0
//     */
//    public List<User> getHotUsers(String category) throws WeiboException{
//    	return User.constructUsers(get(getBaseURL()+"users/hot.json","category",  category, true));
//    }
//    
//    /**
//     * 更新当前登录用户所关注的某个好友的备注信息。
//     * @param userid 需要修改备注信息的用户ID。
//     * @param remark 备注信息
//     * @return
//     * @throws WeiboException
//     * @see<a href="http://open.t.sina.com.cn/wiki/index.php/User/friends/update_remark">friends/update_remark</a> 
//     */
//    public User updateRemark(String userid,String remark) throws WeiboException {
//    	if(!URLEncodeUtils.isURLEncoded(remark))
//    		remark=URLEncodeUtils.encodeURL(remark);
//	return new User(http.post(getBaseURL()+"user/friends/update_remark.json",
//    			new PostParameter[]{new PostParameter("user_id", userid),
//    			new PostParameter("remark", remark)},
//    			true).asJSONObject());
//    }
//    /**
//     * 更新当前登录用户所关注的某个好友的备注信息。
//     * @param userid 需要修改备注信息的用户ID。
//     * @param remark 备注信息
//     * @return
//     * @throws WeiboException
//     * @see<a href="http://open.t.sina.com.cn/wiki/index.php/User/friends/update_remark">friends/update_remark</a> 
//     */
//    public User updateRemark(Long userid,String remark) throws WeiboException {
//	return updateRemark(Long.toString(userid), remark);
//    }
//    
//    /**
//     * 返回当前用户可能感兴趣的用户
//     * @return user list
//     * @throws WeiboException
//     * * @see<a href="http://open.t.sina.com.cn/wiki/index.php/Users/suggestions">friends/update_remark</a> 
//     */
//    public List<User> getSuggestionUsers() throws WeiboException {
//    	return User.constructUsers(get(getBaseURL()+"users/suggestions.json","with_reason", "0", true));
//    }
//    
//    
//    //-----------------------获取下行数据集(timeline)接口-----------------------------
//    /**
//     * 返回最新的20条公共微博。返回结果非完全实时，最长会缓存60秒
//     * @return list of statuses of the Public Timeline
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/public_timeline">statuses/public_timeline </a>
//     */
//    public List<Status> getPublicTimeline() throws
//            WeiboException {
//        return Status.constructStatuses(get(getBaseURL() +
//                "statuses/public_timeline.json", true));
//    }
//    /**返回最新的公共微博
//     * @param count 每次返回的记录数
//     * @param baseApp 是否仅获取当前应用发布的信息。0为所有，1为仅本应用。
//     * @return
//     * @throws WeiboException
//     */
//    public List<Status> getPublicTimeline(int count,int baseApp) throws WeiboException {
//    	return Status.constructStatuses(get(getBaseURL() +
//        "statuses/public_timeline.json", 
//        new PostParameter[]{
//    		new PostParameter("count", count),
//    		new PostParameter("base_app", baseApp)
//    	}
//        , false));
//    }
//    /**
//     * 获取当前登录用户及其所关注用户的最新20条微博消息。<br/>
//     * 和用户登录 http://t.sina.com.cn 后在“我的首页”中看到的内容相同。
//     * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
//     *
//     * @return list of the Friends Timeline
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
//     */
    public List<Status> getFriendsTimeline() throws
            WeiboException {
    	return new Timeline().getFriendsTimeline().getStatuses();
    }
//    /**
//     *获取当前登录用户及其所关注用户的最新微博消息。<br/>
//     * 和用户登录 http://t.sina.com.cn 后在“我的首页”中看到的内容相同。
//     * @param paging 相关分页参数
//     * @return list of the Friends Timeline
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since  Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
//     */
//    public List<Status> getFriendsTimeline(Paging paging) throws
//            WeiboException {
//    	return Status.constructStatuses(get(getBaseURL() + "statuses/friends_timeline.json",null, paging, true));
//    }
//    /**
//     * 获取当前登录用户及其所关注用户发表的不大于指定微博id的微博消息。<br/>
//     * 即：比指定微博id发表时间早的微博（包括此id代表的微博）
//     * @param maxId 指定微博id
//     * @return
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since pWeibo 09-12 00:01
//     */
    public List<Status> getFriendsTimelineMax(String maxId) throws WeiboException{
    	return new Timeline().getFriendsTimeline(0,0,maxId).getStatuses();
    }
    /**
     * xujun 20121015 
     * 获取当前登录用户及其所关注用户发表的大于指定微博id的微博消息。<br/>
     * 即：比指定微博id发表时间晚的微博
     * @param sinceId
     * @return
     * @throws WeiboException
     */
    public List<Status> getFriendsTimelineSince(String sinceId) throws WeiboException{
    	return new Timeline().getFriendsTimelineSince(0,0,sinceId).getStatuses();
    }
//    
//    /**
//     *获取当前登录用户及其所关注用户的最新微博消息。<br/>
//     * 和用户登录 http://t.sina.com.cn 后在“我的首页”中看到的内容相同。
//     * @return list of the home Timeline
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
//     * @since  Weibo4J 1.2.0
//     */
//    public List<Status> getHomeTimeline() throws
//            WeiboException {
//    	return Status.constructStatuses(get(getBaseURL() + "statuses/home_timeline.json", true));
//    }
//
//
//    /**
//     *获取当前登录用户及其所关注用户的最新微博消息。<br/>
//     * 和用户登录 http://t.sina.com.cn 后在“我的首页”中看到的内容相同。
//     * @param paging controls pagination
//     * @return list of the home Timeline
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
//     * @since  Weibo4J 1.2.0
//     */
//    public List<Status> getHomeTimeline(Paging paging) throws
//            WeiboException {
//    	return Status.constructStatuses(get(getBaseURL() + "statuses/home_timeline.json", null, paging, true));
//    }
//    /**
//     * 返回指定用户最新发表的微博消息列表
//     * @param id   根据用户ID(int64)或者微博昵称(string)返回指定用户的最新微博消息列表。
//     * @param paging 分页信息
//     * @return list of the user Timeline
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
//     */
//    public List<Status> getUserTimeline(String id, Paging paging)
//            throws WeiboException {
////        return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline/" + id + ".xml",
////                null, paging, http.isAuthenticationEnabled()), this);
//        return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.json",
//        		 new PostParameter[]{new PostParameter("id", id)}, paging, http.isAuthenticationEnabled()));
//    }
//    public List<Status> getUserTimeline(String id,Integer baseAPP,Integer feature, Paging paging)
//    throws WeiboException {
//    	Map<String,String> maps= new HashMap<String, String>();
//    	if(id!=null){
//    		maps.put("id", id);
//    	}
//    	if(baseAPP!=null){
//    		maps.put("base_app", baseAPP.toString());
//    	}
//    	if(feature!=null){
//    		maps.put("feature", feature.toString());
//    	}
//    	return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.json",
//    			generateParameterArray(maps), paging, http.isAuthenticationEnabled()));
//    }
//    /**
//     * 返回指定用户最新发表的微博消息列表
//     * @param id 根据用户ID(int64)或者微博昵称(string)返回指定用户的最新微博消息列表。
//     * @return the 20 most recent statuses posted in the last 24 hours from the user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
//     */
    public List<Status> getUserTimeline(String id) throws WeiboException {
    	return new Timeline().getUserTimelineByUid(id).getStatuses();
    }
    
    public List<Status> getUserTimeline(String uid, Paging page,
			Integer base_app, Integer feature) throws WeiboException {
    	return new Timeline().getUserTimelineByUid(uid, page, base_app, feature).getStatuses();
    }
    
    public Status getOneUserTimeline(String id) throws WeiboException {
    	return new Timeline().showStatus(id);
    }
//    /**
//     *返回当前用户最新发表的微博消息列表
//     * @return the 20 most recent statuses posted in the last 24 hours from the user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
//     */
//    public List<Status> getUserTimeline() throws
//            WeiboException {
//    	return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.json"
//                , true));
//    }
//
//    /**
//     *返回当前用户最新发表的微博消息列表
//     * @param paging controls pagination
//     * @return the 20 most recent statuses posted in the last 24 hours from the user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
//     * @since Weibo4J 1.2.0
//     */
//    public List<Status> getUserTimeline(Paging paging) throws
//            WeiboException {
//    	return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.json"
//                , null, paging, true));
//    }
//    /**
//     *返回当前用户最新发表的微博消息列表
//     * @param paging controls pagination
//     * @param base_app 是否基于当前应用来获取数据。1为限制本应用微博，0为不做限制。
//     * @param feature 微博类型，0全部，1原创，2图片，3视频，4音乐. 返回指定类型的微博信息内容。
//     * @return the 20 most recent statuses posted in the last 24 hours from the user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
//     * @since Weibo4J 1.2.0
//     */
//    public List<Status> getUserTimeline(Integer baseAPP,Integer feature,Paging paging) throws
//    WeiboException {
//    	return getUserTimeline(null,baseAPP,feature,paging);
//    }
//    /**
//     * 返回最新20条提到登录用户的微博消息（即包含@username的微博消息）
//     * @return the 20 most recent replies
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/mentions">Statuses/mentions </a>
//     */
    public List<Status> getMentions() throws WeiboException {
        return new Timeline().getMentions().getStatuses();
    }
//
//    /**
//     * 返回最新提到登录用户的微博消息（即包含@username的微博消息）
//     * @param paging 分页数据
//     * @return the 20 most recent replies
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/mentions">Statuses/mentions </a>
//     */
      public List<Status> getMentions(Paging page, Integer filter_by_author,
  			Integer filter_by_source, Integer filter_by_type) throws WeiboException {
    	  return new Timeline().getMentions(page, filter_by_author, filter_by_source, filter_by_type).getStatuses(); 
      }
// 
//    /**
//     * 返回最新20条发送及收到的评论。
//     * @return a list of comments objects
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_timeline">Statuses/comments_timeline</a>
//     */
//    public List<Comment> getCommentsTimeline() throws WeiboException {
//    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_timeline.json", true));
//    }
//    /**
//     * 返回最新n条发送及收到的评论。
//     * @param paging 分页数据
//     * @return a list of comments objects
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_timeline">Statuses/comments_timeline</a>
//     */
//    public List<Comment> getCommentsTimeline(Paging paging) throws WeiboException {
//    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_timeline.json",null,paging, true));
//    }
//    
//    /**
//     * 获取最新20条当前用户发出的评论
//     * @return a list of comments objects
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_by_me">Statuses/comments_by_me</a>
//     */
//    public List<Comment> getCommentsByMe() throws WeiboException {
//    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_by_me.json", true));
//    }
//    /**
//     * 获取当前用户发出的评论
//     *@param paging 分页数据
//     * @return a list of comments objects
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_by_me">Statuses/comments_by_me</a>
//     */
//    public List<Comment> getCommentsByMe(Paging paging) throws WeiboException {
//    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_by_me.json",null,paging, true));
//    }
//    
//    /**
//     * 返回最新20条当前登录用户收到的评论
//     * @return a list of comments objects
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0 
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_to_me">Statuses/comments_to_me</a>
//     */
    public List<Comment> getCommentsToMe() throws WeiboException {
    	return new Comments().getCommentToMe().getComments();
    }
    /**
     * 返回范围内当前登录用户收到的评论
     * @return
     * @throws WeiboException
     */
    public List<Comment> getCommentsToMe(Paging page, Integer filter_by_source,
			Integer filter_by_author) throws WeiboException {
    	return new Comments().getCommentToMe(page, filter_by_source, filter_by_author).getComments();
    }
//    /**
//     * 返回当前登录用户收到的评论
//     *@param paging 分页数据
//     * @return a list of comments objects
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_to_me">Statuses/comments_to_me</a>
//     */
//    public List<Comment> getCommentsToMe(Paging paging) throws WeiboException {
//    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_to_me.json",null,paging, true));
//    }
//
//
//    
//    /**
//     * 返回用户转发的最新20条微博信息 
//     * @param id specifies the id of user
//     * @return statuses
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0 
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/repost_by_me">Statuses/repost_by_me</a>
//     */
//    public List<Status> getrepostbyme(String id)throws WeiboException {
//    	return Status.constructStatuses(get(getBaseURL()+"statuses/repost_by_me.json","id",id,true));
//    }
//    /**
//     * 返回用户转发的最新n条微博信息 
//     * @param id specifies the id of user
//     * @return statuses
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0 
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/repost_by_me">Statuses/repost_by_me</a>
//     */
//    public List<Status> getrepostbyme(String id,Paging paging)throws WeiboException {
//    	return Status.constructStatuses(get(getBaseURL()+"statuses/repost_by_me.json",
//    			new PostParameter[]{new PostParameter("id", id)},
//    			paging,true));
//    }
    /**
     * 返回一条原创微博的最新20条转发微博信息本接口无法对非原创微博进行查询。
     * @param id specifies the id of original status.
     * @return a list of statuses object
     * @throws WeiboException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/repost_timeline">Statuses/repost_timeline</a>
     */
    public List<Status> getRepostTimeline(String id)throws WeiboException{
    	return new Timeline().getRepostTimeline(id).getStatuses();
    }
    /**
     * 返回一条原创微博的最新n条转发微博信息本接口无法对非原创微博进行查询。
     * @param id specifies the id of original status.
     * @return a list of statuses object
     * @throws WeiboException
     * @since Weibo4J 1.2.0  
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/repost_timeline">Statuses/repost_timeline</a>
     */
    public List<Status> getRepostTimeline(String id, Paging paging)throws WeiboException{
    	return new Timeline().getRepostTimeline(id, paging).getStatuses();
    }
//    /**
//     * 根据微博消息ID返回某条微博消息的20条评论列表
//     * @param id specifies the ID of status
//     * @return a list of comments objects
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0  
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments">Statuses/comments</a>
//     */
    public List<Comment> getComments(String id) throws WeiboException {
    	return new Comments().getCommentById(id).getComments();
    }
    
    public List<Comment> getComments(String id, Paging page,
			Integer filter_by_author) throws WeiboException {
    	return new Comments().getCommentById(id, page, filter_by_author).getComments();
    }
//    /**
//     * 根据微博消息ID返回某条微博消息的n评论列表
//     * @param id specifies the ID of status
//     * @return a list of comments objects
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments">Statuses/comments</a>
//     */
//    public List<Comment> getComments(String id,Paging paging) throws WeiboException {
//    	return Comment.constructComments(get(getBaseURL() + "statuses/comments.json",
//    			new PostParameter[]{new PostParameter("id", id)},paging,
//    			true));
//    }
//    /**
//     * 批量获取n条微博消息的评论数和转发数。要获取评论数和转发数的微博消息ID列表，用逗号隔开
//     * 一次请求最多可以获取100条微博消息的评论数和转发数
//     * @param ids ids a string, separated by commas
//     * @return a list of counts objects
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0  
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/counts">Statuses/counts</a>
//     */
//    public List<Count> getCounts(String ids) throws WeiboException{
//    	return Count.constructCounts(get(getBaseURL() + "statuses/counts.json","ids",ids, true));
//    }
//
//    
//    /**
//     *获取当前用户Web主站未读消息数
//     * @return count objects
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @throws JSONException
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/unread">Statuses/unread</a>
//     */
//    public Count getUnread() throws WeiboException{
//    	return new Count(get(getBaseURL() + "statuses/unread.json", true));
//    }
//    /**
//     * 获取当前用户Web主站未读消息数
//     * @param withNewStatus 1表示结果中包含new_status字段，0表示结果不包含new_status字段。new_status字段表示是否有新微博消息，1表示有，0表示没有
//     * @param sinceId 参数值为微博id。该参数需配合with_new_status参数使用，返回since_id之后，是否有新微博消息产生
//     * @return
//     * @throws WeiboException
//     * @throws JSONException
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/unread">Statuses/unread</a>
//     */
//    public Count getUnread(Integer withNewStatus,Long sinceId) throws WeiboException, JSONException {
//    	Map<String, String> maps= new HashMap<String, String>();
//    	if(withNewStatus!=null)
//    		maps.put("with_new_status",Integer.toString( withNewStatus));
//    	if(sinceId!=null)
//    		maps.put("since_id",Long.toString( sinceId));
//    	return new Count(get(getBaseURL() + "statuses/unread.json", generateParameterArray(maps),true).asJSONObject());
//    }
//    
//    /**
//     *将当前登录用户的某种新消息的未读数为0
//     * @param type  1. 评论数，2.@ me数，3. 私信数，4. 关注数
//     * @return
//     * @throws WeiboException
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/reset_count">statuses/reset_count</a> 
//     */
//    public Boolean resetCount(int type)throws WeiboException{
//    	boolean res=false;
//    	JSONObject json = null;
//    	    try {
//		 json=http.post(getBaseURL()+"statuses/reset_count.json",
//			new PostParameter[]{new PostParameter("type",type)},true).asJSONObject();
//		res=json.getBoolean("result");
//    	    } catch (JSONException je) {
//		throw new WeiboException(je.getMessage() + ":" + json,
//			je);
//	    }
//	    return res;
//    }
//
//    /**
//     * 返回新浪微博官方所有表情、魔法表情的相关信息。包括短语、表情类型、表情分类，是否热门等。
//     * @param type 表情类别。"face":普通表情，"ani"：魔法表情，"cartoon"：动漫表情
//     * @param language 语言类别，"cnname"简体，"twname"繁体
//     * @return
//     * @throws WeiboException
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Emotions">Emotions</a> 
//     */
//    public List<Emotion> getEmotions(String type,String language)throws WeiboException {
//    	if(type==null)
//    		type="face";
//    	if(language==null)
//    		language="cnname";
//    	Map<String, String> maps= new HashMap<String, String>();
//    	maps.put("type", type);
//    	maps.put("language", language);
//		return Emotion.constructEmotions(get(getBaseURL()+"emotions.json",generateParameterArray(maps),true));
//    }
//    /**
//     * 返回新浪微博简体中文的普通表情。
//     * @return
//     * @throws WeiboException
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Emotions">Emotions</a> 
//     */
//    public List<Emotion> getEmotions()throws WeiboException {
//    	return getEmotions(null,null);
//    }
//    //--------------微博访问接口----------
//    
//    /**
//     * 根据ID获取单条微博消息，以及该微博消息的作者信息。
//     * @param id 要获取的微博消息ID
//     * @return 微博消息
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/show">statuses/show </a>
//     */
//    public Status showStatus(String id) throws WeiboException {
//    	return new Status(get(getBaseURL() + "statuses/show/" + id + ".json", true));
//    }
//    /**
//     * 根据ID获取单条微博消息，以及该微博消息的作者信息。
//     * @param id 要获取的微博消息ID
//     * @return 微博消息
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/show">statuses/show </a>
//     */
//    public Status showStatus(long id) throws WeiboException {
//    	return showStatus(Long.toString(id));
//    }
//
//    /**
//     * 发布一条微博信息
//     * @param status 要发布的微博消息文本内容
//     * @return the latest status
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/update">statuses/update </a>
//     */
    public Status updateStatus(String status) throws WeiboException{

    	return new Timeline().UpdateStatus(status);
    }
//    
//    /**
//     * 发布一条微博信息
//     * @param status    要发布的微博消息文本内容
//     * @param latitude  纬度。有效范围：-90.0到+90.0，+表示北纬。
//     * @param longitude 经度。有效范围：-180.0到+180.0，+表示东经。
//     * @return the latest status
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/update">statuses/update </a>
//     * @since Weibo4J 1.2.0
//     */
    public Status updateStatus(String status, double latitude, double longitude) throws WeiboException{
    	
    	return new Timeline().UpdateStatus(status, (float)latitude, (float)longitude, "yytv");
    }
//
//    /**
//     * 发布一条微博信息
//     * @param status  要发布的微博消息文本内容
//     * @param inReplyToStatusId 要转发的微博消息ID
//     * @return the latest status
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/update">statuses/update </a>
//     */
//    public Status updateStatus(String status, long inReplyToStatusId) throws WeiboException {
//    	 return new Status(http.post(getBaseURL() + "statuses/update.json",
//                 new PostParameter[]{new PostParameter("status", status), new PostParameter("in_reply_to_status_id", String.valueOf(inReplyToStatusId)), new PostParameter("source", source)}, true));
//    }
//
//    /**
//     *发布一条微博信息
//     * @param status           要发布的微博消息文本内容
//     * @param inReplyToStatusId 要转发的微博消息ID
//     * @param latitude  纬度。有效范围：-90.0到+90.0，+表示北纬。
//     * @param longitude 经度。有效范围：-180.0到+180.0，+表示东经。
//     * @return the latest status
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/update">statuses/update </a>
//     * @since Weibo4J 1.2.0
//     */
//    public Status updateStatus(String status, long inReplyToStatusId
//            , double latitude, double longitude) throws WeiboException {
//    	return new Status(http.post(getBaseURL() + "statuses/update.json",
//                new PostParameter[]{new PostParameter("status", status),
//                        new PostParameter("lat", latitude),
//                        new PostParameter("long", longitude),
//                        new PostParameter("in_reply_to_status_id",
//                                String.valueOf(inReplyToStatusId)),
//                        new PostParameter("source", source)}, true));
//    }
//    
    /**
     * 发表图片微博消息。目前上传图片大小限制为<5M。
     * @param status 要发布的微博消息文本内容
     * @param item 要上传的图片
     * @return the latest status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/upload">statuses/upload </a>
     */
    public Status uploadStatus(String status,ImageItem item) throws WeiboException {
    	return new Timeline().UploadStatus(status, item);
    }
//    /**
//     * 发表图片微博消息。目前上传图片大小限制为<5M。
//     * @param status 要发布的微博消息文本内容
//     * @param item 要上传的图片
//     * @param latitude  纬度。有效范围：-90.0到+90.0，+表示北纬。
//     * @param longitude 经度。有效范围：-180.0到+180.0，+表示东经。
//     * @return
//     * @throws WeiboException
//     */
//    public Status uploadStatus(String status,ImageItem item, double latitude, double longitude) throws WeiboException {
//    	if(!URLEncodeUtils.isURLEncoded(status))
//    		status=URLEncodeUtils.encodeURL(status);
//    	return new Status(http.multPartURL(getBaseURL() + "statuses/upload.json",
//		new PostParameter[]{new PostParameter("status", status), 
//	        new PostParameter("source", source),
//		new PostParameter("lat", latitude),
//                new PostParameter("long", longitude),	
//	},item, true));
//    }
//
//    /**
//     * 发表图片微博消息。目前上传图片大小限制为<5M。
//     * @param status 要发布的微博消息文本内容
//     * @param file 要上传的图片
//     * @return
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/upload">statuses/upload </a>
//     */
//    public Status uploadStatus(String status,File file) throws WeiboException {
//    	if(!URLEncodeUtils.isURLEncoded(status))
//    		status=URLEncodeUtils.encodeURL(status);
//    	return new Status(http.multPartURL("pic",getBaseURL() + "statuses/upload.json",
//                new PostParameter[]{new PostParameter("status", status), new PostParameter("source", source)},file, true));
//    }
//    /**
//     * 发表图片微博消息。目前上传图片大小限制为<5M。
//     * @param status 要发布的微博消息文本内容
//     * @param file 要上传的图片
//     * @param latitude  纬度。有效范围：-90.0到+90.0，+表示北纬。
//     * @param longitude 经度。有效范围：-180.0到+180.0，+表示东经。
//     * @return
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/upload">statuses/upload </a>
//     */
//    public Status uploadStatus(String status,File file, double latitude, double longitude) throws WeiboException {
//    	if(!URLEncodeUtils.isURLEncoded(status))
//    		status=URLEncodeUtils.encodeURL(status);
//    	return new Status(http.multPartURL("pic",getBaseURL() + "statuses/upload.json",
//		new PostParameter[]{new PostParameter("status", status), 
//	    new PostParameter("source", source),
//	    new PostParameter("lat", latitude),
//            new PostParameter("long", longitude)},
//            file, true));
//    }
//    
//
//    /**
//     * Destroys the status specified by the required ID parameter.  The authenticating user must be the author of the specified status.
//     * <br>This method calls http://api.t.sina.com.cn/statuses/destroy/id.format
//     *
//     * @param statusId The ID of the status to destroy.
//     * @return the deleted status
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/destroy">statuses/destroy </a>
//     */
//    public Status destroyStatus(long statusId) throws WeiboException {
//    	return destroyStatus(Long.toString(statusId));
//    }
//    public Status destroyStatus(String statusId) throws WeiboException {
//	return new Status(http.post(getBaseURL() + "statuses/destroy/" + statusId + ".json",
//		new PostParameter[0], true));
//    }
//    
//    /**
//     * 转发微博
//     * @param sid 要转发的微博ID
//     * @param status 添加的转发文本。必须做URLEncode,信息内容不超过140个汉字。如不填则默认为“转发微博”。
//     * @return a single status
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
    public Status repost(String sid,String status) throws WeiboException {
    	return repost(sid, status,0);
    }
//    /**
//     * 转发微博
//     * @param sid 要转发的微博ID
//     * @param status 添加的转发文本。必须做URLEncode,信息内容不超过140个汉字。如不填则默认为“转发微博”。
//     * @param isComment 是否在转发的同时发表评论。1表示发表评论，0表示不发表.
//     * @return a single status
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
    public Status repost(String sid,String status,int isComment) throws WeiboException {
    	return new Timeline().Repost(sid, status, isComment);
    }
    
    /**
     * xujun 20121112: destroy status
     */
    public Status destroyStatus(String statusId) throws WeiboException{
    	return new Timeline().destroyStatus(statusId);
    }
//    
//    /**
//    *对一条微博信息进行评论
//    * @param 评论内容。必须做URLEncode,信息内容不超过140个汉字。
//    * @param id 要评论的微博消息ID
//    * @param cid 要回复的评论ID,可以为null.如果id及cid不存在，将返回400错误
//    * </br>如果提供了正确的cid参数，则该接口的表现为回复指定的评论。<br/>此时id参数将被忽略。<br/>即使cid参数代表的评论不属于id参数代表的微博消息，通过该接口发表的评论信息直接回复cid代表的评论。
//    * @return the comment object
//    * @throws WeiboException
//    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comment">Statuses/comment</a>
//    */
   public Comment updateComment(String comment, String id) throws WeiboException {
       
	   return new Comments().createComment(comment,id);
	   
   }
   /**
    * 删除评论。注意：只能删除登录用户自己发布的评论，不可以删除其他人的评论。
    * @param statusId 欲删除的评论ID
    * @return the deleted status
    * @throws WeiboException when Weibo service or network is unavailable
    * @since Weibo4J 1.2.0
    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comment_destroy">statuses/comment_destroy </a>
    */
	public Comment destroyComment(String commentId) throws WeiboException {
		return new Comments().destroyComment(commentId);
	}
//
//   /**
//    * 批量删除评论。注意：只能删除登录用户自己发布的评论，不可以删除其他人的评论。
//    * @Ricky
//    * @param ids 欲删除的一组评论ID，用半角逗号隔开，最多20个
//    * @return
//    * @throws WeiboException
//    * @since Weibo4J 1.2.0
//    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comment/destroy_batch">statuses/comment/destroy batch</a> 
//    */
//   public List<Comment> destroyComments(String ids)throws WeiboException{
//   	return Comment.constructComments(http.post(getBaseURL()+"statuses/comment/destroy_batch.json",
//   			new PostParameter[]{new PostParameter("ids",ids)},true));
//   }
//   public List<Comment> destroyComments(String[] ids)throws WeiboException{
//   	StringBuilder sb = new StringBuilder();
//	    for(String id : ids) {
//		   sb.append(id).append(',');
//	    }
//	    sb.deleteCharAt(sb.length() - 1);
//   	return Comment.constructComments(http.post(getBaseURL()+"statuses/comment/destroy_batch.json",
//   			new PostParameter[]{new PostParameter("ids",sb.toString())},true));
//   }
//
   /**
    * 回复评论
    * @param sid 要回复的评论ID。
    * @param cid 要评论的微博消息ID
    * @param comment 要回复的评论内容。必须做URLEncode,信息内容不超过140个汉字
    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/reply">Statuses/reply</a> 
    * @throws WeiboException when Weibo service or network is unavailable
    * @since Weibo4J 1.2.0
    */
	public Comment reply(String sid, String cid, String comment)
			throws WeiboException {
		return new Comments().replyComment(sid, cid, comment);
	}
//   
//  //--------------auth method----------
//    /**
//     *
//     * @param consumerKey OAuth consumer key
//     * @param consumerSecret OAuth consumer secret
//     * @since Weibo4J 1.2.0
//     */
//    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret){
//        this.http.setOAuthConsumer(consumerKey, consumerSecret);
//    }
//
//    /**
//     * Retrieves a request token
//     * @return generated request token.
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://oauth.net/core/1.0/#auth_step1">OAuth Core 1.0 - 6.1.  Obtaining an Unauthorized Request Token</a>
//     */
//    public RequestToken getOAuthRequestToken() throws WeiboException {
//        return http.getOAuthRequestToken();
//    }
//
//    public RequestToken getOAuthRequestToken(String callback_url) throws WeiboException {
//        return http.getOauthRequestToken(callback_url);
//    }
//
//    /**
//     * Retrieves an access token assosiated with the supplied request token.
//     * @param requestToken the request token
//     * @return access token associsted with the supplied request token.
//     * @throws WeiboException when Weibo service or network is unavailable, or the user has not authorized
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Oauth/access_token">Oauth/access token </a>
//     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
//     * @since Weibo4J 1.2.0
//     */
//    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws WeiboException {
//        return http.getOAuthAccessToken(requestToken);
//    }
//
//    /**
//     * Retrieves an access token assosiated with the supplied request token and sets userId.
//     * @param requestToken the request token
//     * @param pin pin
//     * @return access token associsted with the supplied request token.
//     * @throws WeiboException when Weibo service or network is unavailable, or the user has not authorized
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Oauth/access_token">Oauth/access token </a>
//     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
//     * @since  Weibo4J 1.2.0
//     */
//    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String pin) throws WeiboException {
//        AccessToken accessToken = http.getOAuthAccessToken(requestToken, pin);
//        return accessToken;
//    }
//
//    /**
//     * Retrieves an access token assosiated with the supplied request token and sets userId.
//     * @param token request token
//     * @param tokenSecret request token secret
//     * @return access token associsted with the supplied request token.
//     * @throws WeiboException when Weibo service or network is unavailable, or the user has not authorized
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Oauth/access_token">Oauth/access token </a>
//     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
//     * @since  Weibo4J 1.2.0
//     */
//    public synchronized AccessToken getOAuthAccessToken(String token, String tokenSecret) throws WeiboException {
//        AccessToken accessToken = http.getOAuthAccessToken(token, tokenSecret);
//        return accessToken;
//    }
//
//    /**
//     * Retrieves an access token assosiated with the supplied request token.
//     * @param token request token
//     * @param tokenSecret request token secret
//     * @param oauth_verifier oauth_verifier or pin
//     * @return access token associsted with the supplied request token.
//     * @throws WeiboException when Weibo service or network is unavailable, or the user has not authorized
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Oauth/access_token">Oauth/access token </a>
//     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
//     * @since  Weibo4J 1.2.0
//     */
//    public synchronized AccessToken getOAuthAccessToken(String token
//            , String tokenSecret, String oauth_verifier) throws WeiboException {
//        return http.getOAuthAccessToken(token, tokenSecret, oauth_verifier);
//    }
//
//    public synchronized AccessToken getXAuthAccessToken(String userId,String passWord,String mode) throws WeiboException {
//    	return http.getXAuthAccessToken(userId, passWord, mode);
//    }
//    public synchronized AccessToken getXAuthAccessToken(String userid, String password) throws WeiboException {
//	return getXAuthAccessToken(userid,password,Constants.X_AUTH_MODE);
//	
//    }
//    /**
//     * Sets the access token
//     * @param accessToken accessToken
//     * @since  Weibo4J 1.2.0
//     */
//    public void setOAuthAccessToken(AccessToken accessToken){
//        this.http.setOAuthAccessToken(accessToken);
//    }
//
//    /**
//     * Sets the access token
//     * @param token token
//     * @param tokenSecret token secret
//     * @since  Weibo4J 1.2.0
//     */
//    public void setOAuthAccessToken(String token, String tokenSecret) {
//        setOAuthAccessToken(new AccessToken(token, tokenSecret));
//    }
//
//
// 
//
//    /* Status Methods */
//
// 
//    public RateLimitStatus getRateLimitStatus()throws
//            WeiboException {
//    	/*modify by sycheng edit with json */
//        return new RateLimitStatus(get(getBaseURL() +
//                "account/rate_limit_status.json", true),this);
//    }
//
//   
//
//  
//
//   
//    /**
//     * Returns the 20 most recent retweets posted by the authenticating user.
//     *
//     * @return the 20 most recent retweets posted by the authenticating user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public List<Status> getRetweetedByMe() throws WeiboException {
//    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_by_me.json",
//                null, true));
//        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_by_me.xml",
//                null, true), this);*/
//    }
//
//    /**
//     * Returns the 20 most recent retweets posted by the authenticating user.
//     * @param paging controls pagination
//     * @return the 20 most recent retweets posted by the authenticating user
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public List<Status> getRetweetedByMe(Paging paging) throws WeiboException {
//    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_by_me.json",
//                null, true));
//        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_by_me.xml",
//                null, paging, true), this);*/
//    }
//
//    /**
//     * Returns the 20 most recent retweets posted by the authenticating user's friends.
//     * @return the 20 most recent retweets posted by the authenticating user's friends.
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public List<Status> getRetweetedToMe() throws WeiboException {
//    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_to_me.json",
//                null, true));
//        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_to_me.xml",
//                null, true), this);*/
//    }
//
//    /**
//     * Returns the 20 most recent retweets posted by the authenticating user's friends.
//     * @param paging controls pagination
//     * @return the 20 most recent retweets posted by the authenticating user's friends.
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public List<Status> getRetweetedToMe(Paging paging) throws WeiboException {
//    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_to_me.json",
//                null, paging, true));
//        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_to_me.xml",
//                null, paging, true), this);*/
//    }
//
//    /**
//     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
//     * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public List<Status> getRetweetsOfMe() throws WeiboException {
//    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweets_of_me.json",
//                null, true));
//        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweets_of_me.xml",
//                null, true), this);*/
//    }
//
//    /**
//     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
//     * @param paging controls pagination
//     * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public List<Status> getRetweetsOfMe(Paging paging) throws WeiboException {
//    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweets_of_me.json",
//                null, paging, true));
//    	/* return Status.constructStatuses(get(getBaseURL() + "statuses/retweets_of_me.xml",
//                null, paging, true), this);*/
//    }
//
//
//    /**
//     * Retweets a tweet. Requires the id parameter of the tweet you are retweeting. Returns the original tweet with retweet details embedded.
//     * @param statusId The ID of the status to retweet.
//     * @return the retweeted status
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public Status retweetStatus(long statusId) throws WeiboException {
//        /*return new Status(http.post(getBaseURL() + "statuses/retweet/" + statusId + ".xml",
//                new PostParameter[0], true), this);*/
//    	return new Status(http.post(getBaseURL() + "statuses/retweet/" + statusId + ".json",
//                new PostParameter[0], true));
//    }
//
//    /**
//     * Returns up to 100 of the first retweets of a given tweet.
//     * @param statusId The numerical ID of the tweet you want the retweets of.
//     * @return the retweets of a given tweet
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public List<RetweetDetails> getRetweets(long statusId) throws WeiboException {
//       /* return RetweetDetails.createRetweetDetails(get(getBaseURL()
//                + "statuses/retweets/" + statusId + ".xml", true), this);*/
//    	 return RetweetDetails.createRetweetDetails(get(getBaseURL()
//                 + "statuses/retweets/" + statusId + ".json", true));
//    }
//
//    /**
//     * Returns a list of the users currently featured on the site with their current statuses inline.
//     *
//     * @return List of User
//     * @throws WeiboException when Weibo service or network is unavailable
//     */
//    public List<User> getFeatured() throws WeiboException {
////        return User.constructUsers(get(getBaseURL() + "statuses/featured.xml", true), this);
//        return User.constructUsers(get(getBaseURL() + "statuses/featured.json", true));
//    }
//
//  
//
//   
//
// 
//
//  
//
//  
//
//    /**
//     * Tests if a friendship exists between two users.
//     *
//     * @param userA The ID or screen_name of the first user to test friendship for.
//     * @param userB The ID or screen_name of the second user to test friendship for.
//     * @return if a friendship exists between two users.
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/exists">friendships/exists </a>
//     */
//    public boolean existsFriendship(String userA, String userB) throws WeiboException {
//        return -1 != get(getBaseURL() + "friendships/exists.json", "user_a", userA, "user_b", userB, true).
//                asString().indexOf("true");
//    }
//
//    private void addParameterToList(List<PostParameter> colors,
//                                      String paramName, String color) {
//        if(null != color){
//            colors.add(new PostParameter(paramName,color));
//        }
//    }
//
//   
//
//    /**
//     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
//     * @param id String
//     * @return User
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public User enableNotification(String id) throws WeiboException {
////        return new User(http.post(getBaseURL() + "notifications/follow/" + id + ".xml", true), this);
//    	return new User(http.post(getBaseURL() + "notifications/follow/" + id + ".json", true).asJSONObject());
//    }
//
//    /**
//     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
//     * @param id String
//     * @return User
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public User disableNotification(String id) throws WeiboException {
////        return new User(http.post(getBaseURL() + "notifications/leave/" + id + ".xml", true), this);
//    	return new User(http.post(getBaseURL() + "notifications/leave/" + id + ".json", true).asJSONObject());
//    }
//
//   
//
//    /* Saved Searches Methods */
//    /**
//     * Returns the authenticated user's saved search queries.
//     * @return Returns an array of numeric user ids the authenticating user is blocking.
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public List<SavedSearch> getSavedSearches() throws WeiboException {
//        return SavedSearch.constructSavedSearches(get(getBaseURL() + "saved_searches.json", true));
//    }
//
//    /**
//     * Retrieve the data for a saved search owned by the authenticating user specified by the given id.
//     * @param id The id of the saved search to be retrieved.
//     * @return the data for a saved search
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public SavedSearch showSavedSearch(int id) throws WeiboException {
//        return new SavedSearch(get(getBaseURL() + "saved_searches/show/" + id
//                + ".json", true));
//    }
//
//    /**
//     * Retrieve the data for a saved search owned by the authenticating user specified by the given id.
//     * @return the data for a created saved search
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public SavedSearch createSavedSearch(String query) throws WeiboException {
//        return new SavedSearch(http.post(getBaseURL() + "saved_searches/create.json"
//                , new PostParameter[]{new PostParameter("query", query)}, true));
//    }
//
//    /**
//     * Destroys a saved search for the authenticated user. The search specified by id must be owned by the authenticating user.
//     * @param id The id of the saved search to be deleted.
//     * @return the data for a destroyed saved search
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public SavedSearch destroySavedSearch(int id) throws WeiboException {
//        return new SavedSearch(http.post(getBaseURL() + "saved_searches/destroy/" + id
//                + ".json", true));
//    }
//
//
//	/**
//	 * Obtain the ListObject feed list
//	 * @param uid		User ID or screen_name
//	 * @param listId	The ID or slug of ListObject
//	 * @param auth		if true, the request will be sent with BASIC authentication header
//	 * @return
//	 * @throws WeiboException
//	 */
//	public List<Status> getListStatuses(String uid, String listId, boolean auth) throws WeiboException {
//		StringBuilder sb = new StringBuilder();
//		sb.append(getBaseURL()).append(uid).append("/lists/").append(listId).append("/statuses.xml").append("?source=").append(CONSUMER_KEY);
//		String httpMethod = "GET";
//		String url = sb.toString();
//		//
//		return Status.constructStatuses(http.httpRequest(url, null, auth, httpMethod), this);
//	}
//
//
//	/**
//	 * Obtain ListObject member list
//	 * @param uid		User ID or screen_name
//	 * @param listId	The ID or slug of ListObject
//	 * @param auth		if true, the request will be sent with BASIC authentication header
//	 * @return
//	 * @throws WeiboException
//	 */
//	public UserWapper getListMembers(String uid, String listId, boolean auth) throws WeiboException {
//		StringBuilder sb = new StringBuilder();
//		sb.append(getBaseURL()).append(uid).append("/").append(listId).append("/members.xml").append("?source=").append(CONSUMER_KEY);
//		String httpMethod = "GET";
//		String url = sb.toString();
//		//
//		return User.constructWapperUsers(http.httpRequest(url, null, auth, httpMethod), this);
//	}
//
//	/**
//	 * Obtain ListObject subscribe user's list 
//	 * @param uid		User ID or screen_name
//	 * @param listId	The ID or slug of ListObject
//	 * @param auth		if true, the request will be sent with BASIC authentication header
//	 * @return
//	 * @throws WeiboException
//	 */
//	public UserWapper getListSubscribers(String uid, String listId, boolean auth) throws WeiboException {
//		StringBuilder sb = new StringBuilder();
//		sb.append(getBaseURL()).append(uid).append("/").append(listId).append("/subscribers.xml").append("?source=").append(CONSUMER_KEY);
//		String httpMethod = "GET";
//		String url = sb.toString();
//		//
//		return User.constructWapperUsers(http.httpRequest(url, null, auth, httpMethod), this);
//	}
//
//
//
//	/**
//	 * confirm list member
//	 * @param uid		User ID or screen_name
//	 * @param listId	The ID or slug of ListObject
//	 * @param targetUid	Target user ID or screen_name
//	 * @param auth		if true, the request will be sent with BASIC authentication header
//	 * @return
//	 * @throws WeiboException
//	 */
//	public boolean isListMember(String uid, String listId, String targetUid, boolean auth)
//			throws WeiboException {
//		StringBuilder sb = new StringBuilder();
//		sb.append(getBaseURL()).append(uid).append("/").append(listId).append("/members/").append(targetUid)
//				.append(".xml").append("?source=").append(CONSUMER_KEY);
//		String url = sb.toString();
//		//
//		String httpMethod = "GET";
//		//
//		Document doc = http.httpRequest(url, null, auth, httpMethod).asDocument();
//		Element root = doc.getDocumentElement();
//		return "true".equals(root.getNodeValue());
//	}
//
//	/**
//	 * confirm subscription list
//	 * @param uid		User ID or screen_name
//	 * @param listId	The ID or slug of ListObject
//	 * @param targetUid	Target user ID or screen_name
//	 * @param auth		if true, the request will be sent with BASIC authentication header
//	 * @return
//	 * @throws WeiboException
//	 */
//	public boolean isListSubscriber(String uid, String listId, String targetUid, boolean auth)
//			throws WeiboException {
//		StringBuilder sb = new StringBuilder();
//		sb.append(getBaseURL()).append(uid).append("/").append(listId).append("/subscribers/").append(targetUid)
//				.append(".xml").append("?source=").append(CONSUMER_KEY);
//		String url = sb.toString();
//		//
//		String httpMethod = "GET";
//		//
//		Document doc = http.httpRequest(url, null, auth, httpMethod).asDocument();
//		Element root = doc.getDocumentElement();
//		return "true".equals(root.getNodeValue());
//	}
//
//    /* Help Methods */
//    /**
//     * Returns the string "ok" in the requested format with a 200 OK HTTP status code.
//     * @return true if the API is working
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @since Weibo4J 1.2.0
//     */
//    public boolean test() throws WeiboException {
//        return -1 != get(getBaseURL() + "help/test.json", false).
//                asString().indexOf("ok");
//    }
//
//    private SimpleDateFormat format = new SimpleDateFormat(
//            "EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Weibo weibo = (Weibo) o;
//
//        if (!baseURL.equals(weibo.baseURL)) return false;
//        if (!format.equals(weibo.format)) return false;
//        if (!http.equals(weibo.http)) return false;
//        if (!searchBaseURL.equals(weibo.searchBaseURL)) return false;
//        if (!source.equals(weibo.source)) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = http.hashCode();
//        result = 31 * result + baseURL.hashCode();
//        result = 31 * result + searchBaseURL.hashCode();
//        result = 31 * result + source.hashCode();
//        result = 31 * result + format.hashCode();
//        return result;
//    }
//
//    @Override
//    public String toString() {
//        return "Weibo{" +
//                "http=" + http +
//                ", baseURL='" + baseURL + '\'' +
//                ", searchBaseURL='" + searchBaseURL + '\'' +
//                ", source='" + source + '\'' +
//                ", format=" + format +
//                '}';
//    }
//
// 
//  
//
//   
//
//    /**
//     * Return your relationship with the details of a user
//     * @param target_id id of the befriended user
//     * @return jsonObject
//     * @throws WeiboException when Weibo service or network is unavailable
//     */
//    public JSONObject showFriendships(String target_id) throws WeiboException {
//    	return get(getBaseURL() + "friendships/show.json?target_id="+target_id, true).asJSONObject();
//    }

//    /**
//     * Return the details of the relationship between two users
//     * @param target_id id of the befriended user
//     * @return jsonObject
//     * @throws WeiboException when Weibo service or network is unavailable
//     * @Ricky  Add source parameter&missing "="
//     */
//    public JSONObject showFriendships(String source_id,String target_id) throws WeiboException {
//    	return get(getBaseURL() + "friendships/show.json?target_id="+target_id+"&source_id="+source_id+"&source="+CONSUMER_KEY, true).asJSONObject();
//    }
//
//  
//
//    /**
//     *  Return infomation of current user
//     * @param ip a specified ip(Only open to invited partners)
//     * @param args User Information args[2]:nickname,args[3]:gender,args[4],password,args[5]:email
//     * @return jsonObject
//     * @throws WeiboException when Weibo service or network is unavailable
//     */
//    public JSONObject register(String ip,String ...args) throws WeiboException {
//    	return http.post(getBaseURL() + "account/register.json",
//                new PostParameter[]{new PostParameter("nick", args[2]),
//									new PostParameter("gender", args[3]),
//									new PostParameter("password", args[4]),
//									new PostParameter("email", args[5]),
//									new PostParameter("ip", ip)}, true).asJSONObject();
//    }
//   
//  
//
//
//  //--------------base method----------
//    public Weibo() {
//        super();
//        format.setTimeZone(TimeZone.getTimeZone("GMT"));
//        http.setRequestTokenURL(Configuration.getScheme() + "api.t.sina.com.cn/oauth/request_token");
//        http.setAuthorizationURL(Configuration.getScheme() + "api.t.sina.com.cn/oauth/authorize");
//        http.setAccessTokenURL(Configuration.getScheme() + "api.t.sina.com.cn/oauth/access_token");
//    }
//
//    /**
//     * Sets token information
//     * @param token
//     * @param tokenSecret
//     */
//    public void setToken(String token, String tokenSecret) {
//        http.setToken(token, tokenSecret);
//    }
//
//    public Weibo(String baseURL) {
//        this();
//        this.baseURL = baseURL;
//    }
////
////    /**
////     * Sets the base URL
////     *
////     * @param baseURL String the base URL
////     */
//    public void setBaseURL(String baseURL) {
//        this.baseURL = baseURL;
//    }
////
////    /**
////     * Returns the base URL
////     *
////     * @return the base URL
////     */
//    public String getBaseURL() {
//        return this.baseURL;
//    }
////
////    /**
////     * Sets the search base URL
////     *
////     * @param searchBaseURL the search base URL
////     * @since Weibo4J 1.2.0
////     */
//    public void setSearchBaseURL(String searchBaseURL) {
//        this.searchBaseURL = searchBaseURL;
//    }
////
////    /**
////     * Returns the search base url
////     * @return search base url
////     * @since Weibo4J 1.2.0
////     */
//    public String getSearchBaseURL(){
//        return this.searchBaseURL;
//    }
////    
////    /**
////     * Issues an HTTP GET request.
////     *
////     * @param url          the request url
////     * @param authenticate if true, the request will be sent with BASIC authentication header
////     * @return the response
////     * @throws WeiboException when Weibo service or network is unavailable
////     */
////
//    private Response get(String url, boolean authenticate) throws WeiboException {
//        return get(url, null, authenticate);
//    }
////
////    /**
////     * Issues an HTTP GET request.
////     *
////     * @param url          the request url
////     * @param authenticate if true, the request will be sent with BASIC authentication header
////     * @param name1        the name of the first parameter
////     * @param value1       the value of the first parameter
////     * @return the response
////     * @throws WeiboException when Weibo service or network is unavailable
////     */
////
//    protected Response get(String url, String name1, String value1, boolean authenticate) throws WeiboException {
//        return get(url, new PostParameter[]{new PostParameter(name1, value1)}, authenticate);
//    }
////
////    /**
////     * Issues an HTTP GET request.
////     *
////     * @param url          the request url
////     * @param name1        the name of the first parameter
////     * @param value1       the value of the first parameter
////     * @param name2        the name of the second parameter
////     * @param value2       the value of the second parameter
////     * @param authenticate if true, the request will be sent with BASIC authentication header
////     * @return the response
////     * @throws WeiboException when Weibo service or network is unavailable
////     */
////
////    protected Response get(String url, String name1, String value1, String name2, String value2, boolean authenticate) throws WeiboException {
////        return get(url, new PostParameter[]{new PostParameter(name1, value1), new PostParameter(name2, value2)}, authenticate);
////    }
////
////    /**
////     * Issues an HTTP GET request.
////     *
////     * @param url          the request url
////     * @param params       the request parameters
////     * @param authenticate if true, the request will be sent with BASIC authentication header
////     * @return the response
////     * @throws WeiboException when Weibo service or network is unavailable
////     */
////    protected Response get(String url, PostParameter[] params, boolean authenticate) throws WeiboException {
////		if (url.indexOf("?") == -1) {
////			url += "?source=" + CONSUMER_KEY;
////		} else if (url.indexOf("source") == -1) {
////			url += "&source=" + CONSUMER_KEY;
////		}
////    	if (null != params && params.length > 0) {
////			url += "&" + HttpClient.encodeParameters(params);
////		}
////        return http.get(url, authenticate);
////    }
////
////    /**
////     * Issues an HTTP GET request.
////     *
////     * @param url          the request url
////     * @param params       the request parameters
////     * @param paging controls pagination
////     * @param authenticate if true, the request will be sent with BASIC authentication header
////     * @return the response
////     * @throws WeiboException when Weibo service or network is unavailable
////     */
////    protected Response get(String url, PostParameter[] params, Paging paging, boolean authenticate) throws WeiboException {
////        if (null != paging) {
////            List<PostParameter> pagingParams = new ArrayList<PostParameter>(4);
////            if (-1 != paging.getMaxId()) {
////                pagingParams.add(new PostParameter("max_id", String.valueOf(paging.getMaxId())));
////            }
////            if (-1 != paging.getSinceId()) {
////                pagingParams.add(new PostParameter("since_id", String.valueOf(paging.getSinceId())));
////            }
////            if (-1 != paging.getPage()) {
////                pagingParams.add(new PostParameter("page", String.valueOf(paging.getPage())));
////            }
////            if (-1 != paging.getCount()) {
////                if (-1 != url.indexOf("search")) {
////                    // search api takes "rpp"
////                    pagingParams.add(new PostParameter("rpp", String.valueOf(paging.getCount())));
////                } else {
////                    pagingParams.add(new PostParameter("count", String.valueOf(paging.getCount())));
////                }
////            }
////            PostParameter[] newparams = null;
////            PostParameter[] arrayPagingParams = pagingParams.toArray(new PostParameter[pagingParams.size()]);
////            if (null != params) {
////                newparams = new PostParameter[params.length + pagingParams.size()];
////                System.arraycopy(params, 0, newparams, 0, params.length);
////                System.arraycopy(arrayPagingParams, 0, newparams, params.length, pagingParams.size());
////            } else {
////                if (0 != arrayPagingParams.length) {
////                    String encodedParams = HttpClient.encodeParameters(arrayPagingParams);
////                    if (-1 != url.indexOf("?")) {
////                        url += "&source=" + CONSUMER_KEY +
////                        		"&" + encodedParams;
////                    } else {
////                        url += "?source=" + CONSUMER_KEY +
////                        		"&" + encodedParams;
////                    }
////                }
////            }
////            return get(url, newparams, authenticate);
////        } else {
////            return get(url, params, authenticate);
////        }
////    }
////
////	private PostParameter[] generateParameterArray(Map<String, String> parames)
////			throws WeiboException {
////		PostParameter[] array = new PostParameter[parames.size()];
////		int i = 0;
////		for (String key : parames.keySet()) {
////			if (parames.get(key) != null) {
////				array[i] = new PostParameter(key, parames.get(key));
////				i++;
////			}
////		}
////		return array;
////}
////	  public final static Device IM = new Device("im");
////	    public final static Device SMS = new Device("sms");
////	    public final static Device NONE = new Device("none");
////
////	    static class Device {
////	        final String DEVICE;
////
////	        public Device(String device) {
////	            DEVICE = device;
////	        }
////	    }
////	//---------------@deprecated---------------------------------------
////
////	public void setToken(AccessToken accessToken) {
////	this.setToken(accessToken.getToken(), accessToken.getTokenSecret());
////	
////    }
////	//----------------------------Tags接口 ----------------------------------------
////
////

	public UserFriendship getUserFriendship(String sourceId, String targetId) throws WeiboException {
		return new Friendships().getUserFriendship(sourceId, targetId);
	}

}