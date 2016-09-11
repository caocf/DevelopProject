package com.moge.gege.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;

public class AvatarImageAdapter extends BaseCachedListAdapter<String>
{
    public AvatarImageAdapter(Context context)
    {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        final ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_avatarimage, null);

            holder = new ViewHolder();
            holder.avatarImage = (ImageView) convertView
                    .findViewById(R.id.avatarImage);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        if(!TextUtils.isEmpty(list.get(position)))
        {
            this.setImage(holder.avatarImage, getImageUrl(list.get(position)) + GlobalConfig.IMAGE_STYLE90_90,
                    R.drawable.icon_default_avatar);
        }
        else
        {
            holder.avatarImage.setImageResource(R.drawable.icon_default_avatar);
        }

        return convertView;
    }

    class ViewHolder
    {
        ImageView avatarImage;

    }
}
