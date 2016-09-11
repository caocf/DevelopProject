package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.network.model.RspFirstUpdateLocalOrder;

public class FirstUpdateLocalOrderRequest extends BaseRequest<RspFirstUpdateLocalOrder> {

    public FirstUpdateLocalOrderRequest(ResponseEventHandler<RspFirstUpdateLocalOrder> listener)
    {
        super(Method.GET, url(), null, RspFirstUpdateLocalOrder.class, listener);
        setNeedMainThread(false);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/terminal/deliverys";
    }

}
