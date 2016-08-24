package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.Coupon;
import com.xhl.world.ui.event.CouponEvent;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.fragment.CouponDetailsFragment;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Sum on 16/2/27.
 */
public class CouponDataHolder extends RecyclerDataHolder {

    public CouponDataHolder(Coupon data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coupon, null);

        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));

        AutoUtils.auto(view);

        return new CouponRecyclerView(context, view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        CouponRecyclerView view = (CouponRecyclerView) vHolder;
        view.onBind((Coupon) data);
    }

    static class CouponRecyclerView extends RecyclerViewHolder implements View.OnClickListener {

        @ViewInject(R.id.fl_coupon_state)
        private FrameLayout fl_coupon_state;

        @ViewInject(R.id.tv_coupon_money)
        private TextView tv_coupon_money;

        @ViewInject(R.id.tv_coupon_use_condition)
        private TextView tv_coupon_use_condition;

        @ViewInject(R.id.tv_coupon_use_time)
        private TextView tv_coupon_use_time;

        @ViewInject(R.id.tv_coupon_use_range)
        private TextView tv_coupon_use_range;

        private Context mContext;

        private Coupon mCoupon;

        public CouponRecyclerView(Context context, View view) {
            super(view);
            x.view().inject(this, view);
            mContext = context;
            view.setOnClickListener(this);
        }

        public void onBind(Coupon coupon) {
            if (coupon == null) {
                return;
            }
            mCoupon = coupon;
            tv_coupon_use_condition.setText(coupon.getTitle());
            //金额
            tv_coupon_money.setText(coupon.getAmount()+"");
            //日期
            tv_coupon_use_time.setText(mContext.getString(R.string.coupon_use_time, coupon.getStartTime(), coupon.getEndTime()));
            //范围
            tv_coupon_use_range.setText(mContext.getString(R.string.coupon_use_range, coupon.getProvision()));
            changeStatus(coupon);
        }

        private void changeStatus(Coupon coupon) {
            String status = coupon.getStatus();
            //非正常状态的优惠券
            if (!status.equalsIgnoreCase(CouponDetailsFragment.Coupon_status_new)) {
                fl_coupon_state.setBackgroundResource(R.drawable.icon_coupon_dis);
            } else {
                fl_coupon_state.setBackgroundResource(R.drawable.icon_coupon_nor);
            }
        }

        @Override
        public void onClick(View v) {
            CouponEvent event = new CouponEvent();
            event.coupon = mCoupon;
            EventBusHelper.post(event);
        }
    }
}
