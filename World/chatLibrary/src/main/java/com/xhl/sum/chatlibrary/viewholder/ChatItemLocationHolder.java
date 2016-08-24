package com.xhl.sum.chatlibrary.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.xhl.sum.chatlibrary.R;
import com.xhl.sum.chatlibrary.event.LocationItemClickEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by wli on 15/9/17.
 */
public class ChatItemLocationHolder extends ChatItemHolder {

  protected TextView contentView;

  public ChatItemLocationHolder(Context context, ViewGroup root, boolean isLeft) {
    super(context, root, isLeft);
  }

  @Override
  public void initView() {
    super.initView();
    conventLayout.addView(View.inflate(getContext(), R.layout.chat_item_location, null));
    contentView = (TextView)itemView.findViewById(R.id.locationView);
    conventLayout.setBackgroundResource(isLeft ? R.drawable.chat_qb_left_selector : R.drawable.chat_qb_right_selector);
    contentView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        LocationItemClickEvent event = new LocationItemClickEvent();
        event.message = message;
        EventBus.getDefault().post(event);
      }
    });
  }

  @Override
  public void bindData(Object o) {
    super.bindData(o);
    AVIMMessage message = (AVIMMessage)o;
    if (message instanceof AVIMLocationMessage) {
      final AVIMLocationMessage locMsg = (AVIMLocationMessage) message;
      contentView.setText(locMsg.getText());
    }
  }
}
