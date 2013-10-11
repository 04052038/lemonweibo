package com.star.yytv.controller.update;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import com.star.yytv.Log;
import com.star.weibo4j.org.json.JSONArray;
import com.star.weibo4j.org.json.JSONObject;
import com.star.yytv.JniInterface;

public class Global { 
    //版本信息 
    public static int localVersion = 0; 
    public static int serverVersion = 0; 
    public static String downloadDir = "app/download/"; 
    public static String downloadUrl = "";
    
    //private static BasicHttpParams httpParams; //HTTP参数 
    
    public static JSONArray GetJsonObject(){
    	/*
    	httpParams = new BasicHttpParams(); 
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
        HttpConnectionParams.setSoTimeout(httpParams, 10000);
    	HttpClient client = new DefaultHttpClient(httpParams);
		StringBuilder builder = new StringBuilder();
		//HttpGet get = new HttpGet("http://www.yaoyaotv.com/beta/queryAppUpdate.php?iszip=0&type=1");
		HttpGet get = new HttpGet(url);
		*/
    	JniInterface jni = new JniInterface();
    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();			          
		nameValuePair.add(new BasicNameValuePair("iszip", "0"));
		nameValuePair.add(new BasicNameValuePair("type", "1"));
		String getRet = jni.getJsonResponse(0, 1, "queryAppUpdate.php", nameValuePair);
		Log.d("Global", "JniInterface getRet = "+ getRet);
    	
		JSONArray jsonDatas = null;
		try{
			JSONObject jsonObject = new JSONObject(getRet);
			JSONObject briefJsonObject = (JSONObject)jsonObject.get("brief");
			int code = briefJsonObject.getInt("retCode");
			Log.d("updateService", "retCode=" + code);
			//int code = Integer.parseInt(retCode);
			//retCode != 0 means has error, data is null
			if (code != 0) {
				return null;
			}
			
			jsonDatas = (JSONArray)jsonObject.get("data");	
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
		/*
		JSONArray jsonDatas = null;
		try {
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200){
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				for (String s = reader.readLine(); s != null; s = reader.readLine()) {
					builder.append(s);
				}
				//Log.d("Global", "get result: " + builder.toString());
				JSONObject jsonObject = new JSONObject(builder.toString());
				JSONObject briefJsonObject = (JSONObject)jsonObject.get("brief");
				int code = briefJsonObject.getInt("retCode");				
				//int code = Integer.parseInt(retCode);
				//retCode != 0 means has error, data is null
				if (code != 0) {
					return null;
				}
				
				jsonDatas = (JSONArray)jsonObject.get("data");					
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		*/
		return jsonDatas;
    }
} 