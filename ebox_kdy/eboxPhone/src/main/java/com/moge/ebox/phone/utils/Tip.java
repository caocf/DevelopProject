package com.moge.ebox.phone.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.moge.ebox.phone.R;


public class Tip {
    private MyDialog mDialog;
    private TextView title;
    private TextView tv_tip;
    private Button btn_tip;
    private onDismissListener listener;

    public Tip(Context context, String text, onDismissListener listener) {
//        mActivity=(Activity) context;
//        if(mActivity.isFinishing())
//        {
//            return;
//        }
        this.listener = listener;
        mDialog = new MyDialog(context , R.style.dialog);
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
        mDialog.setContentView(R.layout.toast);
        title = (TextView) mDialog.findViewById(R.id.title);
        title.setVisibility(View.GONE);
        tv_tip = (TextView) mDialog.findViewById(R.id.tv_tip);
        tv_tip.setText(text);
        btn_tip = (Button) mDialog.findViewById(R.id.btn_tip);
        btn_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

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

    public void show() {
        if(mDialog == null)
        {
            return;
        }


        mDialog.show();
        mDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface arg0) {
                if(listener != null)
                {
                    listener.onDismiss();
                }
                mDialog = null;

            }
        });


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
        public MyDialog(Context context, int theme) {
            super(context, theme);
        }
        public MyDialog(Context context) {
            super(context);
        }

    }
}