package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespTradingDetailModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * trading detail query
 */
public class TradingDetailRequest extends BaseRequest<RespTradingDetailModel>
{
    public TradingDetailRequest(String tradingId,
            ResponseEventHandler<RespTradingDetailModel> listener)
    {
        super(Method.GET, getRequestUrl(tradingId), "",
                RespTradingDetailModel.class, listener);
    }

    private static String getRequestUrl(String tradingId)
    {
        return NetworkConfig.generalAddress + "/v1/trading/" + tradingId;
    }

}
