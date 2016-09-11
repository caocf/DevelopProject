package com.ebox.pub.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ebox.R;

public class Tip {
    private MyDialog mDialog;
    private TextView title;
    private TextView description;
    private Handler closeHandler;
    private onDismissListener listener;
    private Runnable run;
    private Activity mActivity;

    public Tip(Context context, String text, onDismissListener listener) {
    	mActivity=(Activity) context;
    	if(mActivity.isFinishing())
		{
			return;
		}
    	this.listener = listener;
        mDialog = new MyDialog(mActivity, R.style.pub_dialog);
        Window window = mDialog.getWindow();
        //WindowManager.LayoutParams wl = window.getAttributes();
        //wl.x = -30;
        //wl.y = 20;
        //window.setAttributes(wl);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        //window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
       View v= View.inflate(context,R.layout.pub_tip,null);
        mDialog.setContentView(v);
        MGViewUtil.scaleContentView((ViewGroup)v);
        title = (TextView) mDialog.findViewById(R.id.title);
        title.setVisibility(View.GONE);
        description = (TextView) mDialog.findViewById(R.id.description);
        description.setText(text);

        mDialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
    }
    
    public Dialog getDialog()
    {
    	return mDialog;
    }
    
    public void closeTip()
    {
    	if(mDialog != null
				&& mDialog.isShowing())
		{
    		mDialog.dismiss();
		}
    }
    
    public void setTitle(String text)
    {
    	title.setText(text);
    	title.setVisibility(View.VISIBLE);
    }

    public void show(long time) {
    	if(mDialog == null)
    	{
    		return;
    	}
    	
    	long showTime = 3000;
    	
    	if(time != 0)
    	{
    		showTime = time;
    	}
        mDialog.show();
        mDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface arg0) {
            	if(listener != null)
            	{
            		listener.onDismiss();
            	}
        		mDialog = null;
        		if(closeHandler != null)
        		{
        			closeHandler.removeCallbacks(run);
        			closeHandler = null;
        		}
             }
            });
        
        closeHandler = new Handler();
        run = new Runnable() 
        {
            public void run()
            {
            	close();
            	closeHandler = null;
            }
        };
        closeHandler.postDelayed(run, showTime);
    }
    
    private void close()
    {
    	if(mDialog != null && mDialog.isShowing())
    	{
    		mDialog.dismiss();
    	}
    }
    
    public interface onDismissListener
	{
		void onDismiss();
	}
    
	private class MyDialog extends Dialog
    {
    	private Activity mActivity;

		public MyDialog(Context context, int theme) {
			super(context, theme);
			mActivity=(Activity) context;
		}
		public MyDialog(Context context) {
			super(context);
			mActivity=(Activity) context;
		}
		@Override
		public void dismiss() {
			if (mActivity!=null&&!mActivity.isFinishing()) 
			{
				super.dismiss();
			}
		}
    }
}