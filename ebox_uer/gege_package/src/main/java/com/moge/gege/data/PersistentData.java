package com.moge.gege.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.moge.gege.AppApplication;
import com.moge.gege.config.Constants;
import com.moge.gege.config.Constants.Config;
import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.AddressModel;
import com.moge.gege.model.BoardModel;
import com.moge.gege.model.UserModel;
import com.moge.gege.model.enums.PayType;
import com.moge.gege.util.LogUtil;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PersistentData
{
    private static final String mAppPrefix = "gege_";
    private static PersistentData mPersistentData = null;
    private SharedPreferences mSharedPreferences;
    private AppApplication mApplication = AppApplication.instance();
    private UserModel mUserInfoModel;
    private String mCommunityName;
    private BoardModel mCommunityInfo;
    private String mGeneralAddress;
    private String mImageAddress;
    private String mChatWebsocketAddress;
    private String mChatAddress;
    private String mPayAddress;
    private String mVersionName;

    private static Gson mGson = new Gson();

    private PersistentData()
    {
        mSharedPreferences = mApplication.getSharedPreferences(
                "persistentData", Context.MODE_PRIVATE);
    }

    public static PersistentData instance()
    {
        if (mPersistentData == null)
        {
            mPersistentData = new PersistentData();
        }
        return mPersistentData;
    }

    public String getCityName()
    {
        return mSharedPreferences.getString(mAppPrefix + "cityName", "南京");
    }

    public void setCityName(String cityName)
    {
        mSharedPreferences.edit().putString(mAppPrefix + "cityName", cityName)
                .commit();
    }

    public boolean isFirstLogin()
    {
        return mSharedPreferences.getBoolean(mAppPrefix + "isFirstLogin", true);
    }

    public void setFirstLoginFalse()
    {
        mSharedPreferences.edit()
                .putBoolean(mAppPrefix + "isFirstLogin", false).commit();
    }

    public void setCookie(String cookie)
    {
        if (Constants.config == Config.DEV)
        {
            mSharedPreferences.edit()
                    .putString(mAppPrefix + "cookie_dev", cookie).commit();
        }
        else
        {
            mSharedPreferences.edit().putString(mAppPrefix + "cookie", cookie)
                    .commit();
        }
    }

    public String getCookie()
    {
        if (Constants.config == Config.DEV)
        {
            return mSharedPreferences
                    .getString(mAppPrefix + "cookie_dev", null);
        }
        else
        {
            return mSharedPreferences.getString(mAppPrefix + "cookie", null);
        }
    }

    public String getCommunityId()
    {
        return getUserInfo().getCommunity_id();
    }

    public String getBid()
    {
        return getUserInfo().getBid();
    }

    public void setUserInfo(UserModel model)
    {
        mUserInfoModel = model;

        SharedPreferences.Editor editor = mSharedPreferences.edit();

        if (model != null)
        {
            String userInfoJson = mGson.toJson(model);
            editor.putString(mAppPrefix + "user_info", userInfoJson);
        }
        else
        {
            editor.putString(mAppPrefix + "user_info", "");
        }

        editor.commit();
    }

    public UserModel getUserInfo()
    {
        if (mUserInfoModel == null)
        {
            String jsonData = mSharedPreferences.getString(mAppPrefix
                    + "user_info", "");
            mUserInfoModel = mGson.fromJson(jsonData, UserModel.class);
            if (mUserInfoModel == null)
            {
                mUserInfoModel = new UserModel();
            }
        }
        return mUserInfoModel;
    }

    public void setCommunityInfo(BoardModel communityInfo)
    {
        mCommunityInfo = communityInfo;

        String jsonData = mGson.toJson(mCommunityInfo);
        mSharedPreferences.edit()
                .putString(mAppPrefix + "community_info", jsonData)
                .commit();
    }

    public BoardModel getCommunityInfo()
    {
        if (mCommunityInfo == null)
        {
            String jsonData = mSharedPreferences.getString(mAppPrefix
                    + "community_info", "");
            mCommunityInfo = mGson.fromJson(jsonData, BoardModel.class);
        }

        return mCommunityInfo;
    }

    public String getCommunityName()
    {
        if (TextUtils.isEmpty(mCommunityName))
        {
            mCommunityName = mSharedPreferences.getString(mAppPrefix
                    + "community_name", "");
        }
        return mCommunityName;
    }

    public void setCommunityName(String communityName)
    {
        mCommunityName = communityName;
        mSharedPreferences.edit()
                .putString(mAppPrefix + "community_name", mCommunityName)
                .commit();
    }

    public int getCommunityUser()
    {
       return mSharedPreferences.getInt(mAppPrefix
               + "community_user", 0);
    }

    public void setCommunityUser(int user)
    {
        mSharedPreferences.edit()
                .putInt(mAppPrefix + "community_user", user)
                .commit();
    }

    public void setGeneralApiAddress(String generalAddress)
    {
        mGeneralAddress = generalAddress;
        mSharedPreferences.edit()
                .putString(mAppPrefix + "general_address", generalAddress)
                .commit();
    }

    public String getGeneralApiAddress()
    {
        if (TextUtils.isEmpty(mGeneralAddress))
        {
            mGeneralAddress = mSharedPreferences.getString(mAppPrefix
                    + "general_address", NetworkConfig.generalAddress);
        }

        return mGeneralAddress;
    }

    public void setImageAddress(String imageAddress)
    {
        mImageAddress = imageAddress;
        mSharedPreferences.edit()
                .putString(mAppPrefix + "image_address", imageAddress).commit();
    }

    public String getImageAddress()
    {
        if (TextUtils.isEmpty(mImageAddress))
        {
            mImageAddress = mSharedPreferences.getString(mAppPrefix
                    + "image_address", NetworkConfig.imageAddress);
        }

        return mImageAddress;
    }

    public void setChatWebsocketAddress(String chatWebAddress)
    {
        mChatWebsocketAddress = chatWebAddress;
        mSharedPreferences.edit()
                .putString(mAppPrefix + "chatweb_address", chatWebAddress)
                .commit();
    }

    public String getChatWebSocketAddress()
    {
        if (TextUtils.isEmpty(mChatWebsocketAddress))
        {
            mChatWebsocketAddress = mSharedPreferences.getString(mAppPrefix
                    + "chatweb_address", NetworkConfig.chatWebsocketAddress);
        }

        return mChatWebsocketAddress;
    }

    public void setChatAddress(String chatAddress)
    {
        mChatAddress = chatAddress;
        mSharedPreferences.edit()
                .putString(mAppPrefix + "chat_address", chatAddress).commit();
    }

    public String getChatAddress()
    {
        if (TextUtils.isEmpty(mChatAddress))
        {
            mChatAddress = mSharedPreferences.getString(mAppPrefix
                    + "chat_address", NetworkConfig.chatAddress);
        }

        return mChatAddress;
    }

    public void setPayAddress(String payAddress)
    {
        mPayAddress = payAddress;
        mSharedPreferences.edit()
                .putString(mAppPrefix + "pay_address", payAddress).commit();
    }

    public String getPayAddress()
    {
        if (TextUtils.isEmpty(mPayAddress))
        {
            mPayAddress = mSharedPreferences.getString(mAppPrefix
                    + "pay_address", NetworkConfig.payAddress);
        }

        return mPayAddress;
    }

    public String getVersionName()
    {
        if (TextUtils.isEmpty(mVersionName))
        {
            mVersionName = mSharedPreferences.getString(mAppPrefix
                    + "version_name", "");
        }

        return mVersionName;
    }

    public void setVersionName(String versionName)
    {
        mVersionName = versionName;
        mSharedPreferences.edit()
                .putString(mAppPrefix + "version_name", versionName).commit();
    }

    public int getUserPoints()
    {
        return mSharedPreferences.getInt(mAppPrefix + "points", 0);
    }

    public void setUserPoints(int points)
    {
        mSharedPreferences.edit()
                .putInt(mAppPrefix + "points", points).commit();
    }

    public int getDefaultPayType()
    {
        return mSharedPreferences.getInt(mAppPrefix + "paytype", PayType.Ali_Pay);
    }

    public void setDefaultType(int paytype)
    {
        mSharedPreferences.edit()
                .putInt(mAppPrefix + "paytype", paytype).commit();
    }

    public void setDeliveryAddress(String uid, AddressModel model)
    {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        if (model != null)
        {
            String userInfoJson = mGson.toJson(model);
            editor.putString(String.format("%s%s_address_info", mAppPrefix, uid), userInfoJson);
        }
        else
        {
            editor.putString(String.format("%s%s_address_info", mAppPrefix, uid), "");
        }

        editor.commit();
    }

    public AddressModel getDeliveryAddress(String uid)
    {
        String jsonData = mSharedPreferences.getString(String.format("%s%s_address_info", mAppPrefix, uid), "");
        if(TextUtils.isEmpty(jsonData))
        {
            return null;
        }

        try
        {
            return mGson.fromJson(jsonData, AddressModel.class);
        }
        catch (JsonSyntaxException e)
        {

        }

        return null;
    }

    public void setUserBalance(String uid, int balance)
    {
        mSharedPreferences.edit()
                .putInt(mAppPrefix + "balance", balance).commit();
    }

    public int getUserBalance(String uid)
    {
        return mSharedPreferences.getInt(mAppPrefix + "balance", 0);
    }


    public CookieStore getCookieStore()
    {
        CookieStore cookies = new BasicCookieStore();
        String tempCookieStore = mSharedPreferences
                .getString("cookieStore", "");
        if (!TextUtils.isEmpty(tempCookieStore))
        {
            String[] cookieStoreArray = tempCookieStore.split(";");
            int lengthCookieStore = cookieStoreArray.length;
            for (int i = 0; i < lengthCookieStore; i++)
            {
                String tempCookie = cookieStoreArray[i];
                String[] cookieArray = tempCookie.split(",");
                if (null != cookieArray && cookieArray.length == 10)
                {
                    String comment = cookieArray[0];
                    String commentURL = cookieArray[1];
                    String domain = cookieArray[2];
                    String expiryDate = cookieArray[3];
                    String name = cookieArray[4];
                    String path = cookieArray[5];
                    String ports = cookieArray[6];
                    String value = cookieArray[7];
                    String version = cookieArray[8];
                    String isSecure = cookieArray[9];

                    LogUtil.d("comment :" + comment);
                    LogUtil.d("commentURL :" + commentURL);
                    LogUtil.d("domain :" + domain);
                    LogUtil.d("expiryDate :" + expiryDate);
                    LogUtil.d("name :" + name);
                    LogUtil.d("path :" + path);
                    LogUtil.d("ports :" + ports);
                    LogUtil.d("value :" + value);
                    LogUtil.d("version :" + version);
                    LogUtil.d("isSecure :" + isSecure);

                    BasicClientCookie cookie = new BasicClientCookie(name,
                            value);
                    cookie.setAttribute(name, value);
                    if (!TextUtils.isEmpty(isSecure)
                            && !"null".equalsIgnoreCase(isSecure))
                    {
                        try
                        {
                            cookie.setSecure(Boolean.valueOf(isSecure));
                        }
                        catch (Exception e)
                        {
                            LogUtil.logException(e);
                        }
                    }
                    cookie.setComment(comment);
                    cookie.setDomain(domain);
                    if (!TextUtils.isEmpty(expiryDate)
                            && !"null".equalsIgnoreCase(expiryDate))
                    {
                        try
                        {
                            cookie.setExpiryDate(new Date(expiryDate));
                        }
                        catch (Exception e)
                        {
                            LogUtil.logException(e);
                        }
                    }
                    cookie.setPath(path);
                    cookie.setValue(value);
                    if (!TextUtils.isEmpty(version)
                            && !"null".equalsIgnoreCase(version))
                    {
                        try
                        {
                            cookie.setVersion(Integer.valueOf(version));
                        }
                        catch (Exception e)
                        {
                            LogUtil.logException(e);
                        }
                    }
                    cookies.addCookie(cookie);
                }
            }
            LogUtil.d("tempCookieStore :" + tempCookieStore);
        }
        return cookies;
    }

    public void setCookieStore(CookieStore cookieStore)
    {
        if (null != cookieStore)
        {
            List<Cookie> cookieList = cookieStore.getCookies();
            StringBuffer sb = new StringBuffer();
            for (Cookie cookie : cookieList)
            {
                if (null != cookie)
                {
                    sb.append(cookie.getComment()).append(",");
                    sb.append(cookie.getCommentURL()).append(",");
                    sb.append(cookie.getDomain()).append(",");
                    sb.append(cookie.getExpiryDate()).append(",");
                    sb.append(cookie.getName()).append(",");
                    sb.append(cookie.getPath()).append(",");
                    sb.append(Arrays.toString(cookie.getPorts())).append(",");
                    sb.append(cookie.getValue()).append(",");
                    sb.append(cookie.getVersion()).append(",");
                    sb.append(cookie.isSecure()).append(";");
                }
            }
            mSharedPreferences.edit()
                    .putString("cookieStore", String.valueOf(sb)).commit();
        }
    }
}
