package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespTopicListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query topics
 */
public class TopicListRequest extends BaseRequest<RespTopicListModel>
{
    public TopicListRequest(String boardId, String cursor,
            ResponseEventHandler<RespTopicListModel> listener)
    {
        super(Method.GET, getRequestUrl(boardId, cursor), "",
                RespTopicListModel.class, listener);
    }

    private static String getRequestUrl(String boardId, String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/board/" + boardId
                + "/topic?cursor=" + cursor;
    }

}
