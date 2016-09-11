package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.base.BaseRsp;

import java.util.HashMap;

public class UploadPicTagRequest extends BaseRequest<BaseRsp> {

    public UploadPicTagRequest(String orderId,HashMap<String ,Object> data,ResponseEventHandler<BaseRsp> listener)
    {
        super(Method.POST, url(orderId), body(data), BaseRsp.class, listener);
        setNeedMainThread(false);
    }


    private static String url(String orderId) {

        return NetworkConfig.BoxServiceAddress + "/v2/delivery/"+orderId+"/image";
    }

    private static String body(Object tag){

        String json = JsonSerializeUtil.bean2Json(tag);

        return json;
    }

}
