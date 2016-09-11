package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespUploadTokenModel;
import com.moge.gege.model.TopicPublishModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

public class GetUploadTokenRequest extends BaseRequest<RespUploadTokenModel>
{
    private TopicPublishModel mPublishModel;

    public GetUploadTokenRequest(
            ResponseEventHandler<RespUploadTokenModel> listener)
    {
        super(Method.GET, getRequestUrl(), "", RespUploadTokenModel.class,
                listener);
    }

    public GetUploadTokenRequest(TopicPublishModel publishModel,
            ResponseEventHandler<RespUploadTokenModel> listener)
    {
        super(Method.GET, getRequestUrl(), "", RespUploadTokenModel.class,
                listener);
        mPublishModel = publishModel;
    }

    public TopicPublishModel getPublishModel()
    {
        return mPublishModel;
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/upload/token";
    }
}
