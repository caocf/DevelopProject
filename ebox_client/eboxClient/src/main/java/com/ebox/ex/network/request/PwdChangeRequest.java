package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.RspLoginOperator;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.req.ReqChangePwd;
import com.ebox.ex.network.model.req.ReqVerifyOperator;

public class PwdChangeRequest extends BaseRequest<BaseRsp> {

    public PwdChangeRequest(ReqChangePwd parm,
                            ResponseEventHandler<BaseRsp> listener)
    {
        super(Method.POST, url(), body(parm), BaseRsp.class, listener);
        setEnableCookie(true);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/operator/password";
    }

    private static String body(ReqChangePwd parms){

        String json = JsonSerializeUtil.bean2Json(parms);

        return json;
    }

}
