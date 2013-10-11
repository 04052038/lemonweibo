package com.star.weibo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;

import com.star.weibo.Home;
import com.star.yytv.Log;

import com.star.yytv.model.OAuthInfoManager;

public class LocalMemory {
	public static final String PORTRAIT="tvpina"+"/"+"portrait"; //头像保存文件夹
	public static final String PRE="tvpina"+"/"+"pre"; //微博图片保存文件夹
	public static final String MOVIE="tvpina"+"/"+"MOVIE"; //MOVIE
	public static final String SERIE="tvpina"+"/"+"SERIE"; //SERIE
	public static final String COLIMN="tvpina"+"/"+"COLIMN"; //栏目
	
	//weibo type, save pic by type
	public static final String WEIBOTYPE_STATUS = "STATUS";
	public static final String WEIBOTYPE_ATME = "ATME";
	public static final String WEIBOTYPE_COMMENT = "COMMENT";
	
	//max file count in PORTRAIT or PRE directory
	public static final int WEIBO_PIC_MAX_NUM = 500;
	
	
	/**
	 * 保存图像至本地
	 * @param drawable
	 * @param filename 图像名
	 * @param cate PORTRAIT（头像） 或者 PRE（微博图片）
	 */
	public void saveDrawable(BitmapDrawable drawable, String filename,String cate) {
		
		File dir = null;
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED))
		{//内部存储	
			log("saveDrawable, no weiboType: memory write");
			String interStore = Home.getInstance().getFilesDir().getAbsolutePath();
			//先判断目录是否存在
			dir = new File(interStore + "/" + cate);
			if (!dir.exists()) { //不存在则创建
				dir.mkdirs();
			}
			//判断文件是否存在
			File image = new File(interStore + "/" + cate + "/" + filename);
			if (!image.exists()) { //不存在则保存
				saveImage(drawable, image);
			}			
		}
		else
		{//sd卡存储	
			log("saveDrawable, no weiboType: sd card write");
			String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
			//先判断目录是否存在
			dir = new File(sdcard + "/" + cate);
			if (!dir.exists()) { //不存在则创建
				dir.mkdirs();
			}
			//判断文件是否存在
			File image = new File(sdcard + "/" + cate + "/" + filename);
			if (!image.exists()) { //不存在则保存
				saveImage(drawable, image);
			}
		}	
		
	}
	
	/**
	 * 保存图像至本地
	 * @param drawable
	 * @param filename 图像名
	 * @param cate PORTRAIT（头像） 或者 PRE（微博图片）
	 * @param weiboType WEIBOTYPE_STATUS(friendsTimeline) or WEIBOTYPE_ATME(at me) or WEIBOTYPE_COMMENT(to me comment)
	 * store path:  U123123/status/portrait
				    U123123/status/pre
	 */
	public void saveDrawable(BitmapDrawable drawable, String filename, String cate, String weiboType) {
		
		if (weiboType == null || weiboType.trim().equalsIgnoreCase("")){
			log("saveDrawable: weiboType is null");
			return;
		}
		log("saveDrawable userid = " + OAuthInfoManager.getInstance().getWeiboUserId());
		File dir = null;
		String rootPath = null;
		String userPath = "tvpina" + "/" + "U" + OAuthInfoManager.getInstance().getWeiboUserId();
		String storePath = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED))
		{   //内部存储	
			rootPath = Home.getInstance().getFilesDir().getAbsolutePath();	
			log("saveDrawable, with weiboType = " + weiboType + ", : memory write");
		}
		else
		{   //sd卡存储			
			rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();	
			log("saveDrawable, with weiboType = " + weiboType + ", : sd card write");
		}
		userPath = rootPath + "/" + userPath;
		storePath = userPath + "/" + weiboType + "/" + cate;
		dir = new File(storePath);
		log("saveDrawable with weiboType, storePath = " + dir.getAbsolutePath());
		if (!dir.exists()) { //不存在则创建
			dir.mkdirs();
		}
		//判断文件是否存在
		File image = new File(storePath + "/" + filename);
		if (!image.exists()) { //不存在则保存
			saveImage(drawable, image);
		}	
	}
	
	/**
	 * 从本地取出图像
	 * @param filename 图像名
	 * @param cate PORTRAIT（头像） 或者 PRE（微博图片）
	 * @return
	 */
	public BitmapDrawable getDrawable(String filename,String cate){
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED))
		{//内部存储
			log("getDrawable, no weiboType: memory read");
			String interStore = Home.getInstance().getFilesDir().getAbsolutePath();
			File image = new File(interStore + "/" + cate+"/"+filename);
			try
			{
				if(image.exists())
				{
					FileInputStream fileInputStream=new FileInputStream(image);
					BitmapDrawable drawable=new BitmapDrawable(fileInputStream);
					return drawable;
				}
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}	
		}
		else
		{//sd卡存储
			log("getDrawable, no weiboType: sd card read");
			String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
			File image = new File(sdcard + "/" + cate+"/"+filename);
			if(image.exists()){
				try {
					FileInputStream fileInputStream=new FileInputStream(image);
					BitmapDrawable drawable=new BitmapDrawable(fileInputStream);
					return drawable;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}	
		}	
		return null;
		
	}
	
	/**
	 * 从本地取出图像
	 * @param filename 图像名
	 * @param cate PORTRAIT（头像） 或者 PRE（微博图片）
	 * @param weiboType WEIBOTYPE_STATUS(friendsTimeline) or WEIBOTYPE_ATME(at me) or WEIBOTYPE_COMMENT(to me comment)
	 * @return
	 * store path:  U123123/status/portrait
				    U123123/status/pre
	 */
	public BitmapDrawable getDrawable(String filename, String cate, String weiboType){
		if (weiboType == null || weiboType.trim().equalsIgnoreCase("")){
			log("getDrawable: weiboType is null");
			return null;
		}
		log("getDrawable userid = " + OAuthInfoManager.getInstance().getWeiboUserId());
		File dir = null;
		String rootPath = null;
		String userPath = "tvpina" + "/" + "U" + OAuthInfoManager.getInstance().getWeiboUserId();
		String storePath = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED))
		{   //内部存储	
			log("getDrawable, with weiboType = " + weiboType + ", : memory read");
			rootPath = Home.getInstance().getFilesDir().getAbsolutePath();				
		}
		else
		{   //sd卡存储	
			log("getDrawable, with weiboType = " + weiboType + ", : sd card read");
			rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();	
		}
		userPath = rootPath + "/" + userPath;
		storePath = userPath + "/" + weiboType + "/" + cate;
		log("saveDrawable with weiboType, storePath = " + storePath);
		File image = new File(storePath + "/" + filename);
		try
		{
			if(image.exists())
			{
				FileInputStream fileInputStream=new FileInputStream(image);
				BitmapDrawable drawable=new BitmapDrawable(fileInputStream);
				return drawable;
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}	
		return null;
		
	}
	
	private void saveImage(BitmapDrawable drawable, File image){
		try 
		{	
			image.createNewFile();
			FileOutputStream fileOutputStream=new FileOutputStream(image);
			if(drawable.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)){
				fileOutputStream.flush();
			}
			fileOutputStream.close();
				
		} catch (IOException e) {
			e.printStackTrace();
			log(e.toString());
		}
	}
	
	/**
	 * delete all files in user directory
	 * @param userId
	 */
	public void clearDrawableUser(String userId){
		String rootPath = Home.getInstance().getFilesDir().getAbsolutePath();
		clearDrawableUser(userId, rootPath);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//sd卡存储			
			rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();	
			clearDrawableUser(userId, rootPath);
		}
	}
	
	/**
	 * delete all files, including user directory, PORTRAIT and PRE
	 * @param userId
	 */
	public void clearDrawableAll(String userId){
		clearDrawableUser(userId);
		clearDrawableOther();
	}
	
	private void clearDrawableUser(String userId, String rootPath){
		String userPath = "tvpina" + "/" + "U" + userId;
		String storePath = rootPath + "/" + userPath;
		File dir = new File(storePath);
		log("clearDrawableUser, storePath = " + dir.getAbsolutePath());
		if (dir.exists()) { 
			deleteFile(dir);
		}
	}
	
	
	/**
	 * clear pic by weiboType
	 * dir structure: U123123/status/portrait
	 *                U123123/status/pre
	 * both portrait and pre are cleared
	 * @param weiboType
	 */
	public void clearDrawable(String weiboType){
		if (weiboType == null || weiboType.trim().equalsIgnoreCase("")){
			log("clearDrawable: weiboType is null");
			return;
		}
		File dir = null;
		String rootPath = null;
		String userid = "" + OAuthInfoManager.getInstance().getWeiboUserId();
		String userPath = "tvpina" + "/" + "U" + userid;
		String storePath = null;
		rootPath = Home.getInstance().getFilesDir().getAbsolutePath();
		userPath = rootPath + "/" + userPath;
		storePath = userPath + "/" + weiboType;
		dir = new File(storePath);
		log("clearDrawable memory, storePath = " + storePath);
		if (dir.exists()) { //不存在则创建
			deleteFile(dir);
		}
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//sd卡存储			
			rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();	
			userPath = "tvpina" + "/" + "U" + userid;
			userPath = rootPath + "/" + userPath;
			storePath = userPath + "/" + weiboType;
			dir = new File(storePath);
			log("clearDrawable sd card, storePath = " + storePath);
			if (dir.exists()) { //不存在则创建
				deleteFile(dir);
			}
		}
	}
	
	/**
	 * clear directory PORTRAIT and PRE
	 */
	public void clearDrawableOther(){
		String rootPath = Home.getInstance().getFilesDir().getAbsolutePath();
		clearDrawableOther(rootPath);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//sd卡存储			
			rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();	
			clearDrawableOther(rootPath);
		}
	}
	
	/**
	 * clear directory PORTRAIT and PRE when file count over WEIBO_PIC_MAX_NUM
	 */
	public void clearDrawableOtherNeed(){
		String rootPath = Home.getInstance().getFilesDir().getAbsolutePath();
		clearDrawableOtherNeed(rootPath);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//sd卡存储			
			rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();	
			clearDrawableOtherNeed(rootPath);
		}
	}
	
	/**
	 * 
	 * @param rootPath  memory rootPath or sd card rootPath
	 */
	private void clearDrawableOther(String rootPath){
		File dir = null;
		String storePath = rootPath + "/" + PORTRAIT;
		dir = new File(storePath);
		log("clearDrawableOther, storePath = " + dir.getAbsolutePath());
		if (dir.exists()) { 
			deleteFile(dir);
		}
		storePath = rootPath + "/" + PRE;
		dir = new File(storePath);
		log("clearDrawableOther, storePath = " + dir.getAbsolutePath());
		if (dir.exists()) { 
			deleteFile(dir);
		}
	}
	
	/**
	 * clear directory PORTRAIT and PRE when file count over WEIBO_PIC_MAX_NUM
	 * @param rootPath  memory rootPath or sd card rootPath
	 * 
	 */
	private void clearDrawableOtherNeed(String rootPath){
		File dir = null;
		int fileNum = 0;
		String storePath = rootPath + "/" + PORTRAIT;
		dir = new File(storePath);
		log("clearDrawableOther, storePath = " + dir.getAbsolutePath());
		if (dir.exists()) { 
			fileNum = getFileCount(dir);
			if (fileNum > WEIBO_PIC_MAX_NUM){
				deleteFile(dir);
			}	
		}
		storePath = rootPath + "/" + PRE;
		dir = new File(storePath);
		log("clearDrawableOther, storePath = " + dir.getAbsolutePath());
		if (dir.exists()) { 
			fileNum = getFileCount(dir);
			if (fileNum > WEIBO_PIC_MAX_NUM){
				deleteFile(dir);
			}	
		}
	}
	
	private int getFileCount(File dir){
		File[] files = dir.listFiles();
		return files.length;
	}
	
	
	public void deleteFile(File file) {
		try{
			if (file.exists()) {
				if (file.isFile()) {
					file.delete();
				} else if (file.isDirectory()) { 
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						deleteFile(files[i]);
					}
					file.delete();
				}
			}
		}catch(Exception ex){
			log(ex.getMessage());
		}
			
	}

	void log(String msg) {
		Log.i("weibo", "LocalMemory--" + msg);
	}
}
