package com.moge.gege.network.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.config.NetworkConfig;
import com.moge.gege.data.PersistentData;
import com.moge.gege.data.dao.PushMsgDAO;
import com.moge.gege.model.BasePushModel;
import com.moge.gege.model.ChatSendModel;
import com.moge.gege.model.DeviceEntityModel;
import com.moge.gege.model.DeviceModel;
import com.moge.gege.model.FriendMsgModel;
import com.moge.gege.model.MessageModel;
import com.moge.gege.model.NewMessageModel;
import com.moge.gege.model.NoticeModel;
import com.moge.gege.model.NotifyModel;
import com.moge.gege.model.OnlineModel;
import com.moge.gege.model.PushChatModel;
import com.moge.gege.model.PushChatSendResultModel;
import com.moge.gege.model.PushFriendMsgModel;
import com.moge.gege.model.PushNoticeModel;
import com.moge.gege.model.PushNotifyModel;
import com.moge.gege.model.PushOnlineModel;
import com.moge.gege.model.ReadMessageModel;
import com.moge.gege.model.ReqChatModel;
import com.moge.gege.model.ReqOnlineModel;
import com.moge.gege.model.ReqReadMessageModel;
import com.moge.gege.model.UnReadChatModel;
import com.moge.gege.model.UnReadNoticeModel;
import com.moge.gege.model.UnReadNotifyModel;
import com.moge.gege.model.enums.MessageContentType;
import com.moge.gege.model.enums.MessageType;
import com.moge.gege.network.util.websocket.WebSocket;
import com.moge.gege.network.util.websocket.WebSocketConnection;
import com.moge.gege.network.util.websocket.WebSocketException;
import com.moge.gege.network.util.websocket.WebSocketOptions;
import com.moge.gege.service.AppService;
import com.moge.gege.ui.BaseActivity;
import com.moge.gege.ui.ChatActivity;
import com.moge.gege.ui.HomeActivity;
import com.moge.gege.ui.MessageNotifyActivity;
import com.moge.gege.util.DeviceInfoUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.PackageUtil;

public class WebSocketManager
{
    private static final String msgVersion = "1.0";
    private static final String onlineAction = "online";
    private static final String chatSendAction = "chat_send";
    private static final String chatReceiveAction = "chat_receive";
    private static final String noticeAction = "notice";
    private static final String notifyAction = "notify";
    private static final String friendAction = "friend";
    public static final String FRIEND_MSG_TAG = "unread_friend";
    public static final String ONLINE_MSG_TAG = "all_online";
    public static final String NOTICE_MSG_TAG = "topic_post";
    public static final String NOTIFY_MSG_TAG = "default";

    // 未读消息数，chat结果，收到的chat消息，其他notice，notify，friend消息

    private Context mContext;
    private static WebSocketManager instance;
    private boolean mConnected = false;
    private final Gson mGson = new Gson();

    private String mCurrentChatUid = "";
    private BaseActivity mCurrentChatActivity;
    private List<MessageModel> mCurrentMsgList = new ArrayList<MessageModel>();

    private PushOnlineModel mPushOnLineModel;

