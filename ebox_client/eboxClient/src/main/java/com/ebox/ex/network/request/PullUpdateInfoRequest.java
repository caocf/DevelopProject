package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.network.model.RspPullUpdateInfo;

public class PullUpdateInfoRequest extends BaseRequest<RspPullUpdateInfo> {

    //下拉全部格口数据
    public PullUpdateInfoRequest(ResponseEventHandler<RspPullUpdateInfo> listener) {
        super(Method.GET, url(), null, RspPullUpdateInfo.class, listener);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/terminal/sync/update";
    }

}
