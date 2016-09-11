package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespActivityListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * my gift list query
 */
public class MyGiftListRequest extends BaseRequest<RespActivityListModel>
{
    public MyGiftListRequest(String cursor,
            ResponseEventHandler<RespActivityListModel> listener)
    {
        super(Method.GET, getRequestUrl(cursor), "",
                RespActivityListModel.class, listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl(String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/user/gift" + "?cursor="
                + cursor;
    }

}
