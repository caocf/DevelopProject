package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.ActivityModel;
import com.moge.gege.model.AttachmentModel;
import com.moge.gege.util.TimeUtil;

public class ActivityListAdapter extends BaseCachedListAdapter<ActivityModel>
        implements OnItemClickListener
{
    private ActivityListListener mListener;

    public ActivityListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(ActivityListListener listener)
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
                    R.layout.item_activity, null);

            holder = new ViewHolder();
            holder.activityImage = (ImageView) convertView
                    .findViewById(R.id.activityImage);
            holder.titleText = (TextView) convertView
                    .findViewById(R.id.titleText);
            holder.descText = (TextView) convertView
                    .findViewById(R.id.descText);
            holder.timeText = (TextView) convertView
                    .findViewById(R.id.timeText);
            holder.locationText = (TextView) convertView
                    .findViewById(R.id.locationText);
            holder.applyCountText = (TextView) convertView
                    .findViewById(R.id.applyCountText);
            holder.applyBtn = (Button) convertView.findViewById(R.id.applyBtn);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        ActivityModel model = list.get(position);

        AttachmentModel attachments = model.getAttachments();
        if (attachments != null && attachments.getImages().size() > 0)
        {
            this.setImage(holder.activityImage, getImageUrl(attachments
                    .getImages().get(0)) + GlobalConfig.IMAGE_STYLE90_90,
                    R.drawable.icon_default_activity);
        }
        else
        {
            holder.activityImage
                    .setImageResource(R.drawable.icon_default_activity);
        }

        holder.titleText.setText(model.getTitle());
        holder.descText.setText(model.getDescript());
        holder.timeText
                .setText(TimeUtil.getDateTimeStr(model.getStart_time() * 1000));
        holder.locationText.setText(model.getLocation());
        holder.applyCountText.setText(model.getApply_num()
                + getString(R.string.apply_unit_desc));

        if (model.isApplyed())
        {
            holder.applyBtn.setText(getString(R.string.cancel));
            holder.applyBtn.setBackgroundResource(R.drawable.bg_btn_gray);
        }
        else
        {
            holder.applyBtn.setText(getString(R.string.apply));
            holder.applyBtn.setBackgroundResource(R.drawable.bg_btn_green);
        }

        holder.applyBtn.setOnClickListener(new MyClickListener(model));

        return convertView;
    }

    public class MyClickListener implements OnClickListener
    {
        ActivityModel model;

        MyClickListener(ActivityModel model)
        {
            this.model = model;
        }

        @Override
        public void onClick(View v)
        {
            if (mListener != null)
            {
                mListener.onActivityApplyClick(model);
            }
        }
    }

    class ViewHolder
    {
        ImageView activityImage;
        TextView titleText;
        TextView timeText;
        TextView locationText;
        TextView descText;
        TextView applyCountText;
        Button applyBtn;
    }

    public interface ActivityListListener
    {
        public void onActivityApplyClick(ActivityModel model);

        public void onActivityClick(ActivityModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onActivityClick(list.get(position - 1));
        }

    }

}
