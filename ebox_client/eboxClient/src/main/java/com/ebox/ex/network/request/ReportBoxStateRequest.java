package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.RspReportTerminalStatus;
import com.ebox.ex.network.model.req.ReqReportTerminalStatus;

public class ReportBoxStateRequest extends BaseRequest<RspReportTerminalStatus> {

    public ReportBoxStateRequest(ReqReportTerminalStatus parm,
                                 ResponseEventHandler<RspReportTerminalStatus> listener) {
        super(Method.POST, url(), body(parm), RspReportTerminalStatus.class, listener);
        setNeedMainThread(false);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/terminal/box";
    }

    private static String body(ReqReportTerminalStatus parms){

        String json = JsonSerializeUtil.bean2Json(parms);

        return json;
    }

}
