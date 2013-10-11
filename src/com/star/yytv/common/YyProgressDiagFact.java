package com.star.yytv.common;

import android.app.ProgressDialog;
import android.content.Context;

public class YyProgressDiagFact{
	
	public static ProgressDialog getYyProgressDiag(Context context)
	{
		ProgressDialog dialog = new ProgressDialog(context);
		
		dialog.setIndeterminate(true);
		dialog.setTitle("");
		dialog.setMessage("正在加载，请稍候  . . .  ");
		
		return dialog;
	}

}
