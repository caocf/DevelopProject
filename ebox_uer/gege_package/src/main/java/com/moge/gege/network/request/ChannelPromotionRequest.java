package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespChannelPromotionModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query channel promotion
 */
public class ChannelPromotionRequest extends BaseRequest<RespChannelPromotionModel>
{
    public ChannelPromotionRequest(
                                   ResponseEventHandler<RespChannelPromotionModel> listener)
    {
        super(Method.GET, getRequestUrl(), "", RespChannelPromotionModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return String.format("%s/v1/user/channel/promotion", NetworkConfig.generalAddress);
    }
}
