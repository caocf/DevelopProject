package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.network.model.req.RspGetNote;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by prin on 2015/11/2.
 * 获得充值记录接口
 */
public class RequestGetNote extends BaseRequest<RspGetNote>{
    public RequestGetNote(HashMap<String,Object> params,ResponseEventHandler<RspGetNote> listener) {
        super(Method.GET, url(params), null, RspGetNote.class, listener);
        setEnableCookie(true);
    }

    private static String url(HashMap<String, Object> params) {
        String url= NetworkConfig.BoxServiceAddress+"/v2/operator/charge?";
        StringBuilder param=new StringBuilder(url);
        Set<Map.Entry<String, Object>> entries = params.entrySet();

        for (Map.Entry<String, Object> m : entries)
        {
            String key = m.getKey();
            Object value = m.getValue();
            param.append(key).append("=").append(value).append("&");
        }
        String req_url=param.toString();
        return req_url.substring(0,req_url.length()-1);
    }


}
