package com.xhl.bqlh.AppConfig;

/**
 * Created by Sum on 15/12/3.
 */

public class Constant {
    public enum Config {
        LOCAL, TEST, DEV, RELEASE
    }

    //统计开关
    public static final boolean openAnalytics = true;
    //异常上报
    public static final boolean openCrash = true;

    //调试模式开关
    public static final boolean isDebug = false;

    public static final Config API = Config.TEST;//服务器配置数据
}
