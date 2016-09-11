package com.ebox.pub.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;

public class DialogUtil {

	public Dialog dialog;
	public class CustomeDialog{
		public Dialog dialog;
		public ImageView spaceshipImage;
		public Context context;
	}

	private CustomeDialog mProgressDialog;
	
	public Activity activity;

	public void createDialog(Context mContext, View view) {
		dialog = new Dialog(mContext);
		activity = (Activity) mContext;
		dialog.setContentView(view);
	}
	
	public void createDialog(Context context, View view, int style, boolean cancel){
		dialog = new Dialog(context, style);
		dialog.setContentView(view);
		dialog.setCancelable(cancel);
		activity = (Activity) context;
	}

	public void createDialog(Context mContext, View view, LayoutParams params) {
		activity = (Activity) mContext;
		dialog = new Dialog(mContext, R.style.pub_dialog);
		dialog.setContentView(view, params);
		dialog.setCancelable(false);
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
				}
				return false;
			}

		});
	}

	public void createDialog(Context mContext, int id) {
		activity = (Activity) mContext;
		dialog = new Dialog(mContext, R.style.pub_dialog);
		dialog.setContentView(id);
		dialog.setCancelable(false);
	}

	public void create(Context mContext, View view) {
		activity = (Activity) mContext;
		dialog = new Dialog(mContext, R.style.pub_dialog);
		dialog.setContentView(view);
		dialog.setCancelable(false);
	}

	public void showDialog() {
		dialog.show();
	}

	public void showDialog(Context mContext, View view) {
		createDialog(mContext, view);
		dialog.show();
	}

	public void closeDialog() {
		dialog.dismiss();
	}

	public void createProgressDialog(Activity activity) {
		this.activity = activity;
		mProgressDialog = DialogUtil.createLoadingDialog(activity, AppApplication.getInstance()
				.getResources().getString(R.string.pub_please_wait));
	}

	public void showProgressDialog() {
		mProgressDialog.dialog.show();
		// 加载动画  
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(  
        		mProgressDialog.context, R.anim.pub_loading_animation);  
        // 使用ImageView显示动画  
		mProgressDialog.spaceshipImage.startAnimation(hyperspaceJumpAnimation);
	}

	public void closeProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.dialog.isShowing()) {
			mProgressDialog.dialog.dismiss();
		}
	}
	
	public boolean isDialogShowing(){
		return (dialog != null && dialog.isShowing());
	}

    public void createProgressDialog(Activity activity, boolean cancel) {
        this.activity = activity;
        mProgressDialog = DialogUtil.createLoadingDialog(activity, AppApplication.getInstance()
                .getResources().getString(R.string.pub_please_wait), cancel);
    }
	
	/** 
     * 得到自定义的progressDialog 
     * @param context 
     * @param msg 
     * @return 
     *//*
    public static CustomeDialog createLoadingDialog(Context context, String msg) {  

    	CustomeDialog dialog = new DialogUtil().new CustomeDialog();
        LayoutInflater inflater = LayoutInflater.from(context);  
        View v = inflater.inflate(R.layout.pub_loading_dialog, null);// 得到加载view  
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.dialog_view);// 加载布局  
        // main.xml中的ImageView  
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);  
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字  
          
        tipTextView.setText(msg);// 设置加载信息  

        Dialog loadingDialog = new Dialog(context, R.style.pub_loading_dialog);// 创建自定义样式dialog  

        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(layout, new RelativeLayout.LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT,  
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        
        dialog.dialog = loadingDialog;
        dialog.spaceshipImage = spaceshipImage;
        dialog.context = context;
        return dialog;  

    }  */

    /**
     * 得到自定义的progressDialog
     * @param context
     * @param msg
     * @return
     */
    public static CustomeDialog createLoadingDialog(Context context, String msg) {

        return createLoadingDialog(context, msg, false);
    }

    public static CustomeDialog createLoadingDialog(Context context, String msg, boolean cancel) {

        CustomeDialog dialog = new DialogUtil().new CustomeDialog();

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.pub_loading_dialog, null);// 得到加载view
        MGViewUtil.scaleContentView(v);//縮放View
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字

        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.pub_loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(cancel);
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.x = -35;
//        wl.y = 120;
        window.setAttributes(wl);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.setGravity(Gravity.CENTER);
        //window.setGravity(Gravity.TOP);
        loadingDialog.setContentView(v);// 设置布局

        dialog.dialog = loadingDialog;
        dialog.spaceshipImage = spaceshipImage;
        dialog.context = context;
        return dialog;

    }

}
