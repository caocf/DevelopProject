package com.xhl.world.chat.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.xhl.sum.chatlibrary.Constants;
import com.xhl.sum.chatlibrary.adapter.MultipleItemAdapter;
import com.xhl.sum.chatlibrary.controller.ChatManager;
import com.xhl.sum.chatlibrary.controller.ConversationHelper;
import com.xhl.sum.chatlibrary.controller.MessageAgent;
import com.xhl.sum.chatlibrary.event.ImTypeMessageEvent;
import com.xhl.sum.chatlibrary.event.ImTypeMessageResendEvent;
import com.xhl.sum.chatlibrary.event.ImageItemClickEvent;
import com.xhl.sum.chatlibrary.event.InputBottomBarEvent;
import com.xhl.sum.chatlibrary.event.InputBottomBarRecordEvent;
import com.xhl.sum.chatlibrary.event.InputBottomBarTextEvent;
import com.xhl.sum.chatlibrary.utils.NotificationUtils;
import com.xhl.sum.chatlibrary.utils.PathUtils;
import com.xhl.sum.chatlibrary.utils.ProviderPathUtils;
import com.xhl.sum.chatlibrary.view.InputBottomBar;
import com.xhl.world.AppApplication;
import com.xhl.world.R;
import com.xhl.world.ui.activity.BaseAppActivity;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayout;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayoutDirection;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.List;

/**
 * Created by Sum on 15/12/24.
 */
@ContentView(R.layout.chat_activity_chat)
public class ChatRoomActivity extends BaseAppActivity implements ChatManager.ConnectionListener, MessageAgent.SendCallback {


    @ViewInject(R.id.chat_room_recyclerView)
    protected RecyclerView recyclerView;

    @ViewInject(R.id.chat_room_swiypRefresh)
    private SwipyRefreshLayout mSwipRefresh;

    @ViewInject(R.id.chat_room_input)
    protected InputBottomBar inputBottomBar;

    @ViewInject(R.id.title_name)
    private TextView mHeadName;

    @Event(R.id.title_back)
    private void onBackClick(View view) {
        this.finish();
    }

    public static final int INTENT_TAKE_PIC_CAMEAR = 0;
    public static final int INTENT_TAKE_PIC_DICM = 1;

    protected LinearLayoutManager layoutManager;
    protected String localCameraPath = PathUtils.getPicturePathByCurrentTime();

    protected AVIMConversation mImConversation;
    protected MessageAgent mMessageManager;
    protected MultipleItemAdapter mItemAdapter;

    private String mUserLogo;
    private String mShopLogo;

    @Override
    protected void initParams() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mItemAdapter = new MultipleItemAdapter();
        mItemAdapter.resetRecycledViewPoolSize(recyclerView);
        recyclerView.setAdapter(mItemAdapter);

