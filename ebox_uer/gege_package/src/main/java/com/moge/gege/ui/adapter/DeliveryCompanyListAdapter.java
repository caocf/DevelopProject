package com.moge.gege.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
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
import com.moge.gege.model.DeliveryCompanyModel;
import com.moge.gege.util.TimeUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DeliveryCompanyListAdapter extends BaseCachedListAdapter<DeliveryCompanyModel>
{

    public DeliveryCompanyListAdapter(Context context)
    {
        super(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_delivery_company, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        DeliveryCompanyModel model = list.get(position);
        if (!TextUtils.isEmpty(model.getLogo()))
        {
            this.setImage(holder.deliveryImage, getImageUrl(model.getLogo()) + GlobalConfig.IMAGE_STYLE90_90,
                    R.drawable.icon_default_avatar);
        }
        else
        {
            holder.deliveryImage
                    .setImageResource(R.drawable.icon_default_avatar);
        }

        holder.deliveryNameText.setText(model.getName());
        holder.deliveryTypeText.setText(model.getDelivery_type());
        return convertView;
    }

    class ViewHolder
    {
        @InjectView(R.id.deliveryImage) ImageView deliveryImage;
        @InjectView(R.id.deliveryNameText) TextView deliveryNameText;
        @InjectView(R.id.deliveryTypeText) TextView deliveryTypeText;

        public ViewHolder(View view){
            ButterKnife.inject(this, view);
        }
    }


}
