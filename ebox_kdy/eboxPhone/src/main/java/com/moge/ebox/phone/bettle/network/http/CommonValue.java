package com.moge.ebox.phone.bettle.network.http;

import com.moge.ebox.phone.config.Constants;

/**
 * 公共配置项目
 *
 * @author John
 */
public class CommonValue {

    public static String PackageName = "ebox.phone";

    public static String BASE_API = "";

    public static String UPLOAD_API = "";

    public static String PIC_API = "";

    public static String DOWNLOAD_APP = "";

    public static String DOWNLOAD_API = "";

    public static String PAY_API = "";    //获得支付单信息
    public static String LOGIN_AIMOGE_API = "";   //获得支付登录状态

    // 根路径地址
    public static final String BASE_IP_DEBUG = "http://api.ebox.dev.gegebox.com";
    public static final String BASE_IP_RELEASE = "http://api.ebox.gegebox.com";

    //base_api
    public static final String BASE_API_DEBUG = BASE_IP_DEBUG + "/v2/";
    public static final String BASE_API_RELEASE = BASE_IP_RELEASE + "/v2/";
    public static final String PAY_API_DEBUG = "http://pay.dev.gegebox.com/v1/";

    //图片路径
    public static final String BASE_API_EDBUG_PIC = BASE_IP_DEBUG + "/v2/images/";
    public static final String BASE_API_RELEASE_PIC = BASE_IP_RELEASE + "/smf";

    public static final String UPLOAD_API_DEBUG = BASE_IP_DEBUG + "/v2/images";

    //图片上传
    public static final String UPLOAD_API_RELEASE = BASE_IP_RELEASE + "/smf/api/upload";

    public static final String DOWNLOAD_APP_DEBUG = BASE_IP_DEBUG + "/smf/api/downloadapp";

    public static final String DOWNLOAD_APP_RELEASE = BASE_IP_RELEASE + "/smf/api/downloadapp";

    public static final String DOWNLOAD_API_DEBUG = BASE_IP_DEBUG + "/smf/api/download";

    public static final String DOWNLOAD_API_RELEASE = BASE_IP_RELEASE + "/smf/api/download";

    public static final String PAY_API_RELEASE = "";
    public static final String LOGIN_AIMOGE_API_RELEASE = "";

    static {
        new CommonValue();
    }

    private CommonValue() {
        switch (Constants.config) {
            case DEBUG:
                BASE_API = BASE_API_DEBUG;
                DOWNLOAD_APP = DOWNLOAD_APP_DEBUG;
                DOWNLOAD_API = DOWNLOAD_API_DEBUG;
                UPLOAD_API = UPLOAD_API_DEBUG;
                PIC_API = BASE_API_EDBUG_PIC;
                PAY_API = PAY_API_DEBUG;
                break;
            case RELEASE:
                BASE_API = BASE_API_RELEASE;
                PIC_API = BASE_API_RELEASE_PIC;
                DOWNLOAD_APP = DOWNLOAD_APP_RELEASE;
                DOWNLOAD_API = DOWNLOAD_API_RELEASE;
                UPLOAD_API = UPLOAD_API_RELEASE;
                PAY_API = PAY_API_RELEASE;
                break;
        }
    }
}