    private WebSocketConnection mConnection = new WebSocketConnection();
    private WebSocket.ConnectionHandler mHandler = new WebSocket.ConnectionHandler()
    {
        @Override
        public void onOpen()
        {
            mConnected = true;
            onlineRequest();
        }

        @Override
        public void onTextMessage(String payload)
        {
            LogUtil.d("recv msg : " + payload);

            BasePushModel baseModel = mGson.fromJson(payload,
                    BasePushModel.class);
            if (baseModel.getAction().equals(onlineAction))
            {
                pushNewMessage(mGson.fromJson(payload, PushOnlineModel.class));
            }
            else if (baseModel.getAction().equals(noticeAction))
            {
                pushNewMessage(mGson.fromJson(payload, PushNoticeModel.class));
            }
            else if (baseModel.getAction().equals(notifyAction))
            {
                pushNewMessage(mGson.fromJson(payload, PushNotifyModel.class));
            }
            else if (baseModel.getAction().equals(friendAction))
            {
                pushNewMessage(mGson
                        .fromJson(payload, PushFriendMsgModel.class));
            }
            else if (baseModel.getAction().equals(chatReceiveAction))
            {
                PushChatModel chatRecvModel = mGson.fromJson(payload,
                        PushChatModel.class);

                if (TextUtils.isEmpty(mCurrentChatUid)
                        || !chatRecvModel.getData().getFrom_uid()
                                .equals(mCurrentChatUid))
                {
                    pushNewMessage(chatRecvModel);
                }
                else
                {
                    // store temp data
                    mCurrentMsgList.add(chatRecvModel.getData());

                    // notify
                    if (mCurrentChatActivity != null)
                    {
                        // read message
                        sendReceiveMessage(chatRecvModel.getData()
                                .getFrom_uid(), chatRecvModel.getData()
                                .getMsg_id());

                        mCurrentChatActivity.runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mCurrentChatActivity
                                        .onRecvChatMsg(mCurrentMsgList);
                                mCurrentMsgList.clear();
                            }
                        });
                    }
                }
            }
            else if (baseModel.getAction().equals(chatSendAction))
            {

                final PushChatSendResultModel chatSendResultModel = mGson
                        .fromJson(payload, PushChatSendResultModel.class);

                if (mCurrentChatActivity != null)
                {
                    mCurrentChatActivity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mCurrentChatActivity
                                    .onRecvChatResultMsg(chatSendResultModel);
                        }
                    });
                }
            }
        }

        @Override
        public void onClose(int code, String reason)
        {
            mConnected = false;
        }

        @Override
        public void onRawTextMessage(byte[] payload)
        {

        }

        @Override
        public void onBinaryMessage(byte[] payload)
        {
        }
    };

    public static WebSocketManager instance()
    {
        if (instance == null)
        {
            instance = new WebSocketManager();
        }

        return instance;
    }

    public void init(Context context)
    {
        mContext = context;
        connect();
    }

    public void reconnect()
    {
        LogUtil.d("reconnect msg server");

        List<BasicNameValuePair> headers = new ArrayList<BasicNameValuePair>();
        headers.add(new BasicNameValuePair("Cookie", PersistentData.instance()
                .getCookie()));

        mConnection.reconnect(headers);
    }

    public void close()
    {
        if (mConnection != null)
        {
            mConnection.disconnect();
        }
    }

    public boolean isConnected()
    {
        return mConnected;
    }

    private void connect()
    {
        LogUtil.d("connect msg server");

        try
        {
            // set cookie
            List<BasicNameValuePair> headers = new ArrayList<BasicNameValuePair>();
            headers.add(new BasicNameValuePair("Cookie", PersistentData
                    .instance().getCookie()));

            WebSocketOptions options = new WebSocketOptions();
            options.setReconnectInterval(500);

            mConnection.connect(NetworkConfig.chatWebsocketAddress
                    + NetworkConfig.chatWebsocketAddressSuffix, null, mHandler,
                    options, headers);
        }
        catch (WebSocketException e)
        {
            mConnected = false;
            LogUtil.e(e.getMessage());
        }
    }

    private void onlineRequest()
    {
        if (!mConnected)
        {
            return;
        }

        ReqOnlineModel reqOnlineModel = new ReqOnlineModel();
        reqOnlineModel.setAction(onlineAction);
        reqOnlineModel.setVersion(msgVersion);
        DeviceEntityModel deviceEntityModel = new DeviceEntityModel();
        DeviceModel deviceModel = new DeviceModel();
        deviceModel.setUdid(DeviceInfoUtil.getDeviceId(mContext));
        deviceModel.setAppver(PackageUtil.getVersionName(mContext));
        deviceModel.setOs("android");
        deviceModel.setPhonemodel(DeviceInfoUtil.getDeviceName(mContext));
        deviceModel.setNetwork(DeviceInfoUtil.getNetType(mContext));
        deviceModel.setApp_name("gege");
        deviceModel.setOsver(DeviceInfoUtil.getSysVersion(mContext));
        deviceEntityModel.setDevice(deviceModel);
        reqOnlineModel.setData(deviceEntityModel);

        Gson gson = new Gson();
        String body = gson.toJson(reqOnlineModel);
        mConnection.sendTextMessage(body);
    }

    public boolean sendChatMessage(String toUid, String content, int msgType,
            int reqId)
    {
        if (!mConnected)
        {
            return false;
        }

        ReqChatModel reqChatModel = new ReqChatModel();
        reqChatModel.setAction(chatSendAction);
        reqChatModel.setVersion(msgVersion);
        ChatSendModel chatSendModel = new ChatSendModel();
        chatSendModel.setTo_uid(toUid);
        chatSendModel.setContent(content);
        chatSendModel.setMsg_type(msgType);
        chatSendModel.setReq_id(reqId);
        reqChatModel.setData(chatSendModel);

        Gson gson = new Gson();
        mConnection.sendTextMessage(gson.toJson(reqChatModel));
        return true;
    }

    public void sendReceiveMessage(String fromUid, String msgId)
    {
        if (!mConnected)
        {
            return;
        }
        ReqReadMessageModel reqReadMsgModel = new ReqReadMessageModel();
        reqReadMsgModel.setAction(chatReceiveAction);
        reqReadMsgModel.setVersion(msgVersion);
        ReadMessageModel readMsgModel = new ReadMessageModel();
        readMsgModel.setFrom_uid(fromUid);
        readMsgModel.setMsg_id(msgId);
        reqReadMsgModel.setData(readMsgModel);

        Gson gson = new Gson();
        mConnection.sendTextMessage(gson.toJson(reqReadMsgModel));
    }

    public void registerChatListener(BaseActivity chatActivity, String toUid)
    {
        mCurrentChatActivity = chatActivity;
        mCurrentChatUid = toUid;
    }

    public void unRegisterChatListener()
    {
        mCurrentChatActivity = null;
        mCurrentChatUid = null;
    }

    private void notifyDataToMainThread(NewMessageModel data)
    {
        Intent intent = new Intent();
        intent.setAction(GlobalConfig.BROADCAST_ACTION_PUSHMSG);
        intent.putExtra("data", data);
        mContext.sendBroadcast(intent);
    }

    private void notifyDataToMainThread(ArrayList<NewMessageModel> data)
    {
        Intent intent = new Intent();
        intent.setAction(GlobalConfig.BROADCAST_ACTION_PUSHMSG);
        intent.putExtra("datalist", data);
        mContext.sendBroadcast(intent);
    }

    public PushOnlineModel getPushOnlineData()
    {
        return mPushOnLineModel;
    }

    private void pushNewMessage(PushOnlineModel model)
    {
        if (model == null)
        {
            return;
        }

        OnlineModel onlineModel = model.getData();
        if (onlineModel == null)
        {
            return;
        }

        ArrayList<NewMessageModel> newMsgList = new ArrayList<NewMessageModel>();
        int totalCount = 0;

        List<UnReadChatModel> unreadChatList = onlineModel.getUnread_chat();
        if (unreadChatList != null && unreadChatList.size() > 0)
        {
            for (UnReadChatModel chatModel : unreadChatList)
            {
                NewMessageModel msgModel = new NewMessageModel();
                msgModel.setMsgType(MessageType.MSG_CHAT);
                msgModel.setTag(chatModel.getFrom_uid());
                msgModel.setCount(chatModel.getCount());
                totalCount += chatModel.getCount();
                newMsgList.add(msgModel);
            }
        }

        List<UnReadNoticeModel> unreadNoticeList = onlineModel
                .getUnread_notice();
        if (unreadNoticeList != null && unreadNoticeList.size() > 0)
        {
            for (UnReadNoticeModel noticeModel : unreadNoticeList)
            {
                NewMessageModel msgModel = new NewMessageModel();
                msgModel.setMsgType(MessageType.MSG_NOTICE);
                msgModel.setTag(noticeModel.getNotice_type());
                msgModel.setAvatar(noticeModel.getNotice_logo());
                msgModel.setCount(noticeModel.getCount());
                totalCount += noticeModel.getCount();
                newMsgList.add(msgModel);
            }
        }

        List<UnReadNotifyModel> unreadNotifyList = onlineModel
                .getUnread_notify();
        if (unreadNotifyList != null && unreadNotifyList.size() > 0)
        {
            for (UnReadNotifyModel notifyModel : unreadNotifyList)
            {
                NewMessageModel msgModel = new NewMessageModel();
                msgModel.setMsgType(MessageType.MSG_NOTIFY);
                msgModel.setTag(notifyModel.getNotify_type());
                msgModel.setAvatar(notifyModel.getNotify_logo());
                msgModel.setCount(notifyModel.getCount());
                totalCount += notifyModel.getCount();
                newMsgList.add(msgModel);
            }
        }

        int friendMsgCount = onlineModel.getUnread_friend();
        if (friendMsgCount > 0)
        {
            NewMessageModel msgModel = new NewMessageModel();
            msgModel.setMsgType(MessageType.MSG_FRIEND);
            msgModel.setTag(FRIEND_MSG_TAG);
            msgModel.setAvatar("drawable://" + R.drawable.icon_friend_relate);
            msgModel.setCount(friendMsgCount);
            msgModel.setTitle(mContext.getResources().getString(
                    R.string.friend_notify));
            totalCount += friendMsgCount;
            newMsgList.add(msgModel);
        }

        PushMsgDAO.instance().insertOnlineNewPushMsg(
                AppApplication.getLoginId(), newMsgList);

        // 本地通知（totalCount条未读消息）
        if (totalCount > 0)
        {
            Intent msgIntent = new Intent(mContext, HomeActivity.class);
            msgIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            msgIntent.putExtra("open_msg", true);
            AppService.instance().showNotity(
                    ONLINE_MSG_TAG,
                    0,
                    mContext.getResources().getString(
                            R.string.general_notify_title),
                    mContext.getResources().getString(
                            R.string.total_unread_msg, totalCount), msgIntent);
        }

        notifyDataToMainThread(newMsgList);
    }

    private void pushNewMessage(PushChatModel chatModel)
    {
        MessageModel model = chatModel.getData();
        NewMessageModel msgModel = new NewMessageModel();
        msgModel.setMsgType(MessageType.MSG_CHAT);
        msgModel.setTag(model.getFrom_uid());
        msgModel.setCount(1);
        if (model.getMsg_type() == MessageContentType.MSG_TEXT)
        {
            msgModel.setContent(model.getContent());
        }
        else
        {
            msgModel.setContent(mContext.getResources().getString(
                    R.string.image_text));
        }
        msgModel.setTime((long) model.getCrts());

        PushMsgDAO.instance().insertNewPushMsg(AppApplication.getLoginId(),
                msgModel);

        // 本地通知
        notifyNewMessage(msgModel);

        notifyDataToMainThread(msgModel);
    }

    private void pushNewMessage(PushNoticeModel noticeModel)
    {
        NoticeModel model = noticeModel.getData();
        NewMessageModel msgModel = new NewMessageModel();
        msgModel.setMsgType(MessageType.MSG_NOTICE);
        msgModel.setTag(model.getNotice_type());
        msgModel.setAvatar(model.getNotice_logo());
        msgModel.setCount(1);
        msgModel.setTitle(model.getNotice_name());
        msgModel.setContent(model.getContent());
        msgModel.setTime(model.getCrts());

        PushMsgDAO.instance().insertNewPushMsg(AppApplication.getLoginId(),
                msgModel);

        // 本地通知
        notifyNewMessage(msgModel);

        notifyDataToMainThread(msgModel);
    }

    private void pushNewMessage(PushNotifyModel notifyModel)
    {
        NotifyModel model = notifyModel.getData();
        NewMessageModel msgModel = new NewMessageModel();
        msgModel.setMsgType(MessageType.MSG_NOTIFY);
        msgModel.setTag(model.getNotify_type());
        msgModel.setAvatar(model.getNotify_logo());
        msgModel.setCount(1);
        msgModel.setTitle(model.getNotify_name());
        msgModel.setContent(model.getContent());
        msgModel.setTime(model.getCrts());

        PushMsgDAO.instance().insertNewPushMsg(AppApplication.getLoginId(),
                msgModel);

        // 本地通知
        notifyNewMessage(msgModel);

        notifyDataToMainThread(msgModel);
    }

    private void pushNewMessage(PushFriendMsgModel friendMsgModel)
    {
        FriendMsgModel model = friendMsgModel.getData();
        NewMessageModel msgModel = new NewMessageModel();
        msgModel.setMsgType(MessageType.MSG_FRIEND);
        msgModel.setTag(FRIEND_MSG_TAG);
        msgModel.setAvatar("drawable://" + R.drawable.icon_friend_relate);
        msgModel.setCount(1);
        msgModel.setTime(model.getCrts());
        msgModel.setTitle(mContext.getResources().getString(
                R.string.friend_notify));

        PushMsgDAO.instance().insertNewPushMsg(AppApplication.getLoginId(),
                msgModel);

        // 本地通知
        notifyNewMessage(msgModel);

        notifyDataToMainThread(msgModel);
    }

    private void notifyNewMessage(NewMessageModel model)
    {
        if (!PushMsgDAO.instance().isNotify(AppApplication.getLoginId(),
                model.getTag()))
        {
            return;
        }

        Intent intent;
        String title;

        if (model.getMsgType() == MessageType.MSG_CHAT)
        {
            intent = new Intent(mContext, ChatActivity.class);
            intent.putExtra("uid", model.getTag());
            title = mContext.getResources()
                    .getString(R.string.friend_msg_title);
        }
        else
        {
            intent = new Intent(mContext, MessageNotifyActivity.class);
            intent.putExtra("msg_type", model.getMsgType());
            intent.putExtra("msg_params", model.getTag());
            intent.putExtra("title", model.getTitle());

            title = model.getTitle();
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        AppService.instance().showNotity(model.getTag(), 0, title,
                model.getContent(), intent);
    }
}
