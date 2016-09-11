package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespDeliveryModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * trading delivery fee query
 */
public class TradingDeliveryFeeRequest extends BaseRequest<RespDeliveryModel>
{
    public TradingDeliveryFeeRequest(String paramContent,
            ResponseEventHandler<RespDeliveryModel> listener)
    {
        super(Method.POST, getRequestUrl(), paramContent,
                RespDeliveryModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.payAddress + "/v1/delivery/fee";
    }
}
