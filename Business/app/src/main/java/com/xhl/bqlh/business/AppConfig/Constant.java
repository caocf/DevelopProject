package com.xhl.bqlh.business.AppConfig;

/**
 * Created by Sum on 15/12/3.
 */

public class Constant {
    public enum Config {
        LOCAL, TEST, DEV, RELEASE
    }

    //统计开关
    public static final boolean openAnalytics = false;
    //异常上报
    public static final boolean openCrash = false;

    //调试模式开关
    public static final boolean isDebug = true;

    public static final Config API = Config.LOCAL;//服务器配置数据
}
