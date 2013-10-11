package com.star.yytv.controller.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.star.yytv.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.star.yytv.Log;

public class UpdateService extends Service {
	//下载状态 
	private final static int DOWNLOAD_COMPLETE = 0; 
	private final static int DOWNLOAD_FAIL = 1;
	
	//文件存储 
	private File updateDir = null; 
	private File updateFile = null; 
	 
	//通知栏 
	private NotificationManager updateNotificationManager = null; 
	private Notification updateNotification = null; 
	//通知栏跳转Intent 
	private Intent updateIntent = null; 
	private PendingIntent updatePendingIntent = null;
	
	private String appTitle = "";
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("[TBY]", "start update service");
		appTitle = getResources().getString(R.string.app_name);
		//获取传值 
	    //titleId = intent.getIntExtra("titleId", 0); 
	    //创建文件 
	    if(android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())){ 
	        updateDir = new File(Environment.getExternalStorageDirectory(), Global.downloadDir); 
	        updateFile = new File(updateDir.getPath(), "yytv.apk");	        
	    } else {
	    	updateDir = new File(getFilesDir(), Global.downloadDir);
	    	updateFile = new File(updateDir.getPath(), "yytv.apk");
	    }
	 
	    this.updateNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE); 
	    this.updateNotification = new Notification(); 
	 
	    //设置下载过程中，点击通知栏，回到主界面 
	    //updateIntent = new Intent(this, AdvanceSettingActivity.class); 
	    updateIntent = new Intent(this, this.getApplicationContext().getClass());
	    updateIntent.setAction(Intent.ACTION_MAIN);
	    updateIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	    updatePendingIntent = PendingIntent.getActivity(this, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT); 
	    //设置通知栏显示内容 
	    updateNotification.icon = R.drawable.download; 
	    updateNotification.tickerText = "开始下载"; 
	    updateNotification.setLatestEventInfo(this, appTitle, "0%", updatePendingIntent); 
	    updateNotification.flags = Notification.FLAG_AUTO_CANCEL;
	    
	    //发出通知 
	    updateNotificationManager.notify(0, updateNotification); 
	 
	    //开启一个新的线程下载APK
	    new Thread(new updateRunnable()).start();
	      
	    return super.onStartCommand(intent, flags, startId); 
	}
	
	private Handler updateHandler = new Handler(){ 
	    @Override
	    public void handleMessage(Message msg) {
	    	switch(msg.what){ 
            case DOWNLOAD_COMPLETE: 
            	Log.d("[TBY]", "download complete.");
                //点击安装PendingIntent 
                Uri uri = Uri.fromFile(updateFile); 
                Intent installIntent = new Intent(Intent.ACTION_VIEW); 
                installIntent.setDataAndType(uri, "application/vnd.android.package-archive"); 
                updatePendingIntent = PendingIntent.getActivity(UpdateService.this, 0, installIntent, PendingIntent.FLAG_UPDATE_CURRENT); 
                  
                updateNotification.defaults = Notification.DEFAULT_SOUND;//铃声提醒  
                updateNotification.setLatestEventInfo(UpdateService.this, appTitle, "下载完成，点击安装。", updatePendingIntent); 
                updateNotificationManager.notify(0, updateNotification); 
                  
                //停止服务 
                stopSelf();
                break;
            case DOWNLOAD_FAIL:
            	Log.d("[TBY]", "download fail.");
                //下载失败 
                updateNotification.setLatestEventInfo(UpdateService.this, appTitle, "下载失败，点击返回。", updatePendingIntent); 
                updateNotificationManager.notify(0, updateNotification);
                //停止服务 
                stopSelf();
                break;
            default: 
            	stopSelf(); 
            } 
	    } 
	}; 
	
	class updateRunnable implements Runnable { 
        Message message = updateHandler.obtainMessage(); 
        public void run() { 
            message.what = DOWNLOAD_COMPLETE; 
            try{ 
                //增加权限<USES-PERMISSION android:name="android.permission.WRITE_EXTERNAL_STORAGE">; 
                if(!updateDir.exists()){ 
                    updateDir.mkdirs(); 
                } 
                
                if(!updateFile.exists()){ 
                    updateFile.createNewFile(); 
                } 
                //下载函数，以QQ为例子 
                //增加权限<USES-PERMISSION android:name="android.permission.INTERNET">; 
                long downloadSize = downloadUpdateFile(Global.downloadUrl,
                		updateFile); 
                if(downloadSize > 0){ 
                    //下载成功 
                    updateHandler.sendMessage(message); 
                } 
            } catch (Exception ex){ 
                ex.printStackTrace(); 
                message.what = DOWNLOAD_FAIL; 
                //下载失败 
                updateHandler.sendMessage(message); 
            } 
        } 
    }
	
	public long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception { 
        int downloadCount = 0; 
        int currentSize = 0; 
        long totalSize = 0; 
        int updateTotalSize = 0; 
          
        HttpURLConnection httpConnection = null; 
        InputStream is = null; 
        FileOutputStream fos = null; 
          
        try { 
            URL url = new URL(downloadUrl); 
            httpConnection = (HttpURLConnection)url.openConnection(); 
            httpConnection.setRequestProperty("User-Agent", "PacificHttpClient"); 
            if(currentSize > 0) { 
                httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-"); 
            } 
            httpConnection.setConnectTimeout(10000); 
            httpConnection.setReadTimeout(20000); 
            updateTotalSize = httpConnection.getContentLength(); 
            if (httpConnection.getResponseCode() == 404) { 
                throw new Exception("fail!"); 
            } 
            is = httpConnection.getInputStream();                    
            fos = new FileOutputStream(saveFile, false); 
            byte buffer[] = new byte[4096]; 
            int readsize = 0; 
            while((readsize = is.read(buffer)) > 0){ 
                fos.write(buffer, 0, readsize); 
                totalSize += readsize; 
                //为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次 
                if((downloadCount == 0)||(int) (totalSize*100/updateTotalSize)-10>downloadCount){  
                    downloadCount += 10; 
                    updateNotification.setLatestEventInfo(UpdateService.this, "正在下载", (int)totalSize*100/updateTotalSize+"%", updatePendingIntent); 
                    updateNotificationManager.notify(0, updateNotification); 
                }                         
            } 
        } finally { 
            if(httpConnection != null) { 
                httpConnection.disconnect(); 
            } 
            if(is != null) { 
                is.close(); 
            } 
            if(fos != null) { 
                fos.close(); 
            } 
        } 
        return totalSize; 
    }
}
