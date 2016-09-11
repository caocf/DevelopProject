package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespFriendMsgListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query chat list, heve read : false - not read ; true - all
 */
public class FriendMsgListRequest extends BaseRequest<RespFriendMsgListModel>
{
    public FriendMsgListRequest(boolean haveRead, String msgId,
            ResponseEventHandler<RespFriendMsgListModel> listener)
    {
        super(Method.GET, getRequestUrl(haveRead, msgId), "",
                RespFriendMsgListModel.class, listener);
    }

    private static String getRequestUrl(boolean haveRead, String msgId)
    {
        if (haveRead)
        {
            return NetworkConfig.chatAddress + "/v1/friend" + "?msg_id="
                    + msgId + "&unread=0";
        }
        else
        {
            return NetworkConfig.chatAddress + "/v1/friend";
        }

    }

}
