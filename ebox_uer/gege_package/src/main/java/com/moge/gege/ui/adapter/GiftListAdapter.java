package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.GiftModel;

public class GiftListAdapter extends BaseCachedListAdapter<GiftModel> implements
        OnItemClickListener
{
    private GiftListListener mListener;

    public GiftListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(GiftListListener listener)
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
                    R.layout.item_gift, null);

            holder = new ViewHolder();
            holder.giftImage = (ImageView) convertView
                    .findViewById(R.id.giftImage);
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            holder.priceText = (TextView) convertView
                    .findViewById(R.id.priceText);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        GiftModel model = list.get(position);

        this.setImage(holder.giftImage, getImageUrl(model.getImage()),
                R.drawable.icon_default);
        holder.nameText.setText(model.getName());
        holder.priceText.setText(getString(R.string.points,
                model.getIntegration()));
        return convertView;
    }

    class ViewHolder
    {
        ImageView giftImage;
        TextView nameText;
        TextView priceText;
    }

    public interface GiftListListener
    {
        public void onGiftItemClick(GiftModel model);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onGiftItemClick(list.get(position));
        }
    }

}
