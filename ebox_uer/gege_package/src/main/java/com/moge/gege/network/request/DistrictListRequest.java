package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespDistrictListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query city list
 */
public class DistrictListRequest extends BaseRequest<RespDistrictListModel>
{
    public DistrictListRequest(int cid,
            ResponseEventHandler<RespDistrictListModel> listener)
    {
        super(Method.GET, getRequestUrl(cid), "", RespDistrictListModel.class,
                listener);
    }

    private static String getRequestUrl(int cid)
    {
        return NetworkConfig.generalAddress + "/v1/district?" + "cid=" + cid;
    }

}
