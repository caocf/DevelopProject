package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespTopicListModel;
import com.moge.gege.model.enums.MyPublishType;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query my publish
 */
public class MyPublishRequest extends BaseRequest<RespTopicListModel>
{
    public MyPublishRequest(int publishType, String cursor,
            ResponseEventHandler<RespTopicListModel> listener)
    {
        super(Method.GET, getRequestUrl(publishType, cursor), "",
                RespTopicListModel.class, listener);
    }

    private static String getRequestUrl(int publishType, String cursor)
    {
        switch (publishType)
        {
            case MyPublishType.TOPIC:
                return NetworkConfig.generalAddress + "/v1/user/topic"
                        + "?cursor=" + cursor;
            case MyPublishType.ACTIVITY:
                return NetworkConfig.generalAddress + "/v1/user/activity"
                        + "?cursor=" + cursor;
            case MyPublishType.SERVICE:
                return NetworkConfig.generalAddress + "/v1/user/living/service"
                        + "?cursor=" + cursor;
            default:
                break;
        }

        return "";
    }

}
