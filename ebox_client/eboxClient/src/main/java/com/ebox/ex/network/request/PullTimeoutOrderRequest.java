package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.network.model.RspPullTimeoutOrder;

public class PullTimeoutOrderRequest extends BaseRequest<RspPullTimeoutOrder> {

    //下拉全部格口数据
    public PullTimeoutOrderRequest(ResponseEventHandler<RspPullTimeoutOrder> listener) {
        super(Method.GET, url(), null, RspPullTimeoutOrder.class, listener);
        setNeedMainThread(false);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/terminal/sync/timeout";
    }

}
