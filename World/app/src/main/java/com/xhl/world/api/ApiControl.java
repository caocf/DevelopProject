package com.xhl.world.api;

/**
 * Created by Sum on 16/1/7.
 */
public final class ApiControl {

    private static ApiImpl mBaseApi;

    public static Api getApi() {
        if (mBaseApi == null) {
            mBaseApi = ApiImpl.instance();
        }
        return mBaseApi;
    }
}
