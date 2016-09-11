package com.moge.gege.data.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.moge.gege.R;
import com.moge.gege.data.DBConstants;
import com.moge.gege.model.NewMessageModel;
import com.moge.gege.model.enums.MessageType;
import com.moge.gege.network.util.WebSocketManager;
import com.moge.gege.util.LogUtil;

public class PushMsgDAO extends BaseDAO
{

    public static final String TAG = PushMsgDAO.class.getName();

    private static PushMsgDAO instance = null;

    public static PushMsgDAO instance()
    {
        if (instance == null)
        {
            instance = new PushMsgDAO();
        }
        return instance;
    }

    public boolean insertOnlineNewPushMsg(String uid, NewMessageModel model)
    {
        NewMessageModel existModel = getPushMsg(uid, model.getTag());
        if (existModel == null)
        {
            this.insertPushMsg(uid, model);
        }
        else
        {
            // update unread flag and unread count
            this.updatePushMsg(uid, model);
        }

        return true;
    }

    public void insertOnlineNewPushMsg(String uid, List<NewMessageModel> list)
    {
        if (TextUtils.isEmpty(uid))
        {
            uid = "0";
        }

        mDb.beginTransaction();
        for (NewMessageModel msg : list)
        {
            insertOnlineNewPushMsg(uid, msg);
        }
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    public boolean insertNewPushMsg(String uid, NewMessageModel model)
    {
        NewMessageModel existModel = getPushMsg(uid, model.getTag());
        if (existModel == null)
        {
            this.insertPushMsg(uid, model);
        }
        else
        {
            model.setCount(existModel.getCount() + model.getCount());
            this.updatePushMsg(uid, model);
        }

        return true;
    }

    private boolean insertPushMsg(String uid, NewMessageModel model)
    {
        if (TextUtils.isEmpty(uid))
        {
            uid = "0";
        }

        ContentValues values = new ContentValues();
        values.put(DBConstants.PushMsg.UID, uid);
        values.put(DBConstants.PushMsg.TAG, model.getTag());
        values.put(DBConstants.PushMsg.MSGTYPE, model.getMsgType());
        values.put(DBConstants.PushMsg.HAVEREAD, model.isHaveRead() ? 1 : 0);
        values.put(DBConstants.PushMsg.AVATAR, model.getAvatar());
        values.put(DBConstants.PushMsg.COUNT, model.getCount());
        values.put(DBConstants.PushMsg.TITLE, model.getTitle());
        values.put(DBConstants.PushMsg.CONTENT, model.getContent());
        values.put(DBConstants.PushMsg.TIME, model.getTime());
        values.put(DBConstants.PushMsg.IS_NOTIFY, model.isNotify());

        long rowId = mDb.insert(DBConstants.Table_Names.PUSHMSG, null, values);
        String debug = String.format("插入消息：uid=%s,", uid)
                + String.format("TAG=%s,", model.getTag())
                + String.format("MSGTYPE=%d,", model.getMsgType());
        LogUtil.d(TAG, debug);

        return rowId != -1 ? true : false;
    }

    public void insertPushMsg(String uid, List<NewMessageModel> list)
    {
        if (TextUtils.isEmpty(uid))
        {
            uid = "0";
        }

        mDb.beginTransaction();
        for (NewMessageModel msg : list)
        {
            insertPushMsg(uid, msg);
        }
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    public boolean readPushMsg(String uid, String tag)
    {
        if (TextUtils.isEmpty(uid))
        {
            uid = "0";
        }

        ContentValues values = new ContentValues();
        values.put(DBConstants.PushMsg.HAVEREAD, 1);
        values.put(DBConstants.PushMsg.COUNT, 0);
        return mDb.update(DBConstants.Table_Names.PUSHMSG, values,
                DBConstants.PushMsg.UID + "=? and " + DBConstants.PushMsg.TAG
                        + "=?", new String[] { uid, tag }) > 0;
    }

    public boolean updatePushMsg(String uid, NewMessageModel model)
    {
        if (TextUtils.isEmpty(uid))
        {
            uid = "0";
        }

        ContentValues values = new ContentValues();
        values.put(DBConstants.PushMsg.HAVEREAD, model.isHaveRead() ? 1 : 0);
        values.put(DBConstants.PushMsg.AVATAR, model.getAvatar());
        values.put(DBConstants.PushMsg.COUNT, model.getCount());
        values.put(DBConstants.PushMsg.TITLE, model.getTitle());
        values.put(DBConstants.PushMsg.CONTENT, model.getContent());
        values.put(DBConstants.PushMsg.TIME, model.getTime());
        return mDb.update(DBConstants.Table_Names.PUSHMSG, values,
                DBConstants.PushMsg.UID + "=? and " + DBConstants.PushMsg.TAG
                        + "=?", new String[] { uid, model.getTag() }) > 0;
    }

    public boolean deletePushMsg(String uid, String tag)
    {
        if (TextUtils.isEmpty(uid))
        {
            uid = "0";
        }

        return mDb.delete(DBConstants.Table_Names.PUSHMSG,
                DBConstants.PushMsg.UID + "=? and " + DBConstants.PushMsg.TAG
                        + "=?", new String[] { uid, tag }) > 0;
    }

    public NewMessageModel getPushMsg(String uid, String tag)
    {
        if (TextUtils.isEmpty(uid))
        {
            uid = "0";
        }

        String[] columns = { DBConstants.PushMsg.TAG,
                DBConstants.PushMsg.MSGTYPE, DBConstants.PushMsg.HAVEREAD,
                DBConstants.PushMsg.AVATAR, DBConstants.PushMsg.COUNT,
                DBConstants.PushMsg.TITLE, DBConstants.PushMsg.CONTENT,
                DBConstants.PushMsg.TIME, DBConstants.PushMsg.IS_NOTIFY };

        String selection = new StringBuffer().append(DBConstants.PushMsg.UID)
                .append("=? and ").append(DBConstants.PushMsg.TAG).append("=?")
                .toString();

        String[] selectionArgs = { uid, tag };

        Cursor cursor = mDb.query(DBConstants.Table_Names.PUSHMSG, columns,
                selection, selectionArgs, null, null, null);

        NewMessageModel msg = null;
        while (cursor.moveToNext())
        {
            msg = new NewMessageModel();
            msg.setTag(cursor.getString(0));
            msg.setMsgType(cursor.getInt(1));
            msg.setHaveRead(cursor.getInt(2) == 1 ? true : false);
            msg.setAvatar(cursor.getString(3));
            msg.setCount(cursor.getInt(4));
            msg.setTitle(cursor.getString(5));
            msg.setContent(cursor.getString(6));
            msg.setTime(cursor.getInt(7));
            msg.setNotify(cursor.getInt(8) == 1 ? true : false);
        }
        cursor.close();
        return msg;
    }

    public List<NewMessageModel> getMessageList(String uid)
    {
        if (TextUtils.isEmpty(uid))
        {
            uid = "0";
        }

        List<NewMessageModel> msgList = new ArrayList<NewMessageModel>();
        String[] columns = { DBConstants.PushMsg.TAG,
                DBConstants.PushMsg.MSGTYPE, DBConstants.PushMsg.HAVEREAD,
                DBConstants.PushMsg.AVATAR, DBConstants.PushMsg.COUNT,
                DBConstants.PushMsg.TITLE, DBConstants.PushMsg.CONTENT,
                DBConstants.PushMsg.TIME, DBConstants.PushMsg.IS_NOTIFY };

        String selection = new StringBuffer().append(DBConstants.PushMsg.UID)
                .append("=?").toString();

        String[] selectionArgs = { uid };

        String orderBy = DBConstants.PushMsg.TIME + " desc";

        Cursor cursor = mDb.query(DBConstants.Table_Names.PUSHMSG, columns,
                selection, selectionArgs, null, null, orderBy);
        while (cursor.moveToNext())
        {
            NewMessageModel msg = new NewMessageModel();
            msg.setTag(cursor.getString(0));
            msg.setMsgType(cursor.getInt(1));
            msg.setHaveRead(cursor.getInt(2) == 1 ? true : false);
            msg.setAvatar(cursor.getString(3));
            msg.setCount(cursor.getInt(4));
            msg.setTitle(cursor.getString(5));
            msg.setContent(cursor.getString(6));
            msg.setTime(cursor.getInt(7));
            msg.setNotify(cursor.getInt(8) == 1 ? true : false);
            msgList.add(msg);
        }
        cursor.close();
        return msgList;
    }

    public boolean setNotifyValue(String uid, String tag, boolean isNotify)
    {
        if (TextUtils.isEmpty(uid))
        {
            uid = "0";
        }

        ContentValues values = new ContentValues();
        values.put(DBConstants.PushMsg.IS_NOTIFY, isNotify ? 1 : 0);
        return mDb.update(DBConstants.Table_Names.PUSHMSG, values,
                DBConstants.PushMsg.UID + "=? and " + DBConstants.PushMsg.TAG
                        + "=?", new String[] { uid, tag }) > 0;
    }

    public boolean isNotify(String uid, String tag)
    {
        boolean isToNotify = true;

        if (TextUtils.isEmpty(uid))
        {
            uid = "0";
        }

        String[] columns = { DBConstants.PushMsg.IS_NOTIFY };

        String selection = new StringBuffer().append(DBConstants.PushMsg.UID)
                .append("=? and ").append(DBConstants.PushMsg.TAG).append("=?")
                .toString();

        String[] selectionArgs = { uid, tag };

        Cursor cursor = mDb.query(DBConstants.Table_Names.PUSHMSG, columns,
                selection, selectionArgs, null, null, null);

        while (cursor.moveToNext())
        {
            isToNotify = cursor.getInt(0) == 1 ? true : false;
        }
        cursor.close();

        return isToNotify;
    }

    // init default message
    public void initLocalMessageList(Context context, String uid)
    {
        if (getPushMsg(uid, WebSocketManager.FRIEND_MSG_TAG) == null)
        {
            NewMessageModel friendModel = new NewMessageModel();
            friendModel.setTag(WebSocketManager.FRIEND_MSG_TAG);
            friendModel.setMsgType(MessageType.MSG_FRIEND);
            friendModel.setHaveRead(true);
            friendModel
                    .setAvatar("drawable://" + R.drawable.icon_friend_relate);
            friendModel.setCount(0);
            friendModel.setTitle(context.getResources().getString(
                    R.string.friend_notify));
//            friendModel.setContent(context.getResources().getString(R.string.no_news));
            friendModel.setTime(0);
            friendModel.setNotify(true);
            insertPushMsg(uid, friendModel);
        }

        if (getPushMsg(uid, WebSocketManager.NOTICE_MSG_TAG) == null)
        {
            NewMessageModel noticeModel = new NewMessageModel();
            noticeModel.setTag(WebSocketManager.NOTICE_MSG_TAG);
            noticeModel.setMsgType(MessageType.MSG_NOTICE);
            noticeModel.setHaveRead(true);
            noticeModel.setAvatar("drawable://" + R.drawable.icon_notice);
            noticeModel.setCount(0);
            noticeModel.setTitle(context.getResources().getString(
                    R.string.related_notify));
//            noticeModel.setContent(context.getResources().getString(R.string.no_news));
            noticeModel.setTime(0);
            noticeModel.setNotify(true);
            insertPushMsg(uid, noticeModel);
        }

        if (getPushMsg(uid, WebSocketManager.NOTIFY_MSG_TAG) == null)
        {
            NewMessageModel notifyModel = new NewMessageModel();
            notifyModel.setTag(WebSocketManager.NOTIFY_MSG_TAG);
            notifyModel.setMsgType(MessageType.MSG_NOTIFY);
            notifyModel.setHaveRead(true);
            notifyModel.setAvatar("drawable://" + R.drawable.icon_notify);
            notifyModel.setCount(0);
            notifyModel.setTitle(context.getResources().getString(
                    R.string.system_notify));
//            notifyModel.setContent(context.getResources().getString(R.string.no_news));
            notifyModel.setTime(0);
            notifyModel.setNotify(true);
            insertPushMsg(uid, notifyModel);
        }

    }
}
