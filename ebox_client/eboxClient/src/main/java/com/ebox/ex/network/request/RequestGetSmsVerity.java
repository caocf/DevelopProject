package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.base.BaseRsp;

import java.util.HashMap;

public class RequestGetSmsVerity extends BaseRequest<BaseRsp> {

    public RequestGetSmsVerity(HashMap<String ,Object> parm,
                               ResponseEventHandler<BaseRsp> listener)
    {
        super(Method.POST, url(parm), body(parm), BaseRsp.class, listener);
    }


    private static String url(HashMap<String ,Object> parm) {

        String phone= (String) parm.get("username");
        Integer  tpye = (Integer) parm.get("type");

        String typeS="";
        if (tpye == 4)
        {
            typeS = "register";
        }
        else if (tpye == 5)
        {
            typeS = "reset";
        }

        return NetworkConfig.BoxServiceAddress + "/v2/operator/"+phone+"/sms?type="+typeS;
    }

    private static String body(Object parms){

        String json = JsonSerializeUtil.bean2Json(parms);

        return json;
    }

}
