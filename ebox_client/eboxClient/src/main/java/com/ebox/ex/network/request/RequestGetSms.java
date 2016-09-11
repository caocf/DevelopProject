package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.RspGetVerifySms;
import com.ebox.ex.network.model.base.BaseRsp;

public class RequestGetSms extends BaseRequest<BaseRsp> {


    public static final int TYPE_SMS=1;
    public static final int TYPE_VOICE=2;

    public static final String SLUG="&voice=1";

    public RequestGetSms(RspGetVerifySms parm,
                         ResponseEventHandler<BaseRsp> listener,int verify_type)
    {
        super(Method.GET, url(parm,verify_type), body(parm), BaseRsp.class, listener);
    }


    private static String url(RspGetVerifySms parm,int verify_code) {

        //http://api.ebox.local.gegebox.com:8888/v2/operator/15805153655/sms?type=register
        String phone=parm.getTelephone();
        String tpye = "";

        if (parm.getType() == 4) {
            tpye = "register";
        }
        else if (parm.getType() == 5)
        {
            tpye = "reset";
        }
        String url=null;
        if (verify_code==TYPE_VOICE){
            url=NetworkConfig.BoxServiceAddress + "/v2/operator/"+phone+"/sms?type="+tpye+SLUG;
        }else if (verify_code==TYPE_SMS){
            url=NetworkConfig.BoxServiceAddress + "/v2/operator/"+phone+"/sms?type="+tpye;
        }

        return url;
    }

    private static String body(RspGetVerifySms parms){

        String json = JsonSerializeUtil.bean2Json(parms);

        return json;
    }

}
