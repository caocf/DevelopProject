package com.ebox.pub.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.pub.service.AppService;

public class Dialog {
    private AlertDialog mDialog;
    private TextView description;
    private Object value;

    public Dialog(Context context, String text, final onClickListener listener, Object value) {
        if (((Activity) context).isFinishing()) {
            return;
        }
        this.value = value;
        mDialog = new AlertDialog.Builder(context).create();
        mDialog.show();
        mDialog.setCancelable(false);
        Window window = mDialog.getWindow();
        View v = View.inflate(context, R.layout.pub_confirm_dialog, null);
        window.setContentView(v);
        MGViewUtil.scaleContentView((ViewGroup) v);
        description = (TextView) window.findViewById(R.id.description);
        description.setText(text);
        Button ok = (Button) window.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onOk(Dialog.this.value);
                }
                mDialog.dismiss();
                AppService.getIntance().hasOnKeyDown();
            }
        });
        Button cancel = (Button) window.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancel(Dialog.this.value);
                }
                mDialog.cancel();
                AppService.getIntance().hasOnKeyDown();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public Dialog(Context context, String text, String btnLeft, String btnRight, final onClickListener listener, Object value) {
        if (((Activity) context).isFinishing()) {
            return;
        }
        this.value = value;
        mDialog = new AlertDialog.Builder(context).create();
        mDialog.show();
        mDialog.setCancelable(false);
        Window window = mDialog.getWindow();

        View v = View.inflate(context, R.layout.pub_confirm_dialog, null);
        window.setContentView(v);
        MGViewUtil.scaleContentView((ViewGroup) v);
        description = (TextView) window.findViewById(R.id.description);
        description.setText(text);
        description.setTextColor(context.getResources().getColor(R.color.pub_red));
        Button ok = (Button) window.findViewById(R.id.btn_ok);
        ok.setText(btnLeft);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onOk(Dialog.this.value);
                }
                mDialog.dismiss();
                AppService.getIntance().hasOnKeyDown();
            }
        });
        Button cancel = (Button) window.findViewById(R.id.btn_cancel);
        cancel.setText(btnRight);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancel(Dialog.this.value);
                }
                mDialog.cancel();
                AppService.getIntance().hasOnKeyDown();
            }
        });
    }

    public AlertDialog getDialog() {
        return mDialog;
    }

    public void closeDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public interface onClickListener {
        void onOk(Object value);

        void onCancel(Object value);
    }
}
