package com.xhl.world.ui.activity.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.model.serviceOrder.OrderDetail;
import com.xhl.world.ui.view.LifeCycleImageView;
import com.xhl.world.ui.view.pub.BaseBar;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by Sum on 15/12/22.
 */
public class OrderItemBar extends BaseBar {

    private LifeCycleImageView iv_item_shop_icon;
    private TextView tv_item_shop_name;
    private LinearLayout ll_item_child_content;

    public OrderItemBar(Context context) {
        super(context);
    }

    public OrderItemBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean autoInject() {
        return false;
    }

    @Override
    protected void initParams() {
        tv_item_shop_name = _findViewById(R.id.tv_item_parent_name);
//        iv_item_shop_icon = _findViewById(R.id.iv_item_shop_icon);
        ll_item_child_content = _findViewById(R.id.ll_item_child_content);
    }

    public void setData(Order order) {

        tv_item_shop_name.setText(order.getCompanyName());
        addOneOrderChildView(order);
    }

    private void addOneOrderChildView(Order order) {

        List<OrderDetail> orderDetails = order.getOrderDetailList();
        OrderManagerItemChildBar childBar = null;
        for (OrderDetail child : orderDetails) {
            childBar = new OrderManagerItemChildBar(getContext());
            childBar.setChildOrderInfo(child);
            childBar.setArrowShow(false);

            int height = getResources().getDimensionPixelSize(R.dimen.item_order_height);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, height);
            childBar.setLayoutParams(params);
            AutoUtils.autoSize(childBar);

            ll_item_child_content.addView(childBar);
        }
        if (childBar != null) {
            childBar.hideLine();
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.bar_order_item_parent;
    }
}
