package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespIndexRecommendPlaceModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * recommend place
 */
public class RecommendPlaceRequest extends
        BaseRequest<RespIndexRecommendPlaceModel>
{
    public RecommendPlaceRequest(
            ResponseEventHandler<RespIndexRecommendPlaceModel> listener)
    {
        super(Method.GET, getRequestUrl(), "",
                RespIndexRecommendPlaceModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/index/recommends";
    }

}
