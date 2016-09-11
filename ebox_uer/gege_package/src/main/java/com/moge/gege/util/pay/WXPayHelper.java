package com.moge.gege.util.pay;

import android.app.Activity;
import android.content.Context;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.PayOrderInfoModel;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.ToastUtil;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;
import java.util.List;

public class WXPayHelper
{
    private static WeakReference<PayHelper> mPayHelper;
    private static IWXAPI mWXApi;

    public static boolean checkValid(Context context)
    {
        if (mWXApi == null)
        {
            mWXApi = WXAPIFactory.createWXAPI(context, GlobalConfig.WXAppID,
                    true);
            mWXApi.registerApp(GlobalConfig.WXAppID);
        }

        if (!mWXApi.isWXAppInstalled())
        {
            ToastUtil.showToastShort(context.getResources().getString(
                    R.string.uninstall_weixin));
            return false;
        }

        if (mWXApi.getWXAppSupportAPI() < Build.PAY_SUPPORTED_SDK_INT)
        {
            ToastUtil.showToastShort(context.getResources().getString(
                    R.string.weixin_not_support_pay));
            return false;
        }

        return true;
    }

    public static boolean toPayOrder(final Activity context,
            PayHelper payHelper,
            String payId, PayOrderInfoModel orderInfo, List<String> orderIdList)
    {
        mPayHelper = new WeakReference<PayHelper>(payHelper);

        if (orderInfo == null || orderIdList == null)
        {
            return false;
        }

        if (!checkValid(context))
        {
            return false;
        }

        if (mWXApi == null)
        {
            mWXApi = WXAPIFactory.createWXAPI(context, GlobalConfig.WXAppID,
                    true);
            mWXApi.registerApp(GlobalConfig.WXAppID);
        }

        PayReq req = new PayReq();
        req.appId = orderInfo.getApp_id();
        req.partnerId = orderInfo.getPartner_id();
        req.prepayId = orderInfo.getPrepay_id();
        req.nonceStr = orderInfo.getNoncestr();
        req.timeStamp = orderInfo.getTimestamp();
        req.packageValue = orderInfo.getPackage_value();
        req.sign = orderInfo.getSign();
        req.extData = payId;

        String log = String
                .format("[wx_pay: appId = %s, partnerId = %s, prepayId = %s,nonceStr = %s,timeStamp = %s,packageValue = %s,sign = %s]",
                        req.appId, req.partnerId, req.prepayId, req.nonceStr,
                        req.timeStamp, req.packageValue, req.sign);
        LogUtil.d(log);

        return mWXApi.sendReq(req);
    }

    public static void finishPayOrder(Context context, String payId,
            PayResp payResp)
    {
        // direct notify pay result
        if (mPayHelper != null)
        {
            PayHelper payHelper = mPayHelper.get();
            payHelper.finishWXPayOrder(payId, payResp);

            mPayHelper = null;
        }
    }

    // not to use
    private static String genExtData(String payId, List<String> orderIdList)
    {
        String extData = payId + "_";
        int i = 0;
        for (; i < orderIdList.size() - 1; i++)
        {
            extData += orderIdList.get(i) + "_";
        }

        extData += orderIdList.get(i);
        return extData;
    }

}
