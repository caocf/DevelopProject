package com.moge.gege.util.widget.chat;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moge.gege.R;
import com.moge.gege.ui.adapter.BaseCachedListAdapter;

public class EmoticonAdapter extends BaseCachedListAdapter<EmoticonEntity>
{

    public EmoticonAdapter(Context context)
    {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        EmoticonEntity emoji = list.get(position);
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_emoticon, null);
            holder.emoticonImage = (ImageView) convertView
                    .findViewById(R.id.emoticonImage);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        if (emoji.getResId() == R.drawable.face_del_icon)
        {
            convertView.setBackgroundDrawable(null);
            holder.emoticonImage.setImageResource(emoji.getResId());
        }
        else if (TextUtils.isEmpty(emoji.getResName()))
        {
            convertView.setBackgroundDrawable(null);
            holder.emoticonImage.setImageDrawable(null);
        }
        else
        {
            holder.emoticonImage.setTag(emoji);
            holder.emoticonImage.setImageResource(emoji.getResId());
        }

        return convertView;
    }

    class ViewHolder
    {

        public ImageView emoticonImage;
    }
}