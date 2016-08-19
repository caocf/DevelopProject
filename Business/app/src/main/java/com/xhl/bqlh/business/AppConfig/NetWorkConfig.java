package com.xhl.bqlh.business.AppConfig;

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

    private static final String generalHost_dev = "http://dev.xhlbqlh.com/";
//    private static final String generalHost_local = "http://192.168.1.130:8080/BQLH/";
    private static final String generalHost_local = "http://192.168.1.105:8080/bqlh-foreground/";
    private static final String generalHost_rel = "http://www.xhlbqlh.com/";
    private static final String generalHost_test = "http://testenv.xhlbqlh.com/";

    //图片上传路径
    private static final String image_upload_path_dev = "download/uploadImage";

    static {
        new NetWorkConfig();
    }

    private NetWorkConfig() {

        switch (Constant.API) {
            case LOCAL:
                generalHost = generalHost_local;
                uploadFilePath = generalHost + image_upload_path_dev;
                imageHost = generalHost_dev;
                break;
            case TEST:
                generalHost = generalHost_test;
                uploadFilePath = generalHost + image_upload_path_dev;
                imageHost = generalHost_dev;
                break;
            case DEV:
                generalHost = generalHost_dev;
                uploadFilePath = generalHost + image_upload_path_dev;
                imageHost = generalHost_dev;
                break;

            case RELEASE:
                generalHost = generalHost_rel;
                uploadFilePath = generalHost + image_upload_path_dev;
                imageHost = generalHost_rel;
                break;
        }

    }

}
