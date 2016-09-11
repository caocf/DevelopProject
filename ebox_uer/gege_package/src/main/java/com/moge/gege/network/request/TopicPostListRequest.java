package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespTopicPostListModel;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query topic post
 */
public class TopicPostListRequest extends BaseRequest<RespTopicPostListModel>
{
    public TopicPostListRequest(boolean isReverse, int topicType,
            String topicId, String cursor,
            ResponseEventHandler<RespTopicPostListModel> listener)
    {
        super(Method.GET, getRequestUrl(isReverse, topicType, topicId, cursor),
                "", RespTopicPostListModel.class, listener);
    }

    private static String getRequestUrl(boolean isReverse, int topicType,
            String topicId, String cursor)
    {
        String extraParam = "";
        if (isReverse)
        {
            extraParam = "&sort=-1";
        }

        if (topicType == TopicType.GENERAL_TOPIC)
        {
            return NetworkConfig.generalAddress + "/v1/topic/" + topicId
                    + "/post?cursor=" + cursor + extraParam;
        }
        else if (topicType == TopicType.ACTIVITY_TOPIC)
        {
            return NetworkConfig.generalAddress + "/v1/activity/" + topicId
                    + "/post?cursor=" + cursor + extraParam;
        }
        else
        {
            return NetworkConfig.generalAddress + "/v1/living/service/"
                    + topicId + "/post?cursor=" + cursor + extraParam;
        }

    }
}
