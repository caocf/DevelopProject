package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespCategoryListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * trading category query
 */
public class TradingCategoryRequest extends BaseRequest<RespCategoryListModel>
{
    public TradingCategoryRequest(
            ResponseEventHandler<RespCategoryListModel> listener)
    {
        super(Method.GET, getRequestUrl(), "", RespCategoryListModel.class,
                listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/trading/category";
    }

}
