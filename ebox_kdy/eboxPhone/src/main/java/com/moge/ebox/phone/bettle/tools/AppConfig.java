package com.moge.ebox.phone.bettle.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class AppConfig
{
  public static final String WEBSITE = "";
  public static final String SENDER_ID = "";
  public static final String CONSUMER_KEY = "";
  public static final String AppSecret = "";
  public static final String REDIRECT_URL = "";
  public static final String QQAppID = "";
  public static final String QQAppKey = "";
  public static final String WEIXIN_APP_ID = "";
  private static final String APP_CONFIG = "config";
  public static final String CONF_APP_UNIQUEID = "APP_UNIQUEID";
  public static final String CONF_COOKIE = "cookie";
  public static final String CONF_ACCESSTOKEN = "accessToken";
  public static final String CONF_ACCESSSECRET = "accessSecret";
  public static final String CONF_EXPIRESIN = "expiresIn";
  public static final String CONF_LOAD_IMAGE = "perf_loadimage";
  public static final String CONF_SCROLL = "perf_scroll";
  public static final String CONF_HTTPS_LOGIN = "perf_httpslogin";
  public static final String CONF_VOICE = "perf_voice";
  public static final String CONF_CHECKUP = "perf_checkup";
  private Context mContext;
  private static AppConfig appConfig;

  public static AppConfig getAppConfig(Context context)
  {
    if (appConfig == null) {
      appConfig = new AppConfig();
      appConfig.mContext = context;
    }
    return appConfig;
  }

  public static SharedPreferences getSharedPreferences(Context context)
  {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  public static boolean isLoadImage(Context context)
  {
    return getSharedPreferences(context)
      .getBoolean("perf_loadimage", true);
  }

  public String getCookie() {
    return get("cookie");
  }

  public void setAccessToken(String accessToken) {
    set("accessToken", accessToken);
  }

  public String getAccessToken() {
    return get("accessToken");
  }

  public void setAccessSecret(String accessSecret) {
    set("accessSecret", accessSecret);
  }

  public String getAccessSecret() {
    return get("accessSecret");
  }

  public void setExpiresIn(long expiresIn) {
    set("expiresIn", String.valueOf(expiresIn));
  }

  public long getExpiresIn() {
    return StringUtils.toLong(get("expiresIn"));
  }

  public String get(String key)
  {
    Properties props = get();
    return props != null ? props.getProperty(key) : null;
  }

  public Properties get() {
    FileInputStream fis = null;
    Properties props = new Properties();
    try
    {
      File dirConf = this.mContext.getDir("config", 0);
      fis = new FileInputStream(dirConf.getPath() + File.separator + "config");

      props.load(fis);
    }
    catch (Exception localException) {
      try {
        fis.close(); } catch (Exception localException1) {  } } finally { try { fis.close();
      } catch (Exception localException2) {
      } }
    return props;
  }

  private void setProps(Properties p) {
    FileOutputStream fos = null;
    try
    {
      File dirConf = this.mContext.getDir("config", 0);
      File conf = new File(dirConf, "config");
      fos = new FileOutputStream(conf);

      p.store(fos, null);
      fos.flush();
    } catch (Exception e) {
      e.printStackTrace();
      try
      {
        fos.close(); } catch (Exception localException1) {  } } finally { try { fos.close();
      } catch (Exception localException2)
      {
      } }
  }

  public void set(Properties ps) {
    Properties props = get();
    props.putAll(ps);
    setProps(props);
  }

  public void set(String key, String value)
  {
    Properties props = get();
    props.setProperty(key, value);
    setProps(props);
  }

  public void remove(String[] key)
  {
    Properties props = get();
    for (String k : key)
      props.remove(k);
    setProps(props);
  }
}
