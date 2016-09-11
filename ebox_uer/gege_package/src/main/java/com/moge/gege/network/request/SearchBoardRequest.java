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
 * search board
 */
public class SearchBoardRequest extends BaseRequest<RespBoardListModel>
{
    public SearchBoardRequest(String word, String cursor,
            ResponseEventHandler<RespBoardListModel> listener)
    {
        super(Method.GET, getRequestUrl(word, cursor), "",
                RespBoardListModel.class, listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl(String word, String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/board/search" + "?"
                + getRequestParam(word, cursor);
    }

    private static String getRequestParam(String word, String cursor)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("word", word));
        list.add(new BasicNameValuePair("cursor", cursor));
        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }
}
