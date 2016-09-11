package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespServiceListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query topics
 */
public class ServiceDetailRequest extends BaseRequest<RespServiceListModel>
{
    public ServiceDetailRequest(String serviceId,
            ResponseEventHandler<RespServiceListModel> listener)
    {
        super(Method.GET, getRequestUrl(serviceId), "",
                RespServiceListModel.class, listener);
    }

    private static String getRequestUrl(String serviceId)
    {
        return NetworkConfig.generalAddress + "/v1/living/service/" + serviceId;
    }

}
