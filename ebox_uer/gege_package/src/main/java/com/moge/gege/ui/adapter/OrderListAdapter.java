package com.moge.gege.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;
import com.moge.gege.model.AttachmentModel;
import com.moge.gege.model.GoodItemModel;
import com.moge.gege.model.OrderModel;
import com.moge.gege.model.TradingModel;
import com.moge.gege.model.enums.OrderStatusType;
import com.moge.gege.util.FunctionUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OrderListAdapter extends BaseCachedListAdapter<OrderModel>
{
    private OrderListListener mListener;

    public OrderListAdapter(Context context)
    {
        super(context);
    }

    public void setListener(OrderListListener listener)
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
                    R.layout.item_order, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderModel model = list.get(position);
        GoodItemModel goodModel = model.getGoods().get(0);
        TradingModel tradingModel = goodModel.getTrading();

        AttachmentModel attachModel = tradingModel.getAttachments();
        if (attachModel != null && attachModel.getImages().size() > 0)
        {
            this.setImage(holder.tradingImage, getImageUrl(attachModel
                    .getImages().get(0)) + GlobalConfig.IMAGE_STYLE90_90,
                    R.drawable.icon_default);
        }
        else
        {
            holder.tradingImage.setImageResource(R.drawable.icon_default);
        }

        if (model.getGoods().size() > 1)
        {
            holder.multiGoodsText.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.multiGoodsText.setVisibility(View.GONE);
        }

        holder.agreeCheckBox.setVisibility(View.GONE);
        holder.titleText.setText(tradingModel.getTitle());
        holder.moneyText.setText(getString(R.string.money,
                FunctionUtils.getDouble((model.getPay_fee()) * 1.0 / 100)));
        holder.orderTimeText.setText(model.getOrder_at());
        holder.orderStatusText.setOnClickListener(new MyClickListener(model));
        if (model.getStatus() == OrderStatusType.NOT_PAYED
                || model.getStatus() == OrderStatusType.PAY_CANCEL
                || model.getStatus() == OrderStatusType.PAY_FAILED
                || model.getStatus() == OrderStatusType.PAY_ING)
        {
            holder.orderStatusText.setEnabled(true);
            if(model.getStatus() == OrderStatusType.PAY_ING)
            {
                holder.orderStatusText.setText(getString(R.string.to_pay));
            }
            else
            {
                holder.orderStatusText.setText(model.getStatus_verbose_name());
            }

            holder.orderStatusText
                    .setBackgroundResource(R.drawable.bg_secondpay_corner);
            holder.orderStatusText.setTextColor(mContext.getResources()
                    .getColor(R.color.order_secondpay_color));
        }
        else
        {
            holder.orderStatusText.setEnabled(false);
            holder.orderStatusText.setText(model.getStatus_verbose_name());
            holder.orderStatusText.setBackgroundResource(0);
            holder.orderStatusText.setTextColor(mContext.getResources()
                    .getColor(R.color.general_gray_color));
        }

        return convertView;
    }

    public class MyClickListener implements OnClickListener
    {
        OrderModel model;

        MyClickListener(OrderModel model)
        {
            this.model = model;
        }

        @Override
        public void onClick(View v)
        {
            if (mListener != null)
            {
                mListener.onOrderPayBtnClick(model);
            }
        }
    }

    class ViewHolder
    {
        @InjectView(R.id.agreeCheckBox) CheckBox agreeCheckBox;
        @InjectView(R.id.tradingImage) ImageView tradingImage;
        @InjectView(R.id.multiGoodsText) TextView multiGoodsText;
        @InjectView(R.id.titleText) TextView titleText;
        @InjectView(R.id.moneyTitle) TextView moneyTitle;
        @InjectView(R.id.moneyText) TextView moneyText;
        @InjectView(R.id.buyNumText) TextView buyNumText;

        @InjectView(R.id.editNumLayout) LinearLayout editNumLayout;
        @InjectView(R.id.reduceNumBtn) Button reduceNumBtn;
        @InjectView(R.id.plusNumBtn) Button plusNumBtn;
        @InjectView(R.id.editNumText) TextView editNumText;

        @InjectView(R.id.orderTimeText) TextView orderTimeText;
        @InjectView(R.id.orderStatusText) TextView orderStatusText;

        public ViewHolder(View view)
        {
            ButterKnife.inject(this, view);
        }

    }

    public interface OrderListListener
    {
        public void onOrderPayBtnClick(OrderModel model);
    }

}
