package com.moge.gege.network.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;

import com.moge.gege.AppApplication;
import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespUserModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.util.DeviceInfoUtil;
import com.moge.gege.util.PackageUtil;

public class RegisterRequest extends BaseRequest<RespUserModel>
{
    public RegisterRequest(String username, String nickname, String avatar,
            int gender, String community, float longitude, float latitude,
            String job, String interest,
            ResponseEventHandler<RespUserModel> listener)
    {
        super(Method.POST, getRequestUrl(), getRequestParam(username, nickname,
                avatar, gender, community, longitude, latitude, job, interest),
                RespUserModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/register";
    }

    private static String getRequestParam(String username, String nickname,
            String avatar, int gender, String community, float longitude,
            float latitude, String job, String interest)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("username", username));
        list.add(new BasicNameValuePair("nickname", nickname));
        list.add(new BasicNameValuePair("avatar", avatar));
        list.add(new BasicNameValuePair("gender", String.valueOf(gender)));
        list.add(new BasicNameValuePair("community", community));
        list.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
        list.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
        list.add(new BasicNameValuePair("profession", job));
        list.add(new BasicNameValuePair("interest", interest));

        Context ctx = AppApplication.instance();
        list.add(new BasicNameValuePair("udid", DeviceInfoUtil.getDeviceId(ctx)));
        list.add(new BasicNameValuePair("appver", PackageUtil
                .getVersionName(ctx)));
        list.add(new BasicNameValuePair("os", "android"));
        list.add(new BasicNameValuePair("phonemodel", DeviceInfoUtil
                .getDeviceName(ctx)));
        list.add(new BasicNameValuePair("network", DeviceInfoUtil
                .getNetType(ctx)));
        list.add(new BasicNameValuePair("app_name", "gege"));
        list.add(new BasicNameValuePair("osver", DeviceInfoUtil
                .getSysVersion(ctx)));

        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }
}
