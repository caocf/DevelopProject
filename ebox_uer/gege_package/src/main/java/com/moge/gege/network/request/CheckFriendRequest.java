package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * check friend relation
 */
public class CheckFriendRequest extends BaseRequest<BaseModel>
{
    public CheckFriendRequest(String uid,
            ResponseEventHandler<BaseModel> listener)
    {
        super(Method.GET, getRequestUrl(uid), "", BaseModel.class, listener);
    }

    private static String getRequestUrl(String uid)
    {
        return NetworkConfig.generalAddress + "/v1/user/friend/" + uid;
    }
}
