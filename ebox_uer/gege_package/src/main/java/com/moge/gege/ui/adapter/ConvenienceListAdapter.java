package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.ConvenienceModel;

public class ConvenienceListAdapter extends
        BaseCachedListAdapter<ConvenienceModel> implements OnItemClickListener
{
    private ConvenienceListListener mListener;

    public ConvenienceListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(ConvenienceListListener listener)
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
                    R.layout.item_convenience, null);

            holder = new ViewHolder();
            holder.titleText = (TextView) convertView
                    .findViewById(R.id.titleText);
            holder.addressText = (TextView) convertView
                    .findViewById(R.id.addressText);
            holder.dialCountText = (TextView) convertView
                    .findViewById(R.id.dialCountText);
            holder.dialLayout = (LinearLayout) convertView
                    .findViewById(R.id.dialLayout);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        ConvenienceModel model = list.get(position);

        holder.titleText.setText(model.getTitle());
        holder.addressText.setText(model.getAddress());
        holder.dialCountText.setText(model.getDial_count()
                + getString(R.string.dialcount_unit));

        holder.dialLayout.setOnClickListener(new MyClickListener(model));

        return convertView;
    }

    public class MyClickListener implements OnClickListener
    {
        ConvenienceModel model;

        MyClickListener(ConvenienceModel model)
        {
            this.model = model;
        }

        @Override
        public void onClick(View v)
        {
            if (mListener != null)
            {
                mListener.onConveniencePhoneClick(model);
            }
        }
    }

    class ViewHolder
    {
        TextView titleText;
        TextView addressText;
        TextView dialCountText;
        LinearLayout dialLayout;
    }

    public interface ConvenienceListListener
    {
        public void onConveniencePhoneClick(ConvenienceModel model);

        public void onConvenienceItemClick(ConvenienceModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onConvenienceItemClick(list.get(position - 1));
        }
    }
}
