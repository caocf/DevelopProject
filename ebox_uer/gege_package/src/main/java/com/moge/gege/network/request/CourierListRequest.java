package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespCourierListModel;
import com.moge.gege.model.RespTopicListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * courier list query
 */
public class CourierListRequest extends BaseRequest<RespCourierListModel>
{
    public CourierListRequest(String cursor,
                              ResponseEventHandler<RespCourierListModel> listener)
    {
        super(Method.GET, getRequestUrl(cursor), "", RespCourierListModel.class,
                listener);
    }

    private static String getRequestUrl(String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/delivery/courier" + "?cursor="
                + cursor;
    }

}
