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
import com.moge.gege.model.AttachmentModel;
import com.moge.gege.model.TopicModel;
import com.moge.gege.util.TimeUtil;

public class MyFavoriteAdapter extends BaseCachedListAdapter<TopicModel>
        implements OnItemClickListener
{
    private MyFavoriteListener mListener;

    public MyFavoriteAdapter(Context context)
    {
        super(context);
    }

    public void setListener(MyFavoriteListener listener)
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
                    R.layout.item_myfavorite, null);

            holder = new ViewHolder();
            holder.markImage = (ImageView) convertView
                    .findViewById(R.id.markImage);
            holder.haveImage = (ImageView) convertView
                    .findViewById(R.id.haveImage);
            holder.titleText = (TextView) convertView
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

        TopicModel model = list.get(position);

        AttachmentModel attachments = model.getAttachments();
        if (attachments != null && attachments.getImages().size() > 0)
        {
            holder.haveImage.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.haveImage.setVisibility(View.GONE);
        }

        if (model.getToped() == 1)
        {
            holder.markImage.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.markImage.setVisibility(View.GONE);
        }

        holder.titleText.setText(model.getTitle());
        holder.descText.setText(model.getDescript());
        holder.timeText.setText(getString(R.string.publish_time,
                String.valueOf(TimeUtil.timeToNow(model.getCrts()))));

        return convertView;
    }

    class ViewHolder
    {
        ImageView markImage;
        ImageView haveImage;
        TextView titleText;
        TextView descText;
        TextView timeText;
    }

    public interface MyFavoriteListener
    {
        public void onMyFavoriteClick(TopicModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onMyFavoriteClick(list.get(position - 1));
        }

    }

}
