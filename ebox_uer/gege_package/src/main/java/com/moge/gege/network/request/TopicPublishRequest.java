package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * publish topic
 */
public class TopicPublishRequest extends BaseRequest<BaseModel>
{
    public TopicPublishRequest(int topicType, String bodyParam,
            ResponseEventHandler<BaseModel> listener)
    {
        super(Method.POST, getRequestUrl(topicType), bodyParam,
                BaseModel.class, listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl(int topicType)
    {
        if (topicType == TopicType.GENERAL_TOPIC)
        {
            return NetworkConfig.generalAddress + "/v1/topic";
        }
        else if (topicType == TopicType.ACTIVITY_TOPIC)
        {
            return NetworkConfig.generalAddress + "/v1/activity";
        }
        else
        {
            return NetworkConfig.generalAddress + "/v1/living/service";
        }
    }

}
