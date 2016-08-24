package com.xhl.world.ui.main.classify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xhl.world.R;
import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.ClassifyItemModel;
import com.xhl.world.ui.activity.SearchActivity;
import com.xhl.world.ui.adapter.SelectStateRecyclerAdapter;
import com.xhl.world.ui.fragment.BaseAppFragment;
import com.xhl.world.ui.main.classify.recyclerHolder.ClassifyItemDataHolder;
import com.xhl.world.ui.utils.SnackMaker;
import com.xhl.world.ui.view.pub.callback.RecycleViewCallBack;
import com.xhl.xhl_library.ui.FragmentCacheManager;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.view.RippleView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/11/25.
 */
@ContentView(R.layout.fragment_classify)
public class ClassifyFragment extends BaseAppFragment implements RecycleViewCallBack {

    @ViewInject(R.id.bar_classify_item_recycler_view)
    private RecyclerView mRecycleView;

    @Event(value = R.id.ripple_bar_home_search, type = RippleView.OnRippleCompleteListener.class)
    private void onSearchClick(View view) {
        getActivity().startActivity(new Intent(getContext(), SearchActivity.class));
    }

    private SelectStateRecyclerAdapter mRecyclerAdapter;
    private LinearLayoutManager mLayoutManager;
    private FragmentCacheManager mFragmentCacheManager;
    private boolean isLoadSuccess = false;
    private boolean isLoading = false;

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

    }

    //创建导航栏数据
    private void createManager(List<ClassifyItemModel> list) {

        for (int i = 0; i < list.size(); i++) {
            ClassifyItemModel mode = list.get(i);
            Bundle bundle = new Bundle();
            bundle.putSerializable("model", mode);
            mFragmentCacheManager.addFragmentToCache(i, ClassifyItemDetailsFragment.class, mode.getId(), bundle);
        }
        mFragmentCacheManager.setCurrentFragment(0);
    }

    //获取导航栏标签数据
    private void loadParentClassifyData() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        showLoadingDialog();

        ApiControl.getApi().categoryQueryList("0", new Callback.CommonCallback<ResponseModel<List<ClassifyItemModel>>>() {
            @Override
            public void onSuccess(ResponseModel<List<ClassifyItemModel>> result) {
                if (result.isSuccess()) {
                    List<ClassifyItemModel> models = result.getResultObj();
                    createManager(models);
                    createRecyclerData(models);
                    isLoadSuccess = true;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                SnackMaker.shortShow(mView,R.string.network_error);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isLoading = false;
                hideLoadingDialog();
            }
        });
    }

    private void createRecyclerData(List<ClassifyItemModel> data) {
        List<RecyclerDataHolder> holders = new ArrayList<>();
        boolean isFirst = true;
        for (ClassifyItemModel mode : data) {
            ClassifyItemDataHolder holder = new ClassifyItemDataHolder(mode);
            holder.setCallBack(this);
            if (isFirst) {
                isFirst = false;
                holder.setHolderState(RecyclerDataHolder.HOLDER_SELECT);
            }
            holders.add(holder);
        }
        //设置显示
        mRecyclerAdapter.setDataHolders(holders);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoadSuccess) {
            loadParentClassifyData();
        }
    }

    @Override
    public void onItemClick(int position) {
        mRecyclerAdapter.setSelectPosition(position);
        mFragmentCacheManager.setCurrentFragment(position);
    }
}
