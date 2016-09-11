package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.network.model.RspPullBoxState;

public class PullBoxStateRequest extends BaseRequest<RspPullBoxState> {

    //下拉全部格口数据
    public PullBoxStateRequest(ResponseEventHandler<RspPullBoxState> listener) {
        super(Method.GET, url(), null, RspPullBoxState.class, listener);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/terminal/box";
    }

}
