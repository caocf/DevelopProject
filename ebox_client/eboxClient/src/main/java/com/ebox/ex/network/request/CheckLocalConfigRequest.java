package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.req.ReqCheckLocalConfig;

/**
 * Created by Android on 2015/10/10.
 */
public class CheckLocalConfigRequest extends BaseRequest<BaseRsp> {

    public CheckLocalConfigRequest(ReqCheckLocalConfig req,ResponseEventHandler<BaseRsp> listener) {
        super(Method.POST, url(), body(req), BaseRsp.class, listener);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/terminal/sync/validate";
    }

    private static String body(Object parms){

        String json = JsonSerializeUtil.bean2Json(parms);

        return json;
    }
}
