package com.xhl.sum.chatlibrary.controller;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.xhl.sum.chatlibrary.Constants;
import com.xhl.sum.chatlibrary.model.ConversationType;
import com.xhl.sum.chatlibrary.model.Room;
import com.xhl.sum.chatlibrary.utils.AVIMConversationCacheUtils;
import com.xhl.sum.chatlibrary.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class ConversationHelper {

    public static boolean isValidConversation(AVIMConversation conversation) {
        if (conversation == null) {
            LogUtils.d("invalid reason : conversation is null");
            return false;
        }
        if (conversation.getMembers() == null || conversation.getMembers().size() == 0) {
            LogUtils.d("invalid reason : conversation members null or empty");
            return false;
        }
        Object type = conversation.getAttribute(ConversationType.TYPE_KEY);
        if (type == null) {
            LogUtils.d("invalid reason : type is null");
            return false;
        }

        int typeInt = (Integer) type;
        if (typeInt == ConversationType.Single.getValue()) {
            if (conversation.getMembers().size() != 2 || !conversation.getMembers().contains(ChatManager.getInstance().getSelfId())) {
                LogUtils.d("invalid reason : oneToOne conversation not correct");
                return false;
            }
        } else if (typeInt == ConversationType.Group.getValue()) {

        } else {
            LogUtils.d("invalid reason : typeInt wrong");
            return false;
        }
        return true;
    }

    public static ConversationType typeOfConversation(AVIMConversation conversation) {
        if (isValidConversation(conversation)) {
            Object typeObject = conversation.getAttribute(ConversationType.TYPE_KEY);
            int typeInt = (Integer) typeObject;
            return ConversationType.fromInt(typeInt);
        } else {
            LogUtils.e("invalid conversation ");
            // 因为 Group 不需要取 otherId，检查没那么严格，避免导致崩溃
            return ConversationType.Group;
        }
    }

    /**
     * 获取单聊对话的另外一个人的 userId
     *
     * @param conversation
     * @return 如果非法对话，则为 selfId
     */
    public static String otherIdOfConversation(AVIMConversation conversation) {
        if (isValidConversation(conversation)) {
            if (typeOfConversation(conversation) == ConversationType.Single) {
                List<String> members = conversation.getMembers();
                if (members.size() == 2) {
                    if (members.get(0).equals(ChatManager.getInstance().getSelfId())) {
                        return members.get(1);
                    } else {
                        return members.get(0);
                    }
                }
            }
        }
        // 尽管异常，返回可以使用的 userId
        return ChatManager.getInstance().getSelfId();
    }

    public static String getConversationName(AVIMConversation conversation) {
        if (isValidConversation(conversation)) {
            String name = conversation.getName();
            if (!TextUtils.isEmpty(name)) {
                return name;
            }
            return "NameIsNull";
        } else {
            return "";
        }
    }

    public static void findAndCacheRooms(final Room.MultiRoomsCallback callback) {
        final List<Room> rooms = ChatManager.getInstance().findRecentRooms();
        List<String> conversationIds = new ArrayList<>();
        for (Room room : rooms) {
            conversationIds.add(room.getConversationId());
        }

        if (conversationIds.size() > 0) {

            AVIMConversationCacheUtils.cacheConversations(conversationIds, new AVIMConversationCacheUtils.CacheConversationCallback() {
                @Override
                public void done(AVException e) {
                    if (e != null) {
                        callback.done(rooms, e);
                    } else {
                        callback.done(rooms, null);
                    }
                }
            });
        } else {
            callback.done(rooms, null);
        }
    }

    public static void updateName(final AVIMConversation conv, String newName, final AVIMConversationCallback callback) {
        conv.setName(newName);
        conv.updateInfoInBackground(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e != null) {
                    if (callback != null) {
                        callback.done(e);
                    }
                } else {
                    if (callback != null) {
                        callback.done(null);
                    }
                }
            }
        });
    }

    public static String getConversationLogo(AVIMConversation conversation) {

        return (String) conversation.getAttribute(Constants.shop_attr_conversation_logo);
    }
}
