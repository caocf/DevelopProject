package com.xhl.bqlh.view.helper.pay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.DialogMaker;

/**
 * Created by Sum on 16/1/5.
 */
public class PayHelper {

    //支付类型
    public static final int pay_type_alipay = 1;
    public static final int pay_type_weixin = 2;

    private PayResult mAliResult;
    private Activity mContext;
    private PayCallBack mCallBack;
    private ProgressDialog mProgressDialog;

    public PayHelper(Activity context, PayCallBack callBack) {
        mCallBack = callBack;
        mContext = context;
        mProgressDialog = DialogMaker.showProgress(mContext, null, mContext.getResources().getString(R.string.create_pay_order), false);
    }

    public synchronized void toPayOrder(final String orderParams) {
        if (TextUtils.isEmpty(orderParams)) {
            if (mCallBack != null) {
                mCallBack.failed("订单异常");
            }
            return;
        }
        new Thread() {
            public void run() {
                PayTask alipay = new PayTask(mContext);
                String result = alipay.pay(orderParams, true);
                mAliResult = new PayResult(result);

                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        payRes();
                    }
                });
            }
        }.start();
    }

    private void payRes() {

        if (mAliResult == null) {
            return;
        }
        String error_msg;
        if (mAliResult.getResultStatus().equals(PayResult.SUCCESS)) {
            if (mCallBack != null) {
                mCallBack.success();
            }

        } else if (mAliResult.getResultStatus().equals(PayResult.CANCEL)) {

            error_msg = mAliResult.getErrorMsg();

            if (mCallBack != null) {
                mCallBack.failed(error_msg);
            }

        } else if (mAliResult.getResultStatus().equals(PayResult.DEALING)) {
            error_msg = mAliResult.getErrorMsg();
            if (mCallBack != null) {
                mCallBack.failed(error_msg);
            }
        } else {
            error_msg = mAliResult.getErrorMsg();
            if (mCallBack != null) {
                mCallBack.failed(error_msg);
            }
        }

    }

    //创建订单
    public synchronized void payOrder(String orderCode) {
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        ApiControl.getApi().orderPay(orderCode, new DefaultCallback<ResponseModel<String>>() {
            @Override
            public void success(ResponseModel<String> result) {
                toPayOrder(result.getObj());
            }

            @Override
            public void finish() {
                mProgressDialog.dismiss();
            }
        });

    }

    public interface PayCallBack {
        void success();

        void failed(String msg);
    }

}
