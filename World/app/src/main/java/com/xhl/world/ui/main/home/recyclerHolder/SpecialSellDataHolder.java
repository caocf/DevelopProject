package com.xhl.world.ui.main.home.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.xhl.world.R;
import com.xhl.world.model.AdvModel;
import com.xhl.world.ui.main.home.HomeItemType;
import com.xhl.world.ui.main.home.bar.HomeSpecialSellBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by Sum on 15/12/2.
 */
public class SpecialSellDataHolder extends RecyclerDataHolder {

    private boolean mIsFirstLoad = true;
    private String mName;

    public SpecialSellDataHolder(List<AdvModel> data) {
        super(data);
    }

    public void setAdName(String name) {
        mName = name;
    }

    @Override
    public int getType() {
        return HomeItemType.Type_Special_sell;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        int marginTop = (int) context.getResources().getDimension(R.dimen.px_dimen_20);
        HomeSpecialSellBar mSpecialSellBar = new HomeSpecialSellBar(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = marginTop;
        mSpecialSellBar.setLayoutParams(params);

        AutoUtils.autoMargin(mSpecialSellBar);

        return new RecyclerViewHolder(mSpecialSellBar);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        if (vHolder.getItemViewType() == getType()) {
            HomeSpecialSellBar mSpecialSellBar = (HomeSpecialSellBar) vHolder.itemView;
            if (mIsFirstLoad) {
                mIsFirstLoad = false;
                mSpecialSellBar.setSpecialSellData((List<AdvModel>) data);
                mSpecialSellBar.setAdName(mName);
            } else {
                mSpecialSellBar.updateShowData((List<AdvModel>) data);
            }
        }
    }
}
