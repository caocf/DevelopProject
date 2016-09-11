package com.ebox.Anetwork;

import com.ebox.ex.network.model.enums.DotType;
import com.ebox.pub.service.global.Constants;
import com.ebox.pub.service.global.GlobalField;

public class NetworkConfig_old {

	public static String BoxServiceAddress = "http://gegelaile.cn:8090/smf/api/BOX_SERVICE/";
	public static final String BoxServiceAddress_debug = "http://192.168.1.26:8080/smf/api/BOX_SERVICE/";
	public static final String BoxServiceAddress_dev = "http://115.231.94.49:8080/smf/api/BOX_SERVICE/";
	//正式
	public static final String BoxServiceAddress_release = "http://gegelaile.cn:8090/smf/api/BOX_SERVICE/";

    //hx
    public static final String BoxServiceAddress_hx = "http://120.25.122.97:8080/smf/api/BOX_SERVICE/";


	public static final String getOperatorInfor = "getOperatorInfor/";
	public static final String getActive = "getActive/";
	public static final String getQrcode = "getQrcode/";
	public static final String getUpdateInfo = "getUpdateInfor/";
	public static final String backfillUpdate = "backfillUpdate/";

	static
    {
        new NetworkConfig_old();
    }

    private NetworkConfig_old()
    {
        switch (Constants.config)
        {
            case DEBUG:
            	BoxServiceAddress = BoxServiceAddress_debug;
                break;
                
            case DEV:
            	BoxServiceAddress = BoxServiceAddress_dev;
                break;
         
            case RELEASE:
                if (GlobalField.config.getDot() == DotType.HENGXI)
                {
                    BoxServiceAddress = BoxServiceAddress_hx;
                }
                else
                {
                    BoxServiceAddress = BoxServiceAddress_release;
                }

                break;
            default:
                break;
        }
    }
}
