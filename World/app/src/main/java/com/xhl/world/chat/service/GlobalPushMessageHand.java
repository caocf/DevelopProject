package com.xhl.world.chat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xhl.sum.chatlibrary.Constants;
import com.xhl.world.AppApplication;
import com.xhl.world.chat.event.MessageEvent;
import com.xhl.world.chat.view.ChatRoomActivity;
import com.xhl.world.config.Constant;
import com.xhl.world.ui.activity.IndexActivity;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.main.MainActivity;

/**
 * Created by Sum on 16/1/4.
 */
public class GlobalPushMessageHand extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        Logger.v("GlobalPushMessageHand:" + intent.getAction());

        String action = intent.getAction();
        //消息推送统一处理
        if (!action.equals(Constants.RECEIVER_NOTIFICATION)) {
            return;
        }

        String tag = intent.getStringExtra(Constants.NOTIFICATION_TAG);
        //群聊消息
        if (Constants.NOTIFICATION_GROUP_CHAT.equals(tag)) {
            gotoChatActivity(context, intent);
        }
        //单聊消息
        else if (Constants.NOTIFICATION_SINGLE_CHAT.equals(tag)) {
            gotoChatActivity(context, intent);
        }
        //系统推送消息
        else {
            if (Constants.NOTIFICATION_SYSTEM.equals(tag)) {
                try {
                    String msg = intent.getStringExtra(Constant.intent_message_data);
                    if (msg != null) {
                        if (AppApplication.appContext.mAppExit) {
                            //外部Context启动App界面
                            Intent startMain = new Intent(context, MainActivity.class);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startMain.putExtra(Constant.intent_message_data, msg);
                            startMain.putExtra(Constant.intent_message_type, Constant.intent_system_message);
                            context.startActivity(startMain);
                        } else {
                            MessageEvent messageEvent = new MessageEvent();
                            messageEvent.message = msg;
                            messageEvent.type = Constant.intent_system_message;
                            EventBusHelper.post(messageEvent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 如果 app 上下文已经缺失跳转到首页
     *
     * @param context
     */
    private void gotoLoginActivity(Context context) {
        Intent startActivityIntent = new Intent(context, IndexActivity.class);
        startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startActivityIntent);
    }

    /**
     * 跳转至聊天页面
     *
     * @param context
     * @param intent
     */
    private void gotoChatActivity(Context context, Intent intent) {
//13276683373
//        Logger.e("-------intent:" + intent + ",context:" + context);
        //{ act=com.xhl.windowOfWorld.pub.notification flg=0x10 cmp=com.xhl.windowofword/com.xhl.world.coreChat.service.GlobalPushMessageHand (has extras) },context:android.app.ReceiverRestrictedContext@37c2f47e
  /*      Intent startMain = new Intent(context, MainActivity.class);

        startMain.putExtra(Constant.intent_message_type, Constant.intent_chat_message);

        context.startActivity(startMain);*/

        Intent startActivityIntent = new Intent(context, ChatRoomActivity.class);
        startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.hasExtra(Constants.MEMBER_ID)) {
            startActivityIntent.putExtra(Constants.MEMBER_ID, intent.getStringExtra(Constants.MEMBER_ID));
        } else {
            startActivityIntent.putExtra(Constants.CONVERSATION_ID, intent.getStringExtra(Constants.CONVERSATION_ID));
        }
        context.startActivity(startActivityIntent);
    }
}
