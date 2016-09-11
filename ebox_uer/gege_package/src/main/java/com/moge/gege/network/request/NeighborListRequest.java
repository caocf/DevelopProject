package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespUserListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * neighbor list query
 */
public class NeighborListRequest extends BaseRequest<RespUserListModel>
{
    public NeighborListRequest(String cursor,
            ResponseEventHandler<RespUserListModel> listener)
    {
        super(Method.GET, getRequestUrl(cursor), "", RespUserListModel.class,
                listener);
    }

    private static String getRequestUrl(String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/user/neighbor" + "?cursor="
                + cursor;
    }

}
