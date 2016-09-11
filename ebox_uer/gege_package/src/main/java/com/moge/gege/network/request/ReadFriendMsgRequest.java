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
 * read friend message
 */
public class ReadFriendMsgRequest extends BaseRequest<BaseModel>
{
    public ReadFriendMsgRequest(String msgId,
            ResponseEventHandler<BaseModel> listener)
    {
        super(Method.POST, getRequestUrl(), getRequestParam(msgId),
                BaseModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.chatAddress + "/v1/friend/read";
    }

    private static String getRequestParam(String msgId)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("msg_id", msgId));
        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }
}
