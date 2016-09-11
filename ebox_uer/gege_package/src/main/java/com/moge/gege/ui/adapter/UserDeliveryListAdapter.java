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
import com.moge.gege.model.BoardModel;
import com.moge.gege.model.DeliveryBoxModel;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.TimeUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserDeliveryListAdapter extends BaseCachedListAdapter<DeliveryBoxModel> {

    public UserDeliveryListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_delivery, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DeliveryBoxModel model = list.get(position);

        if (!TextUtils.isEmpty(model.getDelivery_logo())) {

            this.setImage(holder.deliveryImage, getImageUrl(model.getDelivery_logo()) + GlobalConfig.IMAGE_STYLE90_90,
                    R.drawable.icon_default_avatar);
        }
        else
        {
            holder.deliveryImage.setImageResource(R.drawable.icon_default_avatar);
        }

        holder.deliveryNameText.setText(model.getDelivery_name());
        if (model.getState() >= 4 && model.getState() <= 7) {
            holder.deliveryTimeText.setText(TimeUtil.timeToNow(model.getFetch_time()));
            holder.passwordText.setText(R.string.delivery_finish);
            holder.passwordText.setTextColor(mContext.getResources().getColor(R.color.text_666_color));

        } else {
            holder.deliveryTimeText.setText(TimeUtil.timeToNow(model.getDeliver_time()));
            holder.passwordText.setText(getString(R.string.box_password, model.getPassword()));
            holder.passwordText.setTextColor(mContext.getResources().getColor(R.color.general_new_red_color));
        }
        holder.courierNameText.setText(model.getOperator_name());
        holder.deliveryNumberText.setText(getString(R.string.delivery_number, model.getNumber()));
        holder.boxNumberText.setText(getString(R.string.box_name, model.getRack_name() + model.getBox_name()));


        if (model.isShowHeader()) {
            holder.historyCountText.setVisibility(View.VISIBLE);
        } else {
            holder.historyCountText.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        @InjectView(R.id.deliveryImage)
        ImageView deliveryImage;
        @InjectView(R.id.deliveryNameText)
        TextView deliveryNameText;
        @InjectView(R.id.courierNameText)
        TextView courierNameText;
        @InjectView(R.id.deliveryNumberText)
        TextView deliveryNumberText;
        @InjectView(R.id.deliveryTimeText)
        TextView deliveryTimeText;
        @InjectView(R.id.boxNumberText)
        TextView boxNumberText;
        @InjectView(R.id.passwordText)
        TextView passwordText;

        @InjectView(R.id.historyCountText)
        TextView historyCountText;


        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
