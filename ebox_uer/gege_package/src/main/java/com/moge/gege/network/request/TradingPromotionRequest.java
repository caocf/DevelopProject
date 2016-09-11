package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespTradingPromotionListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * trading list query
 */
public class TradingPromotionRequest extends
        BaseRequest<RespTradingPromotionListModel>
{
    public TradingPromotionRequest(float longitude, float latitude,
            ResponseEventHandler<RespTradingPromotionListModel> listener)
    {
        super(Method.GET, getRequestUrl(longitude, latitude), "",
                RespTradingPromotionListModel.class, listener);
    }

    private static String getRequestUrl(float longitude, float latitude)
    {
        return NetworkConfig.generalAddress + "/v1/trading/promotions"
                + "?longitude=" + longitude + "&latitude=" + latitude;
    }

}
