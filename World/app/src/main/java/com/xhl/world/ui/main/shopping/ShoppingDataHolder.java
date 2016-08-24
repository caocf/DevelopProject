package com.xhl.world.ui.main.shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xhl.world.model.ShoppingItemDetailsModel;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerViewHolder;

/**
 * Created by Sum on 15/12/15.
 */
public class ShoppingDataHolder extends RecyclerDataHolder {

    private ShoppingItemDetailsModel mData;

    private int mType = 0;

    private ShopItemBar itemBar;

    public ShoppingDataHolder(ShoppingItemDetailsModel data) {
        super(data);
        mData = data;
    }

    //添加Context避免首次onCreateViewHolder未执行，View为Null出现显示问题
    //订单主布局采用RecyclerView实现，需要注意View未生成的时候数据问题
    public ShopItemBar getBar(Context context) {
        if (itemBar == null) {
            itemBar = new ShopItemBar(context);
            itemBar.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
            itemBar.setAdapterPosition(mType);//先设置View位置，在设置数据，设置数据动态生成子View需要设置位置值
            itemBar.setShoppingDetails(mData);
        }
        return itemBar;
    }

    public void setViewType(int type) {
        mType = type;
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, int position) {
        if (itemBar == null) {
            itemBar = new ShopItemBar(context);
            itemBar.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        }
        return new RecyclerViewHolder(itemBar);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Object data) {
        ShopItemBar bar = (ShopItemBar) vHolder.itemView;
        itemBar = bar;
        bar.setAdapterPosition(position);
        bar.setShoppingDetails((ShoppingItemDetailsModel) data);
        if (getHolderState() == HOLDER_SELECT) {
            bar.updateCheckState(true);
            bar.checkAllState();
        } else if (getHolderState() == HOLDER_UN_SELECT) {
            bar.updateCheckState(false);
            bar.clearAllState();
        }
    }
}
