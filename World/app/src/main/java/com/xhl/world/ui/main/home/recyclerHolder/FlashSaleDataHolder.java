package com.xhl.world.ui.main.home.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.xhl.world.R;
import com.xhl.world.ui.main.home.HomeItemType;
import com.xhl.world.ui.main.home.bar.HomeFlashSaleBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import java.util.List;

/**
 * Created by Sum on 15/12/2.
 */
public class FlashSaleDataHolder extends RecyclerDataHolder {

    private HomeFlashSaleBar mFlashSaleBar;

    private boolean isFirstAdd = false;

    public FlashSaleDataHolder(Object data) {
        super(data);
    }

    @Override
    public int getType() {
        return HomeItemType.Type_flash_sale;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        int margin = (int) context.getResources().getDimension(R.dimen.px_dimen_20);
        mFlashSaleBar = new HomeFlashSaleBar(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = margin;
        mFlashSaleBar.setLayoutParams(params);

        return new RecyclerViewHolder(mFlashSaleBar);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        if (!isFirstAdd) {
            isFirstAdd = true;
            mFlashSaleBar = (HomeFlashSaleBar) vHolder.itemView;
            mFlashSaleBar.setFlashSaleData((List<String>) data);
        }
    }
}
