package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespAddressModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * trading address query
 */
public class TradingAddressRequest extends BaseRequest<RespAddressModel>
{
    public TradingAddressRequest(String addressId,
            ResponseEventHandler<RespAddressModel> listener)
    {
        super(Method.GET, getRequestUrl(addressId), "", RespAddressModel.class,
                listener);
    }

    private static String getRequestUrl(String addressId)
    {
        return NetworkConfig.generalAddress + "/v1/trading/address/"
                + addressId;
    }

}
