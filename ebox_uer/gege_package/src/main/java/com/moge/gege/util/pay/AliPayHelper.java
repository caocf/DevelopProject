package com.moge.gege.util.pay;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.moge.gege.util.ToastUtil;

public class AliPayHelper
{
    private static Result mAliResult;
    private static WeakReference<PayHelper> mPayHelper;
    private static String mPayId;

    private static Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 1:
                {
                    mAliResult = new Result((String) msg.obj);

                    if (mAliResult.getResultStatus().equals(Result.SUCCESS))
                    {
                    }
                    else if (mAliResult.getResultStatus()
                            .equals(Result.DEALING))
                    {
                        ToastUtil.showToastShort(mAliResult.getErrorMsg());
                    }
                    else
                    {
                        ToastUtil.showToastShort(mAliResult.getErrorMsg());
                    }

                    if (mPayHelper != null)
                    {
                        PayHelper payHelper = mPayHelper.get();
                        payHelper.finishAliOrderPay(mPayId, mAliResult);

                        mPayHelper = null;
                    }
                }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public static void toPayOrder(final Activity context, PayHelper payHelper,
            String payId, final String orderParams)
    {
        if (TextUtils.isEmpty(orderParams))
        {
            return;
        }

        mPayHelper = new WeakReference<PayHelper>(payHelper);
        mPayId = payId;

        new Thread()
        {
            public void run()
            {
                PayTask alipay = new PayTask(context);
                String result = alipay.pay(orderParams);

                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

}
