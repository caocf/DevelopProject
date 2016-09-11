package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.network.model.base.BaseRsp;

public class RequestReSendSms extends BaseRequest<BaseRsp> {


    public RequestReSendSms(String orderId, ResponseEventHandler<BaseRsp> listener) {
        super(Method.GET, url(orderId), "", BaseRsp.class, listener);
    }


    private static String url(String order_id) {
        return NetworkConfig.BoxServiceAddress + "/v2/delivery/" + order_id + "/sendsms";
    }

}
