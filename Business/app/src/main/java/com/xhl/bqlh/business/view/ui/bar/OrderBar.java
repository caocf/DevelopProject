package com.xhl.bqlh.business.view.ui.bar;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.OrderModel;
import com.xhl.bqlh.business.Model.Type.OrderType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.BaseBar;
import com.xhl.bqlh.business.view.custom.ComplexText;
import com.xhl.bqlh.business.view.event.OrderDetailsEvent;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.helper.pub.Callback.RecycleViewCallBack;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;

import de.greenrobot.event.EventBus;

/**
 * Created by Sum on 16/4/15.
 */
public class OrderBar extends BaseBar {

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

    @ViewInject(R.id.tv_order_status)
    private TextView tv_order_status;//订单状态

    @ViewInject(R.id.tv_order_account)
    private TextView tv_order_account;//赊账

    @ViewInject(R.id.line_hor)
    private View line_hor;//订单状态

    @Event(R.id.rl_details)
    private void onDetailsClick(View view) {
        if (mIsNeedDetails) {
            OrderDetailsEvent event = new OrderDetailsEvent();
            event.storeOrderCode = orderModel.getStoreOrderCode();
            event.name = orderModel.getReceivingPersonName();
            EventBus.getDefault().post(event);
        }
    }

    @Event(R.id.tv_order_state)
    private void onSendClick(View view) {
        if (mCanSendOrder) {//发货
            AlertDialog.Builder dialog = DialogMaker.getDialog(getContext());
            dialog.setTitle("订单发货");
            dialog.setMessage("您确定该订单已发货?");
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendOrder();
                }
            });
            dialog.setNegativeButton(R.string.dialog_cancel, null);
            dialog.show();
        }
    }

    private void sendOrder() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        ApiControl.getApi().orderSend(orderModel.getStoreOrderCode(), new Callback.CommonCallback<ResponseModel<Object>>() {
            @Override
            public void onSuccess(ResponseModel<Object> result) {
                if (result.isSuccess()) {
                    ToastUtil.showToastShort("发货成功");
                    mCanSendOrder = false;
                    tv_order_state.setText(R.string.order_state_2);
                    tv_order_state.setTextColor(ContextCompat.getColor(getContext(), R.color.app_orange));
                    tv_order_state.setBackgroundResource(R.drawable.code_order_state);
                    orderModel.setDistributionStatus("2");
                } else {
                    ToastUtil.showToastShort(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isLoading = false;
            }
        });
    }

    private OrderModel orderModel;

    public OrderBar(Context context) {
        super(context);
    }

    //点击事件回调处理
    private RecycleViewCallBack callBack;
    private int position;

    private boolean mCanSendOrder = false;//是否可以发货
    private boolean mCanCancelOrder = false;//是否可以取消
    private boolean isLoading = false;

    private boolean mIsNeedDetails = true;
    private boolean mIsNeedLine = true;

    public OrderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {

    }

    public void detailShow(OrderModel order) {
        mIsNeedDetails = false;
        mIsNeedLine = false;
        onBindData(order);
        //发货状态按钮
        if (mCanSendOrder) {
            mCanSendOrder = false;
            tv_order_state.setText(R.string.order_state_1);
            tv_order_state.setTextColor(ContextCompat.getColor(getContext(), R.color.app_orange));
            tv_order_state.setBackgroundResource(R.drawable.code_order_state);
        }
    }

    public void onBindData(OrderModel order) {
        if (order == null) {
            return;
        }
        orderModel = order;
        if (order.isNeedShowType) {
            ViewHelper.setViewVisible(tv_order_type, true);
        } else {
            ViewHelper.setViewVisible(tv_order_type, false);
        }
        if (!mIsNeedLine) {
            ViewHelper.setViewGone(line_hor, true);
        }

        tv_order_type.setText(order.getOrderTypeDesc());
        //订单编号
        tv_order_num.setText(mContext.getString(R.string.order_num, order.getStoreOrderCode()));
        //支付金额
        String orderMoney = mContext.getString(R.string.order_money, order.getRealOrderMoney());
        ComplexText.TextBuilder builder = new ComplexText.TextBuilder(orderMoney);
        int color = ContextCompat.getColor(mContext, R.color.app_price_color);
        builder.setTextColor(color, 5, orderMoney.length());
        tv_order_money.setText(builder.Build());

        //下单时间
        tv_order_create_time.setText(order.getOrderTime());
        //收货人
        tv_order_shop.setText(order.getReceivingName());
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
        } else {
            ViewHelper.setViewGone(tv_order_account, true);
        }

        //订单状态
        String status = order.getDistributionStatus();
        int orderState = Integer.parseInt(status);
        //订单类型
        String type = order.getOrderType();
        int orderType = Integer.parseInt(type);
        //拆单标示
        if (orderType == OrderType.order_type_shop) {
            float realOrderMoney = order.getRealOrderMoney().floatValue();
            if (realOrderMoney != order.getRealPayMoney()) {
                ViewHelper.setViewGone(tv_order_status, false);
                tv_order_status.setText(R.string.order_state_info_cd);
                tv_order_status.setBackgroundResource(R.drawable.code_order_status_1);
            } else {
                ViewHelper.setViewGone(tv_order_status, true);
            }
        }

        if (orderState == OrderType.order_state_not_send) {
            //车销
            if (orderType == OrderType.order_type_car) {
                tv_order_state.setText("点击发货");
                tv_order_state.setBackgroundResource(R.drawable.code_task_state);
                tv_order_state.setTextColor(ContextCompat.getColor(getContext(), R.color.app_while));
                mCanSendOrder = true;
            } else {
                tv_order_state.setText(R.string.order_state_1);
                tv_order_state.setTextColor(ContextCompat.getColor(getContext(), R.color.app_orange));
                tv_order_state.setBackgroundResource(R.drawable.code_order_state);
                mCanSendOrder = false;
            }
            mCanCancelOrder = orderType == OrderType.order_type_car || orderType == OrderType.order_type_self;
        } else if (orderState == OrderType.order_state_have_send) {
            mCanCancelOrder = false;
            tv_order_state.setText(R.string.order_state_2);
            tv_order_state.setBackgroundResource(R.drawable.code_order_state);
            tv_order_state.setTextColor(ContextCompat.getColor(mContext, R.color.app_orange));
        } else if (orderState == OrderType.order_state_finish) {
            mCanCancelOrder = false;
            tv_order_state.setText(R.string.order_state_3);
            tv_order_state.setTextColor(ContextCompat.getColor(mContext, R.color.base_light_text_color));
            tv_order_state.setBackgroundResource(R.color.transparent);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_item_order;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setCallBack(RecycleViewCallBack callBack) {
        this.callBack = callBack;
    }
}
