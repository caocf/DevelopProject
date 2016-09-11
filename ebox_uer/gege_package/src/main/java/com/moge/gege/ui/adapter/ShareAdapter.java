package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.LocalServiceModel;
import com.moge.gege.util.widget.horizontalListview.widget.AdapterView.OnItemClickListener;

public class ShareAdapter extends BaseCachedListAdapter<LocalServiceModel>
        implements OnItemClickListener
{
    private ShareListener mListener;

    public ShareAdapter(Context context)
    {
        super(context);
    }

    public void setListener(ShareListener listener)
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
                    R.layout.item_share, null);

            holder = new ViewHolder();
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            holder.iconImage = (ImageView) convertView
                    .findViewById(R.id.iconImage);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        LocalServiceModel model = list.get(position);
        holder.iconImage.setImageResource(model.getResId());
        holder.nameText.setText(model.getName());

        return convertView;
    }

    class ViewHolder
    {
        ImageView iconImage;
        TextView nameText;
    }

    public interface ShareListener
    {
        public void onShareItemClick(int position, LocalServiceModel model);
    }

    @Override
    public void onItemClick(
            com.moge.gege.util.widget.horizontalListview.widget.AdapterView<?> parent,
            View view, int position, long id)
    {
        if (mListener != null)
        {
            mListener.onShareItemClick(position, list.get(position));
        }
    }

}
