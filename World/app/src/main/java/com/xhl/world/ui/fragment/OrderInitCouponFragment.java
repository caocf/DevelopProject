package com.xhl.world.ui.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.Coupon;
import com.xhl.world.ui.event.CouponEvent;
import com.xhl.world.ui.recyclerHolder.CouponDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/3/5.
 * 订单可用的优惠券
 */
@ContentView(R.layout.fragment_order_coupon)
public class OrderInitCouponFragment extends BaseAppFragment {

    @ViewInject(R.id.rl_null_show)
    private RelativeLayout mOrderNullShow;

    @ViewInject(R.id.tv_null_hint)
    private TextView tv_null_hint;

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.title_other)
    private Button title_other;

    @Event(R.id.title_other)
    private void onCancle(View view) {
        if (mListener != null) {
            getSumContext().popTopFragment(null);
            mListener.selectCoupon(null);
        }
    }

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @ViewInject(R.id.coupon_recycler_view)
    private RecyclerView mRecyclerView;

    private selectCouponListener mListener;

    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mRecyclerAdapter;
    private List<Coupon> mData;//优惠券信息

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof selectCouponListener) {
            mListener = (selectCouponListener) context;
        }
    }

    @Override
    protected void initParams() {
        tv_null_hint.setText("暂无可用优惠券");
        title_name.setText("选择优惠券");
        title_other.setText("取消");

        if (mData == null || mData.size() == 0) {
            mOrderNullShow.setVisibility(View.VISIBLE);
        } else {
            mOrderNullShow.setVisibility(View.GONE);
        }

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerAdapter = new RecyclerAdapter(getContext());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        //显示优惠券
        showData(mData);
    }

    private void showData(List<Coupon> data) {
        if (data == null) {
            return;
        }
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (Coupon coupon : data) {
            holders.add(new CouponDataHolder(coupon));
        }
        mRecyclerAdapter.setDataHolders(holders);
    }


    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof List) {
            mData = (List<Coupon>) data;
        }
    }

    public interface selectCouponListener {
        void selectCoupon(Coupon coupon);
    }

    //选择优惠券事件操作
    public void onEvent(CouponEvent event) {
        if (mListener != null) {
            getSumContext().popTopFragment(null);
            mListener.selectCoupon(event.coupon);
        }
    }
}
