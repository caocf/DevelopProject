package com.xhl.world.ui.main.home_new.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.xhl.world.model.ProductModel;
import com.xhl.world.ui.main.home_new.bar.HomeSuggestBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import java.util.List;

/**
 * Created by Sum on 15/12/2.
 */
public class GuessDataHolder extends RecyclerDataHolder {


    public GuessDataHolder(Object data) {
        super(data);
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        return new RecyclerViewHolder(new HomeSuggestBar(context));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        HomeSuggestBar bar = (HomeSuggestBar) vHolder.itemView;
        bar.onBindGuessLike((List<ProductModel>) data);
    }
}
