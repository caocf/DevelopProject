package com.xhl.bqlh.business.Db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xhl.bqlh.business.AppDelegate;
import com.xhl.bqlh.business.Model.SignConfigModel;

import java.util.Date;

/**
 * Created by Sum on 15/12/29.
 */
public class PreferenceData {

    private static final String mAppPrefix = "xhl_";
    private static final String NAME = "default_config";
    private static final String TAG_download_url = "tag_download_url";
    private static final String TAG_Login_phone = "tag_login_phone";


    private static final String TAG_sign_config = "TAG_sign_config";
    private static final String TAG_sign_state_start = "TAG_sign_state_start";
    private static final String TAG_sign_state_end = "TAG_sign_state_end";
    private static final String TAG_sign_time_start = "TAG_sign_time_start";

    //蓝牙信息保存
    private static final String TAG_bluetooth_name = "TAG_bluetooth_name";
    private static final String TAG_bluetooth_address = "TAG_bluetooth_address";

    private static PreferenceData preference;
    private SharedPreferences mPreference;
    private AppDelegate mContext = AppDelegate.appContext;

    private PreferenceData() {
        mPreference = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceData getInstance() {

        if (preference == null) {
            synchronized (PreferenceData.class) {
                preference = new PreferenceData();
            }
        }
        return preference;
    }

    public void bluetoothAddressSave(String address) {
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putString(tag(TAG_bluetooth_address), address);
        edit.commit();
    }

    public String bluetoothAddressGet() {
        return mPreference.getString(tag(TAG_bluetooth_address), "");
    }

    public void bluetoothNameSave(String name) {
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putString(tag(TAG_bluetooth_name), name);
        edit.commit();
    }

    public String bluetoothNameGet() {
        return mPreference.getString(tag(TAG_bluetooth_name), "");
    }

    /**
     * 服务端配置
     */
    public void signConfig(SignConfigModel sign) {
        if (sign != null) {
            SharedPreferences.Editor edit = mPreference.edit();
            edit.putString(tag(TAG_sign_config), new Gson().toJson(sign));
            edit.commit();
        }
    }

    public SignConfigModel signConfig() {
        String string = mPreference.getString(tag(TAG_sign_config), "");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return new Gson().fromJson(string, SignConfigModel.class);
    }


    /**
     * 当天是否已经签到
     */
    public boolean todaySignStart() {
        return mPreference.getBoolean(tag(TAG_sign_state_start), false);
    }

    public void todaySignStart(boolean sign) {
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putBoolean(tag(TAG_sign_state_start), sign);
        edit.commit();
    }

    /**
     * 当天是否已经签退
     */
    public boolean todaySignEnd() {
        return mPreference.getBoolean(tag(TAG_sign_state_end), false);
    }

    public void todaySignEnd(boolean sign) {
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putBoolean(tag(TAG_sign_state_end), sign);
        edit.commit();
    }

    /**
     * 当天的签到最后一次时间
     */
    public void signLastTime(Date date) {
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putLong(tag(TAG_sign_time_start), date.getTime());
        edit.commit();
    }

    public long signLastTime() {
        return mPreference.getLong(tag(TAG_sign_time_start), 0);
    }


    public void setDownloadUrl(String id) {
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putString(tag(TAG_download_url), id);
        edit.commit();
    }

    public String getDownloadUrl() {
        return mPreference.getString(tag(TAG_download_url), "");
    }

    /**
     * 记录用户
     */
    public void setLoginPhone(String phone) {
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putString(tag(TAG_Login_phone), phone);
        edit.commit();
    }

    public String getLoginPhone() {
        return mPreference.getString(tag(TAG_Login_phone), "");
    }

    //检测版本
    public boolean versionNeedUpdate(String newVersionName) {
        boolean isNeedUpdate = false;
        if (TextUtils.isEmpty(newVersionName)) {
            return false;
        }
        //读取当前版本数据
        PackageInfo packageInfo = AppDelegate.appContext.getPackageInfo();
        String curVersion = packageInfo.versionName;

        //新版本大于当前版本
        if (newVersionName.length() > curVersion.length() || newVersionName.compareTo(curVersion) > 0) {
            isNeedUpdate = true;
        }
        return isNeedUpdate;
    }


    private static String tag(String tag) {
        return mAppPrefix + tag;
    }

}
