package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespApplyListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * activity apply list
 */
public class ActivityApplyListRequest extends BaseRequest<RespApplyListModel>
{
    public ActivityApplyListRequest(String activityId,
            ResponseEventHandler<RespApplyListModel> listener)
    {
        super(Method.GET, getRequestUrl(activityId), "",
                RespApplyListModel.class, listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl(String activityId)
    {
        return NetworkConfig.generalAddress + "/v1/activity/" + activityId
                + "/apply";
    }

}
