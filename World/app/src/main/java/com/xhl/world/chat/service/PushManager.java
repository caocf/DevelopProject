package com.xhl.world.chat.service;

import android.content.Context;
import android.text.TextUtils;

import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.PushService;
import com.google.gson.Gson;
import com.xhl.sum.chatlibrary.model.LeanchatUser;
import com.xhl.world.AppApplication;
import com.xhl.world.chat.PushMessage;
import com.xhl.world.ui.main.MainActivity;
import com.xhl.xhl_library.utils.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzw on 15/6/11.
 */
public class PushManager {
    private static PushManager pushManager;
    private Context context;

    public synchronized static PushManager getInstance() {
        if (pushManager == null) {
            pushManager = new PushManager();
        }
        return pushManager;
    }

    public void init(Context context) {
        this.context = context;
        PushService.setDefaultPushCallback(context, MainActivity.class);
        subscribeCurrentUserChannel();
    }

    //订阅个人频道
    public void subscribeCurrentUserChannel() {
        //推送的用户id，用作个人推送
        String currentUserId = AppApplication.appContext.getLoginUserInfo().userId;
        if (!TextUtils.isEmpty(currentUserId)) {
            PushService.subscribe(context, currentUserId, MainActivity.class);
        }
    }

    public void unsubscribeCurrentUserChannel() {
        String currentUserId = LeanchatUser.getCurrentUserId();
        if (!TextUtils.isEmpty(currentUserId)) {
            PushService.unsubscribe(context, currentUserId);
        }
    }

    //测试发送消息
    public void sendTestData() {

        JSONObject object = new JSONObject();

        try {
            String data = "";
            PushMessage message = new PushMessage();
            message.setPushContent("Content");
            message.setPushTitle("Title");
            message.setPushType("2");
            message.setPushAttr("103c462f78cc45a89eb95804bf3a4bd5");

            data = new Gson().toJson(message);

            object.put("action", AppReceiver.PUSH_ACTION);
            object.put(AppReceiver.PUSH_DATA, data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AVPush push = new AVPush();
        push.setData(object);
        Logger.e("push data:" + object.toString());
        push.setPushToAndroid(true);
        push.sendInBackground();

    }

}