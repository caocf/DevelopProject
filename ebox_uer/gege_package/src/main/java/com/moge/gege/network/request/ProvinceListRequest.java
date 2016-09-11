package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespDistrictListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query topic like
 */
public class ProvinceListRequest extends BaseRequest<RespDistrictListModel>
{
    public ProvinceListRequest(
            ResponseEventHandler<RespDistrictListModel> listener)
    {
        super(Method.GET, getRequestUrl(), "", RespDistrictListModel.class,
                listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/province";
    }

}
