package com.moge.gege.network.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespCommunityListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * search community
 */
public class SearchCommunityRequest extends BaseRequest<RespCommunityListModel>
{
    public SearchCommunityRequest(String word, int cityId, String cursor,
            ResponseEventHandler<RespCommunityListModel> listener)
    {
        super(Method.GET, getRequestUrl(word, cityId, cursor), "",
                RespCommunityListModel.class, listener);
    }

    private static String getRequestUrl(String word, int cityId, String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/community/search" + "?"
                + getRequestParam(word, cityId, cursor);
    }

    private static String getRequestParam(String word, int cityId, String cursor)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("word", word));
        list.add(new BasicNameValuePair("city_id", String.valueOf(cityId)));
        list.add(new BasicNameValuePair("cursor", cursor));
        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }
}
