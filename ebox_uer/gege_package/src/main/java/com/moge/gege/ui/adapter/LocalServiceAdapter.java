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
import com.moge.gege.model.LocalServiceModel;

public class LocalServiceAdapter extends
        BaseCachedListAdapter<LocalServiceModel> implements OnItemClickListener
{
    private LocalServiceListener mListener;

    public LocalServiceAdapter(Context context)
    {
        super(context);
    }

    public void setListener(LocalServiceListener listener)
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
                    R.layout.item_localservice, null);

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

    public interface LocalServiceListener
    {
        public void onServiceClick(int position, LocalServiceModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onServiceClick(position, list.get(position));
        }
    }

}
