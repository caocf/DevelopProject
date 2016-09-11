package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespDialResultModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * convenience dial request
 */
public class ConvenienceDialRequest extends BaseRequest<RespDialResultModel>
{
    public ConvenienceDialRequest(String id,
            ResponseEventHandler<RespDialResultModel> listener)
    {
        super(Method.POST, getRequestUrl(id), "", RespDialResultModel.class,
                listener);
    }

    private static String getRequestUrl(String id)
    {
        return NetworkConfig.generalAddress + "/v1/convenience/service/" + id
                + "/dial";
    }
}
