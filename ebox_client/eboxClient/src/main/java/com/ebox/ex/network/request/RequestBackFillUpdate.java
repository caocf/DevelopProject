package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.base.BaseReq;
import com.ebox.ex.network.model.base.BaseRsp;

public class RequestBackFillUpdate extends BaseRequest<BaseRsp> {

    public RequestBackFillUpdate(Integer req,
                                 ResponseEventHandler<BaseRsp> listener) {
        super(Method.POST, url(req), body(), BaseRsp.class, listener);
        setNeedMainThread(false);
    }

    private static String url(Integer req) {

        return NetworkConfig.BoxServiceAddress + "/v2/terminal/app/update/result/"+req;
    }

    private static String body(){

        String json = JsonSerializeUtil.bean2Json(new BaseReq());

        return json;
    }

}
