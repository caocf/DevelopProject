package com.ebox.mall.warehouse.network.request;
import com.ebox.mall.warehouse.model.RespTradingDetailModel;
import com.ebox.mall.warehouse.network.WHNetworkConfig;
import com.ebox.mall.warehouse.network.util.BaseRequest;
import com.ebox.Anetwork.ResponseEventHandler;


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
        return WHNetworkConfig.generalAddress + "/v1/trading/" + tradingId;
    }

}
