package com.xhl.world.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.Response;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.Entities.QueryConditionEntity;
import com.xhl.world.model.serviceOrder.Product;
import com.xhl.world.ui.recyclerHolder.SearchDetailsDataHolder;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.view.pub.PageScrollListener;
import com.xhl.world.ui.view.pub.callback.RecyclerViewScrollBottomListener;
import com.xhl.xhl_library.Base.BaseFragment;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.line.GirdViewItemDecoration;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayout;
import com.xhl.xhl_library.ui.swipyrefresh.SwipyRefreshLayoutDirection;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/12/7.
 */
@ContentView(R.layout.fragment_search_details)
public class SearchDetailsFragment extends BaseFragment implements RecyclerViewScrollBottomListener {

    @ViewInject(R.id.swipy_refresh_layout)
    private SwipyRefreshLayout mSwipRefresh;

    @ViewInject(R.id.search_details_recyclerView)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.tv_null_hint)
    private TextView tv_null_hint;

    private RecyclerAdapter mAdapter;
    private GridLayoutManager mManager;
    private QueryConditionEntity mCondition;

    private int mPageNo = 0;
    private final int mPageSize = 10;
    private boolean mIsLoading = false;
    private boolean mNoMore = false;
    //滚动到顶部
    @ViewInject(R.id.ripple_scroll_to_top)
    private RippleView mRippleToTop;

    @Event(value = R.id.ripple_scroll_to_top, type = RippleView.OnRippleCompleteListener.class)
    private void scrollTopClick(View v) {
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    protected void initParams() {
        mAdapter = new RecyclerAdapter(getContext());

        mManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new GirdViewItemDecoration(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        PageScrollListener listener = new PageScrollListener(mManager);
        listener.setTopView(mRippleToTop);
        listener.addBottomListener(this);
        mRecyclerView.addOnScrollListener(listener);

        initPullRefresh();
    }

    private void initPullRefresh() {
        mSwipRefresh.setColorSchemeResources(R.color.app_green, R.color.app_blue);
        mSwipRefresh.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    queryProduct();
                }
            }
        });
    }

    //设置查询条件
    public void setQueryCondition(QueryConditionEntity condition) {
        mCondition = condition;
        queryProduct();
    }

    private void queryProduct() {
        if (mCondition == null) {
            mSwipRefresh.setRefreshing(false);
            return;
        }
        mNoMore = false;
        mPageNo = 0;
        //pageNo
        mCondition.setPageNo(String.valueOf(mPageNo));
        mCondition.setPageSize(String.valueOf(mPageSize));

        query();
    }

    private void query() {
        if (mIsLoading) {
            return;
        }

        mIsLoading = true;

        ApiControl.getApi().queryProductByPage(mCondition, new Callback.CommonCallback<ResponseModel<Response<Product>>>() {
            @Override
            public void onSuccess(ResponseModel<Response<Product>> result) {
                if (result.isSuccess()) {
                    List<Product> products = result.getResultObj().getRows();
                    if (products.size() < mPageSize) {
                        mNoMore = true;
                    }
                    createData(products);
                } else {
                    SnackMaker.shortShow(mSwipRefresh, result.getMessage());
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
                mSwipRefresh.setRefreshing(false);
                mIsLoading = false;
            }
        });
    }

    private void createData(List<Product> products) {
        if (products == null || products.size() <= 0) {
            if (mPageNo == 0) {
                tv_null_hint.setVisibility(View.VISIBLE);
            } else {
                tv_null_hint.setVisibility(View.INVISIBLE);
            }
            return;
        }
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (Product product : products) {
            holders.add(new SearchDetailsDataHolder(product));
        }

        if (mPageNo == 0) {
            mAdapter.setDataHolders(holders);
        } else {
            mAdapter.addDataHolder(holders);
        }

    }


    @Override
    public void onScrollBottom() {
        if (mIsLoading) {
            return;
        }
        if (mNoMore) {
            SnackMaker.shortShow(mSwipRefresh, R.string.bottom_tip);
            return;
        }
        mPageNo++;
        mCondition.setPageNo(String.valueOf(mPageNo));
        mCondition.setPageNo(String.valueOf(mPageSize));
        query();
    }

}
