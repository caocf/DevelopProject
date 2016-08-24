package com.xhl.world.chat.view;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import com.xhl.sum.chatlibrary.Constants;
import com.xhl.sum.chatlibrary.controller.ConversationHelper;
import com.xhl.sum.chatlibrary.model.ConversationType;
import com.xhl.sum.chatlibrary.model.Room;
import com.xhl.world.R;
import com.xhl.world.chat.event.ConversationItemClickEvent;
import com.xhl.world.chat.recyclerHolder.ChatMessageDataHolder;
import com.xhl.world.ui.fragment.BaseAppFragment;
import com.xhl.world.ui.utils.ToastUtil;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayout;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayoutDirection;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Sum on 15/12/24.
 */
@ContentView(R.layout.chat_fragment_conversation)
public class ConversationFragment extends BaseAppFragment {

    @ViewInject(R.id.chat_message_recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.chat_message_refresh)
    private SwipyRefreshLayout mSwipRefresh;

    private LinearLayoutManager mLayoutManager;

    private RecyclerAdapter mAdapter;

    private boolean isFirstLoad = true;

    @Override
    protected void initParams() {

        mAdapter = new RecyclerAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        initPullRefresh();
    }


    private void initPullRefresh() {
        mSwipRefresh.setRefreshing(false);
        mSwipRefresh.setColorSchemeResources(R.color.app_green, R.color.app_blue);
        mSwipRefresh.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    updateConversationList();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateConversationList();
    }

    private void createChatMessageData(List<Room> rooms) {

        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getConversation() == null) {//去除没有获取到的会话
                continue;
            }
            ChatMessageDataHolder conversation = new ChatMessageDataHolder(room);
            holders.add(conversation);
        }
        mAdapter.setDataHolders(holders);
        mAdapter.notifyDataSetChanged();

        hideLoadingDialog();
        mSwipRefresh.setRefreshing(false);
    }


    private void updateConversationList() {

        if (isFirstLoad) {
            showLoadingDialog();
            isFirstLoad = false;
        }

        ConversationHelper.findAndCacheRooms(new Room.MultiRoomsCallback() {
            @Override
            public void done(List<Room> roomList, AVException exception) {
                if (filterException(exception)) {
                    //更新最后一条信息
                    updateLastMessage(roomList);
                    //
                    cacheRelatedUsers(roomList);

                    List<Room> sortedRooms = sortRooms(roomList);
                    createChatMessageData(sortedRooms);
                }
            }
        });
    }

    private void updateLastMessage(final List<Room> roomList) {
        for (final Room room : roomList) {
            AVIMConversation conversation = room.getConversation();
            if (null != conversation) {
                conversation.getLastMessage(new AVIMSingleMessageQueryCallback() {
                    @Override
                    public void done(AVIMMessage avimMessage, AVIMException e) {
                        if (filterException(e) && null != avimMessage) {
                            room.setLastMessage(avimMessage);
                            int index = roomList.indexOf(room);
                            mAdapter.notifyItemChanged(index);
                        }
                    }
                });
                //设置会话Logo通过自定义属性完成(已经在创建会话的时候添加了)
//                String shopLogo = (String) conversation.getAttribute(Constants.shop_attr_conversation_logo);
//                room.setConverSationIcon(shopLogo);//会话logo就是商家店铺logo
            }
        }
    }

    private void cacheRelatedUsers(List<Room> rooms) {
        List<String> needCacheUsers = new ArrayList<String>();
        for (Room room : rooms) {
            AVIMConversation conversation = room.getConversation();
            if (ConversationHelper.typeOfConversation(conversation) == ConversationType.Single) {
                needCacheUsers.add(ConversationHelper.otherIdOfConversation(conversation));
            }
        }
    }

    private List<Room> sortRooms(final List<Room> roomList) {
        List<Room> sortedList = new ArrayList<Room>();
        if (null != roomList) {
            sortedList.addAll(roomList);
            Collections.sort(sortedList, new Comparator<Room>() {
                @Override
                public int compare(Room lhs, Room rhs) {
                    long value = lhs.getLastModifyTime() - rhs.getLastModifyTime();
                    if (value > 0) {
                        return -1;
                    } else if (value < 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        }
        return sortedList;
    }

    private boolean filterException(Exception e) {
        if (e != null) {
//            SnackMaker.shortShow(mSwipRefresh, e.getMessage());
            ToastUtil.showToastShort(e.getMessage());
            hideLoadingDialog();
            return false;
        } else {
            return true;
        }
    }

    public void onEvent(ConversationItemClickEvent event) {

        AVIMConversation conversation = event.room.getConversation();

        ConversationType type = ConversationHelper.typeOfConversation(conversation);
        if (type == ConversationType.Single) {//单聊UI界面
            Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
            intent.putExtra(Constants.CONVERSATION_ID, event.conversationId);
            startActivity(intent);
        } else if (type == ConversationType.Robot) {//机器人UI界面

        }

    }
}
