package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.OrderDetail;
import com.xhl.bqlh.business.R;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import java.math.BigDecimal;

/**
 * Created by Sum on 16/4/18.
 */
public class OrderProductDataHolder extends RecyclerDataHolder<OrderDetail> {
    public OrderProductDataHolder(OrderDetail data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View view = View.inflate(context, R.layout.item_order_product1, null);

        view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, OrderDetail data) {
        View parent = vHolder.itemView;
        TextView name = (TextView) parent.findViewById(R.id.tv_product_name);
        name.setText(data.getProductName());
        //数量
        TextView num = (TextView) parent.findViewById(R.id.tv_product_numb);
        BigDecimal dataNum = data.getNum();
        num.setText(context.getResources().getString(R.string.product_num, data.getNum()));

        TextView account_discount_price = (TextView) parent.findViewById(R.id.tv_product_account_discount_price);
        //优惠价
        TextView discount_price = (TextView) parent.findViewById(R.id.tv_product_discount_price);
        float discount = data.getDiscount();
        float unitPrice = data.getUnitPrice().floatValue();
        if (discount == 0) {
            discount_price.setText("");
            account_discount_price.setText("");
        } else {
            account_discount_price.setText(context.getResources().getString(R.string.product_price_discount_all, dataNum.intValue() * discount));
            discount_price.setText(context.getResources().getString(R.string.product_price_discount, unitPrice, data.getSkuResult().getUnit()));
        }

        //原价  = 单价+单个商品优惠价
        TextView origin_price = (TextView) parent.findViewById(R.id.tv_product_origin_price);
        origin_price.setText(context.getResources().getString(R.string.product_price_origin, discount + unitPrice, data.getSkuResult().getUnit()));


        //统计总价
        TextView price = (TextView) parent.findViewById(R.id.tv_product_account_price);
        price.setText(context.getResources().getString(R.string.product_price2, data.getTotalPrice()));

    }
}
