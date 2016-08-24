package com.xhl.world;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.utils.Log;
import com.xhl.sum.chatlibrary.controller.ChatManager;
import com.xhl.sum.chatlibrary.controller.ConversationEventHandler;
import com.xhl.sum.chatlibrary.model.LeanchatUser;
import com.xhl.world.chat.event.ChatLoginFinish;
import com.xhl.world.chat.service.PushManager;
import com.xhl.world.config.Constant;
import com.xhl.world.config.NetWorkConfig;
import com.xhl.world.data.PreferenceData;
import com.xhl.world.model.user.UserInfo;
import com.xhl.world.ui.activity.BaseUserOpActivity;
import com.xhl.world.ui.utils.ToastUtil;
import com.xhl.world.ui.utils.umeng.UmConfig;
import com.xhl.xhl_library.Base.BaseApplication;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.Properties;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 15/11/23.
 */
public class AppApplication extends BaseApplication {
    public static AppApplication appContext;
    public String mCookie;
    private boolean mIsLogin = false;
    private UserInfo mUser = null;
    //默认app是退出状态，service自启的时候会创建APPApplication对象
    public boolean mAppExit = true;

    @Override
    public void onCreate() {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize().init(this);

        appContext = this;
        mCookie = getCookie();
        ToastUtil.init(this);

        //测试账号
        String appId = "6Jl5vvzSK3Cs8dcTM2JfcB9p-gzGzoHsz";
        String appKey = "5Ta5xiSTQWtFRPKjeokO8q3T";
        //初始化
        AVOSCloud.initialize(this, appId, appKey);

        //应用异常统计
        AVAnalytics.enableCrashReport(this, !Constant.isDebug);

        //消息聊天初始化
//        AVOSInit();
        //分享登陆数据初始化
        initShareData();
    }

    private void AVOSInit() {

        AVOSCloud.setLastModifyEnabled(true);
        AVOSCloud.setDebugLogEnabled(Constant.isDebug);

        //推送初始化
        PushManager.getInstance().init(appContext);

        //聊天初始化
        initChatManager();

        //保存设备号
        AVInstallation installation = AVInstallation.getCurrentInstallation();
        PreferenceData.getInstance().setInstallationId(installation.getInstallationId());
    }

    private void initShareData() {
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone(UmConfig.QQAppID, UmConfig.QQAppKey);

        //weixin
        PlatformConfig.setWeixin(UmConfig.WXAppID, UmConfig.WXAppSecret);

        //关闭日志
        Log.LOG = true;
    }

    private void initChatManager() {
        ChatManager chatManager = ChatManager.getInstance();
        chatManager.init(this);
        chatManager.setConversationEventHandler(ConversationEventHandler.getInstance());
        ChatManager.setDebugEnabled(Constant.isDebug);
        LeanchatUser.alwaysUseSubUserClass(LeanchatUser.class);
    }

    public void chatDefaultLogin() {
        if (isLogin()) {
            String userId = mUser.id;
            ChatLoginFinish loginFinish = new ChatLoginFinish();
            loginFinish.chatUserId = userId;

            EventBus.getDefault().post(loginFinish);
        }
    }

    public boolean isLogin(Context context) {
        if (!isLogin()) {
            context.startActivity(new Intent(context, BaseUserOpActivity.class));
            return false;
        }
        return true;
    }

    /**
     * 用户是否登录
     *
     * @return
     */
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

    public UserInfo getLoginUserInfo() {
        if (mUser != null)
            return mUser;
        else {
            mUser = new UserInfo();
            mUser.get4App(appContext);
            return mUser;
        }
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

    public void modifyLoginInfo(final UserInfo user) {
        this.mUser = user;
        addAllProperties(new Properties() {
            {
                user.save2App(this);
            }
        });
    }

    /**
     * 注销时清除用户信息
     */
    private void clearUserInfo() {
        if (mUser != null) {
            mUser.clear2App(appContext);
            mUser = null;
        } else {
            mUser = new UserInfo();
            mUser.clear2App(appContext);
            mUser = null;
        }
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

        clearUserInfo();
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

    public String getUserFaceImage() {
        String url;
        if (mUser == null) {
            mUser = new UserInfo();
            mUser.get4App(appContext);
        }
        if (TextUtils.isEmpty(mUser.vipPic)) {
            return null;
        }
        //第三方注册的时候直接存放了用户头像的URL
        if (URLUtil.isNetworkUrl(mUser.vipPic)) {
            return mUser.vipPic;
        }
        url = NetWorkConfig.imageHost + mUser.vipPic;
        return url;
    }

}
