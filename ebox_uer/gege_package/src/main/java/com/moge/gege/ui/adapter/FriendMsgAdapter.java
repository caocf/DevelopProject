package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.FriendMsgModel;
import com.moge.gege.model.UserModel;
import com.moge.gege.model.enums.FriendOperateResult;
import com.moge.gege.util.TimeUtil;

public class FriendMsgAdapter extends BaseCachedListAdapter<FriendMsgModel>
{
    private FriendMsgListListener mListener;

    public FriendMsgAdapter(Context context)
    {
        super(context);
    }

    public void setListener(FriendMsgListListener listener)
    {
        mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_friendmsg, null);

            holder = new ViewHolder();
            holder.avatarImage = (ImageView) convertView
                    .findViewById(R.id.avatarImage);
            holder.titleText = (TextView) convertView
                    .findViewById(R.id.titleText);
            holder.descText = (TextView) convertView
                    .findViewById(R.id.descText);
            holder.timeText = (TextView) convertView
                    .findViewById(R.id.timeText);
            holder.agreeBtn = (Button) convertView.findViewById(R.id.agreeBtn);
            holder.dealText = (TextView) convertView
                    .findViewById(R.id.dealText);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        FriendMsgModel model = list.get(position);
        UserModel userModel = model.getFriend();

        this.setImage(holder.avatarImage, getImageUrl(userModel.getAvatar()) + GlobalConfig.IMAGE_STYLE90_90,
                R.drawable.icon_default_avatar);
        holder.avatarImage.setOnClickListener(new MyClickListener(model));
        holder.titleText.setText(userModel.getNickname());
        holder.descText.setText(getFriendDetailMsg(model.getAction(),
                userModel.getNickname()));
        holder.timeText.setText(TimeUtil.timeToNow(model.getCrts()));

        if (model.getAction().equals(FriendOperateResult.APPLY_FRIEND)
                && model.getTreated() != FriendOperateResult.NOT_DEAL)
        {
            holder.dealText.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.dealText.setVisibility(View.GONE);
        }

        if (model.getAction().equals(FriendOperateResult.APPLY_FRIEND)
                && model.getTreated() == FriendOperateResult.NOT_DEAL)
        {
            holder.agreeBtn.setVisibility(View.VISIBLE);
            holder.agreeBtn.setOnClickListener(new MyClickListener(model));
        }
        else
        {
            holder.agreeBtn.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class MyClickListener implements OnClickListener
    {
        FriendMsgModel model;

        MyClickListener(FriendMsgModel model)
        {
            this.model = model;
        }

        @Override
        public void onClick(View v)
        {
            if (mListener == null)
            {
                return;
            }
            switch (v.getId())
            {
                case R.id.avatarImage:
                    mListener.onFriendMsgAvatarClick(model);
                    break;
                case R.id.agreeBtn:
                    mListener.onFriendMsgAgreeClick(model);
                    break;
            }
        }
    }

    private String getFriendDetailMsg(String action, String nickname)
    {
        if (action.equals(FriendOperateResult.APPLY_FRIEND))
        {
            return getString(R.string.apply_friend, "");
        }
        else if (action.equals(FriendOperateResult.ACCEPT_FRIEND))
        {
            return getString(R.string.accept_friend, "");
        }
        else
        {
            return getString(R.string.refuse_friend, "");
        }
    }

    class ViewHolder
    {
        ImageView avatarImage;
        TextView titleText;
        TextView descText;
        TextView timeText;
        Button agreeBtn;
        TextView dealText;
    }

    public interface FriendMsgListListener
    {
        public void onFriendMsgAvatarClick(FriendMsgModel model);

        public void onFriendMsgAgreeClick(FriendMsgModel model);

        public void onFriendMsgDisagreeClick(FriendMsgModel model);
    }

}
