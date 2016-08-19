package com.xhl.bqlh.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xhl.bqlh.model.ClassifyModel;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

/**
 * Created by Sum on 16/7/11.
 */
public class ShopCategoryDataHolder extends RecyclerDataHolder<ClassifyModel> {

    public ShopCategoryDataHolder(ClassifyModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return null;
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ClassifyModel data) {

    }
}
