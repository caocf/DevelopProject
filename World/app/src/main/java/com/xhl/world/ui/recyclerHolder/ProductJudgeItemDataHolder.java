package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.xhl.world.model.EvaluateModel;
import com.xhl.world.ui.activity.bar.ProductJudgeBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/1/23.
 */
public class ProductJudgeItemDataHolder extends RecyclerDataHolder {

    public ProductJudgeItemDataHolder(EvaluateModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {

        ProductJudgeBar bar = new ProductJudgeBar(context);

        return new RecyclerViewHolder(bar);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        ProductJudgeBar bar = (ProductJudgeBar) vHolder.itemView;
        bar.onBindJudgeData((EvaluateModel) data);
    }
}
