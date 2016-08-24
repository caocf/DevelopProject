package com.xhl.sum.chatlibrary.model;

/**
 * 对话类型，单聊，群聊，机器人
 */
public enum ConversationType {
    Single(0), Group(1), Robot(2);
    /**
     * 创建的时候直接设置 type 字段
     */
    public static final String TYPE_KEY = "type";
    /**
     * 查找对话的时候，要加前缀 attr. 其实type保存在conversation的attr中
     * 登录网站后台，_Conversation 表可看到
     */
    public static final String ATTR_TYPE_KEY = "attr.type";

    int value;

    ConversationType(int value) {
        this.value = value;
    }

    public static ConversationType fromInt(int i) {
        if (i < 3) {
            return values()[i];
        } else {
            return Group;
        }
    }

    public int getValue() {
        return value;
    }
}
