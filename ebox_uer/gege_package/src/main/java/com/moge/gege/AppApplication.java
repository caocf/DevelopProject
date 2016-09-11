package com.moge.gege;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.model.UserModel;
import com.moge.gege.model.enums.LoginFromType;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.ui.BaseActivity;
import com.moge.gege.ui.event.Event;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.util.DeviceInfoUtil;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.ImageLoaderUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.ViewUtil;

import de.greenrobot.event.EventBus;

public class AppApplication extends Application
{
    private static AppApplication application;
    public static Context mGlobalContext;
    private static boolean mLoggedState = false;
    private static long mSynShoppingCartTime = 0;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mGlobalContext = this.getBaseContext();
        application = this;

        ViewUtil.init(this);
        FunctionUtils.init(this);
        ToastUtil.init(this);
        RequestManager.init(this);
        ImageLoaderUtil.instance().init(this);
    }

    public static AppApplication instance()
    {
        return application;
    }

    public Context getGlobalContext()
    {
        return mGlobalContext;
    }

    public void exit()
    {
        sendBroadcast(new Intent(GlobalConfig.BROADCAST_ACTION_FINISH));
    }

    public static void setLoginState(boolean state)
    {
        mLoggedState = state;
    }

    public static boolean isLogin()
    {
        return mLoggedState;
    }

    public static boolean checkLoginState(Activity context)
    {
        return checkLoginState(context, LoginFromType.FROM_GENERAL);
    }

    public static boolean checkLoginState(Activity context, int from)
    {
        if (isLogin())
        {
            return true;
        }

        String cookie = PersistentData.instance().getCookie();
        if (TextUtils.isEmpty(cookie))
        {
            UIHelper.showLoginActivity(context, from);
        }
        else
        {
            // open limit when network error
            if (!DeviceInfoUtil.isHaveInternet(context))
            {
                return true;
            }

            if (context instanceof BaseActivity)
            {
                ((BaseActivity) context).doSignin(context, from);
            }
        }
        return false;
    }

    public static void login(UserModel model)
    {

//        // start service(connect websocket)
//        if (AppService.instance() == null)
//        {
//            AppApplication.instance().startService(
//                    new Intent(AppApplication.instance(), AppService.class));
//        }
//        else
//        {
//            // reconnect websocket
//            AppService.instance().reconnectSocket();
//        }

        PersistentData.instance().setUserInfo(model);
        setLoginState(true);
        EventBus.getDefault().post(new Event.LoginEvent());
    }

    public static void logout(Context context)
    {
//        if (AppService.instance() != null)
//        {
//            AppService.instance().disconnectSocket();
//        }

        setLoginState(false);
        PersistentData.instance().setCookie("");
        PersistentData.instance().setUserInfo(null);
        UIHelper.logout(context);
    }

    // todo list!!!
    public static String getLoginId()
    {
        if (DeviceInfoUtil.isHaveInternet(AppApplication.instance()))
        {
            if (isLogin())
            {
                return PersistentData.instance().getUserInfo().get_id();
            }
            else
            {
                return "";
            }
        }
        else
        {
            return PersistentData.instance().getUserInfo().get_id();
        }
    }

    public static boolean isCookieEmpty()
    {
        return TextUtils.isEmpty(PersistentData.instance().getCookie());
    }

    public static boolean shouldToSynShoppingCart()
    {
        long intervalTime = System.currentTimeMillis() - mSynShoppingCartTime;
        return intervalTime >= 10 * 60 * 1000;
    }

    public static void setSynShoppingCartTime(long time)
    {
        mSynShoppingCartTime = time;
    }

}
