package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespConvenienceModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query convenience detail
 */
public class ConvenienceDetailRequest extends BaseRequest<RespConvenienceModel>
{
    public ConvenienceDetailRequest(String id,
            ResponseEventHandler<RespConvenienceModel> listener)
    {
        super(Method.GET, getRequestUrl(id), "", RespConvenienceModel.class,
                listener);
    }

    private static String getRequestUrl(String id)
    {
        return NetworkConfig.generalAddress + "/v1/convenience/service/" + id;
    }

}
