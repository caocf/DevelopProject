package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespCommunityUserModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * community user request
 */
public class CommunityUserRequest extends BaseRequest<RespCommunityUserModel>
{
    public CommunityUserRequest(String communityId,
                                ResponseEventHandler<RespCommunityUserModel> listener)
    {
        super(Method.GET, getRequestUrl(communityId), "",
                RespCommunityUserModel.class, listener);
    }

    private static String getRequestUrl(String communityId)
    {
        return NetworkConfig.generalAddress + "/v1/community/" + communityId + "/info";
    }

}
