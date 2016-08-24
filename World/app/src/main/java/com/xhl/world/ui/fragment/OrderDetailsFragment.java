package com.xhl.world.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhl.world.R;
import com.xhl.world.model.Type.OrderState;
import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.model.serviceOrder.OrderDetail;
import com.xhl.world.ui.activity.bar.OrderManagerItemChildBar;
import com.xhl.world.ui.event.OrderManagerEvent;
import com.xhl.xhl_library.ui.view.RippleView;
import com.xhl.xhl_library.utils.NumberUtil;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 16/1/5.
 */
@ContentView(R.layout.fragment_order_details)
public class OrderDetailsFragment extends BaseAppFragment {

    @ViewInject(R.id.title_name)
    private TextView title_name;

    @ViewInject(R.id.ripple_select_address)
    private RippleView ripple_select_address;
    @ViewInject(R.id.tv_location_people)
    private TextView tv_location_people;//收件人
    @ViewInject(R.id.tv_location_phone)
    private TextView tv_location_phone;//收件人手机号
    @ViewInject(R.id.tv_location_details)
    private TextView tv_location_details;//收件人地址
    @ViewInject(R.id.iv_right)
    private ImageView iv_right;

    @ViewInject(R.id.tv_order_id)
    private TextView tv_order_id;//订单Id
    @ViewInject(R.id.tv_order_state)
    private TextView tv_order_state;//订单状态
    @ViewInject(R.id.ll_content)
    private LinearLayout ll_content;//字订单内容


    @ViewInject(R.id.tv_price_order_postal)
    private TextView tv_price_order_postal;//快递方式
    @ViewInject(R.id.tv_price_order)
    private TextView tv_price_order;//商品价格
    @ViewInject(R.id.rl_order_revenue)
    private RelativeLayout rl_order_revenue;
    @ViewInject(R.id.tv_price_order_revenue)
    private TextView tv_price_order_revenue;//关税价格
    @ViewInject(R.id.tv_price_total)
    private TextView tv_price_total;//应付总额
    @ViewInject(R.id.rl_order_integral)
    private RelativeLayout rl_order_integral;//积分父容器
    @ViewInject(R.id.tv_price_order_integral)
    private TextView tv_price_order_integral;//积分
    @ViewInject(R.id.rl_order_real_pay)
    private RelativeLayout rl_order_real_pay;//实付父容器
    @ViewInject(R.id.tv_price_real_pay)
    private TextView tv_price_real_pay;//实付价格


    @ViewInject(R.id.tv_order_info_time_xd)
    private TextView tv_order_time_xd;//下单时间
    @ViewInject(R.id.tv_order_info_time_fh)
    private TextView tv_order_time_fh;//发货时间
    @ViewInject(R.id.tv_order_info_time_fk)
    private TextView tv_order_time_fk;//付款时间

    @ViewInject(R.id.ll_action)
    private LinearLayout ll_action;//订单的三个操作按钮
    @ViewInject(R.id.button_1)
    private Button button_1;
    @ViewInject(R.id.button_2)
    private Button button_2;
    @ViewInject(R.id.button_3)
    private Button button_3;

    @Event(value = R.id.title_back, type = RippleView.OnRippleCompleteListener.class)
    private void onBackClick(View view) {
        getSumContext().popTopFragment(null);
    }

    private Order mOrder;

    @Override
    protected void initParams() {
        title_name.setText("订单详情");
        ripple_select_address.setNeedShowRipple(false);
        iv_right.setVisibility(View.INVISIBLE);
        //地址信息
        setLocationShow();
        //按钮控制显示
        setTextShow();
        //商品信息
        addChildView();
        //订单信息
        tv_order_id.setText(getString(R.string.order_info_id, mOrder.getStoreOrderCode()));//订单编号
        tv_order_time_xd.setText(getString(R.string.order_info_time_xd, mOrder.getOrderTime()));//下单时间
        tv_order_time_fh.setText(getString(R.string.order_info_time_fh, mOrder.getDeliveryTime() == null ? "暂无数据" : mOrder.getDeliveryTime()));//发货时间
        tv_order_time_fk.setText(getString(R.string.order_info_time_fk, mOrder.getPayTime() == null ? "暂无数据" : mOrder.getPayTime()));//付款时间

        setMoneyShow();

    }

