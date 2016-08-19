package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.ProductReturnDetail;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.custom.ComplexText;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Summer on 2016/7/28.
 */
public class OrderReturnProductDataHolder extends RecyclerDataHolder<ProductReturnDetail> {

    public OrderReturnProductDataHolder(ProductReturnDetail data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View view = View.inflate(context, R.layout.item_order_product_return, null);

        view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ProductReturnDetail data) {

        View content = vHolder.itemView;

        TextView tv_product_name = (TextView) content.findViewById(R.id.tv_product_name);
        tv_product_name.setText(data.getProductName());

        TextView tv_product_numb = (TextView) content.findViewById(R.id.tv_product_numb);
        String text = "退货数量：x" + data.getNum();
        ComplexText.TextBuilder b0 = new ComplexText.TextBuilder(text);
        b0.setTextColor(ContextCompat.getColor(context, R.color.colorAccent), 5, text.length());
        tv_product_numb.setText(b0.Build());

        TextView tv_product_origin_price = (TextView) content.findViewById(R.id.tv_product_origin_price);
        String orderMoney = context.getString(R.string.product_price_4, data.getApplyReturnPrice());
        ComplexText.TextBuilder b1 = new ComplexText.TextBuilder(orderMoney);
        int color = ContextCompat.getColor(context, R.color.app_price_color);
        b1.setTextColor(color, 4, orderMoney.length());
        tv_product_origin_price.setText(b1.Build());

        TextView tv_product_verify_price = (TextView) content.findViewById(R.id.tv_product_verify_price);
        String ver = context.getString(R.string.product_price_5, data.getVerifyReturnPrice());
        ComplexText.TextBuilder b2 = new ComplexText.TextBuilder(ver);
        b2.setTextColor( ContextCompat.getColor(context, R.color.app_red), 4, ver.length());
        tv_product_verify_price.setText(b2.Build());
    }
}
