package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespNewBoardListModel;
import com.moge.gege.model.RespQRCodeModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * hot board
 */
public class QRCodeRequest extends BaseRequest<RespQRCodeModel>
{
    public QRCodeRequest(String content,
            ResponseEventHandler<RespQRCodeModel> listener)
    {
        super(Method.GET, getRequestUrl(content), "",
                RespQRCodeModel.class, listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl(String content)
    {
        return NetworkConfig.generalAddress + "/v1/qrcode" + "?content="
                + content;
    }
}
