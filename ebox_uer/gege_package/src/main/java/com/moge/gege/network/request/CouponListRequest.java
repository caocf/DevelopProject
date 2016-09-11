package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespApplyListModel;
import com.moge.gege.model.RespUserCouponListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * coupon list
 */
public class CouponListRequest extends BaseRequest<RespUserCouponListModel>
{
    public CouponListRequest(String cursor,
            ResponseEventHandler<RespUserCouponListModel> listener)
    {
        super(Method.GET, getRequestUrl(cursor), "",
                RespUserCouponListModel.class, listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl(String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/trading/coupon" + "?cursor=" + cursor;
    }
}
