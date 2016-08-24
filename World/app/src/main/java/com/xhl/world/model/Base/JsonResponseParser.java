package com.xhl.world.model.Base;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.util.ParameterizedTypeUtil;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by wyouflf on 15/11/5.
 */
public class JsonResponseParser implements ResponseParser {

    Gson gson = new Gson();

    @Override
    public void checkResponse(UriRequest request) throws Throwable {
//        Logger.v("request url:" + request.getRequestUri());
    }

    /**
     * 转换result为resultType类型的对象
     *
     * @param resultType  返回值类型(可能带有泛型信息)
     * @param resultClass 返回值类型
     * @param result      字符串数据
     * @return
     * @throws Throwable
     */
    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        Logger.v("response:" + result);
        if (resultClass == List.class) {
            return JSON.parseArray(result, (Class<?>) ParameterizedTypeUtil.getParameterizedType(resultType, List.class, 0));
        } else {
            Object o = gson.fromJson(result, resultType);
//            Object o = JSON.parseObject(result, resultType);
            return o;
        }

    }
}
