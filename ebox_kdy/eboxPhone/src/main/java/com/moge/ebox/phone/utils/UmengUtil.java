package com.moge.ebox.phone.utils;

import java.util.HashMap;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

public class UmengUtil
{

    public static void initConfig(Context context)
    {
        MobclickAgent.setDebugMode(false);
        MobclickAgent.setSessionContinueMillis(60000);
        MobclickAgent.onKillProcess(context);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(context);
    }

    public static void statPageStart(String pageName)
    {
        MobclickAgent.onPageStart(pageName);
    }

    public static void statPageEnd(String pageName)
    {
        MobclickAgent.onPageEnd(pageName);
    }

    public static void statDurationStart(Context context)
    {
        MobclickAgent.onResume(context);
    }

    public static void statDurationEnd(Context context)
    {
        MobclickAgent.onPause(context);
    }

    public static void statRegisterEvent(Context context, String username)
    {
        HashMap<String, String> registerEvent = new HashMap<String, String>();
        registerEvent.put("user", username);
        MobclickAgent.onEvent(context, "event_register", registerEvent);
    }

}
