package com.xhl.bqlh.business.view.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xhl.bqlh.business.R;
import com.xhl.xhl_library.ui.SwipeRefresh.MaterialProgressDrawable;


/**
 * Created by Sum on 15/12/17.
 */
public class DialogMaker {


    public static void showAlterDialog(Context context, String Title, String message, DialogInterface.OnClickListener cancel, DialogInterface.OnClickListener ok) {
        AlertDialog dialog = getDialog(context)
                .setTitle(Title)
                .setMessage(message)
                .setNegativeButton("取消", cancel)
                .setPositiveButton("确定", ok)
                .create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void showAlterDialog(Context context, String Title, String message, String negative, DialogInterface.OnClickListener cancel, String positive, DialogInterface.OnClickListener ok) {
        AlertDialog dialog = getDialog(context)
                .setTitle(Title)
                .setMessage(message)
                .setNegativeButton(negative, cancel)
                .setPositiveButton(positive, ok)
                .create();
        dialog.show();
    }

    public static AlertDialog.Builder getDialog(Context context) {
        return new AlertDialog.Builder(context, R.style.dialog_alert);
    }

    public static AlertDialog showLoadingDialog(Context context, String content) {
        return showLoadingDialog(context, content, R.style.dialog_no_bg);
    }

    public static AlertDialog showLoadingDialog(Context context, String content, int style) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context, style);
        View view = LayoutInflater.from(context).inflate(R.layout.pub_loading_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.loading);
        View viewParent = view.findViewById(R.id.load_content);

//        TextView tv = (TextView) view.findViewById(R.id.loading_hint);
//        if (!TextUtils.isEmpty(content)) {
//            tv.setText(content);
//        } else {
//            tv.setVisibility(View.GONE);
//        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
        view.setLayoutParams(params);

        MaterialProgressDrawable drawable = new MaterialProgressDrawable(context, viewParent);
        int color1 = ContextCompat.getColor(context, R.color.colorPrimary);
        drawable.setColorSchemeColors(color1);
        imageView.setImageDrawable(drawable);
        drawable.setAlpha(255);
        drawable.start();
        dialog.setView(view);

        AlertDialog res = dialog.create();
        return res;
    }


    public static ProgressDialog showProgress(Context context,
                                              CharSequence title, CharSequence message, boolean cancelable) {
        try {
            if (context == null) {
                return null;
            }
            final ProgressDialog dialog = ProgressDialog.show(context, title, message);
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setCancelable(cancelable);
            return dialog;
        } catch (Exception e) {
        }
        return null;
    }


    public static void refuseShowSetLocation(final Activity context) {
        AlertDialog.Builder dialog = getDialog(context);
        dialog.setTitle("定位拒绝");
        dialog.setMessage("请您到系统设置中应用允许定位");
        dialog.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
                context.finish();
            }
        });
        dialog.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.finish();
            }
        });
        dialog.create().show();
    }

    public static void refuseShowSetCamera(final Activity context) {
        AlertDialog.Builder dialog = getDialog(context);
        dialog.setTitle("相机拒绝");
        dialog.setMessage("请您到系统设置中应用允许打开摄像头");
        dialog.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
                context.finish();
            }
        });
        dialog.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.finish();
            }
        });
        dialog.create().show();
    }
}
