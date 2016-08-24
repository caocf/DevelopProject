package com.xhl.xhl_library.Base;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.xhl.xhl_library.Base.User.AppConfig;
import com.xhl.xhl_library.BaseConfig;
import com.xhl.xhl_library.utils.AppManager;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.x;

import java.util.Properties;

/**
 * Created by Sum on 15/11/21.
 */
public class BaseApplication extends Application {

    public static int ScreenWidth, ScreenHeight;

    @Override
    public void onCreate() {
        super.onCreate();

        DisplayMetrics dm = getResources().getDisplayMetrics();
        ScreenWidth = dm.widthPixels;
        ScreenHeight = dm.heightPixels;

//        if (BaseConfig.isCatchCrash) {
//            Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
//        }

        x.Ext.init(this);
        x.Ext.setDebug(BaseConfig.isDebug);

        Logger.i("ScreenHeight:" + ScreenHeight + " ScreenWidth:" + ScreenWidth + " densityDpi:" + dm.densityDpi);
    }

    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) info = new PackageInfo();
        return info;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == 0) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet"))
                    netType = 3;
                else
                    netType = 2;
            }
        } else if (nType == 1) {
            netType = 1;
        }
        return netType;
    }

    public void exitApp() {
        AppManager.getAppManager().AppExit(this);
    }

    public String getCookie() {
        return AppConfig.getAppConfig(this).getCookie();
    }

    public void setCookie(String cookie) {
        AppConfig.getAppConfig(this).setCookie(cookie);
    }

    public void addAllProperties(Properties ps) {
        AppConfig.getAppConfig(this).addAllPs(ps);
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key) {
        return AppConfig.getAppConfig(this).get(key);
    }

    public void removeProperty(String[] key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    public void clearAppCache() {
        deleteDatabase("webview.db");
        deleteDatabase("webview.db-shm");
        deleteDatabase("webview.db-wal");
        deleteDatabase("webviewCache.db");
        deleteDatabase("webviewCache.db-shm");
        deleteDatabase("webviewCache.db-wal");
        //清理缓存SD卡文件
        x.image().clearCacheFiles();
    }

}
