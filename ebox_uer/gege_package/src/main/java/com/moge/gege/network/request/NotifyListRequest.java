package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespNotifyListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query notify list, heve read : false - not read ; true - all
 */
public class NotifyListRequest extends BaseRequest<RespNotifyListModel>
{
    public NotifyListRequest(boolean haveRead, String notifyType, String msgId,
            ResponseEventHandler<RespNotifyListModel> listener)
    {
        super(Method.GET, getRequestUrl(haveRead, notifyType, msgId), "",
                RespNotifyListModel.class, listener);
    }

    private static String getRequestUrl(boolean haveRead, String notifyType,
            String msgId)
    {
        if (haveRead)
        {
            return NetworkConfig.chatAddress + "/v1/notify" + "?notify_type="
                    + notifyType + "&msg_id=" + msgId + "&unread=0";
        }
        else
        {
            return NetworkConfig.chatAddress + "/v1/notify" + "?notify_type="
                    + notifyType;
        }

    }

}
