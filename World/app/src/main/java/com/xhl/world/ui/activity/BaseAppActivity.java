package com.xhl.world.ui.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import com.avos.avoscloud.AVAnalytics;
import com.xhl.world.R;
import com.xhl.world.ui.event.ReLoadEvent;
import com.xhl.world.ui.utils.DialogMaker;
import com.xhl.world.ui.utils.SystemBarTintManager;
import com.xhl.xhl_library.Base.Sum.SumFragmentActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 15/12/3.
 */
public abstract class BaseAppActivity extends SumFragmentActivity {
    private AlertDialog mDialog;
    private ProgressDialog progressDialog;

    /**
     *  重新加载数据事件
     */
    protected void onReloadClick() {
    }

    private boolean isResume;

    //如果子类不用注册事件监听重写返回false
    protected boolean needEventBusRegister() {
        return true;
    }

    private SystemBarTintManager mTintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (needEventBusRegister()) {
            EventBus.getDefault().register(this);
        }
        //为状态栏着色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        setStatusBarRes(R.color.colorPrimaryDark);
    }

    protected void setStatusBarRes(int resId) {
        mTintManager.setStatusBarTintResource(resId);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {

        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
        isResume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        AVAnalytics.onPause(this);
        isResume = false;
    }

    @Override
    protected void onDestroy() {

        try {
            if (needEventBusRegister()) {
                EventBus.getDefault().unregister(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected int getFragmentContainerId() {
        //默认无
        return 0;
    }

    public void onEvent(ReLoadEvent event) {
        if (isResume) {
            onReloadClick();
        }
    }


    public void showLoadingDialog() {
        mDialog = DialogMaker.showLoadingDialog(this, null);
        if (!mDialog.isShowing()) {
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(true);
            mDialog.show();
        }
    }

    public void hideLoadingDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void showProgressLoading(String message) {
        progressDialog = DialogMaker.showProgress(this, "", message, false);
        if (progressDialog != null) {
            progressDialog.show();
        }
    }
}
