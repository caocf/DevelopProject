package com.xhl.bqlh.business;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.baidu.mapapi.SDKInitializer;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.xhl.bqlh.business.AppConfig.Constant;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.AppConfig.NetWorkConfig;
import com.xhl.bqlh.business.Model.UserInfo;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.helper.LocationService;
import com.xhl.xhl_library.Base.BaseApplication;

import java.util.Properties;

/**
 * Created by Sum on 16/3/17.
 */
public class AppDelegate extends BaseApplication {

    public LocationService mLocation;
    public RefWatcher watcher;
    public static AppDelegate appContext;
    private boolean mIsLogin = false;
    private UserInfo mUser = null;
    public String mCookie;

    public static RefWatcher getRefWatcher(Context context) {

        AppDelegate delegate = (AppDelegate) context.getApplicationContext();

        return delegate.watcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;
        //百度SDK初始化
        SDKInitializer.initialize(this);

        //Leak
        watcher = LeakCanary.install(this);

        mLocation = LocationService.initLocation(this);

        ToastUtil.init(this);

        mCookie = getCookie();

        //初始化
        AVOSCloud.initialize(this, GlobalParams.LeanCloud_id, GlobalParams.LeanCloud_key);
        //统计开关
        AVAnalytics.setAnalyticsEnabled(Constant.openAnalytics);
        //应用异常统计
        AVAnalytics.enableCrashReport(this, Constant.openCrash);
    }

    public boolean isLogin() {
        if (mIsLogin && mUser != null && !TextUtils.isEmpty(mCookie)) {
            return true;
        }
        boolean login = false;
        try {
            String loginStr = getProperty("user.login");
            if (TextUtils.isEmpty(loginStr)) {
                login = false;
            } else {
                login = (loginStr.equals("1"));
            }
            if (login && mUser == null) {
                UserInfo user = new UserInfo();
                user.get4App(appContext);
                this.mUser = user;
                this.mIsLogin = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return login;
    }

    public void saveLoginInfo(final UserInfo user) {
        this.mIsLogin = true;
        this.mUser = user;
        addAllProperties(new Properties() {
            {
                setProperty("user.login", "1");
                mUser.save2App(this);
            }
        });
    }

    /**
     * 退出登录
     */
    public void setUserLogout() {
        this.mIsLogin = false;
        addAllProperties(new Properties() {
            {
                setProperty("user.login", "0");
            }
        });
        if (mUser != null) {
            mUser.clear2App(appContext);
            mUser = null;
        } else {
            mUser = new UserInfo();
            mUser.clear2App(appContext);
            mUser = null;
        }
    }

    public String getUserName() {
        if (mUser != null)
            return mUser.userName;
        else {
            mUser = new UserInfo();
            mUser.get4App(appContext);
            return mUser.userName;
        }
    }

    public String getUserEmail() {
        if (mUser != null)
            return mUser.email;
        else {
            mUser = new UserInfo();
            mUser.get4App(appContext);
            return mUser.email;
        }
    }

    public String getUserFaceImage() {
        String url;
        if (mUser == null) {
            mUser = new UserInfo();
            mUser.get4App(appContext);
        }
        if (TextUtils.isEmpty(mUser.headImage)) {
            return null;
        }
        //第三方注册的时候直接存放了用户头像的URL
        if (URLUtil.isNetworkUrl(mUser.headImage)) {
            return mUser.headImage;
        }
        url = NetWorkConfig.imageHost + mUser.headImage;
        return url;
    }

    public UserInfo getUserInfo() {
        if (mUser == null) {
            mUser = new UserInfo();
            mUser.get4App(appContext);
        }
        return mUser;
    }

}
