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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.ActivityModel;
import com.moge.gege.model.AttachmentModel;
import com.moge.gege.model.LogisticsModel;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.TimeUtil;
import com.moge.gege.util.ViewUtil;
import com.moge.gege.util.widget.RoundedImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class LogisticsListAdapter extends BaseCachedListAdapter<LogisticsModel>
{
    public LogisticsListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
//        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_logistics, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        LogisticsModel model = list.get(position);

        holder.titleText.setText(model.getMessage());
        holder.timeText.setText(model.getTime());

        RelativeLayout.LayoutParams lineParams = (RelativeLayout.LayoutParams) holder.lineView.getLayoutParams();
        RelativeLayout.LayoutParams pointParams = (RelativeLayout.LayoutParams) holder.pointImage.getLayoutParams();

        if(position == 0)
        {
            lineParams.addRule(RelativeLayout.ALIGN_TOP, R.id.pointImage);
            lineParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.timeText);

            pointParams.width = FunctionUtil.dip2px(mContext, 15);
            pointParams.height = FunctionUtil.dip2px(mContext, 15);
            pointParams.leftMargin = FunctionUtil.dip2px(mContext, 4);

            holder.titleText.setTextColor(mContext.getResources().getColor(R.color.general_new_blue_color));
            holder.timeText.setTextColor(mContext.getResources().getColor(R.color.general_new_blue_color));

            holder.pointImage.setImageResource(R.drawable.general_new_blue_color);
            holder.pointImage.setBorderColor(mContext.getResources().getColor(R.color.logistics_border_color));
            holder.pointImage.setBorderWidth(5.0f);
        }
        else if(position == getCount() - 1)
        {
            lineParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lineParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.pointImage);
        }
        else
        {
            lineParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            pointParams.width = FunctionUtil.dip2px(mContext, 10);
            pointParams.height = FunctionUtil.dip2px(mContext, 10);
            pointParams.leftMargin = FunctionUtil.dip2px(mContext, 6);

            holder.titleText.setTextColor(mContext.getResources().getColor(R.color.text_666_color));
            holder.timeText.setTextColor(mContext.getResources().getColor(R.color.text_999_color));

            holder.pointImage.setImageResource(R.drawable.line_color);
            holder.pointImage.setBorderWidth(0.0f);
        }

        holder.pointImage.setLayoutParams(pointParams);
        holder.lineView.setLayoutParams(lineParams);

        return convertView;
    }


    class ViewHolder {
        @InjectView(R.id.lineView)
        View lineView;
        @InjectView(R.id.pointImage)
        RoundedImageView pointImage;
        @InjectView(R.id.titleText)
        TextView titleText;
        @InjectView(R.id.timeText)
        TextView timeText;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }


}
