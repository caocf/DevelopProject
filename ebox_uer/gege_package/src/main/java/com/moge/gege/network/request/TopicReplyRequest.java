package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query topic post
 */
public class TopicReplyRequest extends BaseRequest<BaseModel>
{
    public TopicReplyRequest(int topicType, String topicId, String boardId,
            String content, ResponseEventHandler<BaseModel> listener)
    {
        super(Method.POST, getRequestUrl(topicType, topicId, boardId), content,
                BaseModel.class, listener);
    }

    private static String getRequestUrl(int topicType, String topicId,
            String boardId)
    {
        if (topicType == TopicType.GENERAL_TOPIC)
        {
            return NetworkConfig.generalAddress + "/v1/topic/" + topicId;
        }
        else if (topicType == TopicType.ACTIVITY_TOPIC)
        {
            return NetworkConfig.generalAddress + "/v1/activity/" + topicId;
        }
        else
        {
            return NetworkConfig.generalAddress + "/v1/living/service/"
                    + topicId;
        }

    }

}
