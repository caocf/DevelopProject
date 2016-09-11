package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.RecvGiftModel;

public class RecvGiftListAdapter extends BaseCachedListAdapter<RecvGiftModel>
{
    public RecvGiftListAdapter(Context context)
    {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_recvgift, null);

            holder = new ViewHolder();
            holder.giftImage = (ImageView) convertView
                    .findViewById(R.id.giftImage);
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            holder.fromText = (TextView) convertView
                    .findViewById(R.id.fromText);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        RecvGiftModel model = list.get(position);

        this.setImage(holder.giftImage,
                getImageUrl(model.getGift().getImage()),
                R.drawable.icon_default);
        holder.nameText.setText(model.getGift().getName());
        holder.fromText.setText(getString(R.string.from) + " : "
                + model.getUser().getNickname());
        return convertView;
    }

    class ViewHolder
    {
        ImageView giftImage;
        TextView nameText;
        TextView fromText;
    }

}
