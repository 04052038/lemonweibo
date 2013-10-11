package com.star.weibo;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.ProgressDialog;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import com.star.yytv.Log;
import android.view.View;
import android.widget.ImageView;

import com.star.weibo.util.LocalMemory;
import com.star.weibo.util.TextUtil;
import com.star.weibo.zoom.ImageZoomView;

/**
 * 异步加载图像<br>
 * 图像保存规则：<br>
 * 		保存头像：sdcard-pina-portrait 以用户id命名<br>
 * 		保存微博图片：sdcard-pina-pre 以图片的url的最后一段命名
 * @author starry
 *
 */
public class AsyncImageLoader {
	private ExecutorService executorService = Executors.newFixedThreadPool(3);
	private Map<Long, SoftReference<Drawable>> imageCache = new HashMap<Long, SoftReference<Drawable>>();
	//user portrait cache
	private Map<Long, SoftReference<Drawable>> portraitImageCache = new HashMap<Long, SoftReference<Drawable>>();
	//status pre cache
	private Map<Long, SoftReference<Drawable>> preImageCache = new HashMap<Long, SoftReference<Drawable>>();
	//status large pic cache
	private Map<String, SoftReference<Drawable>> statusImageCache = new HashMap<String, SoftReference<Drawable>>();
	private Handler handler = new Handler();
	private LocalMemory localMemory = new LocalMemory();
	private String picName = "";

	private static AsyncImageLoader asyncImageLoader;

	public static AsyncImageLoader getInstance() {
		if (asyncImageLoader == null) {
			asyncImageLoader = new AsyncImageLoader();
		}
		return asyncImageLoader;
	}
	/**
	 * 加载头像
	 * @param id 用户id
	 * @param url 头像url
	 * @param iv 头像ImageView
	 */
	public void loadPortrait(final long id, final String url, final ImageView iv) {
		loadPortrait(id, url, iv, null);
	}
	
