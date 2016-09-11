package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespDistrictListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query city list
 */
public class CityListRequest extends BaseRequest<RespDistrictListModel>
{
    public CityListRequest(int pid,
            ResponseEventHandler<RespDistrictListModel> listener)
    {
        super(Method.GET, getRequestUrl(pid), "", RespDistrictListModel.class,
                listener);
    }

    private static String getRequestUrl(int pid)
    {
        return NetworkConfig.generalAddress + "/v1/city?" + "pid=" + pid;
    }

}
