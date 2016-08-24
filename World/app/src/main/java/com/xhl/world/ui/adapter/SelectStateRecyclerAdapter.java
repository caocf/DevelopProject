package com.xhl.world.ui.adapter;

import android.content.Context;

import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

/**
 * Created by Sum on 15/12/11.
 */
public class SelectStateRecyclerAdapter extends RecyclerAdapter {


    public SelectStateRecyclerAdapter(Context context) {
        super(context);
    }


    public void setSelectPosition(int position) {
       // clearSelectState();
        if (mHolders != null && position < mHolders.size()) {

            for (int i = 0; i < mHolders.size(); i++) {
                RecyclerDataHolder holder = mHolders.get(i);
                if (holder != null) {
                    if (position == i) {
                        holder.setHolderState(RecyclerDataHolder.HOLDER_SELECT);
                    } else {
                        holder.setHolderState(RecyclerDataHolder.HOLDER_UN_SELECT);
                    }
                }
            }
            notifyDataSetChanged();
//
//            RecyclerDataHolder holder = mHolders.get(position);
//            holder.setHolderState(RecyclerDataHolder.HOLDER_SELECT);
        }
    }

    public void clearPosition(int postion) {
        if (mHolders != null) {
            mHolders.get(postion).setHolderState(RecyclerDataHolder.HOLDER_NORMAL);
        }
    }

    public void selectAll() {
        if (mHolders != null) {
            for (int i = 0; i < mHolders.size(); i++) {
                RecyclerDataHolder holder = mHolders.get(i);
                if (holder != null) {
                    holder.setHolderState(RecyclerDataHolder.HOLDER_SELECT);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void clearSelectState() {
        if (mHolders != null) {
            for (int i = 0; i < mHolders.size(); i++) {
                RecyclerDataHolder holder = mHolders.get(i);
                if (holder != null) {
                    holder.setHolderState(RecyclerDataHolder.HOLDER_UN_SELECT);
                }
            }
            notifyDataSetChanged();
        }
    }
}
