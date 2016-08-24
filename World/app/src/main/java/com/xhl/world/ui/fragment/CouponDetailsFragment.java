package com.xhl.world.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.Response;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.Coupon;
import com.xhl.world.ui.recyclerHolder.CouponDataHolder;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.view.pub.PageScrollListener;
import com.xhl.world.ui.view.pub.callback.RecyclerViewScrollBottomListener;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayout;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayoutDirection;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/2/27.
 */
@ContentView(R.layout.fragment_coupon_details)
public class CouponDetailsFragment extends BaseAppFragment implements RecyclerViewScrollBottomListener {

    public static final String Coupon_status_new = "A";
    public static final String Coupon_status_used = "U";
    public static final String Coupon_status_time_out = "O";

    private String mTag;

    @ViewInject(R.id.rl_null_show)
    private RelativeLayout mOrderNullShow;

    @ViewInject(R.id.coupon_recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.swipy_refresh_layout)
    private SwipyRefreshLayout mSwipRefresh;

    private boolean mIsLoading = false;

    public static CouponDetailsFragment instance(String tag) {
        CouponDetailsFragment fragment = new CouponDetailsFragment();

        Bundle bundle = new Bundle();

        bundle.putString("tag", tag);

        fragment.setArguments(bundle);

        return fragment;
    }

    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mRecyclerAdapter;
    private int pageSize = 10;
    private int pageNo = 0;
    private boolean exitMore = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //查询优惠券格式
        mTag = getArguments().getString("tag");
    }

    @Override
    protected void initParams() {
//        mOrderNullShow.setVisibility(View.VISIBLE);

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerAdapter = new RecyclerAdapter(getContext());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        PageScrollListener pageScrollListener = new PageScrollListener(mLayoutManager);
        pageScrollListener.addBottomListener(this);
        mRecyclerView.addOnScrollListener(pageScrollListener);

        initPullRefresh();

        queryData();
    }


    private void initPullRefresh() {
        mSwipRefresh.setColorSchemeResources(R.color.app_green, R.color.app_blue);
        mSwipRefresh.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    pageNo = 0;
                    exitMore = true;
                    queryData();
                }
            }
        });
    }

    private void queryData() {
        if (mIsLoading) {
            return;
        }

        mIsLoading = true;

        ApiControl.getApi().couponList(pageNo, pageSize, mTag, new Callback.CommonCallback<ResponseModel<Response<Coupon>>>() {
            @Override
            public void onSuccess(ResponseModel<Response<Coupon>> result) {
                if (result.isSuccess()) {
                    List<Coupon> coupons = result.getResultObj().getRows();
                    if (coupons == null || coupons.size() < pageSize) {
                        exitMore = false;
                    }
                    showData(coupons);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(mSwipRefresh, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mIsLoading = false;
                mSwipRefresh.setRefreshing(false);
            }
        });

    }

    private void showData(List<Coupon> data) {

        List<RecyclerDataHolder> holders = new ArrayList<>();
        if (data == null || data.size() == 0) {
            if (pageNo == 0) {
                mOrderNullShow.setVisibility(View.VISIBLE);
            } else {
                mOrderNullShow.setVisibility(View.GONE);
            }
        }
        for (Coupon coupon : data) {
            holders.add(new CouponDataHolder(coupon));
        }
        if (pageNo == 0) {
            mRecyclerAdapter.setDataHolders(holders);
        } else {
            mRecyclerAdapter.addDataHolder(holders);
        }
    }

    @Override
    public void onScrollBottom() {
        if (!exitMore) {
            SnackMaker.shortShow(mSwipRefresh, R.string.bottom_tip);
            return;
        }
        pageNo++;
        queryData();
    }

}
