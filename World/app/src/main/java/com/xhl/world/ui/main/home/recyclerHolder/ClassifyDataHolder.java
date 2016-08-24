package com.xhl.world.ui.main.home.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xhl.world.ui.main.home.HomeItemType;
import com.xhl.world.ui.main.home.bar.HomeClassifyBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 15/12/2.
 */
public class ClassifyDataHolder extends RecyclerDataHolder {


    private HomeClassifyBar mClassifyBar;
    public ClassifyDataHolder(Object data) {
        super(data);
    }

    @Override
    public int getType() {
        return HomeItemType.Type_classify;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        mClassifyBar=new HomeClassifyBar(context);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1,-2);
        mClassifyBar.setLayoutParams(params);

        return new RecyclerViewHolder(mClassifyBar);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
    }
}
