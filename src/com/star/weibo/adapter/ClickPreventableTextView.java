package com.star.weibo.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import android.view.MotionEvent;
import android.text.Layout;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;

/**
 * 重写TextView的onTouchEvent方法，使得点击text中的 ClickableSpan文本时，触发ClickableSpan的
 * onClick，点击非ClickableSpan文本时，不触发事件，从而可以触发父节点ListView的OnItemClick
 * 该类只能用在ListView的item中
 * @author xujun 20120915
 *
 */
public class ClickPreventableTextView extends TextView {

	public ClickPreventableTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ClickPreventableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ClickPreventableTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	        TextView widget = (TextView) this;
	        Object text = widget.getText();
	        if (text instanceof Spanned) {
	        	Spanned buffer = (Spanned) text;

	            int action = event.getAction();

	            if (action == MotionEvent.ACTION_UP
	                    || action == MotionEvent.ACTION_DOWN) {
	                int x = (int) event.getX();
	                int y = (int) event.getY();

	                x -= widget.getTotalPaddingLeft();
	                y -= widget.getTotalPaddingTop();

	                x += widget.getScrollX();
	                y += widget.getScrollY();

	                Layout layout = widget.getLayout();
	                int line = layout.getLineForVertical(y);
	                int off = layout.getOffsetForHorizontal(line, x);

	                ClickableSpan[] link = buffer.getSpans(off, off,
	                        ClickableSpan.class);

	                if (link.length != 0) {
	                    if (action == MotionEvent.ACTION_UP) {
	                        link[0].onClick(widget);
	                    } else if (action == MotionEvent.ACTION_DOWN) {
	                        // Selection.setSelection(buffer,
	                         //        buffer.getSpanStart(link[0]),
	                               //  buffer.getSpanEnd(link[0]));
	                    }
	                    return true;
	                }
	            }

	        }

	        return false;
	    }
	
}
