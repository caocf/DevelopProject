package com.xhl.bqlh.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xhl.bqlh.model.CarModel;
import com.xhl.bqlh.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.bqlh.view.ui.bar.CarItemBar;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

import java.util.List;

/**
 * Created by Sum on 16/7/9.
 */
public class CarInfoDataHolder extends RecyclerDataHolder<List<CarModel>> {
    private int type;

    private RecycleViewCallBack callBack;

    public CarInfoDataHolder(List<CarModel> data, int type) {
        super(data);
        this.type = type;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new RecyclerViewHolder(new CarItemBar(context));
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, List<CarModel> data) {
        CarItemBar bar = (CarItemBar) vHolder.itemView;
        bar.onBindData(data);
        bar.setmCallback(callBack);
    }

    public void setCallBack(RecycleViewCallBack callBack) {
        this.callBack = callBack;
    }
}
