package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespTradingBuyListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * trading buy list
 */
public class TradingBuyListRequest extends BaseRequest<RespTradingBuyListModel>
{
    public TradingBuyListRequest(String tradingId,
            ResponseEventHandler<RespTradingBuyListModel> listener)
    {
        super(Method.GET, getRequestUrl(tradingId), "",
                RespTradingBuyListModel.class, listener);
    }

    private static String getRequestUrl(String tradingId)
    {
        return NetworkConfig.generalAddress + "/v1/trading/" + tradingId
                + "/buy";
    }

}
