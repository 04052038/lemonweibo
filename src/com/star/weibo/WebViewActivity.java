package com.star.weibo;

import android.app.Activity;
import android.os.Bundle; 
import com.star.yytv.Log;
import android.view.KeyEvent; 
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView; 
import android.webkit.WebViewClient;
import android.webkit.HttpAuthHandler;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.star.yytv.R;
import com.star.yytv.common.YyProgressDiagFact;

/**
 *  微博网页浏览的Activity
 * @author xujun 20120912
 *
 */
public class WebViewActivity extends Activity {
	private WebView webview;
	private ProgressDialog progressDialog;
	
	public static final String WEBURL = "weburl";
	
	@Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weibo_webview); 
        webview = (WebView) findViewById(R.id.webview); 
        //设置WebView属性，能够执行Javascript脚本 
        webview.getSettings().setJavaScriptEnabled(true); 
        webview.getSettings().setPluginsEnabled(true);
        //设置加载进来的页面自适应手机屏幕
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        //设置可放大缩小
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        
        //加载需要显示的网页 
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString(WEBURL);
        progressDialog = YyProgressDiagFact.getYyProgressDiag(this);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        if (url != null && url != ""){
        	Log("oncreate:url=" + url);
        	webview.loadUrl(url); 
        }
        //设置Web视图 
        webview.setWebViewClient(new WeiboWebViewClient ()); 
    }  
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (progressDialog != null && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}
	
	@Override
    //设置回退 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) { 
            webview.goBack(); //goBack()表示返回WebView的上一页面 
            Log("webview goBack");
            return true; 
        } 
        return super.onKeyDown(keyCode, event); 
    } 
	
	/**
	 * xujun 20120921
	 * WebView完全退出flash的方法，停止声音的播放
	 * @param name
	 */
	private void callHiddenWebViewMethod(String name){
		if (webview != null){
			try{
				Method method = WebView.class.getMethod(name);
				method.invoke(webview);
			}catch (NoSuchMethodException e){
				Log("No such method: " + name + ","+ e.toString());
			}catch (IllegalAccessException e){
				Log("Illegal Access: " + name + ","+ e.toString());
			}catch (InvocationTargetException e){
				Log("Invocation Target Exception: " + name + ","+ e.toString());
			}
		}
	}

	@Override
	protected void onPause(){
		super.onPause();
		//xujun 20121018  解决回退键白屏问题，增加下述代码
		//xujun 20121028 webview.onPause 是 API level 11新增的，为兼容较低版本，采用反射机制
		callHiddenWebViewMethod("onPause");
		//xujun 20121018  解决回退键白屏问题，注释掉此段代码
		/*
		webview.pauseTimers();
		if (isFinishing()){
			webview.loadUrl("about:blank");
			setContentView(new FrameLayout(this));
		}
		callHiddenWebViewMethod("onPause");
		*/
	}

	@Override
	protected void onResume(){
		super.onResume();
	    //xujun 20121018  解决回退键白屏问题，增加下述代码
		callHiddenWebViewMethod("onResume");
		//xujun 20121018  解决回退键白屏问题，注释掉此段代码
		//callHiddenWebViewMethod("onResume");
	}
	
	//Web视图 
    private class WeiboWebViewClient extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            view.loadUrl(url); 
            Log("shouldOverrideUrlLoading:url=" + url);
            return true; 
        } 
        
        public void onPageStarted(WebView view, String url, Bitmap favicon){
        	super.onPageStarted(view, url, favicon);
        	Log("onPageStarted:url=" + url);
        	//onPause中调用了webview.loadUrl("about:blank")，不需要弹出进度对话框；
        	//新浪微博短链接以http://t.cn开头，也不需要弹出进度对话框，只有短链接对应的真实地址才需要弹出进度对话框
        	if ((! url.startsWith("about")) && (! url.startsWith("http://t.cn")) && (! progressDialog.isShowing())){
        		progressDialog.show();
        	}	
        }
        
        public void onPageFinished(WebView view, String url){
        	super.onPageFinished(view, url);
        	Log("onPageFinished:url=" + url);
        	if (progressDialog.isShowing()){
        		progressDialog.dismiss();
        	}
        }
        
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
        	if (progressDialog.isShowing()){
        		progressDialog.dismiss();
        	}
        	Log("onReceivedError,url=" + failingUrl);
        	//alertDialog.setMessage(description);
        	//alertDialog.show();
        	Toast.makeText(WebViewActivity.this, description, Toast.LENGTH_LONG).show(); 
        	
        }
        
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm){
        	if (progressDialog.isShowing()){
        		progressDialog.dismiss();
        	}
        	Log("onReceivedHttpAuthRequest,url=" + host + ", url=" + view.getUrl());
        	Toast.makeText(WebViewActivity.this, host + ", url=" + view.getUrl(), Toast.LENGTH_SHORT).show(); 
        }
        
    } 
    
    void Log(String msg){
		Log.i("weibo", "WebViewActivity--"+msg);
	}

}
