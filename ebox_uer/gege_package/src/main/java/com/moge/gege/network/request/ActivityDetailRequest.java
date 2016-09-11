package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespTopicDetailModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query activity detail
 */
public class ActivityDetailRequest extends BaseRequest<RespTopicDetailModel>
{
    public ActivityDetailRequest(String activityId,
            ResponseEventHandler<RespTopicDetailModel> listener)
    {
        super(Method.GET, getRequestUrl(activityId), "",
                RespTopicDetailModel.class, listener);
    }

    private static String getRequestUrl(String activityId)
    {
        return NetworkConfig.generalAddress + "/v1/activity/" + activityId;
    }

}
