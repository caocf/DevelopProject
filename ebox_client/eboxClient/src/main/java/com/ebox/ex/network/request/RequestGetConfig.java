package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.RspGetConfig;
import com.ebox.ex.network.model.req.ReqGetConfig;

public class RequestGetConfig extends BaseRequest<RspGetConfig> {

    public RequestGetConfig(ReqGetConfig parm,
                            ResponseEventHandler<RspGetConfig> listener)
    {
        super(Method.POST, url(), body(parm), RspGetConfig.class, listener);
        setNeedMainThread(false);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/terminal/app/update/1";
    }

    private static String body(ReqGetConfig parms){

        String json = JsonSerializeUtil.bean2Json(parms);

        return json;
    }

}
