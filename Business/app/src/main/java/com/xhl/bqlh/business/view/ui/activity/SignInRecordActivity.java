package com.xhl.bqlh.business.view.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;
import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.SignRecordModel;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.recyclerHolder.SignRecordDataHolder;
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
 * Created by Sum on 16/4/21.
 * 考勤记录
 */
@ContentView(R.layout.activity_sign_record)
public class SignInRecordActivity extends BaseAppActivity {


    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mRefresh;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.tv_text_null)
    private View tv_text_null;

    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private boolean mIsQuerying = false;

    @Override
    protected void initParams() {
        super.initToolbar();
        setTitle(R.string.attendance_record);

        super.initRefreshStyle(mRefresh, SwipeRefreshLayoutDirection.BOTH);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mRefresh.isRefreshing()) {
                    mRefresh.setRefreshing(true);
                }
                loadRecord();
            }
        }, 50);
    }

    private void loadRecord() {
        if (mIsQuerying) {
            return;
        }
        mIsQuerying = true;

        ApiControl.getApi().signRecord(getPageSize(), getPageIndex(), new Callback.CommonCallback<ResponseModel<SignRecordModel>>() {
            @Override
            public void onSuccess(ResponseModel<SignRecordModel> result) {
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
                mIsQuerying = false;
            }
        });

    }

    private void createData(List<SignRecordModel> datas) {
        if (datas.size() == 0 && getPageIndex() > 1) {
            SnackUtil.shortShow(mToolbar, R.string.load_null);
            return;
        }

        List<RecyclerDataHolder> holders = new ArrayList<>();

        for (SignRecordModel sign : datas) {
            holders.add(new SignRecordDataHolder(sign));
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
    }

    @Override
    public void onRefreshLoadData() {
        loadRecord();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu_sign_record, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, SignInRecordTableActivity.class));
        AVAnalytics.onEvent(this, "click sign calendar");
        return true;
    }
}
