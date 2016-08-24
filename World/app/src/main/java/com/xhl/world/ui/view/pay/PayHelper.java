package com.xhl.world.ui.view.pay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.ui.utils.DialogMaker;
import com.xhl.world.ui.utils.ToastUtil;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;

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
        Logger.e("" + orderParams);
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
    public synchronized void payOrder(String orderCode, String orderPrice) {
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        ApiControl.getApi().toAliPay(orderPrice, orderCode, new Callback.CommonCallback<ResponseModel<String>>() {
            @Override
            public void onSuccess(ResponseModel<String> result) {
                if (result.isSuccess()) {
                    toPayOrder(result.getResultObj());
                } else {
                    ToastUtil.showToastLong(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.showToastLong(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mProgressDialog.dismiss();
            }
        });

    }

    public interface PayCallBack {
        void success();

        void failed(String msg);
    }

}
