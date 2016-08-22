package com.xhl.bqlh.view.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.ClassifyModel;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.utils.barcode.ui.CaptureActivity;
import com.xhl.bqlh.view.base.BaseAppFragment;
import com.xhl.bqlh.view.helper.FragmentContainerHelper;
import com.xhl.bqlh.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.view.ui.adapter.SelectStateRecyclerAdapter;
import com.xhl.bqlh.view.ui.recyclerHolder.ClassifyIndexDataHolder;
import com.xhl.xhl_library.ui.FragmentCacheManager;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/7/1.
 */
@ContentView(R.layout.fragment_home_classify)
public class HomeClassifyFragment extends BaseAppFragment implements RecycleViewCallBack<ClassifyModel> {


    @Event(R.id.fl_search)
    private void onSearchClick(View view) {
        FragmentContainerHelper.startFragment(getContext(), FragmentContainerHelper.fragment_search);
    }
    @Event(R.id.fl_scan)//扫码
    private void onScanClick(View view) {
        startActivity(new Intent(getContext(), CaptureActivity.class));
    }

    @Override
    protected boolean isNeedRegisterEventBus() {
        return true;
    }

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecycleView;

    private SelectStateRecyclerAdapter mRecyclerAdapter;
    private LinearLayoutManager mLayoutManager;
    private FragmentCacheManager mFragmentCacheManager;

    @Override
    protected void initParams() {

        mFragmentCacheManager = new FragmentCacheManager();
        mFragmentCacheManager.setUp(this, R.id.fl_content);

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerAdapter = new SelectStateRecyclerAdapter(getContext());
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());

        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(mRecyclerAdapter);

        onRefreshLoadData();
    }

    @Override
    public void onRefreshLoadData() {
        super.onRefreshLoadData();
        showLoadingDialog();

        ApiControl.getApi().classify(new Callback.CommonCallback<ResponseModel<GarbageModel>>() {
            @Override
            public void onSuccess(ResponseModel<GarbageModel> result) {
                if (result.isSuccess()) {
                    List<ClassifyModel> cateList = result.getObj().getCateList();
                    createCategory(cateList);
                    networkErrorHide();
                } else {
                    ToastUtil.showToastLong(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                networkErrorShow();
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

    //创建分类
    private void createCategory(List<ClassifyModel> models) {
        List<RecyclerDataHolder> holders = new ArrayList<>();
        boolean isFirst = true;
        int i = 0;
        for (ClassifyModel mode : models) {
            String categoryName = mode.getCategoryAppName();
            if (TextUtils.isEmpty(categoryName) || categoryName.equals("null")) {
                continue;
            }
            ClassifyIndexDataHolder holder = new ClassifyIndexDataHolder(mode);
            holder.setCallBack(this);
            if (isFirst) {
                isFirst = false;
                holder.setHolderState(RecyclerDataHolder.HOLDER_SELECT);
            }
            holders.add(holder);
        }
        //设置显示
        mRecyclerAdapter.setDataHolders(holders);

        createChildManager(models);
    }

    //创建导航栏数据
    private void createChildManager(List<ClassifyModel> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ClassifyModel mode = list.get(i);
            String categoryName = mode.getCategoryAppName();
            if (TextUtils.isEmpty(categoryName) || categoryName.equals("null")) {
                continue;
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", mode.getChildren());
            mFragmentCacheManager.addFragmentToCache(i, HomeClassifyChildFragment.class, "tag_" + i, bundle);
        }
        mFragmentCacheManager.setCurrentFragment(0);
    }

    @Override
    public void onItemClick(int position, ClassifyModel classifyModel) {
        mRecyclerAdapter.setSelectPosition(position);
        mFragmentCacheManager.setCurrentFragment(position);
    }

    @Override
    public void onEvent(CommonEvent event) {
        super.onEvent(event);
        if (event.getEventTag() == CommonEvent.ET_RELOAD_USER_INFO) {
            onRefreshLoadData();
        }
    }
}