        initPullRefresh();
    }

    private void initPullRefresh() {
        mSwipRefresh.setRefreshing(false);
        mSwipRefresh.setDirection(SwipyRefreshLayoutDirection.TOP);

        mSwipRefresh.setColorSchemeResources(R.color.app_green, R.color.app_blue);
        mSwipRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    loadData();
                }
            }
        });
    }

    private void loadData() {

        AVIMMessage message = mItemAdapter.getFirstMessage();
        if (null == message) {
            mSwipRefresh.setRefreshing(false);
        } else {
            //下拉历史数据
            mImConversation.queryMessages(message.getMessageId(), message.getTimestamp(), 20, new AVIMMessagesQueryCallback() {
                @Override
                public void done(List<AVIMMessage> list, AVIMException e) {
                    mSwipRefresh.setRefreshing(false);
                    if (filterException(e)) {
                        if (null != list && list.size() > 0) {
                            mItemAdapter.addMessageList(list);
                            mItemAdapter.notifyDataSetChanged();
                            layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatManager.getInstance().setConnectionListener(this);
        initByIntent(getIntent());
    }

    @Override
    protected boolean needRoot() {
        return false;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.rl_content;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (null != mImConversation) {
            NotificationUtils.addTag(mImConversation.getConversationId());

            NotificationUtils.cancelNotification(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mImConversation) {
            NotificationUtils.removeTag(mImConversation.getConversationId());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatManager.getInstance().setConnectionListener(null);
    }

    /**
     * 设置会话
     *
     * @param conversation
     */
    public void setConversation(AVIMConversation conversation) {
        mImConversation = conversation;

        inputBottomBar.setTag(mImConversation.getConversationId());

        fetchMessages();
        //会话名称
        String name = mImConversation.getName();
        if (name != null) {
            mHeadName.setText(name);
        }

        NotificationUtils.addTag(conversation.getConversationId());

        mMessageManager = new MessageAgent(conversation);
        mMessageManager.setSendCallback(this);
    }

    public void showUserName(boolean isShow) {
        mItemAdapter.showUserName(isShow);
    }

    /**
     * 拉取消息，必须加入 conversation 后才能拉取消息
     */
    private void fetchMessages() {
        mImConversation.queryMessages(new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
                if (filterException(e)) {
                    mItemAdapter.setMessageList(list);
                    recyclerView.setAdapter(mItemAdapter);
                    mItemAdapter.notifyDataSetChanged();
                    scrollToBottom();
                }
            }
        });
    }

    /**
     * 输入事件处理，接收后构造成 AVIMTextMessage 然后发送
     * 因为不排除某些特殊情况会受到其他页面过来的无效消息，所以此处加了 tag 判断
     */
    public void onEvent(InputBottomBarTextEvent textEvent) {
        if (null != mImConversation && null != textEvent) {
            if (!TextUtils.isEmpty(textEvent.sendContent) && mImConversation.getConversationId().equals(textEvent.tag)) {
                sendText(textEvent.sendContent);
            }
        }
    }

    /**
     * 处理推送过来的消息
     * 同理，避免无效消息，此处加了 conversation id 判断
     */
    public void onEvent(ImTypeMessageEvent event) {
        if (null != mImConversation && null != event && mImConversation.getConversationId().equals(event.conversation.getConversationId())) {
            mItemAdapter.addMessage(event.message);
            mItemAdapter.notifyDataSetChanged();
            scrollToBottom();
        }
    }

    /**
     * 重新发送已经发送失败的消息
     */
    public void onEvent(ImTypeMessageResendEvent event) {
        if (null != mImConversation && null != event &&
                null != event.message && mImConversation.getConversationId().equals(event.message.getConversationId())) {
            if (AVIMMessage.AVIMMessageStatus.AVIMMessageStatusFailed == event.message.getMessageStatus()
                    && mImConversation.getConversationId().equals(event.message.getConversationId())) {
                mImConversation.sendMessage(event.message, new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        mItemAdapter.notifyDataSetChanged();
                    }
                });
                mItemAdapter.notifyDataSetChanged();
            }
        }
    }

    //图片点击事件
    public void onEvent(ImageItemClickEvent event) {

        AVIMMessage message = event.message;
        if (message instanceof AVIMImageMessage) {
            AVIMImageMessage imageMsg = (AVIMImageMessage) message;
            String url = imageMsg.getFileUrl();

            if (URLUtil.isValidUrl(url)) {
                pushFragmentToBackStack(ImageDetailsFragment.class, url);
            } else {
                SnackMaker.shortShow(mSwipRefresh, "图片路径错误 =.=!!");
            }
        }

    }

    public void onEvent(InputBottomBarEvent event) {
        if (null != mImConversation && null != event && mImConversation.getConversationId().equals(event.tag)) {
            switch (event.eventAction) {
                case InputBottomBarEvent.INPUTBOTTOMBAR_IMAGE_ACTION:
                    selectImageFromLocal();
                    break;
                case InputBottomBarEvent.INPUTBOTTOMBAR_CAMERA_ACTION:
                    selectImageFromCamera();
                    break;
            }
        }
    }

    public void onEvent(InputBottomBarRecordEvent recordEvent) {
        if (null != mImConversation && null != recordEvent &&
                !TextUtils.isEmpty(recordEvent.audioPath) &&
                mImConversation.getConversationId().equals(recordEvent.tag)) {
            sendAudio(recordEvent.audioPath);
        }
    }

    public void selectImageFromLocal() {

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, INTENT_TAKE_PIC_DICM);

    }

    public void selectImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri imageUri = Uri.fromFile(new File(localCameraPath));

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, INTENT_TAKE_PIC_CAMEAR);
        }
    }

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(mItemAdapter.getItemCount() - 1, 0);
    }

    protected boolean filterException(Exception e) {
        if (e != null) {
            Logger.e(e.getMessage());
            toast(e.getMessage());
            return false;
        } else {
            return true;
        }
    }

    protected void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case INTENT_TAKE_PIC_DICM:
                    if (data == null) {
                        toast("读取图片失败，请重新选择");
                        return;
                    }
                    Uri uri = data.getData();

                    String localSelectPath = ProviderPathUtils.getPath(this, uri);
                    inputBottomBar.hideMoreLayout();

                    sendImage(localSelectPath);
                    break;

                case INTENT_TAKE_PIC_CAMEAR:
                    inputBottomBar.hideMoreLayout();
                    sendImage(localCameraPath);
                    break;
            }
        }
    }


    private void sendText(String content) {
        mMessageManager.sendText(content);
    }

    private void sendImage(String imagePath) {
        mMessageManager.sendImage(imagePath);
    }

    private void sendAudio(String audioPath) {
        mMessageManager.sendAudio(audioPath);
    }

    @Override
    public void onConnectionChanged(boolean connect) {
        if (!connect) {
            SnackMaker.shortShow(mSwipRefresh, R.string.network_error);
        }
    }

    @Override
    public void onStart(AVIMTypedMessage message) {
        mItemAdapter.addMessage(message);
        mItemAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    @Override
    public void onError(AVIMTypedMessage message, Exception e) {
        Logger.e("send msg error:" + message.getFrom() + " E:" + e.getMessage());
    }

    @Override
    public void onSuccess(AVIMTypedMessage message) {
        mItemAdapter.notifyDataSetChanged();
    }

    private void updateConversation(AVIMConversation conversation) {
        if (null != conversation) {
            setConversation(conversation);
            // showUserName(true);
            //showUserName(ConversationHelper.typeOfConversation(conversation) != ConversationType.Single);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initByIntent(intent);
    }

    private void initByIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (null != extras) {
            if (extras.containsKey(Constants.MEMBER_ID)) { //点击商户称进入聊天
                //根据member_id查询会话，不存在会创建
                String member_id = extras.getString(Constants.MEMBER_ID);

                String shop_name = extras.getString(Constants.CONVERSATION__NAME, "临时会话");
                String shop_logo = extras.getString(Constants.shop_attr_conversation_logo, "");
                String shop_url = extras.getString(Constants.shop_attr_conversation_url, "");

                mShopLogo = shop_logo;
                mUserLogo = AppApplication.appContext.getUserFaceImage();
                if (mItemAdapter != null) {
                    mItemAdapter.setChatAvatar(mUserLogo, mShopLogo);
                }

                getConversation(shop_name, shop_logo, shop_url, member_id);

            } else if (extras.containsKey(Constants.CONVERSATION_ID)) { //点击会话进入聊天

                //根据会话Id获取会话
                String conversationId = extras.getString(Constants.CONVERSATION_ID);

                AVIMConversation conversation = ChatManager.getInstance().getConversation(conversationId);
                //设置头像

                mShopLogo = ConversationHelper.getConversationLogo(conversation);

                mUserLogo = AppApplication.appContext.getUserFaceImage();
                if (mItemAdapter != null) {
                    mItemAdapter.setChatAvatar(mUserLogo, mShopLogo);
                }

                updateConversation(conversation);
            }
        }
    }

    /**
     * 获取 conversation，为了避免重复的创建，此处先 query 是否已经存在只包含该 member 的 conversation
     */
    private void getConversation(final String name, String logo, String url, final String memberId) {

        ChatManager.getInstance().fetchSingleShopConversation(name, logo, url, memberId, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation conversation, AVIMException e) {
                if (e == null) {

                    ChatManager.getInstance().getRoomsTable().insertRoom(conversation.getConversationId());

                    updateConversation(conversation);
                } else {
                    Logger.e(e.getMessage());
                }
            }
        });
    }
}
