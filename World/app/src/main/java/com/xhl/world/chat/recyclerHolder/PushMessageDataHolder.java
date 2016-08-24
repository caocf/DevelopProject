package com.xhl.world.chat.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.chat.PushMessage;
import com.xhl.world.chat.event.MessageEvent;
import com.xhl.world.config.Constant;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Sum on 16/2/24.
 */
public class PushMessageDataHolder extends RecyclerDataHolder {
    public PushMessageDataHolder(Object data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.chat_item_push_msg, null);

        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));

        AutoUtils.auto(view);

        return new pushMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        pushMessageViewHolder holder = (pushMessageViewHolder) vHolder;
        holder.bindData((PushMessage) data);
    }

    static class pushMessageViewHolder extends RecyclerViewHolder {

        private TextView tv_msg_title, tv_msg_time, tv_msg_content;

        PushMessage mMsg;

        public pushMessageViewHolder(View view) {
            super(view);
            tv_msg_title = (TextView) view.findViewById(R.id.tv_msg_title);
            tv_msg_time = (TextView) view.findViewById(R.id.tv_msg_time);
            tv_msg_content = (TextView) view.findViewById(R.id.tv_msg_content);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMsg != null) {
                        MessageEvent messageEvent = new MessageEvent();
                        messageEvent.type = Constant.intent_system_message;
                        messageEvent.pushMessage = mMsg;
                        EventBusHelper.post(messageEvent);
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    return true;
                }
            });
        }

        public void bindData(PushMessage msg) {
            if (msg == null) {
                return;
            }
            mMsg = msg;
            tv_msg_title.setText(msg.getPushTitle());
            tv_msg_content.setText(msg.getPushContent());
            tv_msg_time.setText(msg.getPushTime());
        }
    }
}
