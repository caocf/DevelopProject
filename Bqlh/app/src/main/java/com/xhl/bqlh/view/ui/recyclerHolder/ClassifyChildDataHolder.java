package com.xhl.bqlh.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xhl.bqlh.model.ClassifyModel;
import com.xhl.bqlh.view.ui.bar.ClassifyItemBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/7/4.
 */
public class ClassifyChildDataHolder extends RecyclerDataHolder<ClassifyModel> {

    private int type;

    public ClassifyChildDataHolder(ClassifyModel data,int type) {
        super(data);
        this.type = type;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        ClassifyItemBar bar = new ClassifyItemBar(context);
        return new RecyclerViewHolder(bar);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, ClassifyModel data) {
        ClassifyItemBar bar = (ClassifyItemBar) vHolder.itemView;
        bar.onBindData(data);
    }
}
