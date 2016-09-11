package com.moge.gege.config;

public class NetworkConfig
{
    public static String generalAddress;
    public static final String debugAddress = "http://192.168.1.3:13000";
    public static final String devAddress = "http://api.dev.aimoge.com";
    public static final String releaseAddress = "http://api.aimoge.com";

    public static String imageAddress;
    private static final String debugImageAddress = "http://moge.qiniudn.com/";
    private static final String releaseImageAddress = "http://moge.qiniudn.com/";

    public static String chatWebsocketAddress;
    private static final String debugChatWebsocketAddress = "ws://192.168.1.3:13200";
    private static final String devChatWebsocketAddress = "ws://poll.dev.aimoge.com";
    private static final String releaseChatWebsocketAddress = "ws://poll.aimoge.com";
    public static String chatWebsocketAddressSuffix = "/v1/poll";

    public static String chatAddress;
    private static final String debugChatAddress = "http://192.168.1.3:13200";
    private static final String devChatAddress = "http://msg.dev.aimoge.com";
    private static final String releaseChatAddress = "http://msg.aimoge.com";

    public static String payAddress;
    public static final String debugPayAddress = "http://192.168.1.3:13100";
    public static final String devPayAddress = "http://pay.dev.aimoge.com";
    public static final String releasePayAddress = "http://pay.aimoge.com";

    // for social share
    public static final String mobileAddress = "http://m.aimoge.com";
    public static final String appDownloadUrl = "http://m.aimoge.com/app/download/latest";

    // protocol url
    public static final String protocolUrl = "http://m.aimoge.com/privacy.html";

    static
    {
        new NetworkConfig();
    }

    private NetworkConfig()
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
                generalAddress = releaseAddress;
                imageAddress = releaseImageAddress;
                chatWebsocketAddress = releaseChatWebsocketAddress;
                chatAddress = releaseChatAddress;
                payAddress = releasePayAddress;
                break;
            default:
                break;
        }
    }

}
