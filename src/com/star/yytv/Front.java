package com.star.yytv;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * <p>文件名称: Front.java </p>
 * <p>文件描述: 前台界面以及后台下载数据承载Activity。</p>
 * <p>版权所有: 版权所有(C)2012-2016</p>
 * <p>公   司: 上海曜众信息科技有限公司</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>完成日期：2012-09-22</p>
 * <p>修改记录1: </p>
 * <pre>
 *    修改日期：
 *    版 本 号：
 *    修 改 人：
 *    修改内容：
 * </pre>
 * <p></p>
 * @version 1.0
 * @author 
 */

public class Front extends Activity 
{
	//关闭当前窗体
	public final static int CLOSE_ACT = 1;
	//后台下载数据
	public final static int DOWNLOAD_ACT = 2;
	
	//界面默认保持时长3秒
	public final static int HOLD_TIME = 2000;
	
	//界面是否活动
	private boolean active = true;
	//时钟任务启动时间
	private long startTime = 0;	
	//时钟
	private Timer timer = null;
	//时钟任务
	private  FrontTimerTask task = null; 
	
	//时钟任务处理器
	private  FrontTimerHandler timerHandler = null;
	
	public ImageView front_img;
	
	private int[] imageResId;
	
	private int imgId = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.front);
		
		front_img = (ImageView) findViewById(R.id.front_img);
		imageResId = new int[] { R.drawable.welcome_page, R.drawable.welcome_page};
		init();
	}	

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) 
		{
			task.setFrontActive(false);
		}

		return true;

	}
	
	/**
	 * 信息初始化
	 */
	public void init()
	{
		//1、设置定时
		timer = new Timer(true);
		startTime = System.currentTimeMillis();		
		timerHandler = new FrontTimerHandler(Front.this);		
		task = new FrontTimerTask(startTime,active,timer,timerHandler);
		timer.schedule(task, 0, 1000);
       
		//2、下载数据:频道和预约节目
//		YyTvLogger.Log(Front.class.getName(),"init beginning2...");
//		Message message = new Message();
//		message.what = DOWNLOAD_ACT;
//		timerHandler.sendMessage(message);
				
	}
	
	private class FrontTimerTask extends TimerTask{
		private long startTime = 0;
		
		private boolean frontActive = true;
		
		private Timer timer = null;
		
		private FrontTimerHandler timerHandler = null;
		
		public FrontTimerTask(long startTime,boolean frontActive,Timer timer,
				FrontTimerHandler timerHandler)
		{
			this.startTime = startTime;
			this.frontActive = frontActive;
			this.timer = timer;
			this.timerHandler = timerHandler;
		}
		
		@Override
		public void run() {
			
			timerHandler.post(runnableUi);
			
			if (this.scheduledExecutionTime() - startTime >= Front.HOLD_TIME 
					|| !frontActive) {
			
				Message message = new Message();
				message.what = Front.CLOSE_ACT;

				timerHandler.sendMessage(message);
				
				timer.cancel();
				this.cancel();
			}
		}
		
		public void setFrontActive(boolean frontActive)
		{
			this.frontActive = frontActive;
		}
	}
	
	Runnable runnableUi = new Runnable(){
		
        @Override  
        public void run() {
        	
        	front_img.setBackgroundResource(imageResId[imgId]);
        	imgId++;
			if(imgId == 2){
				imgId = 0;
			}
        }  
          
    };
   
}