    private void setMoneyShow() {
        float tax = 0;
        float productPrice = 0;
        List<OrderDetail> orderDetailList = mOrder.getOrderDetailList();
        for (OrderDetail od : orderDetailList) {
            String taxPrice = od.getTaxprice();//关税
            if (!TextUtils.isEmpty(taxPrice)) {
                float f = Float.parseFloat(taxPrice);
                tax += f;
            }
            String totalPrice = od.getTotalPrice();//总价
            if (!TextUtils.isEmpty(totalPrice)) {
                float anInt = Float.parseFloat(totalPrice);
                productPrice += anInt;
            }
        }
        if (tax == 0) {
            rl_order_revenue.setVisibility(View.GONE);
        } else
            tv_price_order_revenue.setText(getString(R.string.price, NumberUtil.getDouble(tax)));//订单关税

        tv_price_order.setText(getString(R.string.price,  NumberUtil.getDouble(productPrice)));//商品总额

        tv_price_total.setText(getString(R.string.price, mOrder.getOnlinePayMoney()));//订单应付价格（在生成总订单的是自动分配在线支付金额）

        String integral = mOrder.getIntegral();
        if (!TextUtils.isEmpty(integral)) {
            rl_order_integral.setVisibility(View.VISIBLE);
            tv_price_order_integral.setText(mOrder.getIntegral());//使用的积分（一个订单积分会总积分里面自动评分）
        }

        float realPayMoney = mOrder.getRealPayMoney();
        if (realPayMoney != 0) {
            rl_order_real_pay.setVisibility(View.VISIBLE);
            tv_price_real_pay.setText(getString(R.string.price, mOrder.getRealPayMoney()));//实际付款价格
        }
    }

    private void setLocationShow() {
        tv_location_details.setText(mOrder.getArea() + " " + mOrder.getAddress());//收件人地址
        tv_location_people.setText(getString(R.string.address_user, mOrder.getReceivingName()));//收件人姓名
        tv_location_phone.setText(mOrder.getPhone());//收件人手机号
    }

    private void addChildView() {

        List<OrderDetail> order_childs = mOrder.getOrderDetailList();

        ll_content.removeAllViews();
        OrderManagerItemChildBar childBar = null;
        for (OrderDetail child : order_childs) {
            childBar = new OrderManagerItemChildBar(getContext());
            childBar.setChildOrderInfo(child);
            childBar.setArrowShow(false);
//            int height = getResources().getDimensionPixelSize(R.dimen.item_order_height);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);
            childBar.setLayoutParams(params);
            //view标示位置
            childBar.setViewTag(mOrder.getViewTag());
            //添加关联的订单
            childBar.setOrder(mOrder);

            checkOrderState(childBar);

            AutoUtils.autoSize(childBar);

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
        switch (mOrder.getOrderState()) {

            case OrderState.order_wait_pay:
                button_1.setVisibility(View.GONE);
                button_2.setText("取消订单");
                button_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionCancelOrder();
                    }
                });
                button_3.setText("付款");
                button_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionPayOrder();
                    }
                });
                break;

            case OrderState.order_wait_pick_sender:
                button_1.setText("提醒发货");
                button_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionHintSend();
                    }
                });
                button_2.setVisibility(View.GONE);
                button_3.setVisibility(View.GONE);
                break;

            case OrderState.order_wait_pick:
                button_1.setVisibility(View.GONE);
                button_2.setText("查看物流");
                button_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionWatchCommerce();
                    }
                });
                button_3.setText("确认收货");
                button_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionConfirmOrder();
                    }
                });
                break;

            case OrderState.order_wait_judge:
                ll_action.setVisibility(View.GONE);
             /*   button_1.setVisibility(View.GONE);
                button_2.setVisibility(View.VISIBLE);
                button_2.setText("申请退货");
                button_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionApply();
                    }
                });
                button_3.setText("去评价");
                button_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionGoJudge();
                    }
                });*/
                break;

            case OrderState.order_cancel:
                ll_action.setVisibility(View.GONE);
                break;

            case OrderState.order_finish:
                ll_action.setVisibility(View.GONE);
               /* tv_order_state.setText(getResources().getString(R.string.order_finish));
                button_1.setVisibility(View.GONE);
                button_2.setVisibility(View.GONE);
                button_3.setText("申请退货");
                button_3.setOnClickListener(new View.OnClickListener() {
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

    private void actionGoJudge() {
        int type = OrderManagerEvent.ORDER_ACTION_pj;
        postEvent(type);
    }

    private void actionApply() {
        int type = OrderManagerEvent.ORDER_ACTION_th;
        postEvent(type);
    }

    private void postEvent(int type) {

        OrderManagerEvent event = new OrderManagerEvent();
        event.setOrder(mOrder);
        event.setOrder_action(type);
        event.setView_tag(mOrder.getViewTag());

        EventBus.getDefault().post(event);
    }

    @Override
    public void onEnter(Object data) {
        if (data instanceof Order) {
            mOrder = (Order) data;
        }
    }
}
