package com.star.yytv;

import java.net.URLEncoder;
import java.util.List;
import org.apache.http.NameValuePair;

public class JniInterface {
	
	static {
		System.loadLibrary("yyserver");
	}	
	
	/**
	 * getJsonResponse function
	 * <p>get http response from server. support GET and POST method.
	 * @param type int, 0: GET method; 1: POST method
	 * @param baseUrlType int, 
	 * <br>0: old server base url, keping's address: http://www.yaoyaotv.com/yytv/; 
	 * <br>1: new server base url, baoyu's address: http://www.yaoyaotv.com/beta/app/.
	 * @param myUrl String, my own relative url based on baseUrlType's base url,
	 * <br>0: old server relative url, such as: UserBooks/jquery; UserMicroblogs/jgetUserByName
	 * <br>1: new server relative url, such as: queryAppUpdate.php; addUserClickTrack.php
	 * @param nameValuePair List<NameValuePair> para name and value list.
	 * @return String jsonObject
	 */
	public String getJsonResponse(int type, int baseUrlType, String myUrl, List<NameValuePair> nameValuePair){
		String jsonStr = null;
		
		if (baseUrlType < 0 || baseUrlType > 1)
			return null;
		
		if (myUrl == null || myUrl.length() == 0)
			return null;
		
		//无参数的GET和POST是否存在？
		if (nameValuePair == null || nameValuePair.size() == 0)
			return null;
		
		try {
			int size = nameValuePair.size();
			String paraString = "";
			//GET need ?
			if (size > 0 && type == 0){
				paraString = "?";
			}
			
			for (int i=0; i<size; i++){
				String name = nameValuePair.get(i).getName();
				String value = nameValuePair.get(i).getValue();
				//value need encode
				String valueEnc = URLEncoder.encode(value, "UTF-8");
				//pack para strings
				paraString += name;
				paraString += "=";
				paraString += valueEnc;
				if (i != size-1){
					paraString += "&";
				}
			}
			
			//GET, myUrl+paraString
			if (type == 0){
				String urlPara = myUrl + paraString;
				//paraString, GET: ?a=1&b=abc, ALL visible ascii code.
				//convert to bytes
				byte[] urlparaBytes = urlPara.getBytes("UTF-8");
				byte[] paraBytes = null;
				
				byte[] retBytes = getServerResponse2(type, baseUrlType, urlparaBytes, paraBytes);
				jsonStr = new String(retBytes, "UTF-8"); 
			} else if (type == 1){
				//paraString, POST: a=1&b=abc, ALL visible ascii code.
				//convert to bytes
				byte[] paraBytes = paraString.getBytes("UTF-8");
				byte[] urlBytes = myUrl.getBytes("UTF-8");
				
				byte[] retBytes = getServerResponse2(type, baseUrlType, urlBytes, paraBytes);
				jsonStr = new String(retBytes, "UTF-8"); 
			}
			
			//判断返回字符串是否合法的JSONOBJECT
			if (!jsonStr.contains("brief"))
				jsonStr = null;
		} catch (Exception e){
			e.printStackTrace();
			jsonStr = null;
		}	
		
		return jsonStr;
	}
	
	/**
	 * getServerResponse function
	 * <p> get http response from server. support GET and POST method.
	 * @param type int, 0: GET method; 1: POST method
	 * @param baseUrlType int, 
	 * <br>0: old server base url, keping's address: http://www.yaoyaotv.com/yytv/; 
	 * <br>1: new server base url, baoyu's address: http://www.yaoyaotv.com/beta/app/.
	 * @param myUrl byte[].
	 * @param myParaValue byte[].
	 * @return byte[] response, bytes.
	 */
	private native byte[] getServerResponse2(int type, int baseUrlType, byte[] myUrl, byte[] myParaValue);
	
	public native byte[] test(byte[] in);
}
