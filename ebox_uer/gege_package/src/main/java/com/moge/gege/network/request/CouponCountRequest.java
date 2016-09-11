package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespCouponCountModel;
import com.moge.gege.model.RespCouponInfoModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * coupon count
 */
public class CouponCountRequest extends BaseRequest<RespCouponCountModel>
{
    public CouponCountRequest(
                              ResponseEventHandler<RespCouponCountModel> listener)
    {
        super(Method.GET, getRequestUrl(), "",
                RespCouponCountModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/trading/coupon/info";
    }
}
