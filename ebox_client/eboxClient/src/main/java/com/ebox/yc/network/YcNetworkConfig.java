package com.ebox.yc.network;

import com.ebox.pub.service.global.Constants;

public class YcNetworkConfig
{
    /*银川*/
    public static String YCServiceAddress="";
    public static final String YcServiceAddress_debug = "http://42.63.25.198:8083/ccs_web/";
    public static final String YcServiceAddress_dev = "http://42.63.25.198:8083/ccs_web/";
    public static final String YcServiceAddress_release = "http://42.63.25.198:8083/ccs_web/";
    /*用户查询当前社区的公告*/
    public static final String qryContents ="overview/qryContents/";
    public static final String qryContentDetail ="overview/qryContentDetail/";
    public static final String qryWorkWin = "work/qryWorkWin/";
    public static final String qryWorkItems = "work/qryWorkItems/";
    public static final String qryWorkGuide = "work/qryWorkGuide/";

    static
    {
        new YcNetworkConfig();
    }

    private YcNetworkConfig()
    {
        switch (Constants.config)
        {
            case DEBUG:
                YCServiceAddress  = YcServiceAddress_debug;
                break;

            case DEV:
                YCServiceAddress  = YcServiceAddress_dev;
                break;

            case RELEASE:
                YCServiceAddress  = YcServiceAddress_release;
                break;
            default:
                break;
        }
    }

}
