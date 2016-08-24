package com.xhl.world.ui.recyclerHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.CollectionModel;
import com.xhl.world.ui.event.CollectionOpEvent;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Sum on 16/1/9.
 */
public class ShopItemDataHolder extends RecyclerDataHolder {

    public ShopItemDataHolder(CollectionModel data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_collection_shop, null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,-2);

        view.setLayoutParams(params);

        AutoUtils.auto(view);

        return new ShopRecyclerView(view);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        ShopRecyclerView bar = (ShopRecyclerView) vHolder;
        bar.bindData((CollectionModel) data);
    }

    static class ShopRecyclerView extends RecyclerViewHolder {

        private LifeCycleImageView imageView;
        private TextView textView;

        private CollectionModel mData;

        public ShopRecyclerView(View view) {
            super(view);
            imageView = (LifeCycleImageView) view.findViewById(R.id.iv_shop_icon);
            textView = (TextView) view.findViewById(R.id.iv_shop_name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postEvent(0);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    postEvent(1);
                    return true;
                }
            });
        }

        public void bindData(CollectionModel shop) {
            if (shop == null) {
                return;
            }
            mData = shop;
            imageView.bindImageUrl(shop.getShopLogo());
            textView.setText(shop.getShopName());
        }

        private void postEvent(int actionType) {
            CollectionOpEvent event = new CollectionOpEvent();
            event.actionType = actionType;
            event.data = mData;
            event.deletePosition = getAdapterPosition();
            event.collectionType = 1;//店铺
            //派发事件
            EventBusHelper.post(event);
        }
    }
}
