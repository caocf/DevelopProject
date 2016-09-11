package com.ebox.Anetwork;

import com.ebox.ex.network.model.enums.DotType;
import com.ebox.pub.service.global.Constants;
import com.ebox.pub.service.global.GlobalField;

public class NetworkConfig {

	public static String BoxServiceAddress = "";

    //订单支付
    public static String pay_address = "";
    public static final String pay_address_dev = "http://pay.dev.gegebox.com";
    public static final String pay_address_release = "http://pay.gegebox.com";

    public static String notify_address="";
    public static String notify_address_dev="http://m.dev.gegebox.com";
    public static String notify_address_release="http://m.gegebox.com";

    //出厂测试，生产测试数据上报  巡检结果上报
    public static String SuperAdmin="";
    public static String SuperAdminDev="http://store.dev.aimoge.com";
    public static String SuperAdminRelease="http://store.aimoge.com";

    //测试
	public static final String BoxServiceAddress_dev = "http://api.ebox.dev.gegebox.com";
	//正式
	public static final String BoxServiceAddress_release = "http://api.ebox.gegebox.com";


    //hx
    public static final String BoxServiceAddress_hx = "http://120.25.122.97:8080/smf/api/BOX_SERVICE/";


	static
    {
        new NetworkConfig();
    }

    private NetworkConfig()
    {
        switch (Constants.config)
        {
            case DEBUG:
                BoxServiceAddress = BoxServiceAddress_dev;
                pay_address=pay_address_dev;
                notify_address=notify_address_dev;
                SuperAdmin=SuperAdminDev;
                break;

            case DEV:
                notify_address=notify_address_dev;
                pay_address=pay_address_dev;
                BoxServiceAddress = BoxServiceAddress_dev;
                SuperAdmin=SuperAdminDev;
                break;

            case RELEASE:
                if (GlobalField.config.getDot() == DotType.HENGXI)
                {
                    BoxServiceAddress = BoxServiceAddress_hx;
                }
                else
                {
                    notify_address=notify_address_release;
                    pay_address=pay_address_release;
                    BoxServiceAddress = BoxServiceAddress_release;
                }
                SuperAdmin=SuperAdminRelease;

                break;
            default:
                break;
        }
    }
}
