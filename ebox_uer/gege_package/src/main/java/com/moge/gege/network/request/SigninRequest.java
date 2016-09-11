package com.moge.gege.network.request;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.moge.gege.AppApplication;
import com.moge.gege.config.NetworkConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.model.RespUserModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.util.DeviceInfoUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.MD5Util;
import com.moge.gege.util.PackageUtil;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 上线请求
 */
public class SigninRequest extends BaseRequest<RespUserModel>
{
    public Activity context;
    public int from;

    public SigninRequest(float longitude, float latitude,
            ResponseEventHandler<RespUserModel> listener)
    {
        super(Method.POST, getRequestUrl(longitude, latitude),
                getRequestParam(longitude, latitude), RespUserModel.class,
                listener);
    }

    public void setParams(Activity context, int from)
    {
        this.context = context;
        this.from = from;
    }

    private static String getRequestUrl(float longitude, float latitude)
    {
        String secureKey = PersistentData.instance().getUserInfo().getSecure_key();
        if(TextUtils.isEmpty(secureKey)) {
            return NetworkConfig.generalAddress + "/v1/signin";
        }
        else
        {
            return NetworkConfig.generalAddress + "/v1/signin?sign=" + getSign(longitude, latitude, secureKey);
        }
    }

    private static List<NameValuePair> getParamList(float longitude, float latitude)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();

        Context ctx = AppApplication.instance();
        list.add(new BasicNameValuePair("app_name", "gege"));
        list.add(new BasicNameValuePair("appver", PackageUtil
                .getVersionName(ctx)));
        list.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
        list.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
        list.add(new BasicNameValuePair("network", DeviceInfoUtil
                .getNetType(ctx)));
        list.add(new BasicNameValuePair("os", "android"));
        list.add(new BasicNameValuePair("osver", DeviceInfoUtil
                .getSysVersion(ctx)));
        list.add(new BasicNameValuePair("phonemodel", DeviceInfoUtil
                .getDeviceName(ctx)));
        list.add(new BasicNameValuePair("timestamp", String.valueOf((int)(System.currentTimeMillis()/1000))));
        list.add(new BasicNameValuePair("udid", DeviceInfoUtil.getDeviceId(ctx)));

        return list;
    }

    private static String getRequestParam(float longitude, float latitude)
    {
        return URLEncodedUtils.format(getParamList(longitude, latitude), HTTP.UTF_8);
    }

    private static String getSign(float longitude, float latitude, String key)
    {
        return genPackage(getParamList(longitude, latitude), key);
    }

    private static String genPackage(List<NameValuePair> params, String key) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(key);

        LogUtil.d(sb.toString());

        String packageSign;
        try {

            packageSign = MD5Util.md5DigestStr(sb.toString().getBytes()).toUpperCase();
        }
        catch(IOException e)
        {
            packageSign = null;
        }

        LogUtil.d(packageSign);
        return packageSign;
    }
}
