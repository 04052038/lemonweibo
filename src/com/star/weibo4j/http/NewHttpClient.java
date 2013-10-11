package com.star.weibo4j.http;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.HttpEntity;   
import org.apache.http.HttpHost;   
import org.apache.http.HttpResponse;   
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;  
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.auth.UsernamePasswordCredentials;   
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;   
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;   
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params. HttpConnectionParams;   
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params. HttpClientParams;   
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BrowserCompatSpec;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.entity.mime.content.ByteArrayBody;

import com.star.weibo4j.model.Configuration;
import com.star.weibo4j.model.Paging;
import com.star.weibo4j.model.PostParameter;
import com.star.weibo4j.model.WeiboException;
import com.star.weibo4j.org.json.JSONException;

import android.util.Log;

public class NewHttpClient implements java.io.Serializable {
	
	private static final long serialVersionUID = -176092625883595548L;
	private static final int OK 				   = 200;						// OK: Success!
	private static final int NOT_MODIFIED 		   = 304;			// Not Modified: There was no new data to return.
	private static final int BAD_REQUEST 		   = 400;				// Bad Request: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.
	private static final int NOT_AUTHORIZED 	   = 401;			// Not Authorized: Authentication credentials were missing or incorrect.
	private static final int FORBIDDEN 			   = 403;				// Forbidden: The request is understood, but it has been refused.  An accompanying error message will explain why.
	private static final int NOT_FOUND             = 404;				// Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
	private static final int NOT_ACCEPTABLE        = 406;		// Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
	private static final int INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken.  Please post to the group so the Weibo team can investigate.
	private static final int BAD_GATEWAY           = 502;// Bad Gateway: Weibo is down or being upgraded.
	private static final int SERVICE_UNAVAILABLE   = 503;// Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.
	
	private static final int DEFAULT_MAX_CONNECTIONS = 30; 
	private static final int DEFAULT_SOCKET_TIMEOUT = 30 * 1000; 
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;

