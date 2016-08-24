package com.xhl.world.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.LogisticsDelivery;
import com.xhl.world.model.LogisticsModel;
import com.xhl.world.ui.recyclerHolder.LogisticsDataHolder;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/1/25.
 */
@ContentView(R.layout.fragment_logistics)
public class LogisticsFragment extends BaseAppFragment {

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.logistics_recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.iv_goods)
    private LifeCycleImageView iv_goods;

    @ViewInject(R.id.tv_logistics_name)
    private TextView tv_logistics_name;

    @ViewInject(R.id.tv_logistics_num)
    private TextView tv_logistics_num;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;
    private LogisticsModel mLogistics;

    @Override
    protected void initParams() {
        title_name.setText("查看物流");

        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerAdapter(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        if (mLogistics != null) {
            showData(mLogistics);
            iv_goods.bindImageUrl(mLogistics.getProductPic());
            tv_logistics_name.setText(getString(R.string.order_logistics_company, mLogistics.getCompany()));
            tv_logistics_num.setText(getString(R.string.order_logistics_num, mLogistics.getLogisticsNo()));
        }
    }

    private void showData(LogisticsModel logistics) {

        List<RecyclerDataHolder> holders = new ArrayList<>();

        List<LogisticsDelivery> delivery = logistics.getDelivery();
        int size = delivery.size();

        for (int i = 0; i < size; i++) {
            LogisticsDelivery logisticsDelivery = delivery.get(i);
            if (i == 0) {
                logisticsDelivery.setLeast(true);
            } else {
                logisticsDelivery.setLeast(false);
            }
            LogisticsDataHolder dataHolder = new LogisticsDataHolder(logisticsDelivery);
            holders.add(dataHolder);
        }

        mAdapter.setDataHolders(holders);

    }

    @Override
    public void onEnter(Object data) {
        if (data != null && data instanceof LogisticsModel) {
            mLogistics = (LogisticsModel) data;
        }
    }
}
