package com.star.yytv;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * 
 * @author yunlong
 *
 */
public class DotMarks extends LinearLayout {
	private ImageView mark_one, mark_two, mark_three, mark_four, mark_five;
	private Drawable guide_focus = getResources().getDrawable(
			R.drawable.guide_focus);
	private Drawable guide_default = getResources().getDrawable(
			R.drawable.guide_default);

	public DotMarks(Context context) {
		super(context);
		init(context);
	}

	public DotMarks(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	/**
	 * 初始化
	 * @param context
	 * 上下文对象
	 */
	private void init(Context context) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View dotmarks = layoutInflater.inflate(R.layout.dot_marks, null);
		this.addView(dotmarks);
		mark_one = (ImageView) dotmarks.findViewById(R.id.dot_mark1);
		mark_two = (ImageView) dotmarks.findViewById(R.id.dot_mark2);
		mark_three = (ImageView) dotmarks.findViewById(R.id.dot_mark3);
		mark_four = (ImageView) dotmarks.findViewById(R.id.dot_mark4);
		mark_five = (ImageView) dotmarks.findViewById(R.id.dot_mark5);
		
		//设置一个默认值
		mark_one.setImageResource(R.drawable.guide_focus);
		mark_two.setImageResource(R.drawable.guide_default);
		mark_three.setImageResource(R.drawable.guide_default);
		mark_four.setImageResource(R.drawable.guide_default);
		mark_five.setImageResource(R.drawable.guide_default);
	}

	/**
	 * 
	 * 根据传递进来的页面的索引切换小图标的显示
	 * 
	 * */
	public void updateMark(int index) {
		switch (index) {
		case 0:
			mark_one.setImageDrawable(guide_focus);
			mark_two.setImageDrawable(guide_default);
			mark_three.setImageDrawable(guide_default);
			mark_four.setImageDrawable(guide_default);
			mark_five.setImageDrawable(guide_default);
			break;
		case 1:
			mark_one.setImageDrawable(guide_default);
			mark_two.setImageDrawable(guide_focus);
			mark_three.setImageDrawable(guide_default);
			mark_four.setImageDrawable(guide_default);
			mark_five.setImageDrawable(guide_default);
			break;
		case 2:
			mark_one.setImageDrawable(guide_default);
			mark_two.setImageDrawable(guide_default);
			mark_three.setImageDrawable(guide_focus);
			mark_four.setImageDrawable(guide_default);
			mark_five.setImageDrawable(guide_default);
			break;
		case 3:
			mark_one.setImageDrawable(guide_default);
			mark_two.setImageDrawable(guide_default);
			mark_three.setImageDrawable(guide_default);
			mark_four.setImageDrawable(guide_focus);
			mark_five.setImageDrawable(guide_default);
			break;
		case 4:
			mark_one.setImageDrawable(guide_default);
			mark_two.setImageDrawable(guide_default);
			mark_three.setImageDrawable(guide_default);
			mark_four.setImageDrawable(guide_default);
			mark_five.setImageDrawable(guide_focus);
			break;

		default:
			break;
		}
	}
	
	public void setMarkShow(){
		mark_five.setVisibility(View.VISIBLE);
	}

}

