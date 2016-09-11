package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespCommunityInfoModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * community detail request
 */
public class CommunityInfoRequest extends BaseRequest<RespCommunityInfoModel>
{
    public CommunityInfoRequest(String communityId,
            ResponseEventHandler<RespCommunityInfoModel> listener)
    {
        super(Method.GET, getRequestUrl(communityId), "",
                RespCommunityInfoModel.class, listener);
    }

    private static String getRequestUrl(String communityId)
    {
        return NetworkConfig.generalAddress + "/v1/community/" + communityId;
    }

}
