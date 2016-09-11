package com.moge.gege.network.request;

import android.text.TextUtils;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespUserModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

public class UserInfoRequest extends BaseRequest<RespUserModel>
{
    public UserInfoRequest(String uid,
            ResponseEventHandler<RespUserModel> listener)
    {
        super(Method.GET, getRequestUrl(uid), "", RespUserModel.class,
                listener);
    }

    private static String getRequestUrl(String uid)
    {
        if (TextUtils.isEmpty(uid))
        {
            return NetworkConfig.generalAddress + "/v1/user";
        }
        else
        {
            return NetworkConfig.generalAddress + "/v1/user/" + uid;
        }
    }

}
