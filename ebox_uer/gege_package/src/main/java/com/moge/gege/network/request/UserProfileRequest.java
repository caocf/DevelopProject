package com.moge.gege.network.request;

import android.text.TextUtils;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespProfileModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

public class UserProfileRequest extends BaseRequest<RespProfileModel>
{
    public UserProfileRequest(String uid,
            ResponseEventHandler<RespProfileModel> listener)
    {
        super(Method.GET, getRequestUrl(uid), "", RespProfileModel.class,
                listener);
    }

    private static String getRequestUrl(String uid)
    {
        if (TextUtils.isEmpty(uid))
        {
            return NetworkConfig.generalAddress + "/v1/profile";
        }
        else
        {
            return NetworkConfig.generalAddress + "/v1/profile/" + uid;
        }
    }

}
