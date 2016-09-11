package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.NoticeModel;
import com.moge.gege.util.TimeUtil;
import com.moge.gege.util.widget.LinkTextView;

public class NoticeAdapter extends BaseCachedListAdapter<NoticeModel> implements
        OnItemClickListener
{
    private NoticeListener mListener;

    public NoticeAdapter(Context context)
    {
        super(context);
    }

    public void setListener(NoticeListener listener)
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
                    R.layout.item_notice, null);

            holder = new ViewHolder();
            holder.titleText = (LinkTextView) convertView
                    .findViewById(R.id.titleText);
            holder.descText = (TextView) convertView
                    .findViewById(R.id.descText);
            holder.timeText = (TextView) convertView
                    .findViewById(R.id.timeText);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        NoticeModel model = list.get(position);

        holder.titleText.setLinkText(model.getTitle());
        holder.descText.setText(model.getContent());
        holder.timeText.setText(TimeUtil.timeToNow(model.getCrts()));

        return convertView;
    }

    class ViewHolder
    {
        LinkTextView titleText;
        TextView descText;
        TextView timeText;
    }

    public interface NoticeListener
    {
        public void onNoticeItemClick(NoticeModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onNoticeItemClick(list.get(position - 1));
        }

    }

}
