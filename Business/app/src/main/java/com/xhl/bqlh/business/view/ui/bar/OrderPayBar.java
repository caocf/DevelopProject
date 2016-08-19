package com.xhl.bqlh.business.view.ui.bar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.business.Model.OrderModel;
import com.xhl.bqlh.business.Model.Type.OrderType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseBar;
import com.xhl.bqlh.business.view.custom.ComplexText;
import com.xhl.bqlh.business.view.event.OrderDetailsEvent;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.xhl_library.utils.NumberUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 16/4/15.
 * 赊账数据的订单
 */
public class OrderPayBar extends BaseBar {

    @ViewInject(R.id.tv_order_type)
    private TextView tv_order_type;//订单类型

    @ViewInject(R.id.tv_order_shop)
    private TextView tv_order_shop;//订单店铺

    @ViewInject(R.id.tv_order_num)
    private TextView tv_order_num;//订单号

    @ViewInject(R.id.tv_order_money)
    private TextView tv_order_money;//订单金额

    @ViewInject(R.id.tv_order_coupons_money)
    private TextView tv_order_coupons_money;//订单优惠金额

    @ViewInject(R.id.tv_order_create_time)
    private TextView tv_order_create_time;//创建时间

    @ViewInject(R.id.tv_order_state)
    private TextView tv_order_state;//订单状态

    @ViewInject(R.id.tv_pay_money)
    private TextView tv_pay_money;//订单状态

    @ViewInject(R.id.tv_order_status)
    private TextView tv_order_status;//优惠标示

    @ViewInject(R.id.tv_order_account)
    private TextView tv_order_account;//赊账标示


    @Event(R.id.ll_content)
    private void onDetailsClick(View view) {
        OrderDetailsEvent event = new OrderDetailsEvent();
        event.storeOrderCode = orderModel.getStoreOrderCode();
        event.name = orderModel.getReceivingPersonName();
        EventBus.getDefault().post(event);
    }

    private OrderModel orderModel;

    public OrderPayBar(Context context) {
        super(context);
    }

    @Override
    protected void initParams() {

    }

    public void onBindData(OrderModel order) {
        if (order == null) {
            return;
        }

        orderModel = order;
        if (order.isNeedShowType) {
            ViewHelper.setViewVisible(tv_order_type,true);
        }else {
            ViewHelper.setViewVisible(tv_order_type,false);
        }
        tv_order_type.setText(order.getOrderTypeDesc());

        //优惠标示
        BigDecimal couponsMoney = order.getCouponsMoney();
        if (couponsMoney != null && couponsMoney.floatValue() != 0) {
            ViewHelper.setViewGone(tv_order_coupons_money, false);

            String comMoney = mContext.getString(R.string.order_money_coupon, couponsMoney.setScale(2, BigDecimal.ROUND_HALF_UP));
            ComplexText.TextBuilder money = new ComplexText.TextBuilder(comMoney);
            int color1 = ContextCompat.getColor(mContext, R.color.app_red);
            money.setTextColor(color1, 5, comMoney.length());
            tv_order_coupons_money.setText(money.Build());


            tv_order_status.setVisibility(View.VISIBLE);
            tv_order_status.setText(R.string.order_state_info_yh);
            tv_order_status.setBackgroundResource(R.drawable.code_order_status_2);
        } else {
            ViewHelper.setViewGone(tv_order_coupons_money, true);
            ViewHelper.setViewGone(tv_order_status, true);
        }
        //赊账标示
        float arrears = order.getArrears();
        if (arrears != 0) {
            ViewHelper.setViewGone(tv_order_account, false);
        }else {
            ViewHelper.setViewGone(tv_order_account, true);
        }

        //订单编号
        tv_order_num.setText(mContext.getString(R.string.order_num, order.getStoreOrderCode()));
        //订单金额
        String orderMoney = mContext.getString(R.string.order_money, order.getRealOrderMoney());
        ComplexText.TextBuilder builder = new ComplexText.TextBuilder(orderMoney);
        int color = ContextCompat.getColor(mContext, R.color.app_price_color);
        builder.setTextColor(color, 5, orderMoney.length());
        tv_order_money.setText(builder.Build());
//        tv_order_money.setText(mContext.getString(R.string.order_money, order.getRealOrderMoney()));
        //下单时间
        tv_order_create_time.setText(order.getOrderTime());
        //收货人
        tv_order_shop.setText(order.getReceivingName());

        String status = order.getDistributionStatus();
        int state = Integer.parseInt(status);
        if (state == OrderType.order_state_not_send) {
            tv_order_state.setText(R.string.order_state_1);
            tv_order_state.setBackgroundResource(R.drawable.code_order_state);
            tv_order_state.setTextColor(ContextCompat.getColor(mContext, R.color.app_orange));
        } else if (state == OrderType.order_state_have_send) {
            tv_order_state.setText(R.string.order_state_2);
            tv_order_state.setBackgroundResource(R.drawable.code_order_state);
            tv_order_state.setTextColor(ContextCompat.getColor(mContext, R.color.app_orange));
        } else if (state == OrderType.order_state_finish) {
            tv_order_state.setText(R.string.order_state_3);
            tv_order_state.setTextColor(ContextCompat.getColor(mContext, R.color.base_light_text_color));
            tv_order_state.setBackgroundResource(R.color.transparent);
        }
        //赊账金额
        tv_pay_money.setText(mContext.getString(R.string.order_details_lost_money, NumberUtil.getDouble(order.getArrears())));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_item_pay_order;
    }
}
