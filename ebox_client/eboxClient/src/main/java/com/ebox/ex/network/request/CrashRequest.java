package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.req.ReqCrashReport;

public class CrashRequest extends BaseRequest<BaseRsp> {

    public CrashRequest(ReqCrashReport parm,
                        ResponseEventHandler<BaseRsp> listener)
    {
        super(Method.POST, url(), body(parm), BaseRsp.class, listener);
        setNeedMainThread(false);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/terminal/app/crash";
    }

    private static String body(ReqCrashReport parms){

        String json = JsonSerializeUtil.bean2Json(parms);

        return json;
    }

}
