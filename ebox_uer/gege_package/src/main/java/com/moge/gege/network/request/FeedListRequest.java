package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespTopicListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * feedd list query
 */
public class FeedListRequest extends BaseRequest<RespTopicListModel>
{
    public FeedListRequest(String cursor,
            ResponseEventHandler<RespTopicListModel> listener)
    {
        super(Method.GET, getRequestUrl(cursor), "", RespTopicListModel.class,
                listener);
    }

    private static String getRequestUrl(String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/index/feeds" + "?cursor="
                + cursor;
    }

}
