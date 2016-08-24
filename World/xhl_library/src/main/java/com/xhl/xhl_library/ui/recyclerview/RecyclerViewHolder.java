package com.xhl.xhl_library.ui.recyclerview;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

public class RecyclerViewHolder extends ViewHolder
{
    
    private View[] mParams;
    
    public RecyclerViewHolder(View view) {
        super(view);
    }
    
    public RecyclerViewHolder setParams(View... params) {
        this.mParams = params;
        return this;
    }
    
    public View getParam(int index) {
        return mParams[index];
    }
    
}
