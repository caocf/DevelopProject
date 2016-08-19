package com.xhl.bqlh.business.view.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.GiftModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.base.Common.DefaultCallback;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.recyclerHolder.GiftSelectDataHolder;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayout;
import com.xhl.xhl_library.ui.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Summer on 2016/7/27.
 */
@ContentView(R.layout.activity_select_gift_product)
public class SelectGiftProductActivity extends BaseAppActivity implements Toolbar.OnMenuItemClickListener {

    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout swipeRefreshLayout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView recyclerView;

    @ViewInject(R.id.tv_text_null)
    private View mNullView;

    private List<GiftModel> mData;

    private RecyclerAdapter mAdapter;

    @Override
    protected void initParams() {
        super.initToolbar(TYPE_child_other_clear);
        setTitle(R.string.menu_gift);

        super.initRefreshStyle(swipeRefreshLayout, SwipeRefreshLayoutDirection.TOP);
        mAdapter = new RecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefreshLoadData();
            }
        }, 300);
    }

    @Override
    public void onRefreshLoadData() {
        ApiControl.getApi().giftQuery(new DefaultCallback<ResponseModel<GiftModel>>() {
            @Override
            public void success(ResponseModel<GiftModel> result) {
                List<GiftModel> objList = result.getObjList();
                loadData(objList);
            }

            @Override
            public void finish() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadData(List<GiftModel> data) {
        mData = data;
        if (data == null || data.size() == 0) {
            ViewHelper.setViewGone(mNullView, false);
            return;
        } else {
            ViewHelper.setViewGone(mNullView, true);
        }
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (GiftModel gift : data) {
            holders.add(new GiftSelectDataHolder(gift));
        }
        mAdapter.setDataHolders(holders);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mToolbar.inflateMenu(R.menu.menu_commit);
        mToolbar.setOnMenuItemClickListener(this);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        List<GiftModel> datas = this.mData;
        //获取商品
        ArrayList<GiftModel> info = new ArrayList<>();
        for (GiftModel gift : datas) {
            if (gift.getGiftNum() > 0) {
                info.add(gift);
            }
        }
        if (info.size() == 0) {
            ToastUtil.showToastShort("赠送商品总数为0");
        } else {
            Intent intent = new Intent();
            intent.putExtra("data", info);
            setResult(RESULT_OK, intent);
            finish();
        }
        return true;
    }
}
