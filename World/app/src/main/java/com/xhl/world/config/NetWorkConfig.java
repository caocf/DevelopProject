package com.xhl.world.config;

/**
 * Created by Sum on 15/11/28.
 */
public class NetWorkConfig {

    //图片域名
    public static String imageHost = "";
    //图片上传统一Uri
    public static String uploadFilePath = "";
    //服务器域名
    public static String generalHost = "";
    private static final String generalHost_dev = "http://58.213.106.92:8081/";
    //    private static final String generalHost_debug = "http://192.168.1.118:8081/";
//    private static final String generalHost_debug = "http://192.168.1.101:8080/SHZC/";
    private static final String generalHost_debug = "http://192.168.1.114:8080/SHZC/";
    private static final String generalHost_local = "http://192.168.1.131:8080/SHZC/";
    private static final String generalHost_rel = "http://www.wawscm.com/";

    private static final String imageHost_dev = "http://58.213.106.82";
    private static final String imageHost_rel = "http://www.wawscm.com";
    //图片上传路径
    private static final String image_upload_path_dev = "appuploadfile/uploadImage";

    //webApp地址
    public static String web_app_index = "http://www.hljd.cn.com/SHZC/mobileWeb/index.html";

    static {
        new NetWorkConfig();
    }

    private NetWorkConfig() {

        switch (Constant.API) {
            case LOCAL:
                generalHost = generalHost_local;
                uploadFilePath = generalHost + image_upload_path_dev;
                imageHost = imageHost_dev;
                break;
            case DEBUG:
                generalHost = generalHost_debug;
                uploadFilePath = generalHost + image_upload_path_dev;
                imageHost = imageHost_dev;
                break;
            case DEV:
                generalHost = generalHost_dev;
                uploadFilePath = generalHost + image_upload_path_dev;
                imageHost = imageHost_dev;
                break;

            case RELEASE:
                generalHost = generalHost_rel;
                uploadFilePath = generalHost + image_upload_path_dev;
                imageHost = imageHost_rel;
                break;
        }

    }

}
