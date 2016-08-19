package com.xhl.bqlh.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.BrandModel;
import com.xhl.bqlh.model.event.ShopEvent;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.bqlh.view.helper.pub.Callback.RecycleViewCallBack;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 16/7/6.
 */
public class SearchBrandDataHolder extends RecyclerDataHolder<BrandModel> {

    private RecycleViewCallBack callBack;

    public SearchBrandDataHolder(BrandModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View view = View.inflate(context, R.layout.item_serach_brand, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Context context, final int position, RecyclerView.ViewHolder vHolder, final BrandModel data) {
        View view = vHolder.itemView;
        TextView tv_brand_name = (TextView) view.findViewById(R.id.tv_brand_name);
        tv_brand_name.setText(data.getBrandName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    callBack.onItemClick(position, data);
                } else {
                    ShopEvent event = new ShopEvent();
                    event.setTag(ShopEvent.TAG_SEARCH_BRAND);
                    event.filterId = data.getId();
                    EventHelper.postDefaultEvent(event);
                }
            }
        });
    }

    public void setCallBack(RecycleViewCallBack callBack) {
        this.callBack = callBack;
    }
}
