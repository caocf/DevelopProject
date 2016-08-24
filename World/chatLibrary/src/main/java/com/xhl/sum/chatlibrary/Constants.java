package com.xhl.sum.chatlibrary;

/**
 * Created by Sum on 16/1/4.
 */
public class Constants {

    //查询一个会话中包含的成员ID
    public static final String OBJECT_ID = "objectId";

    //app内部统一管理的广播
    public static final String RECEIVER_NOTIFICATION = "com.xhl.windowOfWorld.pub.notification";

    private static final String LEANMESSAGE_CONSTANTS_PREFIX = "com.xhl.";

    public static final String CHAT_TYPE = "chat_type";
    public static final String CHAT_CONVERSATION = "chat_conversation";
    public static final String CHAT_ROOM = "chat_room";

    //成员Id
    public static final String MEMBER_ID = getPrefixConstant("member_id");
    //会话Id
    public static final String CONVERSATION_ID = getPrefixConstant("conversation_id");
    //会话名称
    public static final String CONVERSATION__NAME = getPrefixConstant("conversation_name");

    public static final String LEANCHAT_USER_ID = getPrefixConstant("leanchat_user_id");

    public static final String ACTIVITY_TITLE = getPrefixConstant("activity_title");

    //商家聊天自定义属性
    public static final String shop_attr_conversation_logo = getXHLConstant("shop_logo");//店铺logo
    public static final String shop_attr_conversation_name = getXHLConstant("shop_name");//店铺名称
    public static final String shop_attr_conversation_url = getXHLConstant("shop_url");//店铺URL



    //Notification
    public static final String NOTIFICATION_TAG = getPrefixConstant("notification_tag");
    public static final String NOTIFICATION_SINGLE_CHAT = Constants.getPrefixConstant("notification_single_chat");
    public static final String NOTIFICATION_GROUP_CHAT = Constants.getPrefixConstant("notification_group_chat");
    public static final String NOTIFICATION_ROBOT_CHAT = Constants.getPrefixConstant("notification_robot_chat");
    public static final String NOTIFICATION_SYSTEM = Constants.getPrefixConstant("notification_system_chat");

    public static String getPrefixConstant(String str) {
        return LEANMESSAGE_CONSTANTS_PREFIX + str;
    }
    public static String getXHLConstant(String str) {
        return "XHL_" + str;
    }
}
