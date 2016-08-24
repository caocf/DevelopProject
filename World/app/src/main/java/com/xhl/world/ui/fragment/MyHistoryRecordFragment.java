package com.xhl.world.ui.fragment;

import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.Response;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.CollectionModel;
import com.xhl.world.ui.event.CollectionOpEvent;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.recyclerHolder.ProductItemDataHolder;
import com.xhl.world.ui.utils.DialogMaker;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.view.pub.PageScrollListener;
import com.xhl.world.ui.view.pub.callback.RecyclerViewScrollBottomListener;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
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
 * Created by Sum on 16/1/3.
 */
@ContentView(R.layout.fragment_my_history_record)
public class MyHistoryRecordFragment extends BaseAppFragment implements RecyclerViewScrollBottomListener {

    @ViewInject(R.id.order_recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.swipy_refresh_layout)
    private SwipyRefreshLayout mSwipRefresh;

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.title_other)
    private Button title_other;

    @ViewInject(R.id.rl_null_show)
    private View mNullView;

    @ViewInject(R.id.iv_null_ic)
    private ImageView iv_null_ic;

    @ViewInject(R.id.tv_null_txt)
    private TextView tv_null_txt;


    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    @Event(R.id.title_other)
    private void onClearClick(View view) {

        DialogMaker.showAlterDialog(getContext(), "浏览记录", "您确定要清空足迹吗?", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clear();
            }
        });

    }

    private void clear() {

        showLoadingDialog();

        ApiControl.getApi().browseDelete(new Callback.CommonCallback<ResponseModel<String>>() {
            @Override
            public void onSuccess(ResponseModel<String> result) {
                if (result.isSuccess()) {
                    loadRecordData();
                } else {
                    SnackMaker.shortShow(mSwipRefresh, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(mSwipRefresh, R.string.network_error);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoadingDialog();
            }
        });
    }

    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter mRecyclerAdapter;
    private List<RecyclerDataHolder> mDatas;
    private int mCurPage = 0;
    private int mPageSize = 10;
    private boolean mExistMore = true;
    private boolean isLoading = false;

    @Override
    protected void initParams() {

        title_name.setText(R.string.my_watch_history);
        title_other.setText("清空");

        mDatas = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerAdapter = new RecyclerAdapter(getContext());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        PageScrollListener scrollListener = new PageScrollListener(mLayoutManager);
        scrollListener.addBottomListener(this);
        mRecyclerView.addOnScrollListener(scrollListener);

        initPullRefresh();
    }

    private void initPullRefresh() {
        mSwipRefresh.setColorSchemeResources(R.color.app_green, R.color.app_blue);
        mSwipRefresh.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    mCurPage = 0;
                    loadRecordData();
                }
            }
        });
    }

    private void loadRecordData() {

        if (isLoading) {
            return;
        }
        isLoading = true;

        ApiControl.getApi().browseQuery(mCurPage, mPageSize, new Callback.CommonCallback<ResponseModel<Response<CollectionModel>>>() {
            @Override
            public void onSuccess(ResponseModel<Response<CollectionModel>> result) {
                if (result.isSuccess()) {
                    List<CollectionModel> productModels = result.getResultObj().getRows();
                    if (productModels.size() < mPageSize) {
                        mExistMore = false;
                    }
                    createData(productModels);
                } else {
                    SnackMaker.shortShow(mSwipRefresh, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(mSwipRefresh, R.string.network_error);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isLoading = false;
                mSwipRefresh.setRefreshing(false);
            }
        });
    }

    private void createData(List<CollectionModel> products) {

        if (products == null || products.size() <= 0) {
            if (mCurPage == 0) {
                mRecyclerAdapter.setDataHolders(null);
                mNullView.setVisibility(View.VISIBLE);
                tv_null_txt.setText("亲，您还没有留下足迹哦~");
                iv_null_ic.setImageResource(R.drawable.icon_null_history);
            } else {
                SnackMaker.shortShow(mSwipRefresh,R.string.bottom_tip);
            }
            return;
        }
        mDatas.clear();

        for (CollectionModel collection : products) {
            RecyclerDataHolder holder = new ProductItemDataHolder(collection);
            mDatas.add(holder);
        }
        if (mCurPage != 0) {
            mRecyclerAdapter.addDataHolder(mDatas);
        } else {
            mRecyclerAdapter.setDataHolders(mDatas);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecordData();
    }

    @Override
    public void onScrollBottom() {
        if (!mExistMore) {
            return;
        }
        mCurPage++;
        loadRecordData();
    }

    public void onEvent(CollectionOpEvent event) {
        if (event.actionType == 0) {//点击
            EventBusHelper.postProductDetails(event.productId);
        }
    }
}
