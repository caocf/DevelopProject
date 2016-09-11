package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.RspUploadPicToken;
import com.ebox.ex.network.model.base.BaseReq;

public class UploadPicTokenRequest extends BaseRequest<RspUploadPicToken> {

    public UploadPicTokenRequest(ResponseEventHandler<RspUploadPicToken> listener)
    {
        super(Method.GET, url(), "", RspUploadPicToken.class, listener);
        setNeedMainThread(false);
    }


    private static String url() {

        return NetworkConfig.BoxServiceAddress + "/v2/upload/token";
    }

    private static String body(){

        String json = JsonSerializeUtil.bean2Json(new BaseReq());

        return json;
    }

}
