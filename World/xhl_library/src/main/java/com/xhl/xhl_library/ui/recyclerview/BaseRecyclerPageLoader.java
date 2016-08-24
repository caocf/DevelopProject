package com.xhl.xhl_library.ui.recyclerview;

import android.content.Context;

import com.xhl.xhl_library.TaskLoader.BaseTaskPageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wendell on 15-10-22.
 */
public abstract class BaseRecyclerPageLoader extends BaseTaskPageLoader<List<RecyclerDataHolder>> {

    public BaseRecyclerPageLoader(Context context) {
        super(context);
    }
    
    public BaseRecyclerPageLoader(Context context, boolean canCancelOnStop) {
        super(context,canCancelOnStop);
    }

    @Override
    protected void onReleaseData(List<RecyclerDataHolder> data) {
    }

    @Override
    protected int getCount(List<RecyclerDataHolder> data) {
        return data.size();
    }

    @Override
    protected List<RecyclerDataHolder> merge(List<RecyclerDataHolder> old, List<RecyclerDataHolder> add) {
        List<RecyclerDataHolder> all = new ArrayList<RecyclerDataHolder>(old.size() + add.size());
        all.addAll(old);
        all.addAll(add);
        return all;
    }

}
