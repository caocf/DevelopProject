package com.xhl.sum.chatlibrary.controller;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.xhl.sum.chatlibrary.utils.LogUtils;

import java.util.List;

/**
 * Created by wli on 15/12/1.
 */
public class ConversationEventHandler extends AVIMConversationEventHandler {

  private static ConversationEventHandler eventHandler;

  public static synchronized ConversationEventHandler getInstance() {
    if (null == eventHandler) {
      eventHandler = new ConversationEventHandler();
    }
    return eventHandler;
  }

  private ConversationEventHandler() {}

  @Override
  public void onMemberLeft(AVIMClient client, AVIMConversation conversation, List<String> members, String kickedBy) {
    LogUtils.i(MessageHelper.nameByUserIds(members) + " left, kicked by " +(kickedBy));
    refreshCacheAndNotify(conversation);
  }

  @Override
  public void onMemberJoined(AVIMClient client, AVIMConversation conversation, List<String> members, String invitedBy) {
    LogUtils.i(MessageHelper.nameByUserIds(members) + " joined , invited by " + (invitedBy));
    refreshCacheAndNotify(conversation);
  }
  @Override
  public void onKicked(AVIMClient client, AVIMConversation conversation, String kickedBy) {
    LogUtils.i("you are kicked by " +(kickedBy));
    refreshCacheAndNotify(conversation);
  }

  @Override
  public void onInvited(AVIMClient client, AVIMConversation conversation, String operator) {
    LogUtils.i("you are invited by " + (operator));
    refreshCacheAndNotify(conversation);
  }

  private void refreshCacheAndNotify(AVIMConversation conversation) {

    //先不做动态处理会话
//    ConversationChangeEvent conversationChangeEvent = new ConversationChangeEvent(conversation);
//    EventBus.getDefault().post(conversationChangeEvent);
  }
}
