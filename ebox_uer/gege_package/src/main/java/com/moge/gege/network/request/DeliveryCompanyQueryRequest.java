package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespDeliveryCompanyModel;
import com.moge.gege.model.RespDeliveryDetailModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query delivery company
 */
public class DeliveryCompanyQueryRequest extends BaseRequest<RespDeliveryCompanyModel>
{
    public DeliveryCompanyQueryRequest(
                                       ResponseEventHandler<RespDeliveryCompanyModel> listener)
    {
        super(Method.GET, getRequestUrl(), "", RespDeliveryCompanyModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return String.format("%s/v1/delivery/company", NetworkConfig.generalAddress);
    }
}
