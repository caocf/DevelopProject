package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.req.ReqRegister;

import java.util.HashMap;

public class RegisterRequest extends BaseRequest<BaseRsp> {

    public RegisterRequest(ReqRegister parm,
                           ResponseEventHandler<BaseRsp> listener)
    {
        super(Method.POST, url(), body(parm), BaseRsp.class, listener);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/operator/register";
    }

    private static String body(ReqRegister parms){

        String json = JsonSerializeUtil.bean2Json(parms);

        return json;
    }

}
