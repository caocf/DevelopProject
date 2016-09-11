package com.moge.gege.network.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.MyBoardModel;
import com.moge.gege.model.RespBoardNewTopicListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * board new topic count request
 */
public class BoardNewTopicRequest extends
        BaseRequest<RespBoardNewTopicListModel>
{
    public BoardNewTopicRequest(List<MyBoardModel> boardList,
            ResponseEventHandler<RespBoardNewTopicListModel> listener)
    {
        super(Method.GET, getRequestUrl(boardList), "",
                RespBoardNewTopicListModel.class, listener);

    }

    private static String getRequestUrl(List<MyBoardModel> boardList)
    {
        return NetworkConfig.generalAddress + "/v1/statistics/board/update?"
                + getRequestParam(boardList);
    }

    private static String getRequestParam(List<MyBoardModel> boardList)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (int i = 0; boardList != null && i < boardList.size(); i++)
        {
            list.add(new BasicNameValuePair("bids", boardList.get(i).get_id()));
        }
        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }
}
