package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespCategoryListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * pet breed list query
 */
public class PetBreedListRequest extends BaseRequest<RespCategoryListModel>
{
    public PetBreedListRequest(
            ResponseEventHandler<RespCategoryListModel> listener)
    {
        super(Method.GET, getRequestUrl(), "", RespCategoryListModel.class,
                listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/living/service/prt/breed";
    }

}
