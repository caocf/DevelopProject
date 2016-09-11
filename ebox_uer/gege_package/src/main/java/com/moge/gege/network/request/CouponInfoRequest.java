package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespCouponInfoModel;
import com.moge.gege.model.RespUserCouponListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * coupon list
 */
public class CouponInfoRequest extends BaseRequest<RespCouponInfoModel>
{
    public CouponInfoRequest(String couponCode,
            ResponseEventHandler<RespCouponInfoModel> listener)
    {
        super(Method.GET, getRequestUrl(couponCode), "",
                RespCouponInfoModel.class, listener);
    }

    private static String getRequestUrl(String couponCode)
    {
        return NetworkConfig.generalAddress + "/v1/trading/coupon/code/" + couponCode;
    }
}
