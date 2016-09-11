package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespTradingListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * trading list query
 */
public class TradingPromotionListRequest extends
        BaseRequest<RespTradingListModel>
{
    public TradingPromotionListRequest(String cursor, float longitude,
            float latitude, String promotionId,
            ResponseEventHandler<RespTradingListModel> listener)
    {
        super(Method.GET, getRequestUrl(cursor, longitude, latitude,
                promotionId), "", RespTradingListModel.class, listener);
    }

    private static String getRequestUrl(String cursor, float longitude,
            float latitude, String promotionId)
    {
        return NetworkConfig.generalAddress + "/v1/trading/promotions/"
                + promotionId + "?cursor=" + cursor + "&longitude=" + longitude
                + "&latitude=" + latitude;
    }

}
