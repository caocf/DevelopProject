package com.moge.gege.network.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespBoardInfoModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * board info request
 */
public class BoardInfoRequest extends BaseRequest<RespBoardInfoModel>
{
    public BoardInfoRequest(String boardId,
            ResponseEventHandler<RespBoardInfoModel> listener)
    {
        super(Method.GET, getRequestUrl(boardId), "", RespBoardInfoModel.class,
                listener);
    }

    private static String getRequestUrl(String boardId)
    {
        return NetworkConfig.generalAddress + "/v1/board/" + boardId;
    }

    private static String getRequestParam(float longitude, float latitude)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
        list.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }
}
