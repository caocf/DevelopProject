package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.req.ReqPickupItem;

public class ReportPickupItemRequest extends BaseRequest<BaseRsp> {

    public ReportPickupItemRequest(ReqPickupItem parms,
                                   ResponseEventHandler<BaseRsp> listener)
    {
        super(Method.POST, url(), body(parms), BaseRsp.class, listener);
        setNeedMainThread(false);
    }


    private static String url() {
        //取超时件和普通取件相同接口

        return NetworkConfig.BoxServiceAddress + "/v2/terminal/delivery/pickup";
    }

    private static String body(ReqPickupItem parms){

        String json = JsonSerializeUtil.bean2Json(parms);

        return json;
    }

}
