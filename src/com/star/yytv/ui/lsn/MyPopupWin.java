package com.star.yytv.ui.lsn;

import com.star.yytv.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MyPopupWin {
	
	private Context mycontext;
	private View popView;
	private LinearLayout programnavi_pop;
	private LinearLayout popwin_layout;
	private LinearLayout msg_pop;
    private TextView pop_txt;
    private Button pop_close;
    private Button pop_checkin;
    private Button pop_detail;
    private Button pop_next;
    private Button pop_fav;
    private Button pop_item1;
    private Button pop_item2;
    private Button pop_item3;
    private PopupWindow popWindow;
    
	public MyPopupWin(Context context){
		this.mycontext = context;
		getViews();
        //需要全屏以实现背景置灰的目的
        popWindow = new PopupWindow(popView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
        //加载动画效果
//        popWindow.setAnimationStyle(R.style.AnimationPreview);
//        popwin_layout.setAnimation(AnimationUtils.loadAnimation(mycontext,R.anim.pop_fade_in));
        //使其聚集   
        popWindow.setFocusable(true);   
        // 设置允许在外点击消失   
        popWindow.setOutsideTouchable(true);                
        //这个是为了点击"返回Back"也能使其消失，并且并不会影响你的背景   
        popWindow.setBackgroundDrawable(new BitmapDrawable());       
	}
	
	private void getViews(){
		LayoutInflater layoutInflater = LayoutInflater.from(mycontext);               	   
        popView = layoutInflater.inflate(R.layout.programnavi_popwin, null);
        programnavi_pop = (LinearLayout)popView.findViewById(R.id.programnavi_pop);

        msg_pop = (LinearLayout)popView.findViewById(R.id.msg_pop);
        pop_txt = (TextView)popView.findViewById(R.id.pop_txt);
        pop_close = (Button)popView.findViewById(R.id.pop_close);
        pop_checkin = (Button)popView.findViewById(R.id.programnavi_pop_checkin);
        pop_detail = (Button)popView.findViewById(R.id.programnavi_pop_detail);
        pop_next = (Button)popView.findViewById(R.id.programnavi_pop_next);
        pop_fav = (Button)popView.findViewById(R.id.programnavi_pop_fav);
        pop_item1 = (Button)popView.findViewById(R.id.pop_item1);
        pop_item2 = (Button)popView.findViewById(R.id.pop_item2);
        pop_item3 = (Button)popView.findViewById(R.id.pop_item3);
	}
	
	public void popWinDismiss(){
		popWindow.dismiss();
//        popwin_layout.setAnimation(AnimationUtils.loadAnimation(mycontext,R.anim.pop_fade_out));
	}
	
	public void setPopWinLocation(View v){
		//X,Y=0,center居中
		popWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		popWindow.update();
	}
	
	public void setProgramnaviPopShow(){
		programnavi_pop.setVisibility(View.VISIBLE);
	}
	
	public void setMsgPopShow(){
		msg_pop.setVisibility(View.VISIBLE);
	}
	
	public void setPopDetailShow(){
		pop_detail.setVisibility(View.VISIBLE);
	}
	
	public void setPopItemShow(){
		pop_item3.setVisibility(View.VISIBLE);
	}
	
	//
	public void setPopText(String txt){
		pop_txt.setText(txt);
	}
	
	public void setPopFavText(String txt){
		pop_fav.setText(txt);
	}
	
	public void setPopItem1Text(String txt){
		pop_item1.setText(txt);
	}
	
	public void setPopItem2Text(String txt){
		pop_item2.setText(txt);
	}
	
	public void setPopItem3Text(String txt){
		pop_item3.setText(txt);
	}
	
	//
	public void setPopCLose(OnClickListener l){
		pop_close.setOnClickListener(l);
	}
	
	public void setPopCheckin(OnClickListener l){
		pop_checkin.setOnClickListener(l);
	}
	
	public void setPopDetail(OnClickListener l){
		pop_detail.setOnClickListener(l);
	}
	
	public void setPopNext(OnClickListener l){
		pop_next.setOnClickListener(l);
	}
	
	public void setPopFav(OnClickListener l){
		pop_fav.setOnClickListener(l);
	}
	
	public void setPopItem1(OnClickListener l){
		pop_item1.setOnClickListener(l);
	}
	
	public void setPopItem2(OnClickListener l){
		pop_item2.setOnClickListener(l);
	}
	
	public void setPopItem3(OnClickListener l){
		pop_item3.setOnClickListener(l);
	}
}
