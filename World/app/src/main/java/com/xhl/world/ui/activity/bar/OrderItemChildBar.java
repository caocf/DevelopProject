package com.xhl.world.ui.activity.bar;

import android.content.Context;
import android.content.res.Resources;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.Entities.ShoppingOrderEntities;
import com.xhl.world.ui.view.pub.BaseBar;
import com.xhl.world.ui.view.LifeCycleImageView;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 15/12/22.
 */
public class OrderItemChildBar extends BaseBar {

    @ViewInject(R.id.iv_item_child_icon)
    private LifeCycleImageView mImage;

    @ViewInject(R.id.tv_item_child_name)
    private TextView tv_item_child_name;

    @ViewInject(R.id.tv_item_child_price)
    private TextView tv_item_child_price;

    @ViewInject(R.id.tv_item_child_count)
    private TextView tv_item_child_count;


    public OrderItemChildBar(Context context) {
        super(context);
    }

    @Override
    protected void initParams() {

    }

    public void setData(ShoppingOrderEntities order) {

        if (order == null) {
            return;
        }
        try {
            mImage.bindImageUrl(order.getGoods_url());
            tv_item_child_name.setText(order.getGoods_title());
            tv_item_child_price.setText(getResources().getString(R.string.price, order.getGoods_price()));
            tv_item_child_count.setText("x" + order.getCount());
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_order_item_child;
    }
}
