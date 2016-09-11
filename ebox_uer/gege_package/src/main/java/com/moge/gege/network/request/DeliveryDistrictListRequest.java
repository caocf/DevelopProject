package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespDistrictListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query district list
 */
public class DeliveryDistrictListRequest extends
        BaseRequest<RespDistrictListModel>
{
    public DeliveryDistrictListRequest(
            ResponseEventHandler<RespDistrictListModel> listener)
    {
        super(Method.GET, getRequestUrl(), "", RespDistrictListModel.class,
                listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/trading/delivery/district";
    }

}
