package com.ebox.mall.warehouse.network.request;

import com.ebox.mall.warehouse.model.RespTradingListModel;
import com.ebox.mall.warehouse.network.WHNetworkConfig;
import com.ebox.mall.warehouse.network.util.BaseRequest;
import com.ebox.Anetwork.ResponseEventHandler;


/**
 * trading list query
 */
public class TradingListRequest extends BaseRequest<RespTradingListModel>
{
    public TradingListRequest(String cursor, 
            ResponseEventHandler<RespTradingListModel> listener)
    {
        super(Method.GET, getRequestUrl(cursor), "", RespTradingListModel.class,
                listener);
    }

    private static String getRequestUrl(String cursor)
    {
        return WHNetworkConfig.generalAddress + "/v1/trading" + "?cursor="
                + cursor ;
//    	   return NetworkConfig.generalAddress + "/v1/trading/promotions/543677c666fa38df7c8b4567" + "?cursor="
//         + cursor ;
    }

}
