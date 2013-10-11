package com.star.weibo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import com.star.yytv.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.star.weibo.Face;
import com.star.weibo.Sina;
import com.star.weibo.WeiboDetail;
import com.star.yytv.model.OAuthInfoManager;
import com.star.yytv.model.YyTvActivityManager;


public class TextUtil {
	//xujun 20120912：匹配@某人的最后一个字符是否是 ： 等特殊字符
	public static final Pattern atPatten = Pattern.compile("[:：\\s]");
	
	//xujun 20121006:增加电视台特殊显示功能，字符串格式为 ^频道名称:节目名称^，如 ^CCTV-1:体育^
    public static final String TV_SPLIT_OUTSIDE = "^";
    public static final String TV_SPLIT_INSIDE = ":";
    //xujun 20121006:链接类型
    public static final int LINK_TYPE_TV = 0;    
    public static final int LINK_TYPE_CHANNEL = 1;
    
	/**   
	 * 加亮 @****： 或 @****: 或@****[空格] <br>
	 * Html.fromHtml(String) *
	 * 
	 * @param str
	 * @return
	 */
	public static Spanned highLight(String str) {
		Pattern pattern = Pattern.compile("@[^\\s:：]+[:：\\s]");
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			String m = matcher.group();
			str = str.replace(m, "<font color=Navy>" + m + "</font>");
		}
		return Html.fromHtml(str);
	}

	/**
	 * 加亮 @****： 或 @****: 或@****[空格] <br>
	 * SpannableString.setSpan(Object what, int start, int end, int flags)
	 * 
	 * @param text
	 * @return
	 */
	// 这个和hightLight(String str) 的效率对比?
	public static SpannableString light(CharSequence text) {
		SpannableString spannableString = new SpannableString(text);
		Pattern pattern = Pattern.compile("@[^\\s:：]+[:：\\s]");
		Matcher matcher = pattern.matcher(spannableString);
		while (matcher.find()) {
			spannableString.setSpan(new ForegroundColorSpan(Color.CYAN),
					matcher.start(), matcher.end(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return spannableString;
	}

	/**
	 * 格式化名字 <br>
	 * 用于保存微博图像，截取url的最后一段做为图像文件名
	 * 
	 * @param url
	 * @return
	 */
	public static String formatName(String url) {
		if (url == null || "".equals(url)) {
			return url;
		}
		int start = url.lastIndexOf("/");
		int end = url.lastIndexOf(".");
		if (start == -1 || end == -1) {
			return url;
		}
		return url.substring(start + 1, end);
	}

	/**
	 * 格式化来源
	 * 
	 * @param name
	 * @return
	 */
	public static String formatSource(String name) {
		if (name == null || "".equals(name)) {
			return name;
		}
		int start = name.indexOf(">");
		int end = name.lastIndexOf("<");
		if (start == -1 || end == -1) {
			return name;
		}
		return name.substring(start + 1, end);
	}
	/**
	 * 匹配表情 （两个连续的单字表情未能正确显示）
	 * @param text
	 * @param context
	 * @return
	 */
	public static SpannableString formatImage(CharSequence text, Context context) {
		SpannableString spannableString = new SpannableString(text);
		Pattern pattern = Pattern.compile("\\[[^0-9]{1,4}\\]"); //正则式出错？两个连续的单字表情都无法正确替换
		Matcher matcher = pattern.matcher(spannableString);
		while (matcher.find()) {
			String faceName = matcher.group();
			String key = faceName.substring(1, faceName.length() - 1);
			if (Face.getfaces(context).containsKey(key)) {
				spannableString.setSpan(new ImageSpan(context, Face.getfaces(
						context).get(key)), matcher.start(), matcher.end(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannableString;
	}
	/**
	 * 将text中@某人的字体加亮，匹配的表情文字以表情显示
	 * @param text
	 * @param context
	 * @return
	 */
	public static SpannableString formatContent(CharSequence text,Context context) {
		//xujun 20120912：解决最后的 @XXX 不能点击问题，在text的最后增加一个空格
		SpannableString spannableString = new SpannableString(text + " ");
		/*
		 * @[^\\s:：]+[:：\\s] 匹配@某人  \\[[^0-9]{1,4}\\] 匹配表情
		 * xujun 20120912： 为解决 @青山@绿水@张三@李四  这类@之间没有分割符的情况，修改匹配@某人的正则表达式为@[^\\s:：@]+[:：\\s]?
		 * xujun 20121006：增加对电视台链接的解析，格式为 ^频道名称:节目名称^，如 ^CCTV-1:体育^， 正则表达式为 ^[^\^]+^   \\^[^\\^]+\\^
		 */
		/*added by yunlong. trends of '#' are showed in blue color, but can't be transferred.*/
		Pattern pattern = Pattern.compile("@[^\\s:：@]+[:：\\s]?|\\[[^0-9]{1,3}\\]|\\[[^0-9]{4}\\]|https?://[a-zA-Z0-9\\-.]+(?:(?:/[a-zA-Z0-9\\-._?,'+\\&%$=~*!():@\\\\]*)+)?|\\^[^\\^]+\\^|#[^#]+#");
		Matcher matcher = pattern.matcher(spannableString);
		while (matcher.find()) {
			String match=matcher.group();
			if(match.startsWith("@")){ //@某人，加亮字体
				//spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
						//matcher.start(), matcher.end(),
						//Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				//xujun 20120908: 支持微博内容的部分文本的超链接
				spannableString.setSpan(new WeiboURLSpan(context,match),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}else if(match.startsWith("#")){
				spannableString.setSpan(new WeiboURLSpan(context,match),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}else if(match.startsWith("[")){ //表情
				String key = match.substring(1, match.length() - 1);
				if (Face.getfaces(context).containsKey(key)) {
					spannableString.setSpan(new ImageSpan(context, Face.getfaces(
							context).get(key)), matcher.start(), matcher.end(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}else if (match.startsWith("http")){
				//xujun 20120908: 支持微博内容的部分文本的超链接
				spannableString.setSpan(new WeiboURLSpan(context,match),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			else if (match.startsWith(TV_SPLIT_OUTSIDE)){
				//xujun 20121006: 支持电视频道和节目的超链接
				 String tvStr = match.substring(1, match.length() - 1);
				 spannableString.setSpan(new ImageSpan(context, Face.getfaces(
							context).get("^")), matcher.start(), matcher.start()+1,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				 spannableString.setSpan(new ImageSpan(context, Face.getfaces(
							context).get("^^")), matcher.end()-1, matcher.end(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				 spannableString.setSpan(new ChannelURLSpan(context,tvStr),
								matcher.start(), matcher.end(),
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
					 	 
			}
			
		}
		return spannableString;
	}
	static void Log(String msg){
		Log.i("weibo", "TextUtil--"+msg);
	}
	
	/**
	 * 支持微博内容的部分文本的超链接
	 * @author xujun 20120908
	 *
	 */
	private static class WeiboURLSpan extends ClickableSpan{
    	protected String mUrl;
    	protected Context spanContext;
    	
    	WeiboURLSpan(Context context, String url){
    		super();
    		spanContext = context;
    		mUrl = url;
    	}
    	public void updateDrawState(TextPaint ds){
    		ds.setColor(ds.linkColor);
    		ds.setUnderlineText(false);
    	}
    	
    	public void onClick(View widget){
    		if (mUrl.startsWith("@")){
    		    String lastChar = mUrl.substring(mUrl.length()-1);
    		    Matcher matcher = TextUtil.atPatten.matcher(lastChar);
    		    String screenname = "";
    		    if (matcher.find()){
    		    	screenname = mUrl.substring(1, mUrl.length()-1).trim();
    		    }else{
    		    	screenname = mUrl.substring(1);
    		    }
    		    if (OAuthInfoManager.getInstance().isLogin(spanContext)){
//    		    	Sina.getInstance().goToUserInfoByScreenname(spanContext, screenname);
    		    }
    		} else if (mUrl.startsWith("http")){
    			Log("showWebView url=" + mUrl);
    			Sina.getInstance().showWebView(spanContext, mUrl);
    		}
    		
    	}
    }
	
	/**
	 * xujun 20121006：增加对频道链接的解析
	 */
	private static class ChannelURLSpan extends WeiboURLSpan {
		protected String channelName;
		
		ChannelURLSpan(Context context, String channelName){
    		super(context, channelName);
    		this.channelName = channelName;
    	}
		
		public void onClick(View widget){
			//
		}
	}
	
	/**
	 * xujun 20121006：增加对节目链接的解析
	 */
	private static class ProgramURLSpan extends ChannelURLSpan {
		
		protected String programName;
		
		ProgramURLSpan(Context context, String channelName, String programName){
    		super(context, channelName);
    		this.programName = programName;
    	}
		
		public void onClick(View widget){
			Toast.makeText(spanContext, "chan:" + channelName + ",pro:" + programName, Toast.LENGTH_LONG).show();
		}
	}
	
	
}
