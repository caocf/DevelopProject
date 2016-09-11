package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.RspLoginOperator;
import com.ebox.ex.network.model.req.ReqVerifyOperator;

public class LoginRequest extends BaseRequest<RspLoginOperator> {

    public LoginRequest(ReqVerifyOperator parm,
                        ResponseEventHandler<RspLoginOperator> listener)
    {
        super(Method.POST, url(), body(parm), RspLoginOperator.class, listener);
        setEnableCookie(true);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/operator/login";
    }

    private static String body(ReqVerifyOperator parms){

        String json = JsonSerializeUtil.bean2Json(parms);

        return json;
    }

}
