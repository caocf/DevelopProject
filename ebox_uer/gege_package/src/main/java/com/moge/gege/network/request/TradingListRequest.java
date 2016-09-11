package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespTradingListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * trading list query
 */
public class TradingListRequest extends BaseRequest<RespTradingListModel>
{
    public TradingListRequest(String cursor, float longitude, float latitude,
            String filterType, String categoryId,
            ResponseEventHandler<RespTradingListModel> listener)
    {
        super(Method.GET, getRequestUrl(cursor, longitude, latitude,
                filterType, categoryId), "", RespTradingListModel.class,
                listener);
    }

    private static String getRequestUrl(String cursor, float longitude,
            float latitude, String filterType, String categoryId)
    {
        return NetworkConfig.generalAddress + "/v1/trading" + "?cursor="
                + cursor + "&longitude=" + longitude + "&latitude=" + latitude
                + "&sort=" + filterType + "&category_id=" + categoryId;
    }

}
