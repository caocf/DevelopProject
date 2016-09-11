package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespApplyResultModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * activity apply or cancel
 */
public class ActivityApplyRequest extends BaseRequest<RespApplyResultModel>
{
    public ActivityApplyRequest(boolean apply, String activityId,
            ResponseEventHandler<RespApplyResultModel> listener)
    {
        super(apply ? Method.POST : Method.DELETE, getRequestUrl(activityId),
                "",
                RespApplyResultModel.class, listener);
    }

    private static String getRequestUrl(String activityId)
    {
        return NetworkConfig.generalAddress + "/v1/activity/" + activityId
                + "/apply";
    }

}
