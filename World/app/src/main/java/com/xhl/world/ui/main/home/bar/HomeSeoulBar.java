package com.xhl.world.ui.main.home.bar;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.AdvModel;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.main.home.recyclerHolder.SeoulBarDataHolder;
import com.xhl.world.ui.view.pub.callback.RecycleViewCallBack;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.layoutManager.FullGridViewManager;
import com.xhl.xhl_library.ui.recyclerview.line.GirdViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 15/11/27.
 */
public class HomeSeoulBar extends BaseHomeBar implements RecycleViewCallBack {

    private TextView bar_home_seoul_title;

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;

    private List<AdvModel> mDatas;

    public HomeSeoulBar(Context context) {
        super(context);
    }

    public HomeSeoulBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
        bar_home_seoul_title = _findViewById(R.id.bar_home_seoul_title);
        mRecyclerView = _findViewById(R.id.bar_home_seoul_recycler_view);

        GridLayoutManager manager = new FullGridViewManager(getContext(), 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerAdapter = new RecyclerAdapter(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new GirdViewItemDecoration(getContext()));
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    public void setAdName(String name){

        if (TextUtils.isEmpty(name)) {
            return;
        }
        bar_home_seoul_title.setText(name);
    }

    public void setFlashSaleData(List<AdvModel> data) {
        if (mDatas == data) {
            return;
        }
        mDatas = data;
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (AdvModel adv : data) {

            SeoulBarDataHolder holder = new SeoulBarDataHolder(adv);
            holder.setCallBack(this);
            holders.add(holder);
        }
        mRecyclerAdapter.setDataHolders(holders);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_home_l_seoul;
    }

    @Override
    public void onItemClick(int position) {

        AdvModel advModel = mDatas.get(position);
        if (advModel != null) {
            EventBusHelper.postAdv(advModel);
        }

    }
}
