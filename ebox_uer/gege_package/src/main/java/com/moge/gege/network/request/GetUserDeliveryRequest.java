package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.RespUserDeliveryListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

public class GetUserDeliveryRequest extends BaseRequest<RespUserDeliveryListModel> {
    public GetUserDeliveryRequest(String cursor,
                                  ResponseEventHandler<RespUserDeliveryListModel> listener) {
        super(Method.GET, getRequestUrl(cursor), "",
                RespUserDeliveryListModel.class, listener);
    }

    private static String getRequestUrl(String cursor) {
        return String.format("%s/v1/user/delivery?cursor=%s", NetworkConfig.generalAddress, cursor);
    }
}
