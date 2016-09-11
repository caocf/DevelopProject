package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespMessageListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query chat list, heve read : false - not read ; true - all
 */
public class ChatListRequest extends BaseRequest<RespMessageListModel>
{
    public ChatListRequest(boolean haveRead, String fromUid, String msgId,
            ResponseEventHandler<RespMessageListModel> listener)
    {
        super(Method.GET, getRequestUrl(haveRead, fromUid, msgId), "",
                RespMessageListModel.class, listener);
    }

    private static String getRequestUrl(boolean haveRead, String fromUid,
            String msgId)
    {
        if (haveRead)
        {
            return NetworkConfig.chatAddress + "/v1/chat" + "?from_uid="
                    + fromUid + "&msg_id=" + msgId + "&unread=0";
        }
        else
        {
            return NetworkConfig.chatAddress + "/v1/chat" + "?from_uid="
                    + fromUid + "&msg_id=" + msgId;
        }

    }

}
