/**
 * 
 */
package com.star.weibo4j.model;

import java.util.ArrayList;
import java.util.List;

import com.star.weibo4j.http.Response;
import com.star.weibo4j.org.json.JSONArray;
import com.star.weibo4j.org.json.JSONException;
import com.star.weibo4j.org.json.JSONObject;


/**
 * @author sinaWeibo
 *
 */
public class Count implements java.io.Serializable{

	private static final long serialVersionUID = 9076424494907778181L;

	private long id;
	
	public void setId(long id) {
		this.id = id;
	}
	public void setComments(long comments) {
		this.comments = comments;
	}
	public void setRt(long rt) {
		this.rt = rt;
	}
	public void setDm(long dm) {
		this.dm = dm;
	}
	public void setMentions(long mentions) {
		this.mentions = mentions;
	}
	public void setFollowers(long followers) {
		this.followers = followers;
	}
	private long comments;
	
	private long rt;
	
	private long dm;
	
	private long mentions;
	
	private long followers;
	
	public Count()
	{
		
	}
	public Count(JSONObject json)throws WeiboException, JSONException{
    	id = json.getLong("id");
    	comments = json.getLong("comments");
    	rt = json.getLong("rt");
    	dm = json.getLong("dm");
    	mentions = json.getLong("mentions");
    	followers = json.getLong("followers");
    }
	Count(Response res) throws WeiboException{
	    JSONObject json=res.asJSONObject();
	    try {
		id = json.getLong("id");
		comments = json.getLong("comments");
	    	rt = json.getLong("rt");
	    	dm = json.getLong("dm");
	    	mentions = json.getLong("mentions");
	    	followers = json.getLong("followers");
	    
	    } catch (JSONException je) {
		throw new WeiboException(je.getMessage() + ":" + json.toString(),
			je);
	    }
	    	
	}
	static List<Count> constructCounts(Response res) throws WeiboException {
	   	 try {
	   		 System.out.println(res.asString());
	            JSONArray list = res.asJSONArray();
	            int size = list.length();
	            List<Count> counts = new ArrayList<Count>(size);
	            for (int i = 0; i < size; i++) {
	            	counts.add(new Count(list.getJSONObject(i)));
	            }
	            return counts;
	        } catch (JSONException jsone) {
	            throw new WeiboException(jsone);
	        } catch (WeiboException te) {
	            throw te;
	        }
	   }
	
	public long getId() {
		return id;
	}
	
    public long getComments() {
		return comments;
	}

	public long getRt() {
		return rt;
	}

	public long getDm() {
		return dm;
	}

	public long getMentions() {
		return mentions;
	}

	public long getFollowers() {
		return followers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Count other = (Count) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
    public String toString() {
        return "Count{ id=" + id +
                ", comments=" + comments +
                ", rt=" + rt + 
                ", dm=" + dm + 
                ", mentions=" + mentions + 
                ", followers=" + followers + 
                '}';
    }
}