	private String proxyHost = Configuration.getProxyHost();
	private int proxyPort = Configuration.getProxyPort();
	private String proxyAuthUser = Configuration.getProxyUser();
	private String proxyAuthPassword = Configuration.getProxyPassword();
	private String token;
	private DefaultHttpClient sHttpClient = null; 

	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * Sets proxy host. System property -Dsinat4j.http.proxyHost or
	 * http.proxyHost overrides this attribute.
	 * 
	 * @param proxyHost
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = Configuration.getProxyHost(proxyHost);
	}

	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * Sets proxy port. System property -Dsinat4j.http.proxyPort or
	 * -Dhttp.proxyPort overrides this attribute.
	 * 
	 * @param proxyPort
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = Configuration.getProxyPort(proxyPort);
	}

	public String getProxyAuthUser() {
		return proxyAuthUser;
	}

	/**
	 * Sets proxy authentication user. System property -Dsinat4j.http.proxyUser
	 * overrides this attribute.
	 * 
	 * @param proxyAuthUser
	 */
	public void setProxyAuthUser(String proxyAuthUser) {
		this.proxyAuthUser = Configuration.getProxyUser(proxyAuthUser);
	}

	public String getProxyAuthPassword() {
		return proxyAuthPassword;
	}

	/**
	 * Sets proxy authentication password. System property
	 * -Dsinat4j.http.proxyPassword overrides this attribute.
	 * 
	 * @param proxyAuthPassword
	 */
	public void setProxyAuthPassword(String proxyAuthPassword) {
		this.proxyAuthPassword = Configuration
				.getProxyPassword(proxyAuthPassword);
	}

	public String setToken(String token) {
		this.token = token;
		return this.token;
	}
	
	public NewHttpClient(){
		initNewHttpClient();
	}

	/**  
	    * @Title: getNewHttpClient  
	    * @Description: Methods Description 
	    * @param @return     
	    * @return HttpClient  
	    * @throws  
	    */   
	      
	    private void initNewHttpClient() {  
	        HttpParams httpParams = new BasicHttpParams(); 
	        ConnManagerParams.setTimeout(httpParams, 1000); 
	        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(10)); 
	        ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
	        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1); 
	        HttpProtocolParams.setContentCharset(httpParams, "UTF-8"); 
	        HttpProtocolParams.setUserAgent(httpParams, "Android client"); 
	        HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
	        HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
	        HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT); 
	        HttpConnectionParams.setTcpNoDelay(httpParams, true); 
	        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE); 	
	        try { 
	            KeyStore trustStore = KeyStore.getInstance(KeyStore  
	                    .getDefaultType());  
	            trustStore.load(null, null);  
	            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);  
	            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
	            SchemeRegistry registry = new SchemeRegistry();  
	            registry.register(new Scheme("http", PlainSocketFactory  
	                    .getSocketFactory(), 80));  
	            registry.register(new Scheme("https", sf, 443));  
	            ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, registry);  
	            sHttpClient = new DefaultHttpClient(manager, httpParams);  
	            
	            CookieSpecFactory csf = new CookieSpecFactory() {  
	                public CookieSpec newInstance(HttpParams params) {  
	                    return new BrowserCompatSpec() {     
	                        public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {  
	                            // Oh, I am easy   
	                        }  
	                    };  
	                }  
	            };  
	            sHttpClient.getCookieSpecs().register("easy", csf);  
	            sHttpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");
	            sHttpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3, false));
	            
	        } catch (Exception e) {  
	            log("httpclient init fail: " + e.getMessage()); 
	        }  
	    }  
	    
	    /**
		 * 处理http getmethod 请求
		 * 
		 */

		public Response get(String url) throws WeiboException {
			return get(url, new PostParameter[0]);
		}

		public Response get(String url, PostParameter[] params)
				throws WeiboException {
			log("Request:");
			log("GET:" + url);
			if (null != params && params.length > 0) {
				String encodedParams = NewHttpClient.encodeParameters(params);
				if (-1 == url.indexOf("?")) {
					url += "?" + encodedParams;
				} else {
					url += "&" + encodedParams;
				}
			}
			HttpGet getmethod = new HttpGet(url);
			return httpRequest(getmethod);

		}

		public Response get(String url, PostParameter[] params, Paging paging)
				throws WeiboException {
			if (null != paging) {
				List<PostParameter> pagingParams = new ArrayList<PostParameter>(4);
				if (-1 != paging.getMaxId()) {
					pagingParams.add(new PostParameter("max_id", String
							.valueOf(paging.getMaxId())));
				}
				if (-1 != paging.getSinceId()) {
					pagingParams.add(new PostParameter("since_id", String
							.valueOf(paging.getSinceId())));
				}
				if (-1 != paging.getPage()) {
					pagingParams.add(new PostParameter("page", String
							.valueOf(paging.getPage())));
				}
				if (-1 != paging.getCount()) {
					if (-1 != url.indexOf("search")) {
						// search api takes "rpp"
						pagingParams.add(new PostParameter("rpp", String
								.valueOf(paging.getCount())));
					} else {
						pagingParams.add(new PostParameter("count", String
								.valueOf(paging.getCount())));
					}
				}
				PostParameter[] newparams = null;
				PostParameter[] arrayPagingParams = pagingParams
						.toArray(new PostParameter[pagingParams.size()]);
				if (null != params) {
					newparams = new PostParameter[params.length
							+ pagingParams.size()];
					System.arraycopy(params, 0, newparams, 0, params.length);
					System.arraycopy(arrayPagingParams, 0, newparams,
							params.length, pagingParams.size());
				} else {
					if (0 != arrayPagingParams.length) {
						String encodedParams = NewHttpClient
								.encodeParameters(arrayPagingParams);
						if (-1 != url.indexOf("?")) {
							url += "&" + encodedParams;
						} else {
							url += "?" + encodedParams;
						}
					}
				}
				return get(url, newparams);
			} else {
				return get(url, params);
			}
		}

		/**
		 * 处理http deletemethod请求
		 */
        /*
		public Response delete(String url, PostParameter[] params)
				throws WeiboException {
			if (0 != params.length) {
				String encodedParams = NewHttpClient.encodeParameters(params);
				if (-1 == url.indexOf("?")) {
					url += "?" + encodedParams;
				} else {
					url += "&" + encodedParams;
				}
			}
			DeleteMethod deleteMethod = new DeleteMethod(url);
			return httpRequest(deleteMethod);

		}
		*/

		/**
		 * 处理http post请求
		 * 
		 */

		public Response post(String url, PostParameter[] params)
				throws WeiboException {
			return post(url, params, true);

		}

		public Response post(String url, PostParameter[] params,
				Boolean WithTokenHeader) throws WeiboException {
			log("Request:");
			log("POST" + url);
			HttpPost postMethod = new HttpPost(url);
			List<BasicNameValuePair> postParams = new ArrayList<BasicNameValuePair>();   
			for (int i = 0; i < params.length; i++) {
				postParams.add(new BasicNameValuePair(params[i].getName(), params[i].getValue())); 
			}
			try{
				postMethod.setEntity(new UrlEncodedFormEntity(postParams, "utf-8")); 
				if (WithTokenHeader) {
					return httpRequest(postMethod);
				} else {
					return httpRequest(postMethod, WithTokenHeader);
				}
			}catch(UnsupportedEncodingException e){
				throw new WeiboException(e);
			}
		}

		/**
		 * 支持multipart方式上传图片
		 * 
		 */
		public Response multPartURL(String url, PostParameter[] params,
				ImageItem item) throws WeiboException {
			HttpPost postMethod = new HttpPost(url);
			try {
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				if (params != null) {
					for (PostParameter entry : params) {
						entity.addPart(URLEncoder.encode(entry.getName(), "UTF-8"), 
								new StringBody(URLEncoder.encode((String) entry.getValue(), "UTF-8")));  
					}
					entity.addPart(item.getName(), new ByteArrayBody(item.getContent(), "weibo.jpg"));
				}
				postMethod.setEntity(entity);  
				return httpRequest(postMethod);

			} catch (Exception ex) {
				throw new WeiboException(ex.getMessage(), ex, -1);
			}
		}

		public Response httpRequest(HttpRequestBase method) throws WeiboException {
			return httpRequest(method, true);
		}

		public Response httpRequest(HttpRequestBase method, Boolean WithTokenHeader)
				throws WeiboException {
			InetAddress ipaddr;
			int responseCode = -1;
			try {
				ipaddr = InetAddress.getLocalHost();
				if (WithTokenHeader) {
					if (token == null) {
						throw new IllegalStateException("Oauth2 token is not set!");
					}
					method.addHeader("Authorization", "OAuth2 " + token);
					method.addHeader("API-RemoteIP", ipaddr.getHostAddress());
				}
				HttpResponse relResonse = sHttpClient.execute(method);
				int resonseCode = relResonse.getStatusLine().getStatusCode();
				if (resonseCode == HttpStatus.SC_OK) {   
					Response response = new Response();
					response.setResponseAsString(EntityUtils.toString(relResonse.getEntity(), "UTF-8"));
					relResonse.getEntity().consumeContent();
		        	Log.i("weibo", "response success:" + response.asString())  ;
		        	return response;
		        	}   
		        else{
		        	Log.i("weibo", "response: error: " + getCause(resonseCode))  ;
		        	throw new WeiboException(getCause(resonseCode), resonseCode);
		        }
			} catch (IOException ioe) {
				throw new WeiboException(ioe.getMessage(), ioe, responseCode);
			} finally {
				// method.releaseConnection();
			}

		}

		/*
		 * 对parameters进行encode处理
		 */
		public static String encodeParameters(PostParameter[] postParams) {
			StringBuffer buf = new StringBuffer();
			for (int j = 0; j < postParams.length; j++) {
				if (j != 0) {
					buf.append("&");
				}
				try {
					buf.append(URLEncoder.encode(postParams[j].getName(), "UTF-8"))
							.append("=")
							.append(URLEncoder.encode(postParams[j].getValue(),
									"UTF-8"));
				} catch (java.io.UnsupportedEncodingException neverHappen) {
				}
			}
			return buf.toString();
		}
	    
	    private static String getCause(int statusCode) {
			String cause = null;
			switch (statusCode) {
			case NOT_MODIFIED:
				break;
			case BAD_REQUEST:
				cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
				break;
			case NOT_AUTHORIZED:
				cause = "Authentication credentials were missing or incorrect.";
				break;
			case FORBIDDEN:
				cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
				break;
			case NOT_FOUND:
				cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
				break;
			case NOT_ACCEPTABLE:
				cause = "Returned by the Search API when an invalid format is specified in the request.";
				break;
			case INTERNAL_SERVER_ERROR:
				cause = "Something is broken.  Please post to the group so the Weibo team can investigate.";
				break;
			case BAD_GATEWAY:
				cause = "Weibo is down or being upgraded.";
				break;
			case SERVICE_UNAVAILABLE:
				cause = "Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
				break;
			default:
				cause = "";
			}
			return statusCode + ":" + cause;
		}
	    
	    private void log(String message){
	    	Log.i("weibo", message);
	    }
}
