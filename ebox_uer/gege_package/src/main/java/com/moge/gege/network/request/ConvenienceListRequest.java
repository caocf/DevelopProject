package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespConvenienceListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query topics
 */
public class ConvenienceListRequest extends
        BaseRequest<RespConvenienceListModel>
{
    public ConvenienceListRequest(int serviceType, String cursor,
            ResponseEventHandler<RespConvenienceListModel> listener)
    {
        super(Method.GET, getRequestUrl(serviceType, cursor), "",
                RespConvenienceListModel.class, listener);
    }

    private static String getRequestUrl(int serviceType, String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/convenience/service"
                + "?cursor=" + cursor + "&service_type="
                + String.valueOf(serviceType);
    }

}
