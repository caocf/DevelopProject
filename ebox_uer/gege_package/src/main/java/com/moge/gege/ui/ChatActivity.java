package com.moge.gege.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moge.gege.AppApplication;
import com.moge.gege.R;
import com.moge.gege.data.PersistentData;
import com.moge.gege.data.dao.PushMsgDAO;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.ChatSendResultModel;
import com.moge.gege.model.IMMessageModel;
import com.moge.gege.model.MessageModel;
import com.moge.gege.model.NewMessageModel;
import com.moge.gege.model.PushChatSendResultModel;
import com.moge.gege.model.RespMessageListModel;
import com.moge.gege.model.RespUserModel;
import com.moge.gege.model.UserModel;
import com.moge.gege.model.enums.IMListType;
import com.moge.gege.model.enums.IMMessageStatusType;
import com.moge.gege.model.enums.MessageContentType;
import com.moge.gege.model.enums.MessageType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.ChatListRequest;
import com.moge.gege.network.request.ReadChatRequest;
import com.moge.gege.network.request.UserInfoRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.network.util.WebSocketManager;
import com.moge.gege.service.AppService;
import com.moge.gege.ui.adapter.ChatListAdapter;
import com.moge.gege.ui.adapter.ChatListAdapter.ChatListListener;
import com.moge.gege.ui.helper.UIHelper;
import com.moge.gege.ui.helper.UploadImageHelper;
import com.moge.gege.util.FileUtil;
import com.moge.gege.util.LogUtil;
import com.moge.gege.util.TimeUtil;
import com.moge.gege.util.ToastUtil;
import com.moge.gege.util.widget.chat.IMFooterView;
import com.moge.gege.util.widget.chat.IMFooterView.IMFooterViewListener;

