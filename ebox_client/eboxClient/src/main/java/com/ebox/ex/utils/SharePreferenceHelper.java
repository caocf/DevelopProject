package com.ebox.ex.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.AppApplication;
import com.ebox.ex.network.model.base.type.AdminUserType;
import com.ebox.pub.utils.MD5Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Android on 2015/10/9.
 */
public class SharePreferenceHelper {


    private static final String SHAREPREFERENCE = "ebox_state";

    private static final String EBOX_RACK = "ebox_rack_num";

    /**
     * 设置超级管理员账号
     *
     * @param context
     * @param user
     */
    public static void setSupperAdmin(Context context, List<AdminUserType> user) {
        SharedPreferences preferences = context.getSharedPreferences(
                SHAREPREFERENCE, Context.MODE_PRIVATE);
        if (preferences != null && user != null) {
            SharedPreferences.Editor edit = preferences.edit();
            JSONObject jo = new JSONObject();
            String name = "";
            try {

                String json = JsonSerializeUtil.list2Json(user);

                name = jo.put("userList", json).toString();

                edit.putString("AdminUserType", name);
                edit.commit();
                //Log.i(GlobalField.tag,"SupperAdminName:"+ name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static AdminUserType[] getSupperAdmin() {
        Context applicationContext = AppApplication.getInstance().getApplicationContext();
        return getSupperAdmin(applicationContext);
    }


    private static AdminUserType[] getSupperAdmin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                SHAREPREFERENCE, Context.MODE_PRIVATE);

        String admin = preferences.getString("AdminUserType", "");
        AdminUserType[] user = null;
        if (admin != null && !admin.equals("")) {
            JSONObject object;
            try {
                object = new JSONObject(admin);
                JSONArray userjo = object.optJSONArray("userList");
                if (userjo != null) {
                    user = new AdminUserType[userjo.length()];
                    for (int i = 0; i < userjo.length(); i++) {
                        user[i] = JsonSerializeUtil.json2Bean(userjo.get(i).toString(), AdminUserType.class);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (user == null || user.length == 0) {
            user = new AdminUserType[1];
            user[0] = new AdminUserType();
            user[0].setAdminName("02520140214");
            user[0].setAdminPassword(MD5Util.getMD5String("198506"));
        }

        return user;
    }

    public static void saveRackNum(int rack, int rackNum) {
        Context context = AppApplication.getInstance().getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(EBOX_RACK, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt("rack" + rack, rackNum);
        edit.commit();
    }

    public static int getRackNum(int rack) {
        Context context = AppApplication.getInstance().getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(EBOX_RACK, Context.MODE_PRIVATE);
        int anInt = preferences.getInt("rack" + rack, 4);
        return anInt;
    }


    /**
     * 保存同步服务端全部订单状态
     *
     * @param isSync true:已经同步 false:未同步
     */
    public static void saveSyncOrderState(boolean isSync) {
        Context context = AppApplication.globalContext;
        SharedPreferences preferences = context.getSharedPreferences(SHAREPREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("isSyncOrder", isSync);
        edit.apply();
    }

    /**
     * 获取服务端订单同步状态
     *
     * @return true:已经同步 false:未同步
     */
    public static boolean getSyncOrderState() {
        Context context = AppApplication.globalContext;
        SharedPreferences preferences = context.getSharedPreferences(SHAREPREFERENCE, Context.MODE_PRIVATE);

        return preferences.getBoolean("isSyncOrder", false);
    }


    /**
     * 保存全部箱门一次性上报的同步状态
     *
     * @param doorState true:完成同步 false:未完成同步
     */
    public static void saveSyncBoxDoorState(boolean doorState) {
        Context context = AppApplication.globalContext;
        SharedPreferences preferences = context.getSharedPreferences(SHAREPREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("syncBoxDoor", doorState);
        edit.apply();
    }

    /**
     * 返回全部箱门是否完成需要一次性上报
     *
     * @return true:完成一次性上报 false：未完成一次性上报
     */
    public static boolean getSyncBoxDoorState() {
        Context context = AppApplication.globalContext;
        SharedPreferences preferences = context.getSharedPreferences(SHAREPREFERENCE, Context.MODE_PRIVATE);
        return preferences.getBoolean("syncBoxDoor", true);
    }


    /**
     * 保存检验配置文件状态
     * @param checked
     */
    public static void saveCheckConifgState(boolean checked) {
        Context context = AppApplication.globalContext;
        SharedPreferences preferences = context.getSharedPreferences(SHAREPREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("checkConfig", checked);
        edit.apply();
    }

    /**
     * 获取是否校验配置文件状态
     * @return
     */
    public static boolean getCheckConfigState()
    {
        Context context = AppApplication.globalContext;
        SharedPreferences preferences = context.getSharedPreferences(SHAREPREFERENCE, Context.MODE_PRIVATE);

        isChangeTerminal_code();

        return preferences.getBoolean("checkConfig", false);
    }

    public static boolean isChangeTerminal_code(){
        Context context = AppApplication.globalContext;
        SharedPreferences preferences = context.getSharedPreferences(SHAREPREFERENCE, Context.MODE_PRIVATE);

        String terminal_code = preferences.getString("terminal_code", "0000");

        String terminal_code_config = AppApplication.getInstance().getTerminal_code();

        if (terminal_code_config.equals(terminal_code))
        {
            Log.i("terminal","当前终端:" + terminal_code_config);
            return false;
        }
        else
        {
            //终端修改设置配置文件为未校验状态
            saveCheckConifgState(false);
            saveInitBoxState(false);
            preferences.edit().putString("terminal_code",terminal_code_config).apply();
            Log.i("terminal","终端更新:" +terminal_code_config);
            return true;
        }
    }

    /**
     * 保存服务端Box初始化状态
     * @param checked
     */
    public static void saveInitBoxState(boolean checked) {
        Context context = AppApplication.globalContext;
        SharedPreferences preferences = context.getSharedPreferences(SHAREPREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("initBox", checked);
        edit.apply();
    }

    /**
     * 获取是否初始化箱门同步状态
     * @return
     */
    public static boolean getInitBoxState(){
        Context context = AppApplication.globalContext;
        SharedPreferences preferences = context.getSharedPreferences(SHAREPREFERENCE, Context.MODE_PRIVATE);
        return preferences.getBoolean("initBox", false);
    }

    public static boolean isReInstall(){
        Context context = AppApplication.globalContext;
        SharedPreferences preferences = context.getSharedPreferences(SHAREPREFERENCE, Context.MODE_PRIVATE);
        return preferences.getBoolean("install", false);
    }

    public static void  saveReInstall(boolean install) {
        Context context = AppApplication.globalContext;
        SharedPreferences preferences = context.getSharedPreferences(SHAREPREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("install", install);
        edit.apply();
    }

    /**
     * 判断是否版本升级
     *
     * @return true:升级版本 false:未升级版本
     */
    public static boolean isVersionUpdate() {

        boolean isUpdateVersion = false;
        Context context = AppApplication.globalContext;
        SharedPreferences preferences = context.getSharedPreferences(SHAREPREFERENCE, Context.MODE_PRIVATE);
        String version_name_new = null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version_name_new = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String version_old = preferences.getString("version_name", "V1.0.0");

        if (version_old.equals("V1.0.0"))//重新安装
        {
            //new OsInitHelper(null).checkLocalConfig().updateBoxState();
            saveReInstall(true);
            Log.d("terminal","重新安装");
        }
        if (version_name_new != null && version_old.compareTo(version_name_new) < 0)
        {
            isUpdateVersion = true;
        }
        preferences.edit().putString("version_name", version_name_new).apply();

        return isUpdateVersion;
    }

    public static String curVersion(){
        Context context = AppApplication.globalContext;
        SharedPreferences preferences = context.getSharedPreferences(SHAREPREFERENCE, Context.MODE_PRIVATE);
        return preferences.getString("version_name","V1.0.0");
    }

}

