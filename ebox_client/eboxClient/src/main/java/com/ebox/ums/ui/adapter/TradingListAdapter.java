package com.ebox.ums.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.mall.warehouse.model.AttachmentModel;
import com.ebox.mall.warehouse.model.TradingModel;
import com.ebox.pub.utils.FunctionUtils;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.adapter.BaseCachedListAdapter;
import com.ebox.pub.utils.MGViewUtil;


public class TradingListAdapter extends BaseCachedListAdapter<TradingModel>
        implements OnItemClickListener {
    private TradingListListener mListener;

    public TradingListAdapter(Context context) {
        super(context);
    }

    public void setListener(TradingListListener listener) {
        mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.mall_item_trading, null);

            holder = new ViewHolder();

            holder.tradingImage = (ImageView) convertView
                    .findViewById(R.id.tradingImage);
            holder.titleText = (TextView) convertView
                    .findViewById(R.id.titleText);
//            holder.praiseText = (TextView) convertView
//                    .findViewById(R.id.praiseText);
//            holder.discountText = (TextView) convertView
//                    .findViewById(R.id.discountText);
            holder.oldPriceText = (TextView) convertView
                    .findViewById(R.id.oldPriceText);
            holder.newPriceText = (TextView) convertView
                    .findViewById(R.id.newPriceText);
            convertView.setTag(holder);
            MGViewUtil.scaleContentView((ViewGroup) convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TradingModel model = list.get(position);

        AttachmentModel attachModel = model.getAttachments();
        if (attachModel != null && attachModel.getImages().size() > 0) {
            this.setImage(holder.tradingImage, getImageUrl(attachModel
                    .getImages().get(0)) + GlobalField.IMAGE_STYLE90_90, 0);
        }

        holder.titleText.setText(model.getTitle());
        holder.oldPriceText.setText(getString(R.string.mall_money,
                FunctionUtils.getDouble(model.getPrice() * 1.0 / 100)));
        holder.oldPriceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        holder.newPriceText
                .setText(getString(R.string.mall_money, FunctionUtils
                        .getDouble(model.getDiscount_price() * 1.0 / 100)));

//        String praisePercent = "100";
//        if (model.getAppraise_count() != 0)
//        {
//            praisePercent = FunctionUtils
//                    .getDouble((double) (model.getFavorable_count() * 1.0 / model
//                            .getAppraise_count()) * 100);
//        }
//        holder.praiseText.setText(getString(R.string.praise) + praisePercent
//                + "%");

//        double discount = (double) ((model.getDiscount_price() * 1.0 / model
//                .getPrice()) * 10);

//        String discountDesc = getString(R.string.free);
//        if (discount > 0.0)
//        {
//            DecimalFormat df = new DecimalFormat("0.0");
//            discountDesc = df.format(discount);
//            holder.discountText.setText(discountDesc
//                    + getString(R.string.discount));
//        }
//        else
//        {
//            holder.discountText.setText(discountDesc);
//        }

        return convertView;
    }

    class ViewHolder {
        ImageView tradingImage;
        TextView titleText;

        TextView discountText;
        TextView oldPriceText;
        TextView newPriceText;
    }

    public static abstract interface TradingListListener {
        public abstract void onTradingItemClick(TradingModel model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (mListener != null) {
            mListener.onTradingItemClick(list.get(position));
        }
    }

}
