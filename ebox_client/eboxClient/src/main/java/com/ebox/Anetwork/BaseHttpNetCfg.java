package com.ebox.Anetwork;

import com.ebox.ex.network.model.enums.DotType;
import com.ebox.pub.service.global.Constants;
import com.ebox.pub.service.global.GlobalField;


public class BaseHttpNetCfg {
	// websocket的suffix
	public static final String Ws_Server_Suffix = "/v1/poll";
	// websocket地址
	public static String Ws_Server_Url = "ws://poll.dev.aimoge.com:4210";
	public static final String Ws_Server_Url_debug = "ws://poll.dev.aimoge.com:4210";
	public static final String Ws_Server_Url_dev = "ws://poll.dev.aimoge.com:4210";
	public static final String Ws_Server_Url_release = "ws://poll.aimoge.com:4210";
	
//	// SOCKET 服务端ID
//	public static String Server_IP = "www.gegelaile.cn";
//	public static final String Server_IP_debug = "192.168.1.26";
//	public static final String Server_IP_dev = "115.231.94.49";
//	public static final String Server_IP_release = "www.gegelaile.cn";
//
//    public static final String hx_Server_IP_release = "120.25.122.97";

	 
//	public static int Server_Port = 8061;
//	public static final int Server_Port_debug = 8061;
//	public static final int Server_Port_dev = 8061;
//	public static final int Server_Port_release = 8061;
//    public static final int hx_Server_Port_release = 8061;
	
	// 服务器地址
	public static String END_POINT = "http://gegelaile.cn:8090/smf/JsonService.do";
	public static String END_POINT_DEBUG = "http://192.168.1.34:8080/smf/JsonService.do";
	public static String END_POINT_DEV = "http://115.231.94.49:8080/smf/JsonService.do";
	public static String END_POINT_RELEASE = "http://gegelaile.cn:8090/smf/JsonService.do";

    public static String END_POINT_hx = "http://120.25.122.97:8080/smf/JsonService.do";
	
	
	public static String DevAddress = "http://pay.aimoge.com";
	public static String DevAddress_debug = "http://pay.aimoge.com";
	public static String DevAddress_dev = "http://pay.aimoge.com";
	public static String DevAddress_release = "http://pay.aimoge.com";
	// 上传图片服务器地址
	public static String imageUploadBaseURL = "http://gegelaile.cn:8090/smf/imageupload/images";
	public static String imageUploadBaseURL_debug = "http://192.168.1.34:8080/smf/imageupload/images";
	public static String imageUploadBaseURL_dev = "http://115.231.94.49:8080/smf/imageupload/images";
	public static String imageUploadBaseURL_release = "http://gegelaile.cn:8090/smf/imageupload/images";
    public static String imageUploadBaseURL_hx = "http://120.25.122.97:8080/smf/imageupload/images";
		 
	static
    {
        new BaseHttpNetCfg();
    }

    private BaseHttpNetCfg()
    {
        switch (Constants.config)
        {
            case DEBUG:
            	Ws_Server_Url = Ws_Server_Url_debug;
//            	Server_IP = Server_IP_debug;
//            	Server_Port  = Server_Port_debug;
            	END_POINT = END_POINT_DEBUG;
            	DevAddress = DevAddress_debug;
            	imageUploadBaseURL = imageUploadBaseURL_debug;
                break;
                
            case DEV:
            	Ws_Server_Url = Ws_Server_Url_dev;
//            	Server_IP = Server_IP_dev;
//            	Server_Port  = Server_Port_dev;
            	END_POINT = END_POINT_DEV;
            	DevAddress = DevAddress_dev;
            	imageUploadBaseURL = imageUploadBaseURL_dev;
                break;
         
            case RELEASE:
                if (GlobalField.config.getDot() == DotType.HENGXI)
                {
                    Ws_Server_Url = Ws_Server_Url_dev;
//                    Server_IP = hx_Server_IP_release;
//                    Server_Port  = hx_Server_Port_release;
                    END_POINT = END_POINT_hx;
                    DevAddress = DevAddress_release;
                    imageUploadBaseURL = imageUploadBaseURL_hx;
                }
                else
                {
                    Ws_Server_Url = Ws_Server_Url_release;
//                    Server_IP = Server_IP_release;
//                    Server_Port  = Server_Port_release;
                    END_POINT = END_POINT_RELEASE;
                    DevAddress = DevAddress_release;
                    imageUploadBaseURL = imageUploadBaseURL_release;
                }

                break;
            default:
                break;
        }
    }
}
