package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespFeedListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * user feed list query
 */
public class UserFeedListRequest extends BaseRequest<RespFeedListModel>
{
    public UserFeedListRequest(String uid, String cursor,
            ResponseEventHandler<RespFeedListModel> listener)
    {
        super(Method.GET, getRequestUrl(uid, cursor), "",
                RespFeedListModel.class, listener);
    }

    private static String getRequestUrl(String uid, String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/user/" + uid + "/feeds"
                + "?cursor=" + cursor;
    }

}
