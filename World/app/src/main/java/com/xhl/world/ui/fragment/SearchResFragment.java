package com.xhl.world.ui.fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xhl.world.R;
import com.xhl.world.ui.recyclerHolder.SearchResDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/12/30.
 * 快速搜索展示界面
 */
@ContentView(R.layout.fragment_search_res)
public class SearchResFragment extends BaseAppFragment {

    @ViewInject(R.id.search_res_recycler_view)
    private RecyclerView mRecyclerView;

    private LinearLayoutManager mManager;
    private RecyclerAdapter mAdapter;
    private List<RecyclerDataHolder> mDatas;

    @Override
    protected void initParams() {
        mManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerAdapter(getContext());

        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mDatas = new ArrayList<>();
    }

    public void setShowData(List<String> data) {
        if (data == null) {
            return;
        }
        mDatas.clear();
        for (String st : data) {
            SearchResDataHolder holder = new SearchResDataHolder(st);
            mDatas.add(holder);
        }
        mAdapter.setDataHolders(mDatas);
        changeVisible();
    }

    public void hideSelf() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(this).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        changeVisible();
    }

    private void changeVisible() {
        boolean isNeedShow = false;
        if (mDatas.size() > 0) {
            isNeedShow = true;
        }
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (isNeedShow && isHidden()) {
            transaction.show(this).commit();
        } else if (!isNeedShow) {
            transaction.hide(this).commit();
        }
    }
}
