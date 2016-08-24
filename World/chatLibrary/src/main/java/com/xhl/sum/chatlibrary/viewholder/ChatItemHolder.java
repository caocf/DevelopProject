package com.xhl.sum.chatlibrary.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.xhl.sum.chatlibrary.R;
import com.xhl.sum.chatlibrary.controller.ChatManager;
import com.xhl.sum.chatlibrary.controller.ConversationHelper;
import com.xhl.sum.chatlibrary.event.ImTypeMessageResendEvent;
import com.xhl.sum.chatlibrary.utils.AVIMConversationCacheUtils;
import com.xhl.sum.chatlibrary.utils.ImageLoadManager;
import com.xhl.sum.chatlibrary.utils.Utils;

import de.greenrobot.event.EventBus;

/**
 * Created by wli on 15/9/17.
 */
public class ChatItemHolder extends CommonViewHolder {

    protected boolean isLeft;

    protected AVIMMessage message;
    protected ImageView avatarView;
    protected TextView timeView;
    protected LinearLayout conventLayout;
    protected FrameLayout statusLayout;
    protected ProgressBar progressBar;
    protected TextView statusView;
    protected ImageView errorView;

    private String mShopLogo;
    private String mUserLogo;

    public ChatItemHolder(Context context, ViewGroup root, boolean isLeft) {
        super(context, root, isLeft ? R.layout.chat_item_left_layout : R.layout.chat_item_right_layout);
        this.isLeft = isLeft;
        initView();
    }

    public void initView() {
        if (isLeft) {
            avatarView = (ImageView) itemView.findViewById(R.id.chat_left_iv_avatar);
            timeView = (TextView) itemView.findViewById(R.id.chat_left_tv_time);
            conventLayout = (LinearLayout) itemView.findViewById(R.id.chat_left_layout_content);
            statusLayout = (FrameLayout) itemView.findViewById(R.id.chat_left_layout_status);
            statusView = (TextView) itemView.findViewById(R.id.chat_left_tv_status);
            progressBar = (ProgressBar) itemView.findViewById(R.id.chat_left_progressbar);
            errorView = (ImageView) itemView.findViewById(R.id.chat_left_tv_error);
        } else {
            avatarView = (ImageView) itemView.findViewById(R.id.chat_right_iv_avatar);
            timeView = (TextView) itemView.findViewById(R.id.chat_right_tv_time);
            conventLayout = (LinearLayout) itemView.findViewById(R.id.chat_right_layout_content);
            statusLayout = (FrameLayout) itemView.findViewById(R.id.chat_right_layout_status);
            progressBar = (ProgressBar) itemView.findViewById(R.id.chat_right_progressbar);
            errorView = (ImageView) itemView.findViewById(R.id.chat_right_tv_error);
            statusView = (TextView) itemView.findViewById(R.id.chat_right_tv_status);
        }
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErrorClick(v);
            }
        });
    }

    @Override
    public void bindData(Object o) {
        message = (AVIMMessage) o;
        timeView.setText(Utils.millisecsToDateString(message.getTimestamp()));
        //加载用户头像
        if (isLeft) {
            if (!TextUtils.isEmpty(mShopLogo)) {
                ImageLoadManager.instance().LoadImage(avatarView, mShopLogo);
            } else {
                String conversationId = message.getConversationId();
                AVIMConversation conversation = AVIMConversationCacheUtils.getCacheConversation(conversationId);
                if (conversation != null) {
                    String logo = ConversationHelper.getConversationLogo(conversation);
                    if (!TextUtils.isEmpty(logo)) {
                        mShopLogo = logo;
                        ImageLoadManager.instance().LoadImage(avatarView, mShopLogo);
                    }
                }
            }

        } else {
            String image = mUserLogo;
            if (!TextUtils.isEmpty(image)) {
                ImageLoadManager.instance().LoadImage(avatarView, image);
            }
        }

        switch (message.getMessageStatus()) {
            case AVIMMessageStatusFailed:
                statusLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                break;
            case AVIMMessageStatusSent:
                statusLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                break;
            case AVIMMessageStatusSending:
                statusLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                break;
            case AVIMMessageStatusNone:
            case AVIMMessageStatusReceipt:
                statusLayout.setVisibility(View.GONE);
                break;
        }

        ChatManager.getInstance().getRoomsTable().clearUnread(message.getConversationId());
    }

    public void onErrorClick(View view) {
        ImTypeMessageResendEvent event = new ImTypeMessageResendEvent();
        event.message = message;
        EventBus.getDefault().post(event);
    }

    public void showTimeView(boolean isShow) {
        timeView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    //店铺图片
    public void setShopLogo(String shopLogo) {
        this.mShopLogo = shopLogo;
    }

    //用户图片
    public void setUserLogo(String userLogo) {
        mUserLogo = userLogo;
    }
}

