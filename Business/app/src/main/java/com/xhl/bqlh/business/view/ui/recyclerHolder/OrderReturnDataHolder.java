package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.ProductReturn;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.ui.activity.OrderReturnDetailsActivity;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Summer on 2016/7/28.
 */
public class OrderReturnDataHolder extends RecyclerDataHolder<ProductReturn> {

    public OrderReturnDataHolder(ProductReturn data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View view = View.inflate(context, R.layout.item_order_return, null);

        view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Context context, int position, RecyclerView.ViewHolder vHolder, final ProductReturn data) {
        final View content = vHolder.itemView;

        TextView tv_shop_name = (TextView) content.findViewById(R.id.tv_shop_name);
        tv_shop_name.setText(data.getRetailerName());

        TextView tv_apply_state = (TextView) content.findViewById(R.id.tv_apply_state);
        tv_apply_state.setText(data.getReturnState());

        TextView tv_apply_time = (TextView) content.findViewById(R.id.tv_apply_time);
        tv_apply_time.setText(data.getApplyTime());

        TextView tv_apply_remark = (TextView) content.findViewById(R.id.tv_apply_remark);
        tv_apply_remark.setText(data.getReturnDesc());

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderReturnDetailsActivity.class);
                intent.putExtra("id", data.getReturnId());
                context.startActivity(intent);
            }
        });
    }
}
