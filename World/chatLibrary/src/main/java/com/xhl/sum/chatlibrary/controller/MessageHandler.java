package com.xhl.sum.chatlibrary.controller;

import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.xhl.sum.chatlibrary.Constants;
import com.xhl.sum.chatlibrary.event.ImTypeMessageEvent;
import com.xhl.sum.chatlibrary.model.ConversationType;
import com.xhl.sum.chatlibrary.utils.LogUtils;
import com.xhl.sum.chatlibrary.utils.NotificationUtils;


import de.greenrobot.event.EventBus;

/**
 * Created by zhangxiaobo on 15/4/20.
 */
public class MessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

    private Context context;

    public MessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        if (message == null || message.getMessageId() == null) {
            LogUtils.w("may be SDK Bug, message or message id is null");
            return;
        }

        if (!ConversationHelper.isValidConversation(conversation)) {
            LogUtils.w("receive msg from invalid conversation");
        }

        if (ChatManager.getInstance().getSelfId() == null) {
            LogUtils.w("selfId is null, please call setupManagerWithUserId ");
            client.close(null);
        } else {
            //当前打开客户端和消息对应客户端不同关闭当前客户端，避免在接收到消息
            if (!client.getClientId().equals(ChatManager.getInstance().getSelfId())) {
                client.close(null);
            } else {
                //添加一个聊天会话到数据库
                ChatManager.getInstance().getRoomsTable().insertRoom(message.getConversationId());
                //避免自己给自己发消息
                if (!message.getFrom().equals(client.getClientId())) {

                    if (NotificationUtils.isShowNotification(conversation.getConversationId())) {
                        sendChatNotification(message, conversation);
                    }
                    //添加到未读记录
                    ChatManager.getInstance().getRoomsTable().increaseUnreadCount(message.getConversationId());
                }
                sendEvent(message, conversation);
            }
        }
    }

    @Override
    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
    }

    /**
     * 因为没有 db，所以暂时先把消息广播出去，由接收方自己处理
     * 稍后应该加入 db
     *
     * @param message
     * @param conversation
     */
    private void sendEvent(AVIMTypedMessage message, AVIMConversation conversation) {
        ImTypeMessageEvent event = new ImTypeMessageEvent();
        event.message = message;
        event.conversation = conversation;
        EventBus.getDefault().post(event);
    }

    private void sendChatNotification(AVIMTypedMessage message, AVIMConversation conversation) {
        if (null != conversation && null != message) {
            String notificationContent;
            if (message instanceof AVIMTextMessage) {
                notificationContent = ((AVIMTextMessage) message).getText();
            } else if (message instanceof AVIMImageMessage) {
                notificationContent = "[图片]";
            } else {
                notificationContent = "未知类型";
            }

            String title = conversation.getName();
            Intent intent = new Intent();
            intent.setAction(Constants.RECEIVER_NOTIFICATION);
            intent.putExtra(Constants.CONVERSATION_ID, conversation.getConversationId());
            intent.putExtra(Constants.MEMBER_ID, message.getFrom());
//            intent.setClass(context, PubReceiver.class);

            if (ConversationHelper.typeOfConversation(conversation) == ConversationType.Single) {

                intent.putExtra(Constants.NOTIFICATION_TAG, Constants.NOTIFICATION_SINGLE_CHAT);
                //聊天界面
            } else if (ConversationHelper.typeOfConversation(conversation) == ConversationType.Robot) {

                intent.putExtra(Constants.NOTIFICATION_TAG, Constants.NOTIFICATION_ROBOT_CHAT);
            } else {
                intent.putExtra(Constants.NOTIFICATION_TAG, Constants.NOTIFICATION_GROUP_CHAT);
            }

            NotificationUtils.showNotification(context, title, notificationContent, intent);
        }
    }
}
