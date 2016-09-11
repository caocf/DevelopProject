package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespServiceListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query topics
 */
public class ServiceListRequest extends BaseRequest<RespServiceListModel>
{
    public ServiceListRequest(int serviceType, String cursor,
            ResponseEventHandler<RespServiceListModel> listener)
    {
        super(Method.GET, getRequestUrl(serviceType, cursor), "",
                RespServiceListModel.class, listener);
    }

    private static String getRequestUrl(int serviceType, String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/living/service" + "?cursor="
                + cursor + "&service_type=" + String.valueOf(serviceType);
    }

}
