package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespAddressListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * trading address query
 */
public class TradingAddressListRequest extends
        BaseRequest<RespAddressListModel>
{
    public TradingAddressListRequest(
            ResponseEventHandler<RespAddressListModel> listener)
    {
        super(Method.GET, getRequestUrl(), "", RespAddressListModel.class,
                listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/trading/address";
    }

}
