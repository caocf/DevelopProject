package com.xhl.world.chat.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.xhl.sum.chatlibrary.controller.ConversationHelper;
import com.xhl.sum.chatlibrary.controller.MessageHelper;
import com.xhl.sum.chatlibrary.model.Room;
import com.xhl.world.R;
import com.xhl.world.chat.event.ConversationItemClickEvent;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 15/12/25.
 */
public class ChatMessageDataHolder extends RecyclerDataHolder {
    public ChatMessageDataHolder(Object data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item_conversation, null);
        AutoUtils.auto(view);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        ConversationViewHolder holder = (ConversationViewHolder) vHolder;
        holder.bindData(data);
    }

    static class ConversationViewHolder extends RecyclerViewHolder {

        LifeCycleImageView recentAvatarView;
        TextView recentNameView;
        TextView recentMsgView;
        TextView recentTimeView;
        TextView recentUnreadView;

        public ConversationViewHolder(View view) {
            super(view);
            initView();
        }

        public void initView() {
            recentAvatarView = (LifeCycleImageView) itemView.findViewById(R.id.iv_recent_avatar);
            recentNameView = (TextView) itemView.findViewById(R.id.recent_time_text);
            recentMsgView = (TextView) itemView.findViewById(R.id.recent_msg_text);
            recentTimeView = (TextView) itemView.findViewById(R.id.recent_teim_text);
            recentUnreadView = (TextView) itemView.findViewById(R.id.recent_unread);
        }

        public void bindData(Object o) {
            if (o instanceof Room) {

                final Room room = (Room) o;
                AVIMConversation conversation = room.getConversation();

                if (null != conversation) {
                    //会话Logo
                    String logo = ConversationHelper.getConversationLogo(conversation);
                    if (!TextUtils.isEmpty(logo)) {
                        recentAvatarView.bindImageUrl(logo);
                    }
                    //会话名称
                    recentNameView.setText(ConversationHelper.getConversationName(conversation));

                    int num = room.getUnreadCount();
                    if (num > 0) {
                        recentUnreadView.setVisibility(View.VISIBLE);
                        recentUnreadView.setText(String.valueOf(num));
                    } else {
                        recentUnreadView.setVisibility(View.GONE);
                    }

                    if (room.getLastMessage() != null) {
                        Date date = new Date(room.getLastMessage().getTimestamp());
                        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
                        recentTimeView.setText(format.format(date));
                        recentMsgView.setText(MessageHelper.outlineOfMsg((AVIMTypedMessage) room.getLastMessage()));
                    }

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ConversationItemClickEvent itemClickEvent = new ConversationItemClickEvent();
                            itemClickEvent.conversationId = room.getConversationId();
                            itemClickEvent.room = room;
                            EventBus.getDefault().post(itemClickEvent);
                        }
                    });
                }
            }
        }
    }
}
