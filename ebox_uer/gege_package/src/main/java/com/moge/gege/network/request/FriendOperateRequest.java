package com.moge.gege.network.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.enums.FriendOperateType;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * check friend relation
 */
public class FriendOperateRequest extends BaseRequest<BaseModel>
{
    private int mType;

    public FriendOperateRequest(int type, String uid, String msgId,
            ResponseEventHandler<BaseModel> listener)
    {
        super(Method.POST, getRequestUrl(type, uid), getRequestParam(msgId),
                BaseModel.class, listener);
        mType = type;
    }

    public int getType()
    {
        return mType;
    }

    private static String getRequestUrl(int type, String uid)
    {
        switch (type)
        {
            case FriendOperateType.APPLY_FRIEND:
                return NetworkConfig.generalAddress + "/v1/user/friend/" + uid
                        + "/apply";
            case FriendOperateType.REFUSE_APPLY:
                return NetworkConfig.generalAddress + "/v1/user/friend/" + uid
                        + "/disagree";
            case FriendOperateType.ACCEPT_APPLY:
                return NetworkConfig.generalAddress + "/v1/user/friend/" + uid
                        + "/agree";
            default:
                break;
        }

        return "";
    }

    private static String getRequestParam(String msgId)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("msg_id", msgId));
        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }
}
