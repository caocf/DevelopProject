package com.moge.gege.network.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespBoardListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * recommend around board
 */
public class RecommendBoardRequest extends BaseRequest<RespBoardListModel>
{
    public RecommendBoardRequest(String cursor, float longitude,
            float latitude, ResponseEventHandler<RespBoardListModel> listener)
    {
        super(Method.GET, getRequestUrl(cursor, longitude, latitude), "",
                RespBoardListModel.class, listener);
    }

    private static String getRequestUrl(String cursor, float longitude,
            float latitude)
    {
        return NetworkConfig.generalAddress + "/v1/board/recommend" + "?"
                + getRequestParam(cursor, longitude, latitude);
    }

    private static String getRequestParam(String cursor, float longitude,
            float latitude)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("cursor", cursor));
        list.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
        list.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }
}
