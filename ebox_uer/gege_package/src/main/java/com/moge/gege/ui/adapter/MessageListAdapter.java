package com.moge.gege.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.NewMessageModel;
import com.moge.gege.model.enums.MessageType;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.util.TimeUtil;
import com.moge.gege.util.widget.LinkTextView;

public class MessageListAdapter extends BaseAdapter implements
        OnItemClickListener, OnItemLongClickListener
{
    private Context mContext;
    private List<NewMessageModel> mDataList;
    private MessageListListener mListener;

    public MessageListAdapter(Context context)
    {
        mContext = context;
    }

    public void setDataSource(List<NewMessageModel> list)
    {
        mDataList = list;
    }

    public void setListener(MessageListListener listener)
    {
        mListener = listener;
    }

    public int getCount()
    {
        return mDataList == null ? 0 : mDataList.size();
    }

    public Object getItem(int position)
    {
        return mDataList.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;

        NewMessageModel model = mDataList.get(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_newmessage, null);

            holder = new ViewHolder();
            holder.avatarImage = (ImageView) convertView
                    .findViewById(R.id.avatarImage);
            holder.newMsgTipView = (ImageView) convertView
                    .findViewById(R.id.newMsgTipView);
            holder.timeText = (TextView) convertView
                    .findViewById(R.id.timeText);
            holder.contentText = (LinkTextView) convertView
                    .findViewById(R.id.contentText);
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            holder.newMsgCountText = (TextView) convertView
                    .findViewById(R.id.newMsgCountText);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        if (model.getMsgType() == MessageType.MSG_CHAT)
        {
            RequestManager.loadImage(holder.avatarImage,
                    RequestManager.getImageUrl(model.getAvatar()) + GlobalConfig.IMAGE_STYLE90_90,
                    R.drawable.icon_default_avatar);
        }
        else
        {
            RequestManager.loadImageByUrl(holder.avatarImage,
                    model.getAvatar(), R.drawable.icon_default_avatar);
        }

        if (model.isHaveRead())
        {
            holder.newMsgTipView.setVisibility(View.GONE);
        }
        else
        {
            holder.newMsgTipView.setVisibility(View.VISIBLE);
        }

        if (model.getTime() == 0)
        {
            holder.timeText.setVisibility(View.GONE);
        }
        else
        {
            holder.timeText.setVisibility(View.VISIBLE);
            holder.timeText.setText(TimeUtil.timeToNow(model.getTime()));
        }
        // holder.contentText.setText(model.getContent());
        if (model.getContent() != null)
        {
            holder.contentText.setLinkText(model.getContent());
        }

        holder.nameText.setText(model.getTitle());
        if (model.getCount() == 0)
        {
            holder.newMsgCountText.setVisibility(View.GONE);
        }
        else
        {
            holder.newMsgCountText.setVisibility(View.VISIBLE);

            if (model.getCount() > 9)
            {
                holder.newMsgCountText
                        .setBackgroundResource(R.drawable.bg_big_newmsg);
            }
            else
            {
                holder.newMsgCountText
                        .setBackgroundResource(R.drawable.bg_small_newmsg);
            }

            if (model.getCount() > 99)
            {
                holder.newMsgCountText.setText("99");
            }
            else
            {
                holder.newMsgCountText.setText(model.getCount() + "");
            }
        }

        return convertView;
    }

    class ViewHolder
    {
        ImageView avatarImage;
        ImageView newMsgTipView;
        TextView timeText;
        LinkTextView contentText;
        TextView nameText;
        TextView newMsgCountText;
    }

    public interface MessageListListener
    {
        public void onMessageItemClick(NewMessageModel model);

        public void onMessageItemLongClick(NewMessageModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onMessageItemClick(mDataList.get(position - 1));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id)
    {
        if (mListener != null)
        {
            mListener.onMessageItemLongClick(mDataList.get(position - 1));
            return true;
        }
        return false;
    }

}
