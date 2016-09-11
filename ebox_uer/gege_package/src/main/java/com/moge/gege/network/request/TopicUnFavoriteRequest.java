package com.moge.gege.network.request;

import android.text.TextUtils;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;

/**
 * unfavorite topic
 */
public class TopicUnFavoriteRequest extends BaseRequest<BaseModel>
{
    public TopicUnFavoriteRequest(int topicType, String topicId, String boardId,
                                  ResponseEventHandler<BaseModel> listener)
    {
        super(Method.DELETE, getRequestUrl() + getRequestParam(topicType, topicId,
                    boardId), "",  BaseModel.class, listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/favorite?";
    }

    private static String getRequestParam(int topicType, String topicId,
            String boardId)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("tid", topicId));
        list.add(new BasicNameValuePair("topic_type", String.valueOf(topicType)));

        if (!TextUtils.isEmpty(boardId))
        {
            list.add(new BasicNameValuePair("bid", boardId));
        }

        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }

}
