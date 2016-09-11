package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

public class LogoutRequest extends BaseRequest<BaseModel>
{
    public LogoutRequest(ResponseEventHandler<BaseModel> listener)
    {
        super(Method.POST, getRequestUrl(), "", BaseModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/logout";
    }

}
