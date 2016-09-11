package com.moge.gege.network.request;

import android.text.TextUtils;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespLikeListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query topic like
 */
public class TopicLikeListRequest extends BaseRequest<RespLikeListModel>
{
    public TopicLikeListRequest(String boardId, String topicId, int topicType,
            ResponseEventHandler<RespLikeListModel> listener)
    {
        super(Method.GET, getRequestUrl(topicType, topicId, boardId), "",
                RespLikeListModel.class, listener);
    }

    private static String getRequestUrl(int topicType, String topicId,
            String boardId)
    {

        if (TextUtils.isEmpty(boardId))
        {
            return NetworkConfig.generalAddress + "/v1/like" + "?tid="
                    + topicId + "&topic_type=" + topicType;
        }
        else
        {
            return NetworkConfig.generalAddress + "/v1/like" + "?tid="
                    + topicId + "&topic_type=" + topicType + "&bid=" + boardId;
        }
    }

}
