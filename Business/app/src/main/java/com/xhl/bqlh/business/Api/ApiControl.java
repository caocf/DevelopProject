package com.xhl.bqlh.business.Api;

/**
 * Created by Sum on 16/4/8.
 */
public final class ApiControl {

    private static Api mApi;

    public static Api getApi() {
        if (mApi == null) {
            synchronized (ApiControl.class) {
                mApi = new ApiImpl();
            }
        }
        return mApi;
    }

}
