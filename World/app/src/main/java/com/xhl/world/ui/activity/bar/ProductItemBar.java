package com.xhl.world.ui.activity.bar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.CollectionModel;
import com.xhl.world.ui.event.CollectionOpEvent;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.world.ui.view.pub.BaseBar;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/1/5.
 */
public class ProductItemBar extends BaseBar {

    @ViewInject(R.id.iv_item_order_icon)
    private LifeCycleImageView iv_item_order_icon;

    @ViewInject(R.id.tv_item_order_name)
    private TextView tv_item_order_name;

    @ViewInject(R.id.tv_item_order_price)
    private TextView tv_item_order_price;

    @ViewInject(R.id.tv_item_order_num)
    private TextView tv_item_order_num;

    @ViewInject(R.id.iv_right)
    private ImageView iv_right;

    @ViewInject(R.id.rl_details)
    private RelativeLayout rl_details;

    private int mPos;
    private CollectionModel mData;

    public ProductItemBar(Context context) {
        super(context);
    }

    public ProductItemBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {

        rl_details.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                postEvent(0);
            }
        });

        rl_details.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                postEvent(1);
                return true;
            }
        });

    }

    private void postEvent(int actionType) {
        CollectionOpEvent event = new CollectionOpEvent();
        event.actionType = actionType;
        event.data = mData;
        event.deletePosition = mPos;
        event.collectionType = 0;//商品
        event.productId = mData.getProductId();
        //派发事件
        EventBusHelper.post(event);
    }

    public void setCurPos(int pos) {
        mPos = pos;
    }


    public void setProductInfo(CollectionModel childModel) {
        mData = childModel;
        iv_item_order_icon.bindImageUrl(childModel.getProductPicUrl());
        tv_item_order_price.setText(getResources().getString(R.string.price, childModel.getProductPrice()));
        tv_item_order_name.setText(childModel.getProductName());
        String productNum = childModel.getProductNum();
        if (!TextUtils.isEmpty(productNum)) {
            tv_item_order_num.setText(getResources().getString(R.string.num, childModel.getProductName()));
        } else
            tv_item_order_num.setVisibility(INVISIBLE);

        iv_right.setVisibility(INVISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_order_manager_child;
    }
}
