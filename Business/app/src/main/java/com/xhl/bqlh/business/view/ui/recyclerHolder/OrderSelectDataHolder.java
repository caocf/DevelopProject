package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.OrderModel;
import com.xhl.bqlh.business.R;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/4/19.
 */
public class OrderSelectDataHolder extends RecyclerDataHolder<OrderModel> {

    public OrderSelectDataHolder(OrderModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new RecyclerViewHolder(View.inflate(context, R.layout.item_order_select, null));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, final OrderModel data) {
        View parent = vHolder.itemView;
        if (data == null) {
            return;
        }
        //订单类型
        TextView tv_order_type = (TextView) parent.findViewById(R.id.tv_order_type);
        tv_order_type.setText(data.getOrderTypeDesc());
        //订单编号
        TextView tv_order_num = (TextView) parent.findViewById(R.id.tv_order_num);
        tv_order_num.setText(context.getString(R.string.order_num, data.getStoreOrderCode()));
        //支付金额
        TextView tv_order_money = (TextView) parent.findViewById(R.id.tv_order_money);
        tv_order_money.setText(context.getString(R.string.order_money, data.getRealOrderMoney()));
        //收货人
        TextView tv_order_shop = (TextView) parent.findViewById(R.id.tv_order_shop);
        tv_order_shop.setText(data.getReceivingName());


        final CheckBox state = (CheckBox) parent.findViewById(R.id.cb_state);
        state.setChecked(data.isChecked);

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean dataChecked = data.isChecked;
                state.setChecked(!dataChecked);
                data.isChecked = !dataChecked;
//                Logger.v("isChecked:" + state.isChecked());
            }
        });

    }
}
