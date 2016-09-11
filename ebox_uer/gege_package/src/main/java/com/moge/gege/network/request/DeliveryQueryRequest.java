package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.RespDeliveryDetailModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query delivery
 */
public class DeliveryQueryRequest extends BaseRequest<RespDeliveryDetailModel>
{
    public DeliveryQueryRequest(String deliveryNumber, String company,
                                ResponseEventHandler<RespDeliveryDetailModel> listener)
    {
        super(Method.GET, getRequestUrl(deliveryNumber, company), "", RespDeliveryDetailModel.class, listener);
    }

    private static String getRequestUrl(String deliveryNumber, String company)
    {
        return String.format("%s/v1/delivery/query?number=%s&company=%s", NetworkConfig.generalAddress, deliveryNumber, company);
    }
}
