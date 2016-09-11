package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespOrderModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query order detail
 */
public class TradingOrderDetailRequest extends BaseRequest<RespOrderModel>
{
    public TradingOrderDetailRequest(String orderId,
            ResponseEventHandler<RespOrderModel> listener)
    {
        super(Method.GET, getRequestUrl(orderId), "", RespOrderModel.class,
                listener);
    }

    private static String getRequestUrl(String orderId)
    {
        return NetworkConfig.generalAddress + "/v1/user/trading/orders/"
                + orderId;
    }

}
