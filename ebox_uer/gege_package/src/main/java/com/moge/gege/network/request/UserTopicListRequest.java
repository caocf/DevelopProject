package com.moge.gege.network.request;

import android.text.TextUtils;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespTopicListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query topics
 */
public class UserTopicListRequest extends BaseRequest<RespTopicListModel>
{
    public UserTopicListRequest(String uid, String cursor,
            ResponseEventHandler<RespTopicListModel> listener)
    {
        super(Method.GET, getRequestUrl(uid, cursor), "",
                RespTopicListModel.class, listener);
    }

    private static String getRequestUrl(String uid, String cursor)
    {
        if (TextUtils.isEmpty(uid))
        {
            return NetworkConfig.generalAddress + "/v1/user/topic" + "?cursor="
                    + cursor;
        }
        else
        {
            return NetworkConfig.generalAddress + "/v1/user/" + uid + "/topic"
                    + "?cursor=" + cursor;
        }

    }

}
