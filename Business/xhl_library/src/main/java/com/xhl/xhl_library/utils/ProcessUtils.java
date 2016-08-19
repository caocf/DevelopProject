package com.xhl.xhl_library.utils;

import android.app.ActivityManager;
import android.content.Context;

import org.xutils.x;

import java.util.List;

/**
 * Created by Sum on 15/11/22.
 */
public class ProcessUtils {

    /**
     * 检测服务是否在运行
     * @param clazz
     * @return
     */
    public static boolean isServiceRunning(Class<?> clazz) {
        boolean isRunning = false;
        Context context = x.app();

        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (serviceList == null || serviceList.size() == 0) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(clazz.getName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static boolean isProcessRunning(String packName) {
        boolean isRunning = false;
        Context context = x.app();

        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> serviceList
                = activityManager.getRunningAppProcesses();

        if (serviceList == null || serviceList.size() == 0) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).processName.equals(packName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
