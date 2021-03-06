package com.ebox.mgt.ui.fragment.pollingfg.model;

import com.android.volley.AuthFailureError;
import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.base.BaseRsp;

import java.util.Map;

/**
 * Created by prin on 2015/10/8.
 */
public class PollingRequest extends BaseRequest<BaseRsp> {

    public PollingRequest(ReqPolling params, ResponseEventHandler<BaseRsp> listener) {

        super(Method.POST, url(), body(params), BaseRsp.class, listener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        headers.put("X-STORE-APPID", "553885d3235aac4b6f55ba74");
        headers.put("X-STORE-APPKEY","8bSR4jhiq3AiIpZ6VDjU0lJ0");
        headers.put("X-STORE-TYPE","COLLECTION");
        return headers;
    }

    private static String url() {
        return NetworkConfig.SuperAdmin+"/v1/ebox/terminal_ops ";
    }

    private static String body(ReqPolling parms) {

        String json = JsonSerializeUtil.bean2Json(parms);
        return json;
    }
}
