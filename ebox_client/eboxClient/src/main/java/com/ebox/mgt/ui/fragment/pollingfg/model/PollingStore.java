package com.ebox.mgt.ui.fragment.pollingfg.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.ebox.AppApplication;

/**
 * Created by admin on 2015/10/11.
 * 将巡检信息存入到sp中
 */
public class PollingStore {
    public static final String POLL_SP_NAME = "poll_sp_name";
    private static SharedPreferences pollSP;
    public static final String POLL_NULL = "";

    public static void getSP() {

        Context context= AppApplication.globalContext;
        pollSP = context.getSharedPreferences(POLL_SP_NAME, Context.MODE_PRIVATE);
    }

    public static void storePoll(String key, String value) {
        SharedPreferences.Editor editor = pollSP.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void clear() {
        SharedPreferences.Editor editor = pollSP.edit();
        editor.putString("super_name","");
        editor.putString("pressureState","");
        editor.putString("elecdegree","");
        editor.putString("discVcrState","");
        editor.putString("monitorCamState","");
        editor.putString("netTest","");
        editor.putString("netRouteModuleState","");
        editor.putString("netLineState","");
        editor.putString("netRouteState","");
        editor.putString("startLockNum","");
        editor.putString("lockLockNum","");
        editor.putString("changeLock","");
        editor.putString("audioTest","");
        editor.putString("scanTest","");
        editor.putString("cameraTest","");
        editor.putString("overtimeNum","");
        editor.putString("isClear","");
        editor.putString("isTestScreen","");
        editor.putString("isTestTouch","");
        editor.commit();

    }

    /**
     * 获得指定key的数据
     * @param key
     * @return
     */
    public static String getStore(String key){
        return pollSP.getString(key, POLL_NULL);
    }

    /**
     * 判断是否完成巡检
     *
     * @return
     */
    public static boolean isOverPoll() {
        boolean isOver = true;

        if (getStore("super_name").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("pressureState").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("elecdegree").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("discVcrState").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("monitorCamState").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("netTest").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("netRouteModuleState").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("netLineState").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("netRouteState").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("startLockNum").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("lockLockNum").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("changeLock").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("audioTest").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("scanTest").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("cameraTest").equals(POLL_NULL)) {
            isOver = false;
        }

        if (getStore("overtimeNum").equals(POLL_NULL)) {
            isOver = false;
        }
        if (getStore("isClear").equals(POLL_NULL)) {
            isOver = false;
        }
        if (getStore("isTestScreen").equals(POLL_NULL)) {
            isOver = false;
        }
        if (getStore("isTestTouch").equals(POLL_NULL)) {
            isOver = false;
        }

        return isOver;
    }

    /**
     * 获得需要上传数据的巡检信息
     */
    public static ReqPolling getPollBean() {
        ReqPolling reqPolling = new ReqPolling();

        reqPolling.setSuper_name(getStore("super_name"));
        reqPolling.setPressureState(getStore("pressureState"));
        reqPolling.setElecdegree(getStore("elecdegree"));
        reqPolling.setDiscVcrState(getStore("discVcrState"));
        reqPolling.setMonitorCamState(getStore("monitorCamState"));
        reqPolling.setNetTest(getStore("netTest"));
        reqPolling.setNetRouteModuleState(getStore("netRouteModuleState"));
        reqPolling.setNetLineState(getStore("netLineState"));
        reqPolling.setNetRouteState(getStore("netRouteState"));
        reqPolling.setStartLockNum(getStore("startLockNum"));
        reqPolling.setLockLockNum(getStore("lockLockNum"));
        reqPolling.setChangeLock(getStore("changeLock"));
        reqPolling.setAudioTest(getStore("audioTest"));
        reqPolling.setScanTest(getStore("scanTest"));
        reqPolling.setCameraTest(getStore("cameraTest"));
        reqPolling.setOvertimeNum(getStore("overtimeNum"));
        reqPolling.setIsClear(getStore("isClear"));
        reqPolling.setIsTestScreen(getStore("isTestScreen"));
        reqPolling.setIsTestTouch(getStore("isTestTouch"));

        return reqPolling;
    }

}
