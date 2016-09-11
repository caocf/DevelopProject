package com.moge.gege.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.data.dao.PushMsgDAO;
import com.moge.gege.model.FriendMsgModel;
import com.moge.gege.model.MessageModel;
import com.moge.gege.model.NewMessageModel;
import com.moge.gege.model.NoticeModel;
import com.moge.gege.model.NotifyModel;
import com.moge.gege.model.RespFriendMsgListModel;
import com.moge.gege.model.RespMessageListModel;
import com.moge.gege.model.RespNoticeListModel;
import com.moge.gege.model.RespNotifyListModel;
import com.moge.gege.model.RespUserModel;
import com.moge.gege.model.UserModel;
import com.moge.gege.model.enums.FriendOperateResult;
import com.moge.gege.model.enums.MessageContentType;
import com.moge.gege.model.enums.MessageType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.ChatListRequest;
import com.moge.gege.network.request.FriendMsgListRequest;
import com.moge.gege.network.request.NoticeListRequest;
import com.moge.gege.network.request.NotifyListRequest;
import com.moge.gege.network.request.UserInfoRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.network.util.WebSocketManager;
import com.moge.gege.service.AppService;
import com.moge.gege.ui.ChatActivity;
import com.moge.gege.ui.ContactsActivity;
import com.moge.gege.ui.HomeActivity;
import com.moge.gege.ui.MessageNotifyActivity;
import com.moge.gege.ui.adapter.MessageListAdapter;
import com.moge.gege.ui.adapter.MessageListAdapter.MessageListListener;
import com.moge.gege.ui.event.Event;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.widget.CustomDialog;

