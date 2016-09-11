package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.network.model.RspUpdateOperatorBalance;

public class RequestUpdateOperatorInfo extends BaseRequest<RspUpdateOperatorBalance> {

    public RequestUpdateOperatorInfo(String operatorId,ResponseEventHandler<RspUpdateOperatorBalance> listener)
    {
        super(Method.GET, getUpdateUrl(operatorId), "", RspUpdateOperatorBalance.class, listener);
        setEnableCookie(true);
    }

    public static String getUpdateUrl(String operatorId) {

        return  NetworkConfig.BoxServiceAddress + "/v2/operator/"+operatorId+"/balance";
    }

}
