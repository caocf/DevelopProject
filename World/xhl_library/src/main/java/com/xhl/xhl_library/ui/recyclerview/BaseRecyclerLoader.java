package com.xhl.xhl_library.ui.recyclerview;

import android.content.Context;

import com.xhl.xhl_library.TaskLoader.BaseTaskLoader;

import java.util.List;

/**
 * Created by Wendell on 15-10-22.
 */
public abstract class BaseRecyclerLoader extends BaseTaskLoader<List<RecyclerDataHolder>> {

    public BaseRecyclerLoader(Context context) {
        super(context);
    }
    
    public BaseRecyclerLoader(Context context, boolean canCancelOnStop) {
        super(context,canCancelOnStop);
    }

    @Override
    protected void onReleaseData(List<RecyclerDataHolder> data) {
    }

}
