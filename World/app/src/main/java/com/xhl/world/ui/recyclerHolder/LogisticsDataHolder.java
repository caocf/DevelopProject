package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.LogisticsDelivery;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Sum on 16/2/13.
 */
public class LogisticsDataHolder extends RecyclerDataHolder {
    public LogisticsDataHolder(LogisticsDelivery data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_logistics, null);

        return new logisticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        logisticsViewHolder holder = (logisticsViewHolder) vHolder;
        holder.onBindData((LogisticsDelivery) data);
    }

    static class logisticsViewHolder extends RecyclerViewHolder {

        @ViewInject(R.id.tv_logistics_time)
        private TextView tv_logistics_time;

        @ViewInject(R.id.tv_logistics_status)
        private TextView tv_logistics_status;

        @ViewInject(R.id.iv_logistics_pass)
        private View iv_logistics_pass;

        @ViewInject(R.id.iv_logistics_cur)
        private View iv_logistics_cur;

        @ViewInject(R.id.view_logistics_line)
        private View line1;

        private int mHeight = 0;

        private View mRootView;

        public logisticsViewHolder(final View view) {
            super(view);
            AutoUtils.auto(view);
            x.view().inject(this, view);
            mRootView = view;

            view.measure(0, 0);

            mHeight = view.getMeasuredHeight();
        }

        public void onBindData(LogisticsDelivery delivery) {
            ViewGroup.LayoutParams params = line1.getLayoutParams();
            if (params != null) {
                params.height = mHeight;
            }

            if (delivery == null) {
                return;
            }
            tv_logistics_time.setText(delivery.getTime());
            tv_logistics_status.setText(delivery.getContext());
            if (delivery.isLeast()) {
                iv_logistics_pass.setVisibility(View.INVISIBLE);
                iv_logistics_cur.setVisibility(View.VISIBLE);
            } else {
                iv_logistics_pass.setVisibility(View.VISIBLE);
                iv_logistics_cur.setVisibility(View.INVISIBLE);
            }
        }
    }
}
