package com.xhl.world.ui.main.classify.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.xhl.world.model.ClassifyItemModel;
import com.xhl.world.ui.main.classify.bar.ClassifyDetailsItemBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 15/12/2.
 */
public class ClassifyDetailsDataHolder extends RecyclerDataHolder {

    private boolean isFirstLoad = false;

    public ClassifyDetailsDataHolder(ClassifyItemModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        ClassifyDetailsItemBar view = new ClassifyDetailsItemBar(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        view.setLayoutParams(params);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        ClassifyDetailsItemBar bar = (ClassifyDetailsItemBar) vHolder.itemView;
        if (!isFirstLoad) {
            isFirstLoad = true;
            bar.setClassifyItemData((ClassifyItemModel) data);
        } else {
            bar.reBindData();
        }

    }
}
