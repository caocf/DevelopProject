package com.xhl.bqlh.model.base;

import com.google.gson.Gson;
import com.xhl.bqlh.AppConfig.Constant;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;

/**
 * Created by Sum on 16/3/11.
 */
public class JsonResParser implements ResponseParser {
    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        if (Constant.isDebug) {
            Logger.v("response->" + result);
        }
        return new Gson().fromJson(result, resultType);
    }
}
