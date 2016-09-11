package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.RspGetOrganization;
import com.ebox.ex.network.model.base.BaseReq;

public class RequestGetOrgnization extends BaseRequest<RspGetOrganization> {

    public RequestGetOrgnization(ResponseEventHandler<RspGetOrganization> listener)
    {
        super(Method.GET, url(), body(), RspGetOrganization.class, listener);
    }


    private static String url() {
        return NetworkConfig.BoxServiceAddress + "/v2/public/organization";
    }

    private static String body(){

        String json = JsonSerializeUtil.bean2Json(new BaseReq());

        return json;
    }

}
