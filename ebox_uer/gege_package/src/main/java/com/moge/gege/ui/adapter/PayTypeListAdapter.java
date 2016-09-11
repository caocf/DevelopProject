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
import com.moge.gege.model.BaseOptionModel;
import com.moge.gege.util.TimeUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PayTypeListAdapter extends BaseCachedListAdapter<BaseOptionModel>
{

    private int mSelectedIndex = 0;

    public PayTypeListAdapter(Context context)
    {
        super(context);
    }

    public void setSelectedIndex(int index)
    {
        mSelectedIndex = index;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_paytype, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        BaseOptionModel model = list.get(position);

        if(model.getResId()!= 0)
        {
            holder.payImage.setImageResource(model.getResId());
        }
        holder.payName.setText(model.getName());

        if (position != mSelectedIndex)
        {
            holder.selectedImage.setVisibility(View.GONE);
        }
        else
        {
            holder.selectedImage.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class ViewHolder
    {
        @InjectView(R.id.payImage) ImageView payImage;
        @InjectView(R.id.payName) TextView payName;
        @InjectView(R.id.selectedImage) ImageView selectedImage;

        public ViewHolder(View view)
        {
            ButterKnife.inject(this, view);
        }
    }

}
