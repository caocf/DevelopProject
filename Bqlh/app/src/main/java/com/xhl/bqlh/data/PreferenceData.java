package com.xhl.bqlh.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xhl.bqlh.AppDelegate;

/**
 * Created by Sum on 15/12/29.
 */
public class PreferenceData {

    private static final String mAppPrefix = "xhl_";
    private static final String NAME = "default_config";
    private static final String TAG_download_url = "tag_download_url";
    private static final String TAG_Login_phone = "tag_login_phone";
    private static final String TAG_search = "tag_search";

    private static final String TAG_area_id = "tag_area_id";
    private static final String TAG_area_name = "tag_area_name";

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

    public void setDownloadUrl(String id) {
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putString(tag(TAG_download_url), id);
        edit.commit();
    }

    public String getDownloadUrl() {
        return mPreference.getString(tag(TAG_download_url), "");
    }

    public SearchData getSearchData() {
        String s = mPreference.getString(tag(TAG_search), "");

        SearchData json = new Gson().fromJson(s, SearchData.class);
        return json;
    }

    public void setSearchData(SearchData search) {
        if (search == null) {
            return;
        }
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putString(tag(TAG_search), new Gson().toJson(search));
        edit.commit();
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

    public void areaId(String areaId) {
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putString(tag(TAG_area_id), areaId);
        edit.commit();
    }

    public String areaId() {
        return mPreference.getString(tag(TAG_area_id), "");
    }

    public void areaName(String areaName) {
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putString(tag(TAG_area_name), areaName);
        edit.commit();
    }

    public String areaName() {
        return mPreference.getString(tag(TAG_area_name), "");
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
