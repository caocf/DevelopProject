package com.moge.gege.network.request;

import android.text.TextUtils;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * delete topic post
 */
public class TopicDeleteReplyRequest extends BaseRequest<BaseModel>
{
    public TopicDeleteReplyRequest(int topicType, String topicId,
            String boardId, String postId,
            ResponseEventHandler<BaseModel> listener)
    {
        super(Method.DELETE,
                getRequestUrl(topicType, topicId, boardId, postId), "",
                BaseModel.class, listener);
    }

    private static String getRequestUrl(int topicType, String topicId,
            String boardId, String postId)
    {
        String urlPrefix;
        if (topicType == TopicType.GENERAL_TOPIC)
        {
            urlPrefix = NetworkConfig.generalAddress + "/v1/topic/" + topicId
                    + "/post/" + postId;
        }
        else if (topicType == TopicType.ACTIVITY_TOPIC)
        {
            urlPrefix = NetworkConfig.generalAddress + "/v1/activity/"
                    + topicId + "/post/" + postId;
        }
        else
        {
            urlPrefix = NetworkConfig.generalAddress + "/v1/living/service/"
                    + topicId + "/post/" + postId;
        }

        if (TextUtils.isEmpty(boardId))
        {
            return urlPrefix;
        }
        else
        {
            return urlPrefix + "?bid=" + boardId;
        }

    }

}
