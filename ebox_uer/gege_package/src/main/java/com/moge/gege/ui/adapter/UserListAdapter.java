package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.UserModel;
import com.moge.gege.model.enums.GenderType;

public class UserListAdapter extends BaseCachedListAdapter<UserModel> implements
        OnItemClickListener
{
    private UserListListener mListener;

    public UserListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(UserListListener listener)
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
                    R.layout.item_user, null);

            holder = new ViewHolder();
            holder.avatarImage = (ImageView) convertView
                    .findViewById(R.id.avatarImage);
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            holder.descText = (TextView) convertView
                    .findViewById(R.id.descText);
            holder.communityText = (TextView) convertView
                    .findViewById(R.id.communityText);
            holder.genderImage = (ImageView) convertView
                    .findViewById(R.id.genderImage);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        UserModel model = list.get(position);

        this.setImage(holder.avatarImage, getImageUrl(model.getAvatar())  + GlobalConfig.IMAGE_STYLE90_90,
                R.drawable.icon_default_avatar);
        holder.nameText.setText(model.getNickname());
        holder.descText.setText(model.getIntroduction());

        if (model.getGender() == GenderType.WOMAN)
        {
            holder.genderImage.setImageResource(R.drawable.icon_woman);
        }
        else
        {
            holder.genderImage.setImageResource(R.drawable.icon_man);
        }

        holder.avatarImage.setOnClickListener(new MyClickListener(model
                .get_id()));

        return convertView;
    }

    public class MyClickListener implements OnClickListener
    {
        String mUserId;

        MyClickListener(String userId)
        {
            mUserId = userId;
        }

        @Override
        public void onClick(View v)
        {
            if (mListener != null)
            {
                mListener.onUserAvatarClick(mUserId);
            }
        }
    }

    class ViewHolder
    {
        ImageView avatarImage;
        TextView nameText;
        TextView descText;
        TextView communityText;
        ImageView genderImage;
    }

    public interface UserListListener
    {
        public void onUserAvatarClick(String uid);

        public void onUserItemClick(UserModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onUserItemClick(list.get(position - 1));
        }
    }

}
