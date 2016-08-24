package com.xhl.world.ui.main.home.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.xhl.world.R;
import com.xhl.world.model.AdvModel;
import com.xhl.world.ui.main.home.HomeItemType;
import com.xhl.world.ui.main.home.bar.HomeSeoulBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by Sum on 15/12/2.
 */
public class SeoulDataHolder extends RecyclerDataHolder {

    private  String mName;
    public SeoulDataHolder(List<AdvModel> data) {
        super(data);
    }
    public void setAdName(String name){
        mName = name;
    }
    @Override
    public int getType() {
        return HomeItemType.Type_Seoul;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        int marginTop = (int) context.getResources().getDimension(R.dimen.px_dimen_20);
        HomeSeoulBar  mSeoulBar = new HomeSeoulBar(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = marginTop;

        mSeoulBar.setLayoutParams(params);

        AutoUtils.autoMargin(mSeoulBar);

        return new RecyclerViewHolder(mSeoulBar);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        if (vHolder.getItemViewType() == getType()) {
            HomeSeoulBar mSeoulBar = (HomeSeoulBar) vHolder.itemView;

            mSeoulBar.setAdName(mName);

            mSeoulBar.setFlashSaleData((List<AdvModel>) data);
        }
    }
}
