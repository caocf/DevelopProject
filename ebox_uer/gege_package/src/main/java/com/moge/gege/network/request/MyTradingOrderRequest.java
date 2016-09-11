package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespOrderListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query my order
 */
public class MyTradingOrderRequest extends BaseRequest<RespOrderListModel>
{
    public MyTradingOrderRequest(String cursor,
            ResponseEventHandler<RespOrderListModel> listener)
    {
        super(Method.GET, getRequestUrl(cursor), "", RespOrderListModel.class,
                listener);
    }

    private static String getRequestUrl(String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/user/trading/orders"
                + "?cursor=" + cursor;
    }

}
