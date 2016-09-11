package com.ebox.mgt.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.pub.ui.adapter.BaseListAdapter;
import com.ebox.pub.utils.MGViewUtil;

public class FragOrderAdapter extends BaseListAdapter<OrderLocalInfo> {

    private Context mContext;

    public FragOrderAdapter(Context context) {
        mContext = context;
    }

    @Override
    public View getView(int position, View layout, ViewGroup parent) {
        ViewHolder holder;
        if (layout == null) {
            holder = new ViewHolder();
            layout = View.inflate(mContext, R.layout.ex_item_order, null);
            holder.tv_itemId = (TextView) layout.findViewById(R.id.tv_itemId);
            holder.tv_boxId = (TextView) layout.findViewById(R.id.tv_boxId);
            holder.tv_oid = (TextView) layout.findViewById(R.id.tv_oid);
            holder.tv_password = (TextView) layout
                    .findViewById(R.id.tv_password);
            holder.tv_state = (TextView) layout.findViewById(R.id.tv_state);
            holder.tv_time_state = (TextView) layout.findViewById(R.id.tv_time_state);
            holder.tv_time_out_state = (TextView) layout.findViewById(R.id.tv_time_out_state);
            layout.setTag(holder);
            MGViewUtil.scaleContentView((ViewGroup) layout);
        } else {
            holder = (ViewHolder) layout.getTag();
        }
        OrderLocalInfo order = list.get(position);
        holder.tv_oid.setText("收件人:" + order.getCustomer_telephone());
        holder.tv_itemId.setText("运单号:" + order.getItem_id());
        holder.tv_boxId.setText("箱门号:" + order.getBox_code());
        holder.tv_password.setText("派件人:" + order.getOperator_telephone());
        holder.tv_time_state.setText("投递时间 :"+order.getDelivery_at());

        holder.tv_time_out_state.setText(getTime(order.getTime_out()));
        holder.tv_state.setText(getState(order.getOrder_state()));
        return layout;
    }

    private String getTime(Integer time_out) {

        if (time_out != 0)
        {
            return "超期";
        }
        return "未超期";
    }

    private String getState(Integer state) {
        if (state == 0)
        {
            return "生成";
        }
        else if (state == 1)
        {
            return "取走";
        }
        return null;
    }

    class ViewHolder {
        TextView tv_oid;
        TextView tv_itemId;
        TextView tv_boxId;
        TextView tv_password;
        TextView tv_state;
        TextView tv_time_state;
        TextView tv_time_out_state;
    }

}