	/**
	 * 加载头像
	 * @param id 用户id
	 * @param url 头像url
	 * @param iv 头像ImageView
	 * @param weiboType WEIBOTYPE_STATUS(friendsTimeline) or WEIBOTYPE_ATME(at me) or WEIBOTYPE_COMMENT(to me comment)
	 */
	public void loadPortrait(final long id, final String url, final ImageView iv, final String weiboType) {
		if (null == url || "".equals(url)) {
			return;
		}
		if (portraitImageCache.containsKey(id)) { //图像存在软引用中，直接加载
			SoftReference<Drawable> softReference = portraitImageCache.get(id);
			if (softReference != null) {
				if (softReference.get() != null) {
					//log("load portrait not null");
					iv.setImageDrawable(softReference.get());
					return;
				}
			}
		}
		//否则开新线程从本地或者网络地址加载图像
		executorService.submit(new Runnable() {
			BitmapDrawable bitmapDrawable = null;

			@Override
			public void run() {
				if (weiboType != null){
					bitmapDrawable = localMemory.getDrawable(String.valueOf(id),
							LocalMemory.PORTRAIT, weiboType);
				}
				if (bitmapDrawable == null){
					bitmapDrawable = localMemory.getDrawable(String.valueOf(id),
							LocalMemory.PORTRAIT);
				}
				if (bitmapDrawable == null) { // 若本地没有指定图片，从网上下载
					try {
						bitmapDrawable = new BitmapDrawable(new URL(url)
								.openStream()); // 获取图片
						portraitImageCache.put(id, new SoftReference<Drawable>(
								bitmapDrawable)); // 加入软引用
						if (weiboType != null){
							AsyncImageSaver.getInstance().saveImage(bitmapDrawable,
									String.valueOf(id), LocalMemory.PORTRAIT, weiboType); // 异步保存至本地
						}else{
							AsyncImageSaver.getInstance().saveImage(bitmapDrawable,
									String.valueOf(id), LocalMemory.PORTRAIT); // 异步保存至本地
						}	
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				//hanlder在主线程加载图像
				if (bitmapDrawable != null){
					handler.post(new Runnable() {

						@Override
						public void run() {
							iv.setImageDrawable(bitmapDrawable);
						}
					});
				}	
			}
		});
	}
	
	/**
	 * 加载TV图片
	 * @param id 图片所在id
	 * @param url 图片url
	 * @param iv 显示图片的ImageView
	 */
	public void loadTV(final long id, final String url, final ImageView iv,final String progType) {
		if (null == url || "".equals(url)) {
			iv.setVisibility(View.GONE);
			return;
		}
		iv.setVisibility(View.VISIBLE);
		if (imageCache.containsKey(id)) { //图像存在在软引用中，直接加载
			SoftReference<Drawable> softReference = imageCache.get(id);
			if (softReference != null) {
				if (softReference.get() == null) {
					//log("load pre null");
					iv.setImageDrawable(softReference.get());
				}
			}
		}
		//否则开新线程从本地或者网络加载
		executorService.submit(new Runnable() {
			BitmapDrawable bitmapDrawable = null;

			@Override
			public void run() {
				bitmapDrawable = localMemory.getDrawable(TextUtil
						.formatName(url), progType);
				if (bitmapDrawable == null) { // 若本地没有指定图片，从网上下载
					try {
						bitmapDrawable = new BitmapDrawable(new URL(url)
								.openStream()); // 获取图片
						imageCache.put(id, new SoftReference<Drawable>(
								bitmapDrawable)); // 加入软引用
						AsyncImageSaver.getInstance()
								.saveImage(bitmapDrawable,
										TextUtil.formatName(url),
										progType); // 异步保存至本地
					} catch (MalformedURLException e) {
						e.printStackTrace();
						log(e.toString());
					} catch (IOException e) {
						e.printStackTrace();
						log(e.toString());
					}
				}
				//handler在主线程加载图像
				if (bitmapDrawable != null){
					handler.post(new Runnable() {

						@Override
						public void run() {
							iv.setImageDrawable(bitmapDrawable);
						}
					});
				}	
			}
		});

	}
	
	/**
	 * 加载图片
	 * @param id 图片所在微博id
	 * @param url 图片url
	 * @param iv 显示图片的ImageView
	 */
	public void loadPre(final long id, final String url, final ImageView iv) {
		loadPre(id, url, iv, null);
	}
	
	/**
	 * 加载图片
	 * @param id 图片所在微博id
	 * @param url 图片url
	 * @param iv 显示图片的ImageView
	 * @param weiboType WEIBOTYPE_STATUS(friendsTimeline) or WEIBOTYPE_ATME(at me) or WEIBOTYPE_COMMENT(to me comment)
	 */
	public void loadPre(final long id, final String url, final ImageView iv, final String weiboType) {
		if (null == url || "".equals(url)) {
			iv.setVisibility(View.GONE);
			return;
		}
		iv.setVisibility(View.VISIBLE);
		if (preImageCache.containsKey(id)) { //图像存在在软引用中，直接加载
			SoftReference<Drawable> softReference = preImageCache.get(id);
			if (softReference != null) {
				if (softReference.get() != null) {
					iv.setImageDrawable(softReference.get());
					return;
				}
			}
		}
		//否则开新线程从本地或者网络加载
		executorService.submit(new Runnable() {
			BitmapDrawable bitmapDrawable = null;

			@Override
			public void run() {
				String filename = TextUtil.formatName(url);
				if (weiboType != null){
					bitmapDrawable = localMemory.getDrawable(filename, LocalMemory.PRE, weiboType);
				}
				if (bitmapDrawable == null){
					bitmapDrawable = localMemory.getDrawable(filename, LocalMemory.PRE);
				}
				if (bitmapDrawable == null) { // 若本地没有指定图片，从网上下载
					try {
						bitmapDrawable = new BitmapDrawable(new URL(url)
								.openStream()); // 获取图片
						preImageCache.put(id, new SoftReference<Drawable>(
								bitmapDrawable)); // 加入软引用
						if (weiboType != null){
							AsyncImageSaver.getInstance()
							.saveImage(bitmapDrawable, filename, LocalMemory.PRE, weiboType); // 异步保存至本地
						} else {
							AsyncImageSaver.getInstance()
							.saveImage(bitmapDrawable, filename, LocalMemory.PRE); // 异步保存至本地
						}
						
					} catch (MalformedURLException e) {
						e.printStackTrace();
						log(e.toString());
					} catch (IOException e) {
						e.printStackTrace();
						log(e.toString());
					}
				}
				//handler在主线程加载图像
				if (bitmapDrawable != null){
					handler.post(new Runnable() {

						@Override
						public void run() {
							iv.setImageDrawable(bitmapDrawable);
						}
					});
				}	
			}
		});

	}
	
	/**
	 * xujun 20120908
	 * 加载微博图片，供查看微博大图使用
	 * @param id 图片所在微博id
	 * @param url 图片url
	 * @param iv 显示图片的ImageZoomView
	 * @param picType  图片类型，取值为 thumb、mid、ori
	 */
	public void loadZoomPic(final long id, final String url, final ImageZoomView iv, final String picType, final ProgressDialog dialog) {
		if (null == url || "".equals(url)) {
            log("loadZoomPic url is null");
			return;
		}
		picName = "" + id + "_" + TextUtil.formatName(url) + "_" + picType;
		if (statusImageCache.containsKey(picName)) { //图像存在在软引用中，直接加载
			SoftReference<Drawable> softReference = statusImageCache.get(picName);
			if (softReference != null) {
				if (softReference.get() != null) {
					log("loadZoomPic softReference");
					iv.setImage(((BitmapDrawable)softReference.get()).getBitmap());
					return;
				}
			}
		}
		if (dialog != null){
			dialog.show();
		}
		//否则开新线程从本地或者网络加载
		executorService.submit(new Runnable() {
			BitmapDrawable bitmapDrawable = null;

			@Override
			public void run() {
				bitmapDrawable = localMemory.getDrawable(picName, LocalMemory.PRE);
				if (bitmapDrawable == null) { // 若本地没有指定图片，从网上下载
					try {
						bitmapDrawable = new BitmapDrawable(new URL(url)
								.openStream()); // 获取图片
						statusImageCache.put(picName, new SoftReference<Drawable>(
								bitmapDrawable)); // 加入软引用
						AsyncImageSaver.getInstance()
								.saveImage(bitmapDrawable,
										picName,
										LocalMemory.PRE); // 异步保存至本地
						log("loadZoomPic network");
					} catch (MalformedURLException e) {
						e.printStackTrace();
						log(e.toString());
					} catch (IOException e) {
						e.printStackTrace();
						log(e.toString());
					}
				}
				//handler在主线程加载图像
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (dialog != null){
							dialog.dismiss();
						}
						if (bitmapDrawable != null){
							iv.setImage(bitmapDrawable.getBitmap());
						}
					}
				});
			}
		});

	}
	
	/**
	 * clear image cache when switching user
	 */
	public void clearUserBuffer(){
		portraitImageCache.clear();
		preImageCache.clear();
	}

	void log(String msg) {
		Log.i("weibo", "AsyncImageLoader--" + msg);
	}

}