public class ChatActivity extends BaseActivity implements ChatListListener,
        IMFooterViewListener, OnTouchListener
{
    private Context mContext;
    // private PullToRefreshListView mPullRefreshScrollView;
    // private ScrollView mScrollView;
    private PullToRefreshListView mRefreshListView;
    private ListView mListView;
    private ChatListAdapter mAdapter;
    private IMFooterView mFooterView;

    private String mLatestMsgId = "";
    private String mToUid = "";
    private UserModel mToUserModel;

    private List<IMMessageModel> mMessageModelList = new ArrayList<IMMessageModel>();
    private int mLastMessageCount = 0;
    private Integer mStartRequestId = 110;
    private Map<Integer, String> mRequestMap = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mToUid = getIntent().getStringExtra("uid");

        mContext = ChatActivity.this;
        initView();
        initData();
    }

    @Override
    protected void initView()
    {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);
        this.setHeaderRightImage(R.drawable.icon_setting);

        mFooterView = (IMFooterView) this.findViewById(R.id.footerView);
        mFooterView.setListner(this);

        mRefreshListView = (PullToRefreshListView) this
                .findViewById(R.id.chatListView);
        mListView = mRefreshListView.getRefreshableView();
        mAdapter = new ChatListAdapter(mContext);
        mAdapter.setDataSource(mMessageModelList);
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnTouchListener(this);

        mRefreshListView.setMode(Mode.PULL_FROM_START);
        mRefreshListView.setRefreshing();
        mRefreshListView
                .setOnRefreshListener(new OnRefreshListener2<ListView>()
                {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                        doChatListRequest(mToUid, mLatestMsgId);
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {

                    }

                });

    }

    @Override
    protected void onHeaderLeftClick()
    {
        finish();

        if (HomeActivity.instance == null)
        {
            UIHelper.showHomePageActivity(mContext);
        }
    }

    @Override
    protected void onHeaderRightClick()
    {
        Intent intent = new Intent(mContext, ChatSettingActivity.class);
        intent.putExtra("tag", mToUid);
        startActivity(intent);
    }

    private void initData()
    {
        // read local unread message
        PushMsgDAO.instance().readPushMsg(AppApplication.getLoginId(), mToUid);
        if (AppService.instance() != null)
        {
            AppService.instance().removeNotify(mToUid, 0);
        }

        doUserInfoRequest(mToUid);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString("uid", "mToUid");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        WebSocketManager.instance().unRegisterChatListener();
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
                            mToUserModel = result.getData();
                            setHeaderLeftTitle(mToUserModel.getNickname());
                            WebSocketManager.instance().registerChatListener(
                                    ChatActivity.this, mToUid);

                            doChatListRequest(mToUid, "");
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                            mRefreshListView.onRefreshComplete();
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        mRefreshListView.onRefreshComplete();
                    }

                });
        executeRequest(request);
    }

    public int getCurrentTop()
    {
        View localView;
        if (mListView.getChildCount() > 1)
        {
            localView = mListView.getChildAt(1);
        }
        else
        {
            return 0;
        }

        if (localView != null)
        {
            return localView.getTop();
        }
        else
        {
            return 0;
        }
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
                                if (TextUtils.isEmpty(mLatestMsgId))
                                {
                                    mMessageModelList.clear();
                                }

                                int returnSize = result.getData().getMsgs()
                                        .size();

                                int i = mListView.getFirstVisiblePosition()
                                        + returnSize + 1;
                                int j = getCurrentTop();

                                addChatListHistory(result.getData().getMsgs());
                                mAdapter.setDataSource(mMessageModelList);
                                mAdapter.notifyDataSetChanged();
                                mRefreshListView.onRefreshComplete();

                                if (TextUtils.isEmpty(mLatestMsgId))
                                {
                                    scrollListViewToBottom();
                                }
                                else
                                {
                                    mListView.setSelectionFromTop(i, j);
                                }

                                // send read message request
                                if (TextUtils.isEmpty(mLatestMsgId))
                                {
                                    doReadChatRequest(mToUid, mMessageModelList
                                            .get(mMessageModelList.size() - 1)
                                            .getMsg().get_id());
                                }

                                mLatestMsgId = mMessageModelList.get(0)
                                        .getMsg().get_id();
                            }
                            else
                            {
                                mRefreshListView.onRefreshComplete();
                            }
                        }
                        else
                        {
                            mRefreshListView.onRefreshComplete();
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        LogUtil.i(error.getMessage());
                        mRefreshListView.onRefreshComplete();
                    }

                });
        executeRequest(request);
    }

    private void doReadChatRequest(String uid, String msgId)
    {
        ReadChatRequest request = new ReadChatRequest(uid, msgId,
                new ResponseEventHandler<BaseModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<BaseModel> request, BaseModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {

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

    private boolean isContainRecord(List<IMMessageModel> list, String id)
    {
        for (IMMessageModel model : list)
        {
            if (model.getMsg().get_id().equals(id))
            {
                return true;
            }
        }

        return false;
    }

    private void addChatListHistory(List<MessageModel> historyList)
    {
        int size = historyList.size();
        for (int i = 0; i < size; i++)
        {
            MessageModel model = historyList.get(i);

            IMMessageModel imModel = new IMMessageModel();
            imModel.setMsg(model);

            if (AppApplication.getLoginId().equals(model.getTo_uid()))
            {
                imModel.setListType(IMListType.IM_FROM);
                imModel.setAvatar(mToUserModel.getAvatar());
            }
            else
            {
                imModel.setListType(IMListType.IM_TO);
                imModel.setAvatar(PersistentData.instance().getUserInfo()
                        .getAvatar());
            }

            imModel.setDateStr(TimeUtil.getDateTimeStr((long) model.getCrts() * 1000));
            mMessageModelList.add(0, imModel);
        }
    }

    @Override
    public void onRecvChatMsg(List<MessageModel> msgList)
    {
        for (MessageModel model : msgList)
        {
            IMMessageModel imModel = new IMMessageModel();
            imModel.setMsg(model);
            imModel.setListType(IMListType.IM_FROM);
            imModel.setAvatar(mToUserModel.getAvatar());
            imModel.setDateStr(TimeUtil.getDateTimeStr((long) model.getCrts() * 1000));
            mMessageModelList.add(imModel);
        }

        mAdapter.notifyDataSetChanged();
        scrollListViewToBottom();
    }

    @Override
    public void onChatAvatarClick(IMMessageModel model)
    {
        UIHelper.showUserCenterActivity(mContext, model.getMsg().getFrom_uid());
    }

    @Override
    public boolean onPublish(String content)
    {
        publishMessage(MessageContentType.MSG_TEXT, content);
        return true;
    }

    private int publishMessage(int msgContentType, String content)
    {
        // add to ui message list
        int reqeustId = getRequestId(msgContentType);
        IMMessageModel imModel = new IMMessageModel();
        MessageModel msgModel = new MessageModel();
        msgModel.setFrom_uid(AppApplication.getLoginId());
        msgModel.setTo_uid(mToUid);
        msgModel.setContent(content);
        msgModel.setCrts(System.currentTimeMillis() / 1000);
        msgModel.setMsg_type(msgContentType); // text mode
        imModel.setMsg(msgModel);
        imModel.setListType(IMListType.IM_TO);
        imModel.setAvatar(PersistentData.instance().getUserInfo().getAvatar());
        imModel.setDateStr(TimeUtil.getDateTimeStr(System.currentTimeMillis()));
        imModel.setStatus(IMMessageStatusType.MSG_SENDING);
        imModel.setReqId(reqeustId);
        mMessageModelList.add(imModel);

        // add to map
        mRequestMap.put(reqeustId, content);

        if (msgContentType == MessageContentType.MSG_IMAGE)
        {
            if (!WebSocketManager.instance().isConnected())
            {
                reqeustId = 0;
                imModel.setStatus(IMMessageStatusType.MSG_SEND_FAILED);
                ToastUtil.showToastShort(R.string.disconnect_server);
            }
        }
        else
        {
            if (!WebSocketManager.instance().sendChatMessage(mToUid, content,
                    msgContentType, reqeustId))
            {
                reqeustId = 0;
                imModel.setStatus(IMMessageStatusType.MSG_SEND_FAILED);
                ToastUtil.showToastShort(R.string.disconnect_server);
            }
        }

        // update ui
        mAdapter.notifyDataSetChanged();
        scrollListViewToBottom();

        return reqeustId;
    }

    private void rePublishMessage(IMMessageModel model)
    {
        model.setStatus(IMMessageStatusType.MSG_SENDING);

        if (model.getMsg().getMsg_type() == MessageContentType.MSG_IMAGE
                && model.getStatus() == IMMessageStatusType.MSG_UPLOAD_IMAGE_FAILED)
        {
            this.onUploadImage(model.getReqId(), model.getMsg().getContent());
            return;
        }

        if (!WebSocketManager.instance().sendChatMessage(mToUid,
                mRequestMap.get(model.getReqId()),
                model.getMsg().getMsg_type(), model.getReqId()))
        {
            model.setStatus(IMMessageStatusType.MSG_SEND_FAILED);
            ToastUtil.showToastShort(R.string.disconnect_server);
        }

        // update ui
        mAdapter.notifyDataSetChanged();
    }

    private void publishImageMessage(int msgContentType, String content)
    {
        // add to ui message list
        int reqeustId = getRequestId(msgContentType);
        IMMessageModel imModel = new IMMessageModel();
        MessageModel msgModel = new MessageModel();
        msgModel.setFrom_uid(AppApplication.getLoginId());
        msgModel.setTo_uid(mToUid);
        msgModel.setContent(content);
        msgModel.setCrts(System.currentTimeMillis() / 1000);
        msgModel.setMsg_type(msgContentType); // text mode
        imModel.setMsg(msgModel);
        imModel.setListType(IMListType.IM_TO);
        imModel.setAvatar(PersistentData.instance().getUserInfo().getAvatar());
        imModel.setDateStr(TimeUtil.getDateTimeStr(System.currentTimeMillis()));
        imModel.setStatus(IMMessageStatusType.MSG_SENDING);
        imModel.setReqId(reqeustId);
        mMessageModelList.add(imModel);

        if (!WebSocketManager.instance().isConnected())
        {
            imModel.setStatus(IMMessageStatusType.MSG_SEND_FAILED);
            ToastUtil.showToastShort(R.string.disconnect_server);
        }

        // update ui
        mAdapter.notifyDataSetChanged();
        scrollListViewToBottom();

        // add to map
        mRequestMap.put(reqeustId, content);
    }

    private void saveChatMessageToDB(int reqId, String content)
    {
        NewMessageModel newMsgModel = new NewMessageModel();
        newMsgModel.setMsgType(MessageType.MSG_CHAT);
        newMsgModel.setTag(mToUid);
        newMsgModel.setCount(0);
        if (mToUserModel != null)
        {
            newMsgModel.setAvatar(mToUserModel.getAvatar());
            newMsgModel.setTitle(mToUserModel.getNickname());
        }
        if (reqId % 2 == 0)
        {
            newMsgModel.setContent(getString(R.string.image_text));
        }
        else
        {
            newMsgModel.setContent(content);
        }

        newMsgModel.setTime(System.currentTimeMillis() / 1000);
        newMsgModel.setHaveRead(true);
        PushMsgDAO.instance().insertNewPushMsg(AppApplication.getLoginId(),
                newMsgModel);
    }

    @Override
    public void onTouch()
    {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        mFooterView.setImfooterGone();
        return false;
    }

    private void scrollListViewToBottom()
    {
        // to do list!!!
        // mScrollView.post(new Runnable()
        // {
        // public void run()
        // {
        // mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        // }
        // });

        // mListView.setSelection(mListView.getCount() - 1);

        new Handler().post(new Runnable()
        {
            public void run()
            {
                mListView.setSelectionFromTop(mAdapter.getCount(), 0);
            }
        });
    }

    @Override
    public void onSelectImageMenu(int index)
    {
        if (index == 0)
        {
            openCamera();
        }
        else
        {
            openAlbum();
        }
    }

    @Override
    public void onUploadPhotoSuccess(String imagename)
    {
        publishMessage(MessageContentType.MSG_IMAGE, imagename);
    }

    @Override
    public void onChatImageClick(IMMessageModel model)
    {
        ArrayList<String> photoList = new ArrayList<String>();
        photoList.add(model.getMsg().getContent());

        UIHelper.showPhotoGalleryActivity(mContext, photoList, 0);
    }

    @Override
    public void onResendClick(IMMessageModel model)
    {
        rePublishMessage(model);
    }

    @Override
    public void onTakeCameraResult(Uri imageUri)
    {
        tryUploadImage(FileUtil.getPath(this, imageUri));
    }

    @Override
    public void onTakeAlbumResult(Uri imageUri)
    {
        tryUploadImage(FileUtil.getPath(this, imageUri));
    }

    private void tryUploadImage(String filepath)
    {
        int reqId = publishMessage(MessageContentType.MSG_IMAGE, "file://"
                + filepath);
        if (reqId != 0)
        {
            onUploadImage(reqId, filepath);
        }
    }

    private void onUploadImage(final int reqId, String filepath)
    {
        new UploadImageHelper().uploadImage(this, filepath,
                new UploadImageHelper.UploadImageListener()
                {
                    @Override
                    public void onUploadResult(int result, String msg,
                            String imagename)
                    {
                        if (result == UploadImageHelper.UPLOAD_SUCCESS)
                        {
                            if (!WebSocketManager.instance().sendChatMessage(
                                    mToUid, imagename,
                                    MessageContentType.MSG_IMAGE, reqId))
                            {
                                updateMsgStatusByReqId(reqId,
                                        IMMessageStatusType.MSG_SEND_FAILED);
                                ToastUtil
                                        .showToastShort(R.string.disconnect_server);
                            }

                            mRequestMap.put(reqId, imagename);
                        }
                        else
                        {
                            updateMsgStatusByReqId(reqId,
                                    IMMessageStatusType.MSG_UPLOAD_IMAGE_FAILED);
                            ToastUtil.showToastShort(msg);
                        }
                    }
                }, false);
    }

    @Override
    public void onRecvChatResultMsg(PushChatSendResultModel resultModel)
    {
        ChatSendResultModel model = resultModel.getData();
        if (model != null && model.getResult() == 0)
        {
            saveChatMessageToDB(model.getReq_id(),
                    mRequestMap.get(model.getReq_id()));
            updateMsgStatusByReqId(model.getReq_id(),
                    IMMessageStatusType.MSG_SEND_SUCCESS);
            mRequestMap.remove(model.getReq_id());
        }
        else
        {
            updateMsgStatusByReqId(model.getReq_id(),
                    IMMessageStatusType.MSG_SEND_FAILED);
        }

        mAdapter.notifyDataSetChanged();
    }

    private Integer getRequestId(int msgContentType)
    {
        if (msgContentType == MessageContentType.MSG_IMAGE)
        {
            mStartRequestId = mStartRequestId % 2 == 0 ? mStartRequestId + 2
                    : mStartRequestId + 1;
        }
        else
        {
            mStartRequestId = mStartRequestId % 2 == 0 ? mStartRequestId + 1
                    : mStartRequestId + 2;
        }

        return mStartRequestId;
    }

    private void updateMsgStatusByReqId(int reqId, int status)
    {
        for (IMMessageModel model : mMessageModelList)
        {
            if (model.getReqId() == reqId)
            {
                model.setStatus(status);
                break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            onHeaderLeftClick();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
