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
 * locate around community
 */
public class LocateCommunityRequest extends BaseRequest<RespCommunityListModel>
{
    public LocateCommunityRequest(float longitude, float latitude, int cityId,
            String cursor, ResponseEventHandler<RespCommunityListModel> listener)
    {
        super(Method.GET, getRequestUrl(longitude, latitude, cityId, cursor),
                "", RespCommunityListModel.class, listener);
    }

    private static String getRequestUrl(float longitude, float latitude,
            int cityId, String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/community" + "?"
                + getRequestParam(longitude, latitude, cityId, cursor);
    }

    private static String getRequestParam(float longitude, float latitude,
            int cityId, String cursor)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
        list.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
        list.add(new BasicNameValuePair("city_id", String.valueOf(cityId)));
        list.add(new BasicNameValuePair("cursor", cursor));
        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }
}
