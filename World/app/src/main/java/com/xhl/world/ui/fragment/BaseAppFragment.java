package com.xhl.world.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.xhl.world.ui.event.GlobalEvent;
import com.xhl.world.ui.utils.DialogMaker;
import com.xhl.xhl_library.Base.Sum.SumFragment;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 15/12/3.
 */
public abstract class BaseAppFragment extends SumFragment {

    private AlertDialog mDialog;
    private ProgressDialog progressDialog;

    //如果子类不用注册事件监听重写返回false
    protected boolean needEventBusRegister() {
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (needEventBusRegister()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (needEventBusRegister()) {
                EventBus.getDefault().unregister(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onEvent(GlobalEvent event) {
    }

    protected void showLoadingDialog() {
        mDialog = DialogMaker.showLoadingDialog(getContext(), null);

        if (!mDialog.isShowing()) {

            mDialog.setCanceledOnTouchOutside(false);

            mDialog.setCancelable(true);
            mDialog.show();
        }
    }

    protected void hideLoadingDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    protected void showProgressLoading() {
        showProgressLoading("卖力加载中...");
    }

    protected void showProgressLoading(String message) {
        progressDialog = DialogMaker.showProgress(getContext(), "", message, false);
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

}
