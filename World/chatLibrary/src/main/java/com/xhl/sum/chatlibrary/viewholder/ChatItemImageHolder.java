package com.xhl.sum.chatlibrary.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.xhl.sum.chatlibrary.R;
import com.xhl.sum.chatlibrary.event.ImageItemClickEvent;
import com.xhl.sum.chatlibrary.utils.ImageLoadManager;

import de.greenrobot.event.EventBus;

/**
 * Created by wli on 15/9/17.
 */
public class ChatItemImageHolder extends ChatItemHolder {

    protected ImageView contentView;

    public ChatItemImageHolder(Context context, ViewGroup root, boolean isLeft) {
        super(context, root, isLeft);
    }

    @Override
    public void initView() {
        super.initView();
        View contentView = View.inflate(getContext(), R.layout.chat_item_image_layout, null);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        conventLayout.addView(contentView);
        this.contentView = (ImageView) itemView.findViewById(R.id.chat_item_image_view);
//        if (isLeft) {
//            this.contentView.setBackgroundResource(R.drawable.chat_qb_left_selector);
//        } else {
//            this.contentView.setBackgroundResource(R.drawable.chat_qb_right_selector);
//        }
        this.contentView.setBackgroundResource(0);

        this.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageItemClickEvent clickEvent = new ImageItemClickEvent();
                clickEvent.message = message;
                EventBus.getDefault().post(clickEvent);
            }
        });
    }

    @Override
    public void bindData(Object o) {
        super.bindData(o);
        contentView.setImageResource(0);
        AVIMMessage message = (AVIMMessage) o;
        if (message instanceof AVIMImageMessage) {
            AVIMImageMessage imageMsg = (AVIMImageMessage) message;
//            contentView.bindImageUrl(imageMsg.getFileUrl());
            ImageLoadManager.instance().LoadImage(contentView, imageMsg.getFileUrl());
        }
    }
}