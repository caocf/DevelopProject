package com.moge.gege.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.moge.gege.R;
import com.moge.gege.model.ScanEboxOrderModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Android on 2015/8/3.
 */
public class ScanOpenDeliveryAdapter extends BaseListAdapter<ScanEboxOrderModel> {

    private Context mContext;

    private OnItemDoorClick l;

    private HashMap<Integer, Integer> btnState;
    private int TAG_OPEN_SUCCESS = 1;
    private int TAG_OPEN_NORMAL = 0;

    public ScanOpenDeliveryAdapter(Context context) {
        this.mContext = context;
    }


    private void initState() {
        int count = list.size();
        btnState = new HashMap<Integer, Integer>();
        for (int i = 0; i < count; i++)
        {
            btnState.put(i, TAG_OPEN_NORMAL);
        }
    }

    public void checkState(int pos) {

        if (btnState != null) {
            if (btnState.containsKey(pos))
            {
                btnState.put(pos, TAG_OPEN_SUCCESS);
            }
            notifyDataSetChanged();
        }
    }

    public void setData(List<ScanEboxOrderModel> data) {
        list.addAll(data);
        initState();
        notifyDataSetChanged();
    }

    public void setListener(OnItemDoorClick l) {
        this.l = l;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_scan_open_delivery, null);

            holder.tv_box_door = (TextView) convertView.findViewById(R.id.tv_box_num);
            holder.tv_order_num = (TextView) convertView.findViewById(R.id.tv_order_num);
            holder.tv_delivery_time = (TextView) convertView.findViewById(R.id.tv_delivery_time);
            holder.bt_open = (Button) convertView.findViewById(R.id.bt_open);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ScanEboxOrderModel model = list.get(position);
        final String door = mContext.getString(R.string.tv_box_num, model.getRack_name() + model.getBox_name());
        holder.tv_order_num.setText(mContext.getString(R.string.tv_order_num, model.getNumber()));
        holder.tv_box_door.setText(door);
        holder.tv_delivery_time.setText(mContext.getString(R.string.tv_delivery_time, model.getDeliver_time()));
        holder.bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.ItemClick(position, model.getOrder_id(), model.getRack_name() + model.getBox_name());
                }
            }
        });
        if (btnState != null)
        {
            refresh(btnState.get(position), holder.bt_open);
        }

        return convertView;
    }

    private void refresh(int state, Button btn) {
        if (state == TAG_OPEN_SUCCESS) {
            btn.setText(mContext.getText(R.string.box_door_opened));
            btn.setClickable(false);
            btn.setBackgroundResource(R.drawable.bg_btn_gray);
        }
        else if (state==TAG_OPEN_NORMAL)
        {
            btn.setText(mContext.getText(R.string.btn_click_open_door));
            btn.setClickable(true);
            btn.setBackgroundResource(R.drawable.bg_btn_red);
        }

    }


    private class ViewHolder {
        TextView tv_order_num;
        TextView tv_box_door;
        TextView tv_delivery_time;
        Button bt_open;
    }


    public interface OnItemDoorClick {
        void ItemClick(int postion, String orderId, String doorNum);
    }
}
