package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.TopicModel;

public class TopedListAdapter extends BaseCachedListAdapter<TopicModel>
        implements OnItemClickListener
{
    private TopedListListener mListener;

    public TopedListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(TopedListListener listener)
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
                    R.layout.item_toped, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.topedText);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        TopicModel model = list.get(position);
        holder.name.setText(model.getTitle());
        return convertView;
    }

    class ViewHolder
    {
        TextView name;
    }

    public interface TopedListListener
    {
        public void onTopedListClick(TopicModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        // TODO Auto-generated method stub
        if (mListener != null)
        {
            mListener.onTopedListClick(list.get(position));
        }
    }

}
