package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.req.ReqChangePwd;
import com.ebox.ex.network.model.req.ReqResetPassword;

public class PwdResetRequest extends BaseRequest<BaseRsp> {

    public PwdResetRequest(ReqResetPassword parm,
                           ResponseEventHandler<BaseRsp> listener)
    {
        super(Method.POST, url(), body(parm), BaseRsp.class, listener);
        setEnableCookie(true);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/operator/password/reset";
    }

    private static String body(Object parms){

        String json = JsonSerializeUtil.bean2Json(parms);

        return json;
    }

}
