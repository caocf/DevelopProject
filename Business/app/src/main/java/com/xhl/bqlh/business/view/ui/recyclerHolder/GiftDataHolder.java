package com.xhl.bqlh.business.view.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.GiftModel;
import com.xhl.bqlh.business.Model.Type.ProductType;
import com.xhl.bqlh.business.R;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Summer on 2016/7/27.
 */
public class GiftDataHolder extends RecyclerDataHolder<GiftModel> {

    public GiftDataHolder(GiftModel data) {
        super(data);
    }

    @Override
    public int getType() {
        return 4;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        View content = View.inflate(context, R.layout.item_gift_product, null);
        content.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));

        return new GiftViewHolder(content);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, GiftModel data) {
        GiftViewHolder holder = (GiftViewHolder) vHolder;

        holder.tv_name.setText(ProductType.GetGiftTitle(data.getGiftName()));

        String num = "赠送数量: " + data.getGiftNum();
        holder.tv_num.setText(num);
    }

    static class GiftViewHolder extends RecyclerViewHolder {

        private TextView tv_name;

        private TextView tv_num;

        public GiftViewHolder(View view) {
            super(view);
            tv_num = (TextView) view.findViewById(R.id.tv_product_max);
            tv_name = (TextView) view.findViewById(R.id.tv_product_name);
        }
    }
}
