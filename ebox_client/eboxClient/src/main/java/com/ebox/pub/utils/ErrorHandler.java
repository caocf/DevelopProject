package com.ebox.pub.utils;

import android.app.Activity;

import com.ebox.R;
import com.ebox.pub.websocket.helper.AppException;

public class ErrorHandler {
	public static void dealException(Activity activity, Object e)
	{
		if(e instanceof AppException)
		{
			//Toast.makeText(AppApplication.globalContext, ((AppException)e).getMessage(), Toast.LENGTH_LONG).show();
			new Tip(activity, ((AppException)e).getMessage(),null).show(0);
		}
		else
		{
			//Toast.makeText(AppApplication.globalContext, R.string.connect_failed, Toast.LENGTH_LONG).show();
			new Tip(activity, 
					activity.getResources().getString(R.string.pub_connect_failed),null).show(0);
		}
	}
	
	public static String getExceptionString(Activity activity, Object e)
	{
		if(e instanceof AppException)
		{
			return ((AppException)e).getMessage();
		}
		else
		{
			return activity.getResources().getString(R.string.pub_connect_failed);
		}
	}
}
