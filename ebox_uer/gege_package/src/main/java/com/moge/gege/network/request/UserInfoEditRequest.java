package com.moge.gege.network.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespUserModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

public class UserInfoEditRequest extends BaseRequest<RespUserModel>
{
    public UserInfoEditRequest(String communityId, String nickname,
            String avatar, int gender, String introduction, String job,
            String interest, ResponseEventHandler<RespUserModel> listener)
    {
        super(Method.PUT, getRequestUrl(), getRequestParam(communityId,
                nickname, avatar, gender, introduction, job, interest),
                RespUserModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/user";
    }

    private static String getRequestParam(String communityId, String nickname,
            String avatar, int gender, String introduction, String job,
            String interest)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("community_id", communityId));
        list.add(new BasicNameValuePair("nickname", nickname));
        list.add(new BasicNameValuePair("avatar", avatar));
        list.add(new BasicNameValuePair("gender", String.valueOf(gender)));
        list.add(new BasicNameValuePair("introduction", introduction));
        list.add(new BasicNameValuePair("profession", job));
        list.add(new BasicNameValuePair("interest", interest));

        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }

}
