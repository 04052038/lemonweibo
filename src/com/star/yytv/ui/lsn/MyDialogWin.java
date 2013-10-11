package com.star.yytv.ui.lsn;

import com.star.yytv.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyDialogWin extends Dialog{
	private Context mycontext;
	private LinearLayout programnavi_pop;
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
    
	public MyDialogWin(Context context){
		super(context, R.style.MyDialog);
		this.mycontext = context;
	}
	
	public MyDialogWin(Context context, int theme){
		super(context, theme);
		this.mycontext = context;
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.programnavi_popwin);
        this.setCanceledOnTouchOutside(true);        
        getViews();
        
    }
	
	private void getViews(){

        programnavi_pop = (LinearLayout)findViewById(R.id.programnavi_pop);
        msg_pop = (LinearLayout)findViewById(R.id.msg_pop);
        pop_txt = (TextView)findViewById(R.id.pop_txt);
//        pop_close = (Button)findViewById(R.id.pop_close);
        pop_checkin = (Button)findViewById(R.id.programnavi_pop_checkin);
        pop_detail = (Button)findViewById(R.id.programnavi_pop_detail);
        pop_next = (Button)findViewById(R.id.programnavi_pop_next);
        pop_fav = (Button)findViewById(R.id.programnavi_pop_fav);
        pop_item1 = (Button)findViewById(R.id.pop_item1);
        pop_item2 = (Button)findViewById(R.id.pop_item2);
        pop_item3 = (Button)findViewById(R.id.pop_item3);
	}
	
	//
	public void setProgramnaviPopShow(){
		programnavi_pop.setVisibility(View.VISIBLE);
	}
	
	public void setMsgPopShow(){
		msg_pop.setVisibility(View.VISIBLE);
	}
	
	public void setPopDetailShow(){
		pop_detail.setVisibility(View.VISIBLE);
	}
	
	public void setPopItem3Show(){
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
	public void setPopCheckin(View.OnClickListener l){
		pop_checkin.setOnClickListener(l);
	}
	
	public void setPopDetail(View.OnClickListener l){
		pop_detail.setOnClickListener(l);
	}
	
	public void setPopNext(View.OnClickListener l){
		pop_next.setOnClickListener(l);
	}
	
	public void setPopFav(View.OnClickListener l){
		pop_fav.setOnClickListener(l);
	}
	
	public void setPopItem1(View.OnClickListener l){
		pop_item1.setOnClickListener(l);
	}
	
	public void setPopItem2(View.OnClickListener l){
		pop_item2.setOnClickListener(l);
	}
	
	public void setPopItem3(View.OnClickListener l){
		pop_item3.setOnClickListener(l);
	}
}
