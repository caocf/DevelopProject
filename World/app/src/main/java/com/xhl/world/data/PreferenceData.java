package com.xhl.world.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xhl.world.AppApplication;
import com.xhl.world.model.AdvListModel;

/**
 * Created by Sum on 15/12/29.
 */
public class PreferenceData {

    private static final String mAppPrefix = "xhl_";
    private static final String NAME = "default_config";
    private static final String TAG_search = "tag_search";
    private static final String TAG_installation_id = "tag_installationId";
    private static final String TAG_Login_phone = "tag_login_phone";
    private static final String TAG_Version_code = "tag_version_code";
    private static final String TAG_CACHE = "tag_cache";

    private static PreferenceData preference;
    private SharedPreferences mPreference;
    private AppApplication mContext = AppApplication.appContext;

    private PreferenceData() {
        mPreference = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceData getInstance() {

        if (preference == null) {
            preference = new PreferenceData();
        }
        return preference;
    }

    /**
     * 获取版本号
     */
    public String versionCode() {
        return mPreference.getString(tag(TAG_Version_code), "");
    }

    //保存当前版本
    private void setVersionCode(String versionCode) {
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putString(tag(TAG_Version_code), versionCode);
        edit.commit();
    }

    /**
     * 搜索历史
     *
     * @return
     */
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

    public void setInstallationId(String id) {
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putString(tag(TAG_installation_id), id);
        edit.commit();
    }

    public String getInstallationId() {
        return mPreference.getString(tag(TAG_installation_id), "");
    }

    //首页请求参数首次加载缓存数据
    public AdvListModel getHomeRequestCache() {
        String s = mPreference.getString(tag(TAG_CACHE), "");
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        return new Gson().fromJson(s, AdvListModel.class);
    }

    //保存请求返回的数据
    public void saveHomeRequestRespone(AdvListModel adv) {
        if (adv == null) {
            return;
        }
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putString(tag(TAG_CACHE), new Gson().toJson(adv));
        edit.commit();
    }

    /**
     * 记录登陆手机号
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
        //读取当前版本数据
        String curVersion = versionCode();
        if (TextUtils.isEmpty(curVersion)) {
            PackageInfo packageInfo = AppApplication.appContext.getPackageInfo();
            String name = packageInfo.versionName;
            curVersion = name;
        }
        //新版本大于当前版本
        if (newVersionName.compareTo(curVersion) > 0) {
            isNeedUpdate = true;
            curVersion = newVersionName;
        }
        setVersionCode(curVersion);
        return isNeedUpdate;
    }


    private static String tag(String tag) {
        return mAppPrefix + tag;
    }

}
