package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.App.TemporaryTask;
import com.xhl.bqlh.business.R;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.xhl.xhl_library.utils.TimeUtil;

/**
 * Created by Sum on 16/4/19.
 */
public class TemporaryTaskDataHolder extends RecyclerDataHolder<TemporaryTask> {

    public TemporaryTaskDataHolder(TemporaryTask data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new RecyclerViewHolder(View.inflate(context, R.layout.item_temporary_task, null));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, final TemporaryTask data) {
        View parent = vHolder.itemView;

        try {
            TextView week = (TextView) parent.findViewById(R.id.tv_task_shop_week);
            week.setText(TimeUtil.WEEK[data.getTaskShop().getWeek() - 1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView name = (TextView) parent.findViewById(R.id.tv_task_shop_name);
        name.setText(data.getTaskShop().getShopName());
        TextView location = (TextView) parent.findViewById(R.id.tv_task_shop_location);
        location.setText("地址:" + data.getTaskShop().getShopLocation());

        final CheckBox state = (CheckBox) parent.findViewById(R.id.cb_state);

        boolean checked = data.isChecked();
        state.setChecked(checked);

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean dataChecked = data.isChecked();
                state.setChecked(!dataChecked);
                data.setChecked(!dataChecked);
            }
        });

    }
}