public class MessageFragment extends BaseFragment implements
        MessageListListener
{
    private Context mContext;
    private PullToRefreshListView mRefreshListView;
    private ListView mListView;
    private MessageListAdapter mAdapter;
    private static final String FRIEND_MSG_TAG = "unread_friend";

    private List<NewMessageModel> mMessageModelList;
    private NewMessageModel mCurNewMessageModel;
    private MessageComparator mComparator = new MessageComparator();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_message,
                container, false);
        initView(layout);
        initData();
        registerMsgBroadCast();
        return layout;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        initData();

        // remove online notify msg
        if (AppService.instance() != null)
        {
            AppService.instance().removeNotify(WebSocketManager.ONLINE_MSG_TAG,
                    0);
        }
    }

    @Override
    protected void initView(View v)
    {
        super.initView(v);

        this.setHeaderTitle(R.string.message);
        this.setHeaderRightImage(R.drawable.icon_friends);

        mContext = this.getActivity();
        mRefreshListView = (PullToRefreshListView) v
                .findViewById(R.id.messageList);
        mListView = mRefreshListView.getRefreshableView();
        mAdapter = new MessageListAdapter(mContext);
        mAdapter.setListener(this);
        mAdapter.setDataSource(mMessageModelList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);
        mListView.setOnItemLongClickListener(mAdapter);

        mRefreshListView.setRefreshing();
        mRefreshListView
                .setOnRefreshListener(new OnRefreshListener2<ListView>()
                {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                        initData();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                    }

                });
    }

    @Override
    protected void onHeaderDoubleClick()
    {
        mRefreshListView.getRefreshableView().smoothScrollToPosition(0);
    }

    @Override
    protected void onHeaderRightClick(View v)
    {
        startActivity(new Intent(mContext, ContactsActivity.class));
    }

    private void initData()
    {
        // init default message list
        PushMsgDAO.instance().initLocalMessageList(mContext,
                AppApplication.getLoginId());

        mMessageModelList = PushMsgDAO.instance().getMessageList(
                AppApplication.getLoginId());
        Collections.sort(mMessageModelList, mComparator);

        pullOnlineNewMessage(mMessageModelList);
        mAdapter.setDataSource(mMessageModelList);
        mAdapter.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                mRefreshListView.onRefreshComplete();
            }
        }, 1000);
    }

    private void registerMsgBroadCast()
    {
        IntentFilter exitFilter = new IntentFilter();
        exitFilter.addAction(GlobalConfig.BROADCAST_ACTION_PUSHMSG);
        mContext.registerReceiver(mPushMsgReceiver, exitFilter);
    }

    private BroadcastReceiver mPushMsgReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals(GlobalConfig.BROADCAST_ACTION_PUSHMSG))
            {
                NewMessageModel msgModel = (NewMessageModel) intent
                        .getSerializableExtra("data");
                if (msgModel != null)
                {
                    pullNewMessage(msgModel);
                }
                else
                {
                    ArrayList<NewMessageModel> msgList = (ArrayList<NewMessageModel>) intent
                            .getSerializableExtra("datalist");
                    pullOnlineNewMessage(msgList);
                }

            }
        }
    };

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (mContext != null)
        {
            mContext.unregisterReceiver(mPushMsgReceiver);
        }
    }

    @Override
    public void onMessageItemClick(NewMessageModel model)
    {
        if (TextUtils.isEmpty(model.getTag()))
        {
            return;
        }

        if (model.getMsgType() == MessageType.MSG_CHAT)
        {
            Intent intent = new Intent(mContext, ChatActivity.class);
            intent.putExtra("uid", model.getTag());
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(mContext, MessageNotifyActivity.class);
            intent.putExtra("msg_type", model.getMsgType());
            intent.putExtra("msg_params", model.getTag());
            intent.putExtra("title", model.getTitle());
            startActivity(intent);
        }

        model.setHaveRead(true);
        model.setCount(0);
        PushMsgDAO.instance().readPushMsg(AppApplication.getLoginId(),
                model.getTag());
        mAdapter.notifyDataSetChanged();
    }

    private void pullOnlineNewMessage(List<NewMessageModel> msgList)
    {
        if (msgList == null || msgList.size() == 0)
        {
            return;
        }

        for (NewMessageModel msgModel : msgList)
        {
            switch (msgModel.getMsgType())
            {
                case MessageType.MSG_CHAT:
                    if (TextUtils.isEmpty(msgModel.getTitle())
                            || TextUtils.isEmpty(msgModel.getContent()))
                    {
                        doUserInfoRequest(msgModel.getTag());
                        doChatListRequest(msgModel.getTag(), "");
                    }
                    break;
                case MessageType.MSG_NOTICE:
                    if (TextUtils.isEmpty(msgModel.getContent()))
                    {
                        doNoticeListRequest(msgModel.getTag(), "");
                    }
                    break;
                case MessageType.MSG_NOTIFY:
                    if (TextUtils.isEmpty(msgModel.getContent()))
                    {
                        doNotifyListRequest(msgModel.getTag(), "");
                    }
                    break;
                case MessageType.MSG_FRIEND:
                    if (TextUtils.isEmpty(msgModel.getContent()))
                    {
                        doFriendMsgListRequest("");
                    }
                    break;
            }
        }

    }

    private void pullNewMessage(NewMessageModel msgModel)
    {
        NewMessageModel existMsgModel = findMessageByTag(msgModel.getTag());
        if (existMsgModel != null)
        {
            existMsgModel.setHaveRead(false);
            existMsgModel.setTitle(msgModel.getTitle());
            existMsgModel.setContent(msgModel.getContent());
            existMsgModel.setCount(existMsgModel.getCount() + 1);
            existMsgModel.setTime(msgModel.getTime());
            existMsgModel.setAvatar(msgModel.getAvatar());
        }
        else
        {
            mMessageModelList.add(msgModel);
            Collections.sort(mMessageModelList, mComparator);
        }

        mAdapter.notifyDataSetChanged();

        switch (msgModel.getMsgType())
        {
            case MessageType.MSG_CHAT:
                doUserInfoRequest(msgModel.getTag());
                break;
            case MessageType.MSG_FRIEND:
                doFriendMsgListRequest("");
                break;
        }

    }

    private NewMessageModel findMessageByTag(String tag)
    {
        for (NewMessageModel model : mMessageModelList)
        {
            if (model.getTag().equals(tag))
            {
                return model;
            }
        }

        return null;
    }

    private void doUserInfoRequest(String uid)
    {
        UserInfoRequest request = new UserInfoRequest(uid,
                new ResponseEventHandler<RespUserModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespUserModel> request,
                            RespUserModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            UserModel model = result.getData();
                            if (model != null)
                            {
                                NewMessageModel newMsgModel = findMessageByTag(model
                                        .get_id());
                                if (newMsgModel != null)
                                {
                                    newMsgModel.setAvatar(model.getAvatar());
                                    newMsgModel.setTitle(model.getNickname());
                                    PushMsgDAO.instance().updatePushMsg(
                                            AppApplication.getLoginId(),
                                            newMsgModel);

                                    // refresh ui
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doChatListRequest(String uid, String msgId)
    {
        ChatListRequest request = new ChatListRequest(true, uid, msgId,
                new ResponseEventHandler<RespMessageListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespMessageListModel> request,
                            RespMessageListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            if (result.getData() != null
                                    && result.getData().getMsgs().size() > 0)
                            {
                                MessageModel msgModel = result.getData()
                                        .getMsgs().get(0);

                                NewMessageModel newMsgModel = findMessageByTag(

                                msgModel.getFrom_uid());
                                if (newMsgModel != null)
                                {
                                    if (msgModel.getMsg_type() == MessageContentType.MSG_TEXT)
                                    {
                                        newMsgModel.setContent(msgModel
                                                .getContent());
                                    }
                                    else
                                    {
                                        newMsgModel
                                                .setContent(getString(R.string.image_text));
                                    }
                                    newMsgModel.setTime((long) msgModel
                                            .getCrts());

                                    PushMsgDAO.instance().updatePushMsg(
                                            AppApplication.getLoginId(),
                                            newMsgModel);

                                    // refresh ui
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doNoticeListRequest(String noticeType, String msgId)
    {
        NoticeListRequest request = new NoticeListRequest(true, noticeType,
                msgId, new ResponseEventHandler<RespNoticeListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespNoticeListModel> request,
                            RespNoticeListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            if (result.getData() != null
                                    && result.getData().getMsgs().size() > 0)
                            {
                                NoticeModel noticeModel = result.getData()
                                        .getMsgs().get(0);

                                NewMessageModel newMsgModel = findMessageByTag(noticeModel
                                        .getNotice_type());
                                if (newMsgModel != null)
                                {
                                    newMsgModel.setTitle(noticeModel
                                            .getNotice_name());
                                    newMsgModel.setContent(noticeModel
                                            .getTitle());
                                    newMsgModel.setTime((long) noticeModel
                                            .getCrts());

                                    PushMsgDAO.instance().updatePushMsg(
                                            AppApplication.getLoginId(),
                                            newMsgModel);

                                    // refresh ui
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private void doNotifyListRequest(String notifyType, String msgId)
    {
        NotifyListRequest request = new NotifyListRequest(true, notifyType,
                msgId, new ResponseEventHandler<RespNotifyListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespNotifyListModel> request,
                            RespNotifyListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            if (result.getData() != null
                                    && result.getData().getMsgs().size() > 0)
                            {
                                NotifyModel notifyModel = result.getData()
                                        .getMsgs().get(0);

                                NewMessageModel newMsgModel = findMessageByTag(notifyModel
                                        .getNotify_type());
                                if (newMsgModel != null)
                                {
                                    newMsgModel.setTitle(notifyModel
                                            .getNotify_name());
                                    newMsgModel.setContent(notifyModel
                                            .getTitle());
                                    newMsgModel.setTime((long) notifyModel
                                            .getCrts());

                                    PushMsgDAO.instance().updatePushMsg(
                                            AppApplication.getLoginId(),
                                            newMsgModel);

                                    // refresh ui
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }
                });
        executeRequest(request);
    }

    private void doFriendMsgListRequest(String msgId)
    {
        FriendMsgListRequest request = new FriendMsgListRequest(false, msgId,
                new ResponseEventHandler<RespFriendMsgListModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespFriendMsgListModel> request,
                            RespFriendMsgListModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            if (result.getData() != null
                                    && result.getData().getMsgs().size() > 0)
                            {
                                FriendMsgModel friendMsgModel = result
                                        .getData().getMsgs().get(0);

                                NewMessageModel newMsgModel = findMessageByTag(FRIEND_MSG_TAG);
                                if (newMsgModel != null)
                                {
                                    if (friendMsgModel.getFriend() != null)
                                    {
                                        newMsgModel.setContent(getFriendDetailMsg(
                                                friendMsgModel.getAction(),
                                                friendMsgModel.getFriend()
                                                        .getNickname()));
                                    }
                                    newMsgModel.setTime(friendMsgModel
                                            .getCrts());

                                    // update database
                                    PushMsgDAO.instance().updatePushMsg(
                                            AppApplication.getLoginId(),
                                            newMsgModel);

                                    // refresh ui
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                    }

                });
        executeRequest(request);
    }

    private String getFriendDetailMsg(String action, String nickname)
    {
        if (action.equals(FriendOperateResult.APPLY_FRIEND))
        {
            return getString(R.string.apply_friend, nickname);
        }
        else if (action.equals(FriendOperateResult.ACCEPT_FRIEND))
        {
            return getString(R.string.accept_friend, nickname);
        }
        else
        {
            return getString(R.string.refuse_friend, nickname);
        }
    }

    @Override
    public void onMessageItemLongClick(NewMessageModel model)
    {
        mCurNewMessageModel = model;
        new CustomDialog.Builder(mContext)
                .setTitle(R.string.general_tips)
                .setMessage(
                        getString(R.string.delete_message_confirm,
                                model.getTitle()))
                .setPositiveButton(R.string.general_confirm,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                mMessageModelList.remove(mCurNewMessageModel);
                                mAdapter.notifyDataSetChanged();

                                PushMsgDAO.instance().deletePushMsg(
                                        AppApplication.getLoginId(),
                                        mCurNewMessageModel.getTag());
                                dialog.dismiss();
                            }

                        })
                .setNegativeButton(R.string.general_cancel,
                        new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                dialog.dismiss();
                            }

                        }).create().show();
    }

    public class MessageComparator implements Comparator<NewMessageModel>
    {
        @Override
        public int compare(NewMessageModel m1, NewMessageModel m2)
        {
            return m1.getTime() > m2.getTime() ? 1 : 0;
        }
    }

    public void onEvent(Event.RefreshEvent event)
    {
        if(event.getRefreshIndex() != HomeActivity.INDEX_MSG)
        {
            return;
        }

        initData();
        onHeaderDoubleClick();
    }

}
