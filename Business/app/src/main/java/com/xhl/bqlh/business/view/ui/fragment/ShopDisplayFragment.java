package com.xhl.bqlh.business.view.ui.fragment;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.ShopDisplayModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppFragment;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.business.view.ui.recyclerHolder.ShopDisplayDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/6/3.
 */
@ContentView(R.layout.fragment_shop_display)
public class ShopDisplayFragment extends BaseAppFragment implements Toolbar.OnMenuItemClickListener, RecycleViewCallBack {

    @ViewInject(R.id.recycler_view)
    private RecyclerView recyclerView;

    @ViewInject(R.id.tv_text_null)
    private View tv_text_null;

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    private RecyclerAdapter mAdapter;
    private List<ShopDisplayModel> mShowImage;

    private String mShopId;

    @Override
    protected void initParams() {
        super.initToolbar();
        mToolbar.inflateMenu(R.menu.user_menu_shop_display);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setTitle(R.string.shop_detail_display);
        mShowImage = new ArrayList<>();

        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.BOTTOM);

        mAdapter = new RecyclerAdapter(getContext());
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = 10;
                outRect.right = 10;
                outRect.bottom = 10;
                outRect.top = 8;
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        onRefreshLoadData();
    }

    @Override
    public void onRefreshLoadData() {
        ApiControl.getApi().shopDisplayQuery(mShopId, getPageSize(), getPageIndex(), new Callback.CommonCallback<ResponseModel<ShopDisplayModel>>() {
            @Override
            public void onSuccess(ResponseModel<ShopDisplayModel> result) {
                if (result.isSuccess()) {
                    setTotalSize(result.getPageSize());
                    createData(result.getObjList());
                } else {
                    SnackUtil.shortShow(mToolbar, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackUtil.shortShow(mToolbar, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mRefresh.setRefreshing(false);
            }
        });
    }

    private void createData(List<ShopDisplayModel> data) {
        if (data.size() == 0 && getPageIndex() > 1) {
            SnackUtil.shortShow(mToolbar, R.string.load_null);
            return;
        }
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (ShopDisplayModel display : data) {
            ShopDisplayDataHolder holder = new ShopDisplayDataHolder(display);
            holder.setCallBack(this);
            holders.add(holder);
        }

        if (getPageIndex() == 1 && holders.size() == 0) {
            ViewHelper.setViewGone(tv_text_null, false);
        } else {
            ViewHelper.setViewGone(tv_text_null, true);
        }
        if (getPageIndex() > 1) {
            mAdapter.addDataHolder(holders);
        } else {
            mAdapter.setDataHolders(holders);
        }
        //添加到显示集合中
        mShowImage.addAll(data);
    }

    @Override
    public void onEnter(Object data) {
        if (data != null) {
            Object[] objects = (Object[]) data;
            mShopId = (String) objects[0];
            String shopName = (String) objects[1];
        }
    }

    @Override
    public void onBackWithData(Object data) {
        super.onBackWithData(data);
        if (data != null && data instanceof ShopDisplayModel) {
            ShopDisplayModel shop = (ShopDisplayModel) data;
            //第一个位置添加信息
            mShowImage.add(0, shop);
            //显示添加到第一个位置
            ShopDisplayDataHolder holder = new ShopDisplayDataHolder(shop);
            holder.setCallBack(this);
            mAdapter.addDataHolder(0, holder);
            recyclerView.smoothScrollToPosition(0);
            ViewHelper.setViewGone(tv_text_null, true);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        getSumContext().pushFragmentToBackStack(ShopDisplayAddFragment.class, mShopId);
        return false;
    }

    @Override
    public void onItemClick(int position) {
        List<ShopDisplayModel> showImage = this.mShowImage;
        if (position < showImage.size()) {
            //下个界面的数据
            getSumContext().pushFragmentToBackStack(ShopDisplayDetailsFragment.class, showImage.get(position));
        }
    }
}
