package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.CourierModel;
import com.moge.gege.model.DeliveryBoxModel;
import com.moge.gege.model.GiftModel;
import com.moge.gege.util.TimeUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CourierListAdapter extends BaseCachedListAdapter<CourierModel> implements
        AdapterView.OnItemClickListener {

    private CourierListListener mListener;

    public CourierListAdapter(Context context) {
        super(context);
    }

    public void setListener(CourierListListener listener){
        mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_courier, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CourierModel model = list.get(position);

        this.setImage(holder.deliveryImage, getImageUrl(model.getDelivery_logo()) + GlobalConfig.IMAGE_STYLE90_90,
                R.drawable.icon_default_avatar);

        holder.deliveryNameText.setText(model.getDelivery_type());
        holder.courierNameText.setText(model.getName());
        holder.phoneText.setText(model.getMobile());

        return convertView;
    }

    class ViewHolder {
        @InjectView(R.id.deliveryImage)
        ImageView deliveryImage;
        @InjectView(R.id.deliveryNameText)
        TextView deliveryNameText;
        @InjectView(R.id.courierNameText)
        TextView courierNameText;
        @InjectView(R.id.phoneText)
        TextView phoneText;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public interface CourierListListener
    {
        public void onCourierItemClick(CourierModel model);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id)
    {
        if (mListener != null)
        {
            mListener.onCourierItemClick(list.get(position - 1));
        }
    }

}
