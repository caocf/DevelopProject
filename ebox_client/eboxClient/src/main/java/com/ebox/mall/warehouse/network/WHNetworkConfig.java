package com.ebox.mall.warehouse.network;

import com.ebox.ex.network.model.enums.DotType;
import com.ebox.pub.service.global.Constants;
import com.ebox.pub.service.global.GlobalField;

public class WHNetworkConfig
{
    public static String generalAddress;
    public static final String debugAddress = "http://192.168.1.3:13000";
    public static final String devAddress = "http://api.dev.aimoge.com";
    public static final String releaseAddress = "http://api.aimoge.com";

    public static String imageAddress;
    private static final String debugImageAddress = "http://moge.qiniudn.com/";
    private static final String releaseImageAddress = "http://moge.qiniudn.com/";

    public static String chatWebsocketAddress;
    private static final String debugChatWebsocketAddress = "ws://192.168.1.3:13200/v1/poll";
    private static final String devChatWebsocketAddress = "ws://dev.poll.aimoge.com/v1/poll";
    private static final String releaseChatWebsocketAddress = "ws://poll.aimoge.com/v1/poll";

    public static String chatAddress;
    private static final String debugChatAddress = "http://192.168.1.3:13200";
    private static final String devChatAddress = "http://api.dev.aimoge.com";
    private static final String releaseChatAddress = "http://api.aimoge.com";

    public static String payAddress;
    public static final String debugPayAddress = "http://192.168.1.3:13100";
    public static final String devPayAddress = "http://pay.dev.aimoge.com";
    public static final String releasePayAddress = "http://pay.aimoge.com";

    static
    {
        new WHNetworkConfig();
    }

    private WHNetworkConfig()
    {
        switch (Constants.config)
        {
            case DEBUG:
                generalAddress = debugAddress;
                imageAddress = debugImageAddress;
                chatWebsocketAddress = debugChatWebsocketAddress;
                chatAddress = debugChatAddress;
                payAddress = debugPayAddress;
                break;
            case DEV:
                generalAddress = devAddress;
                imageAddress = releaseImageAddress;
                chatWebsocketAddress = devChatWebsocketAddress;
                chatAddress = devChatAddress;
                payAddress = devPayAddress;
                break;
            case RELEASE:
                if (GlobalField.config.getDot() == DotType.HENGXI)
                {
                    generalAddress = devAddress;
                    imageAddress = releaseImageAddress;
                    chatWebsocketAddress = devChatWebsocketAddress;
                    chatAddress = devChatAddress;
                    payAddress = devPayAddress;
                }
                else
                {
                    generalAddress = releaseAddress;
                    imageAddress = releaseImageAddress;
                    chatWebsocketAddress = releaseChatWebsocketAddress;
                    chatAddress = releaseChatAddress;
                    payAddress = releasePayAddress;
                }

                break;
            default:
                break;
        }
    }

}
