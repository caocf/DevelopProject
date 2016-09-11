package com.moge.gege.network.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * gift list query
 */
public class SendGiftRequest extends BaseRequest<BaseModel>
{
    public SendGiftRequest(String uid, String gid, int gtype,
            ResponseEventHandler<BaseModel> listener)
    {
        super(Method.POST, getRequestUrl(uid), getRequestParam(gid, gtype),
                BaseModel.class, listener);
    }

    private static String getRequestUrl(String uid)
    {
        return NetworkConfig.generalAddress + "/v1/user/" + uid + "/gift";
    }

    private static String getRequestParam(String gid, int gtype)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("gid", gid));
        list.add(new BasicNameValuePair("gtype", String.valueOf(gtype)));
        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }

}
