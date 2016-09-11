package com.moge.gege.network.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.text.TextUtils;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.enums.TopicType;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * delete topic
 */
public class TopicDeleteRequest extends BaseRequest<BaseModel>
{
    public TopicDeleteRequest(int topicType, String topicId, String boardId,
            ResponseEventHandler<BaseModel> listener)
    {
        super(Method.DELETE, getRequestUrl(topicType, topicId) + "?"
                + getRequestParam(topicType, boardId), "", BaseModel.class,
                listener);
    }

    private static String getRequestUrl(int topicType, String topicId)
    {
        if (topicType == TopicType.GENERAL_TOPIC)
        {
            return NetworkConfig.generalAddress + "/v1/topic/" + topicId;
        }
        else if (topicType == TopicType.ACTIVITY_TOPIC)
        {
            return NetworkConfig.generalAddress + "/v1/activity/" + topicId;
        }
        else
        {
            return NetworkConfig.generalAddress + "/v1/living/service/"
                    + topicId;
        }
    }

    private static String getRequestParam(int topicType, String boardId)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();

        if (topicType > TopicType.CATEGORY_TOPIC)
        {
            list.add(new BasicNameValuePair("service_type", String
                    .valueOf(topicType - TopicType.CATEGORY_TOPIC)));
        }

        if (!TextUtils.isEmpty(boardId))
        {
            list.add(new BasicNameValuePair("bid", boardId));
        }

        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }

}
