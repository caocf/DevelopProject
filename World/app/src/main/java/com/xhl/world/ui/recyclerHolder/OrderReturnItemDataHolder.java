package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.serviceOrder.OrderDetail;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Sum on 16/1/21.
 */
public class OrderReturnItemDataHolder extends RecyclerDataHolder {

    public OrderReturnItemDataHolder(OrderDetail data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_order_return, null);
//        int pixelSize = context.getResources().getDimensionPixelSize(R.dimen.item_order_height);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, 224));

        AutoUtils.autoSize(view);

        return new orderReturnRecyclerView(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        orderReturnRecyclerView vHo = (orderReturnRecyclerView) vHolder;
        vHo.onBindData((OrderDetail) data);
    }

    static class orderReturnRecyclerView extends RecyclerViewHolder {

        @ViewInject(R.id.iv_item_order_icon)
        private LifeCycleImageView iv_item_order_icon;

        @ViewInject(R.id.tv_order_id)
        private TextView tv_order_id;

        @ViewInject(R.id.tv_item_order_name)
        private TextView tv_item_order_name;

        @ViewInject(R.id.tv_item_order_price)
        private TextView tv_item_order_price;

        @ViewInject(R.id.tv_item_order_num)
        private TextView tv_item_order_num;

        public orderReturnRecyclerView(View view) {
            super(view);
            x.view().inject(this, view);
        }

        public void onBindData(OrderDetail orderDetail) {

            tv_order_id.setText("订单号:" + orderDetail.getStoreOrderCode());
            iv_item_order_icon.bindImageUrl(orderDetail.getProductPic());
            tv_item_order_name.setText(orderDetail.getProductName());
            tv_item_order_price.setText("￥" + orderDetail.getUnitPrice());

            tv_item_order_num.setText("申请数量:" + orderDetail.getApplyReturnNum());

        }
    }
}
