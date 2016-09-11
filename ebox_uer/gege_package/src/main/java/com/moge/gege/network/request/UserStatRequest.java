package com.moge.gege.network.request;

import android.text.TextUtils;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespUserStatModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

public class UserStatRequest extends BaseRequest<RespUserStatModel>
{
    public UserStatRequest(String id,
            ResponseEventHandler<RespUserStatModel> listener)
    {
        super(Method.GET, getRequestUrl(id), "", RespUserStatModel.class,
                listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl(String id)
    {
        if (TextUtils.isEmpty(id))
        {
            return NetworkConfig.generalAddress + "/v1/user/statistics";
        }
        else
        {
            return NetworkConfig.generalAddress + "/v1/user/" + id
                    + "/statistics";
        }
    }

}
