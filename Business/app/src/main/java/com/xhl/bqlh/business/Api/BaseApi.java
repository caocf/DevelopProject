package com.xhl.bqlh.business.Api;

import android.text.TextUtils;

import com.xhl.bqlh.business.AppConfig.Constant;
import com.xhl.bqlh.business.AppDelegate;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;

import java.util.List;

/**
 * Created by Sum on 16/4/14.
 */
public abstract class BaseApi {

    public static final String PAGE_NUM = "currentPage";
    public static final String PAGE_SIZE = "showCount";

    public static final int TIME = 3 * 60 * 1000;

    //添加Cookie
    protected void addExtendParams(RequestParams params) {

        String cookie = AppDelegate.appContext.mCookie;
        params.setConnectTimeout(TIME);
        if (!TextUtils.isEmpty(cookie)) {
            params.setHeader("Cookie", cookie);
        }
        if (Constant.isDebug) {
            Logger.v("req url:" + params.getUri() + " localCookie:" + cookie);
        }
        print(params);
    }

    private void print(RequestParams params) {
        if (Constant.isDebug) {
            List<KeyValue> bodyParams = params.getQueryStringParams();
            if (bodyParams.size() <= 0) {
                return;
            }
            StringBuilder builder = new StringBuilder();
            for (KeyValue key : bodyParams) {
                if (key != null) {
                    builder.append(key.key).append(":").append(key.value == null ? "null" : key.value).append(" ");
                }
            }
            String log = builder.toString();
            Logger.v("body params [ " + log + " ]");
        }
    }

}
