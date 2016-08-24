package com.xhl.world.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.xhl.world.R;
import com.xhl.world.model.InitReturnOrderModel;
import com.xhl.world.model.serviceOrder.OrderDetail;
import com.xhl.world.ui.recyclerHolder.OrderReturnItemDataHolder;
import com.xhl.world.ui.view.myLoadRelativeLayout;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayout;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayoutDirection;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/1/19.
 */
@ContentView(R.layout.fragment_manager_return_order)
public class OrderReturnManagerFragment extends BaseAppFragment {


    @ViewInject(R.id.order_recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.swipy_refresh_layout)
    private SwipyRefreshLayout mSwipRefresh;

    @ViewInject(R.id.load_content)
    private myLoadRelativeLayout mLoadView;

    @ViewInject(R.id.rl_null_show)
    private RelativeLayout mOrderNullShow;

    private InitReturnOrderModel mReturnOrder;


    public static OrderReturnManagerFragment instance(int tag, InitReturnOrderModel data) {
        OrderReturnManagerFragment fragment = new OrderReturnManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tag", tag);
        bundle.putSerializable("data", data);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTag = getArguments().getInt("tag");
        mReturnOrder = (InitReturnOrderModel) getArguments().getSerializable("data");
    }

    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mRecyclerAdapter;
    private int mTag;

    @Override
    protected void initParams() {
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerAdapter = new RecyclerAdapter(getContext());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        initPullRefresh();
        loadData();
    }


    private void initPullRefresh() {
        mSwipRefresh.setColorSchemeResources(R.color.app_green, R.color.app_blue);
        mSwipRefresh.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    loadData();
                }
            }
        });
    }

    private void loadData() {
        if (mReturnOrder == null) {
            return;
        }
        ArrayList<ArrayList<OrderDetail>> returnList = mReturnOrder.getReturnList();
        if (returnList != null) {
            ArrayList<OrderDetail> details = returnList.get(mTag);
            if (details.size() == 0) {
                dataNullShow();
                return;
            }
            List<RecyclerDataHolder> holders = new ArrayList<>();
            for (OrderDetail order : details) {
                OrderReturnItemDataHolder holder = new OrderReturnItemDataHolder(order);
                holders.add(holder);
            }
            mRecyclerAdapter.setDataHolders(holders);
            mSwipRefresh.setRefreshing(false);
        } else {
            dataNullShow();
        }
    }

    private void dataNullShow() {
        mOrderNullShow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
