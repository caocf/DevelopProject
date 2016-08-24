package com.xhl.world.ui.utils.umeng.login;

import java.util.Map;

/**
 * Created by Sum on 15/12/10.
 */
public interface LoginCallBack {

    void onGetInfoSuccess(Map<String, String> info);

    void onGetInfoFailed(int status);

    void onGetInfoCancel(int status);

}
