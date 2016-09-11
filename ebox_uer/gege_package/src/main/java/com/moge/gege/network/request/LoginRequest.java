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

public class LoginRequest extends BaseRequest<RespUserModel>
{
    public LoginRequest(String mobile, String code, float longitude,
            float latitude, ResponseEventHandler<RespUserModel> listener)
    {
        super(Method.POST, getRequestUrl(), getRequestParam(mobile, code,
                longitude, latitude), RespUserModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/login";
    }

    private static String getRequestParam(String mobile, String code,
            float longitude, float latitude)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("mobile", mobile));
        list.add(new BasicNameValuePair("code", code));
        list.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
        list.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));

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
