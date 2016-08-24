package com.xhl.world.chat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xhl.sum.chatlibrary.Constants;
import com.xhl.sum.chatlibrary.utils.NotificationUtils;
import com.xhl.world.chat.DBHelper;
import com.xhl.world.chat.PushMessage;
import com.xhl.world.config.Constant;
import com.xhl.xhl_library.utils.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Sum on 15/12/24.
 * 自定义服务端接受到数据
 */
public class AppReceiver extends BroadcastReceiver {

    //自定义的广播Action
    public static final String PUSH_ACTION = "com.xhl.push.app.receiver";
    public static final String PUSH_DATA = "com.xhl.push.data";
    //固定的推送数据体key
    public final static String PUSH_ACTION_DATA_KEY = "com.avos.avoscloud.Data";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!TextUtils.isEmpty(action)) {
            //获取推送的action，根据服务端自定义
            if (action.equals(PUSH_ACTION)) {

                Bundle extras = intent.getExtras();

                String pushData = extras.getString(PUSH_ACTION_DATA_KEY);

                //推送的消息内容
                String pushMsg = "";

                try {
                    JSONObject object = new JSONObject(pushData);
                    pushMsg = object.getString(PUSH_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Logger.e("push data:" + pushMsg);

                if (!TextUtils.isEmpty(pushMsg)) {

                    PushMessage message = new Gson().fromJson(pushMsg, PushMessage.class);

                    DBHelper.getInstance().addOneMessage(message);

                    Intent notificationIntent = new Intent(context, GlobalPushMessageHand.class);
                    //系统通知类型
                    notificationIntent.putExtra(Constants.NOTIFICATION_TAG,Constants.NOTIFICATION_SYSTEM);

                    notificationIntent.setAction(Constants.RECEIVER_NOTIFICATION);

                    notificationIntent.putExtra(Constant.intent_message_data,pushMsg);

                    NotificationUtils.showNotification(context, message.getPushTitle(), message.getPushContent(), notificationIntent);
                }
            }
        }
    }
}
