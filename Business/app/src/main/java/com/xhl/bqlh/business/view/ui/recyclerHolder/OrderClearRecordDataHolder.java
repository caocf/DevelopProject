package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.OrderClearModel;
import com.xhl.bqlh.business.R;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Sum on 16/5/22.
 */
public class OrderClearRecordDataHolder extends RecyclerDataHolder<OrderClearModel> {

    public OrderClearRecordDataHolder(OrderClearModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View view = View.inflate(context, R.layout.item_order_clear_reocrd, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        return new SignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, OrderClearModel data) {
        SignViewHolder holder = (SignViewHolder) vHolder;
        holder.onBindData(data);
    }

    private class SignViewHolder extends RecyclerViewHolder {

        @ViewInject(R.id.tv_money)
        private TextView tv_money;

        @ViewInject(R.id.tv_maker)
        private TextView tv_maker;

        @ViewInject(R.id.tv_time)
        private TextView tv_time;

        @ViewInject(R.id.tv_state)
        private TextView tv_state;

        public SignViewHolder(View view) {
            super(view);
            x.view().inject(this, view);
        }

        public void onBindData(OrderClearModel model) {
            if (model != null) {
                tv_maker.setText("备注:" + model.getRemark());
                tv_time.setText(model.getCreationTime());
                tv_money.setText(model.getRepayment());
                int state = model.getCheckStatus();
                if (state == 1) {
                    tv_state.setText("待确认");
                } else if (state == 2) {
                    tv_state.setText("已确认");
                } else {
                    tv_state.setText("");
                }
            }
        }
    }
}
