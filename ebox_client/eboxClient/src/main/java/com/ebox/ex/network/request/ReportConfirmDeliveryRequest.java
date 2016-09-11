package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.RspConfirmDelivery;
import com.ebox.ex.network.model.req.ReqConfirmDelivery;

public class ReportConfirmDeliveryRequest extends BaseRequest<RspConfirmDelivery> {

    public ReportConfirmDeliveryRequest(ReqConfirmDelivery parm,
                                        ResponseEventHandler<RspConfirmDelivery> listener) {
        super(Method.POST, url(), body(parm), RspConfirmDelivery.class, listener);
        setNeedMainThread(false);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/terminal/delivery/store";
    }

    private static String body(ReqConfirmDelivery parms) {

        String json = JsonSerializeUtil.bean2Json(parms);

        return json;
    }

}
