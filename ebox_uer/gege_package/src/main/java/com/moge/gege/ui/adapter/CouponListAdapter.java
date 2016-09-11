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
import com.moge.gege.model.UserCouponModel;
import com.moge.gege.util.FunctionUtil;
import com.moge.gege.util.FunctionUtils;
import com.moge.gege.util.TimeUtil;

public class CouponListAdapter extends BaseCachedListAdapter<UserCouponModel>
        implements OnItemClickListener
{
    private CouponListListener mListener;
    private String mCouponId;

    public CouponListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(CouponListListener listener)
    {
        mListener = listener;
    }

    public void setSelectCoupon(String couponId)
    {
        mCouponId = couponId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_coupon, null);

            holder = new ViewHolder();

            holder.couponImage = (ImageView) convertView
                    .findViewById(R.id.couponImage);
            holder.descText = (TextView) convertView
                    .findViewById(R.id.descText);
            holder.expireTimeText = (TextView) convertView
                    .findViewById(R.id.expireTimeText);
            holder.nameText = (TextView) convertView
                    .findViewById(R.id.nameText);
            holder.priceText = (TextView) convertView
                    .findViewById(R.id.priceText);
            holder.statusText = (TextView) convertView
                    .findViewById(R.id.statusText);
            holder.selectedImage = (ImageView) convertView
                    .findViewById(R.id.selectedImage);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        UserCouponModel model = list.get(position);

        holder.descText.setText(model.getCoupon().getDescript());
        holder.expireTimeText.setText(TimeUtil.getDataStr(
                model.getExpried_time() * 1000));
        holder.nameText
                .setText(model.getCoupon().getName());
        holder.priceText.setText(getString(
                R.string.money,
                FunctionUtils
                        .getDouble(model.getCoupon().getFee() * 1.0 / 100)));

        String imageUrl = model.getCoupon().getLogo();
        if (model.isExpried())
        {
            holder.statusText.setText(getString(R.string.coupon_expired));
            holder.priceText.setTextColor(mContext.getResources()
                    .getColor(R.color.coupon_invalid_color));
            holder.statusText.setTextColor(mContext.getResources()
                    .getColor(R.color.coupon_invalid_color));
            this.setImage(holder.couponImage, getImageUrl(imageUrl),
                    R.drawable.icon_expired_coupon);
        }
        else
        {
            holder.statusText.setText(getString(R.string.coupon_not_use));
            holder.priceText.setTextColor(
                    mContext.getResources().getColor(R.color.coupon_fee_color));
            holder.statusText.setTextColor(mContext.getResources()
                    .getColor(R.color.coupon_status_color));
            this.setImage(holder.couponImage, getImageUrl(imageUrl),
                    R.drawable.icon_unexpired_coupon);
        }

        if (mCouponId.equals(model.get_id()))
        {
            holder.selectedImage.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.selectedImage.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class MyClickListener implements OnClickListener
    {
        UserCouponModel model;

        MyClickListener(UserCouponModel model)
        {
            this.model = model;
        }

        @Override
        public void onClick(View v)
        {
            if (mListener != null)
            {
                mListener.onCouponItemClick(model);
            }
        }
    }

    class ViewHolder
    {
        ImageView couponImage;
        TextView descText;
        TextView expireTimeText;
        TextView nameText;
        TextView priceText;
        TextView statusText;
        ImageView selectedImage;
    }

    public interface CouponListListener
    {
        public void onCouponItemClick(UserCouponModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (mListener != null)
        {
            mListener.onCouponItemClick(list.get(position - 1));
        }

    }

}
