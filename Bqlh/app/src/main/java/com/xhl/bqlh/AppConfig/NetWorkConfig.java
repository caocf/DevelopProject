package com.xhl.bqlh.AppConfig;

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
    private static final String generalHost_test = "http://testenv.xhlbqlh.com/";
    private static final String generalHost_rel = "http://www.xhlbqlh.com/";
//        private static final String generalHost_local = "http://192.168.1.111:8080/BQLH/";
    private static final String generalHost_local = "http://192.168.1.211:8080/bqlh-foreground/";
    //图片上传路径
    private static final String image_upload_path = "download/uploadImage";

    static {
        new NetWorkConfig();
    }

    private NetWorkConfig() {

        switch (Constant.API) {
            case LOCAL:
                generalHost = generalHost_local;
                imageHost = generalHost_rel;
                break;
            case TEST:
                generalHost = generalHost_test;
                imageHost = generalHost_rel;
                break;
            case DEV:
                generalHost = generalHost_dev;
                imageHost = generalHost_rel;
                break;

            case RELEASE:
                generalHost = generalHost_rel;
                imageHost = generalHost_rel;
                break;
        }

        uploadFilePath = generalHost + image_upload_path;
    }

}
