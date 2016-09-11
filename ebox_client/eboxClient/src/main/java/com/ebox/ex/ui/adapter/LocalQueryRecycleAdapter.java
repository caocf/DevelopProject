package com.ebox.ex.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.MGViewUtil;

import java.util.List;

/**
 * Created by Android on 2015/10/30.
 */
public class LocalQueryRecycleAdapter extends RecyclerView.Adapter {

    private List<OrderLocalInfo> mOrders;
    private Context mContext;
    private LocalQueryListener listener;
    private boolean isQueryLocal;
    private int white;

    public LocalQueryRecycleAdapter(List<OrderLocalInfo> mOrders, LocalQueryListener listener, boolean isQueryLocal) {
        this.mOrders = mOrders;
        this.listener = listener;
        this.isQueryLocal = isQueryLocal;

    }

    public void addData(List<OrderLocalInfo> orders) {
        if (mOrders == null) {
            mOrders = orders;
        } else {

            mOrders.addAll(orders);
        }
        notifyDataSetChanged();
    }

    public void setData(List<OrderLocalInfo> orders) {
        if (mOrders != null) {
            mOrders.clear();
        }
        mOrders = orders;
        notifyDataSetChanged();
    }

    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view;
        if (!isQueryLocal) {
            view = LayoutInflater.from(mContext).inflate(R.layout.ex_bar_recycle_order_item_1, null);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.ex_bar_recycle_order_item, null);
        }
        MGViewUtil.scaleContentView((ViewGroup) view);
        white = mContext.getResources().getColor(R.color.ex_while);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderLocalInfo orderLocalInfo = mOrders.get(position);

        ViewHolder myView = (ViewHolder) holder;
        String time1 = getTime1(orderLocalInfo.getDelivery_at());
        String time2 = getTime2(orderLocalInfo.getDelivery_at());
        if (position % 2 != 0) {
            myView.ll_query_item.setBackgroundColor(white);
        }
        myView.tv_time1.setText(time1);
        myView.tv_time2.setText(time2);
        myView.tv_item_id.setText(orderLocalInfo.getItem_id());

        if (isQueryLocal)//本地查询
        {
            //超期状态
            checkTimeState(myView.tv_state, orderLocalInfo.getTime_out());
        } else {
            //服务端订单状态
            checkOrderState(myView, orderLocalInfo);
        }

        myView.tv_customer.setText(orderLocalInfo.getCustomer_telephone());

    }

    private void checkOrderState(ViewHolder holder, OrderLocalInfo info) {
        int state = info.getOrder_state();

        String res = FunctionUtil.getItemStatusDesc(state);
        if (state == 5 || state == 6) {
            try {
                holder.tv_state.setTextColor(mContext.getResources().getColor(R.color.ex_black));
                if (info.getPick_time() != null) {
                    holder.tv_state_time1.setText(getTime1(info.getPick_time()));
                    holder.tv_state_time1.setVisibility(View.VISIBLE);
                    holder.tv_state_time2.setText(getTime2(info.getPick_time()));
                    holder.tv_state_time2.setVisibility(View.VISIBLE);
                }
                holder.ll_send.setVisibility(View.GONE);
                holder.v_send_null.setVisibility(View.VISIBLE);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (holder.ll_send.getVisibility() != View.VISIBLE) {
                holder.ll_send.setVisibility(View.VISIBLE);
            }
            if (holder.v_send_null.getVisibility() == View.VISIBLE) {
                holder.v_send_null.setVisibility(View.GONE);
            }
            if (holder.tv_state_time1.getVisibility() == View.VISIBLE) {
                holder.tv_state_time1.setVisibility(View.GONE);
                holder.tv_state_time2.setVisibility(View.GONE);
            }
            holder.tv_state.setTextColor(mContext.getResources().getColor(R.color.ex_red));
        }
        holder.tv_state.setText(res);
    }

    private String getTime1(String time) {
        if (time == null) {
            return null;
        }
        int index = time.indexOf(" ");
        return time.substring(index + 1, time.length());
    }

    private String getTime2(String time) {
        if (time == null) {
            return null;
        }
        int index = time.indexOf(" ");
        return time.substring(0, index);
    }

    private void checkTimeState(TextView tv, int state) {
        if (state == 0) {
            tv.setTextColor(mContext.getResources().getColor(R.color.ex_green));
            tv.setText("未超期");
        } else if (state == 1) {
            tv.setText("已超期");
        }
    }


    @Override
    public int getItemCount() {

        if (mOrders == null) {
            return 0;
        }
        return mOrders.size();
    }

    public void removeItemData(int position) {
        if (mOrders != null) {
            mOrders.remove(position);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tv_time1, tv_time2;

        public TextView tv_item_id, tv_customer, tv_state, tv_state_time1, tv_state_time2;

        public View v_send_null;
        public ImageButton bt_send_sms, bt_send_phone;
        public Button tv_recycle;
        public RelativeLayout fl_recycle;
        public LinearLayout ll_query_item, ll_send;
        private LocalQueryListener mListener;

        public ViewHolder(View root, LocalQueryListener listener) {
            super(root);
            mListener = listener;
            ll_query_item = (LinearLayout) root.findViewById(R.id.ll_query_item);
            ll_send = (LinearLayout) root.findViewById(R.id.ll_send);
            if (isQueryLocal) {
                fl_recycle = (RelativeLayout) root.findViewById(R.id.fl_recycle);
                tv_recycle = (Button) root.findViewById(R.id.tv_recycle);
                tv_recycle.setOnClickListener(this);
            }
            tv_time1 = (TextView) root.findViewById(R.id.tv_time1);
            tv_time2 = (TextView) root.findViewById(R.id.tv_time2);
            v_send_null = root.findViewById(R.id.v_send_null);
            tv_item_id = (TextView) root.findViewById(R.id.tv_item_id);
            tv_customer = (TextView) root.findViewById(R.id.tv_customer);
            tv_state = (TextView) root.findViewById(R.id.tv_state);
            tv_state_time1 = (TextView) root.findViewById(R.id.tv_state_time1);
            tv_state_time2 = (TextView) root.findViewById(R.id.tv_state_time2);

            bt_send_sms = (ImageButton) root.findViewById(R.id.bt_send_sms);
            bt_send_sms.setOnClickListener(this);
            bt_send_phone = (ImageButton) root.findViewById(R.id.bt_send_phone);
            bt_send_phone.setOnClickListener(this);

            MGViewUtil.scaleContentView(root);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            OrderLocalInfo info = mOrders.get(position);

            switch (v.getId()) {
                case R.id.bt_send_sms:
                    if (mListener != null) {
                        mListener.sendSms(info);
                    }

                    break;
                case R.id.bt_send_phone:
                    if (mListener != null) {
                        mListener.sendPhone(info);
                    }
                    break;
                case R.id.tv_recycle:

                    if (mListener != null) {
                        mListener.recycle(info, position);
                    }
                    break;
            }

        }
    }

    public interface LocalQueryListener {
        void sendSms(OrderLocalInfo info);

        void sendPhone(OrderLocalInfo info);

        void recycle(OrderLocalInfo info, int position);
    }
}
