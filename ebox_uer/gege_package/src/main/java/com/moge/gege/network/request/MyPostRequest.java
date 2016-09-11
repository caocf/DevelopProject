package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespMyPostListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query my post
 */
public class MyPostRequest extends BaseRequest<RespMyPostListModel>
{
    public MyPostRequest(String cursor,
            ResponseEventHandler<RespMyPostListModel> listener)
    {
        super(Method.GET, getRequestUrl(), "", RespMyPostListModel.class,
                listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/user/post";
    }

}
