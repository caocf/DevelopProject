package com.xhl.xhl_library.Base.User;

import android.content.Context;
import android.text.TextUtils;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class AppConfig {
    private static final String APP_CONFIG = "config";
    public static final String CONF_COOKIE = "cookie";
    private Context mContext;
    private static AppConfig appConfig;

    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            synchronized (AppConfig.class) {
                appConfig = new AppConfig();
                appConfig.mContext = context;
            }
        }
        return appConfig;
    }

    public void setCookie(String cookie) {
        set(CONF_COOKIE, cookie);
    }

    public String getCookie() {
        return get(CONF_COOKIE);
    }

    public String get(String key) {
        Properties props = getStorePro();
        return props != null ? props.getProperty(key) : null;
    }

    private Properties getStorePro() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            File dirConf = this.mContext.getDir(APP_CONFIG, 0);
            File store_file = new File(dirConf.getPath(), APP_CONFIG);
            LogUtil.v("store_file:" + store_file.getAbsolutePath());
            if (!store_file.exists()) {
                store_file.createNewFile();
            }
            fis = new FileInputStream(store_file);
            props.load(fis);
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception localException2) {
            }
        }
        return props;
    }

    /**
     * 存储到文件中
     *
     * @param p
     */
    private void storePro(Properties p) {
        FileOutputStream fos = null;
        try {
            File dirConf = this.mContext.getDir(APP_CONFIG, 0);
            File conf = new File(dirConf, APP_CONFIG);
            if (!conf.exists()) {
                conf.createNewFile();
            }
            fos = new FileOutputStream(conf);
            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception localException2) {
            }
        }
    }

    public void addAllPs(Properties ps) {
        Properties props = getStorePro();
        props.putAll(ps);
        storePro(props);
    }

    public void set(String key, String value) {
        Properties props = getStorePro();
        if (TextUtils.isEmpty(value)) {
            value = "";
        }
        props.setProperty(key, value);
        storePro(props);
    }

    public void remove(String[] key) {
        Properties props = getStorePro();
        for (String k : key)
            props.remove(k);
        storePro(props);
    }
}
