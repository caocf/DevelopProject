package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.req.ReqAlarmReport;

public class ReportAlarmRequest extends BaseRequest<BaseRsp> {

    public ReportAlarmRequest(ReqAlarmReport parm,
                              ResponseEventHandler<BaseRsp> listener)
    {
        super(Method.POST, url(), body(parm), BaseRsp.class, listener);
        setNeedMainThread(false);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/terminal/app/alarm";
    }

    private static String body(ReqAlarmReport parms){

        String json = JsonSerializeUtil.bean2Json(parms);

        return json;
    }

}
