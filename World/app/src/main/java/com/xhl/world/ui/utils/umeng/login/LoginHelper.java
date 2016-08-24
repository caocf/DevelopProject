package com.xhl.world.ui.utils.umeng.login;

import android.app.Activity;
import android.content.Intent;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by Sum on 15/12/9.
 */
public class LoginHelper implements UMAuthListener {

    private Activity context;
    private LoginCallBack loginCallBack;
    public static final String TAG = "LoginHelper";
    private UMShareAPI mShareAPI;

    public LoginHelper(Activity context) {
        this.context = context;
        mShareAPI = UMShareAPI.get(context);
    }

    public void loginByQQ() {
        mShareAPI.doOauthVerify(context, SHARE_MEDIA.QQ, this);

    }

    public void loginByWeiXin() {
        mShareAPI.doOauthVerify(context, SHARE_MEDIA.WEIXIN, this);
    }


    public void setLoginCallBack(LoginCallBack loginCallBack) {
        this.loginCallBack = loginCallBack;
    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//        Logger.v(TAG + " status:" + i + " data: " + share_media + " " + map);

        final String openId = map.get("openid");

        mShareAPI.getPlatformInfo(context, share_media, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if (loginCallBack != null) {
                    map.put("openid", openId);
                    loginCallBack.onGetInfoSuccess(map);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if (loginCallBack != null) {
                    loginCallBack.onGetInfoFailed(i);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                if (loginCallBack != null) {
                    loginCallBack.onGetInfoCancel(i);
                }
            }
        });
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//        Logger.v(TAG + " onError:" + i + " data: " + throwable.getMessage());
        if (loginCallBack != null) {
            loginCallBack.onGetInfoFailed(i);
        }
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        if (loginCallBack != null) {
            loginCallBack.onGetInfoCancel(i);
        }
    }

    public void onActivityResult(int req, int res, Intent data) {
        mShareAPI.onActivityResult(req, res, data);
    }
}
