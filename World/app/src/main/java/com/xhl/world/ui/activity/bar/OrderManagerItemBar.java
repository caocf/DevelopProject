package com.xhl.world.ui.activity.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.Type.OrderState;
import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.model.serviceOrder.OrderDetail;
import com.xhl.world.ui.event.OrderManagerEvent;
import com.xhl.world.ui.view.pub.BaseBar;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 16/1/5.
 */
public class OrderManagerItemBar extends BaseBar {

    @ViewInject(R.id.tv_shop_name)
    private TextView tv_shop_name;

    @ViewInject(R.id.tv_order_state)
    private TextView tv_order_state;

    @ViewInject(R.id.ll_content)
    private LinearLayout ll_content;

    @ViewInject(R.id.ll_action)
    private LinearLayout ll_action;

    @ViewInject(R.id.button_1)
    private Button button_1;

    @ViewInject(R.id.button_2)
    private Button button_2;

    @ViewInject(R.id.button_3)
    private Button button_3;

    private Order mOrder;

    private int mViewTag;

    public OrderManagerItemBar(Context context) {
        super(context);
    }

    public OrderManagerItemBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
        ll_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                postEvent(OrderManagerEvent.ORDER_ACTION_DETAILS);
            }
        });
    }

    public void onBindData(Order orderModel) {

        if (mOrder == orderModel) {
            return;
        }
        mOrder = orderModel;
        //设置平台名称
        tv_shop_name.setText(orderModel.getCompanyName());

        setTextShow();
        addChildView();

    }

    private void addChildView() {

        ll_content.removeAllViews();

        List<OrderDetail> orderDetails = mOrder.getOrderDetailList();
        OrderManagerItemChildBar childBar = null;
        for (OrderDetail child : orderDetails) {
            childBar = new OrderManagerItemChildBar(getContext());

            childBar.setChildOrderInfo(child);

//            int height = getResources().getDimensionPixelSize(R.dimen.item_order_height);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);

            childBar.setLayoutParams(params);

            AutoUtils.autoSize(childBar);

            childBar.setViewTag(mViewTag);

            childBar.setOrder(mOrder);//添加商品关联的订单

            checkOrderState(childBar);

            ll_content.addView(childBar);
        }
        if (childBar != null) {
            childBar.hideLine();
        }
    }

    //检测是否显示单个商品操作栏
    private void checkOrderState(OrderManagerItemChildBar bar) {
        if (mOrder.getOrderState() == OrderState.order_wait_judge ||
                mOrder.getOrderState() == OrderState.order_finish) {
            bar.completeActionShow();
            bar.isNeedHideLine = false;//view的线条不能隐藏
        }
    }

    private void setTextShow() {
        ll_action.setVisibility(VISIBLE);
        switch (mOrder.getOrderState()) {

            case OrderState.order_wait_pay:
                button_1.setVisibility(GONE);
                button_2.setVisibility(VISIBLE);
                button_3.setVisibility(VISIBLE);
                button_2.setText("取消订单");
                button_2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionCancelOrder();
                    }
                });
                button_3.setText("付款");
                button_3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionPayOrder();
                    }
                });
                break;

            case OrderState.order_wait_pick_sender:
                button_1.setText("提醒发货");
                button_1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionHintSend();
                    }
                });
                button_1.setVisibility(VISIBLE);
                button_2.setVisibility(GONE);
                button_3.setVisibility(View.GONE);
                break;

            case OrderState.order_wait_pick:
                button_1.setVisibility(GONE);
                button_2.setVisibility(VISIBLE);
                button_3.setVisibility(VISIBLE);
                button_2.setText("查看物流");
                button_2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionWatchCommerce();
                    }
                });
                button_3.setText("确认收货");
                button_3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionConfirmOrder();
                    }
                });
                break;

            case OrderState.order_wait_judge:
                ll_action.setVisibility(GONE);
              /*  button_1.setVisibility(GONE);
                button_2.setVisibility(VISIBLE);
                button_2.setText("申请退货");
                button_2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionApply();
                    }
                });
                button_3.setVisibility(VISIBLE);
                button_3.setText("去评价");
                button_3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionGoJudge();
                    }
                });*/
                break;

            case OrderState.order_cancel:
                ll_action.setVisibility(GONE);
                break;

            case OrderState.order_finish:
                ll_action.setVisibility(GONE);
              /*  tv_order_state.setText(getResources().getString(R.string.order_finish));
                button_1.setVisibility(GONE);
                button_3.setVisibility(GONE);
                button_2.setVisibility(VISIBLE);
                button_2.setText("申请退货");
                button_2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionApply();
                    }
                });*/
                break;
        }
        tv_order_state.setText(mOrder.getOrderStateDesc());
    }

    private void actionPayOrder() {
        int type = OrderManagerEvent.ORDER_ACTION_PAY;
        postEvent(type);
    }

    private void actionCancelOrder() {
        int type = OrderManagerEvent.ORDER_ACTION_CANCEL;
        postEvent(type);
    }

    private void actionConfirmOrder() {
        int type = OrderManagerEvent.ORDER_ACTION_sh;
        postEvent(type);
    }

    private void actionHintSend() {
        int type = OrderManagerEvent.ORDER_ACTION_tx;
        postEvent(type);
    }

    private void actionWatchCommerce() {
        int type = OrderManagerEvent.ORDER_ACTION_wl;
        postEvent(type);
    }

    private void postEvent(int type) {

        OrderManagerEvent event = new OrderManagerEvent();
        event.setOrder(mOrder);
        event.setOrder_action(type);
        event.setView_tag(mViewTag);

        EventBus.getDefault().post(event);
    }

    public void setViewTag(int tag) {
        mViewTag = tag;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_order_manager;
    }
}
