package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.business.view.ui.bar.SelectProductBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/6/15.
 */
public class ProductConfirmNewDataHolder extends RecyclerDataHolder<ShopCarModel> {
    private RecycleViewCallBack mCallBack;

    public ProductConfirmNewDataHolder(ShopCarModel data, RecycleViewCallBack callBack) {
        super(data);
        mCallBack = callBack;
    }
    @Override
    public int getType() {
        return 3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new RecyclerViewHolder(new SelectProductBar(context));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ShopCarModel data) {
        SelectProductBar bar = (SelectProductBar) vHolder.itemView;
        bar.onBindData(data);
        bar.setCallBack(mCallBack);
    }
}
