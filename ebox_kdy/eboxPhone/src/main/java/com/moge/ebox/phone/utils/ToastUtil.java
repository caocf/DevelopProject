package com.moge.ebox.phone.utils;

import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.config.Constants;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtil
{
    private static Context mContext;

    private static Toast mToast;

    public static void init(Context context)
    {
        mContext = context;
    }

    public static void showToastShort(String msg)
    {
    	if(mContext==null){
    		mContext = EboxApplication.getInstance();
    	}
        String text = TextUtils.isEmpty(msg) ? "" : FunctionUtils.toDBC(msg);

        int duration = Toast.LENGTH_SHORT;

        if (null != mToast)
        {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        else
        {
            mToast = Toast.makeText(mContext, text, duration);
        }
        mToast.show();
    }

    public static void showToastLong(String msg)
    {
        Toast.makeText(mContext,
                TextUtils.isEmpty(msg) ? "" : FunctionUtils.toDBC(msg),
                Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(int msgId)
    {
    	if(mContext==null){
    		mContext = EboxApplication.getInstance();
    	}
        String msg = mContext.getResources().getString(msgId);
        Toast.makeText(mContext,
                TextUtils.isEmpty(msg) ? "" : FunctionUtils.toDBC(msg),
                Toast.LENGTH_SHORT).show();
    }
    
    public static void showToastShort(Context context,int msgId)
    {
        String msg = context.getResources().getString(msgId);
        Toast.makeText(context,
                TextUtils.isEmpty(msg) ? "" : FunctionUtils.toDBC(msg),
                Toast.LENGTH_SHORT).show();
    }


    public static void showToastLong(int msgId)
    {
    	if(mContext==null){
    		mContext = EboxApplication.getInstance();
    	}
        String msg = mContext.getResources().getString(msgId);
        Toast.makeText(mContext,
                TextUtils.isEmpty(msg) ? "" : FunctionUtils.toDBC(msg),
                Toast.LENGTH_LONG).show();
    }


    /**
     * 测试显示
     */
    public static void showLog(String log){
        if (Constants.config== Constants.Config.DEBUG){
            ToastUtil.showToastLong(log);
        }
    }
}
