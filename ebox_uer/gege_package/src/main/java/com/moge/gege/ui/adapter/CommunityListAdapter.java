package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.CommunityModel;

public class CommunityListAdapter extends BaseListAdapter<CommunityModel>
        implements OnItemClickListener
{
    private Context mContext;
    private OnCommunityListener mListener;

    public CommunityListAdapter(Context context)
    {
        mContext = context;
    }

    public void setListener(OnCommunityListener listener)
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
                    R.layout.item_community, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        CommunityModel model = list.get(position);
        holder.name.setText(model.getName());
        holder.address.setText(model.getAddress());
        return convertView;
    }

    class ViewHolder
    {
        TextView name;
        TextView address;
    }

    public static abstract interface OnCommunityListener
    {
        public abstract void onCommunityItemClick(CommunityModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onCommunityItemClick(list.get(position - 1));
        }
    }

}
