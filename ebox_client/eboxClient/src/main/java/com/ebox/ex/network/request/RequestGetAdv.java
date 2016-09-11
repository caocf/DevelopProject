package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.RspGetAdv;
import com.ebox.ex.network.model.base.BaseReq;

public class RequestGetAdv extends BaseRequest<RspGetAdv> {

    public RequestGetAdv(ResponseEventHandler<RspGetAdv> listener)
    {
        super(Method.POST, url(), body(), RspGetAdv.class, listener);
        setNeedMainThread(false);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/terminal/app/update/3";
    }

    private static String body(){

        String json = JsonSerializeUtil.bean2Json(new BaseReq());

        return json;
    }

}
