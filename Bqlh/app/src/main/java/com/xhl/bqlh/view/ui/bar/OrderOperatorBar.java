package com.xhl.bqlh.view.ui.bar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.OrderModel;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.model.type.OrderState;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseBar;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.DialogMaker;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.bqlh.view.helper.pay.PayHelper;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Sum on 16/7/12.
 */
public class OrderOperatorBar extends BaseBar {

    public OrderOperatorBar(Context context) {
        super(context);
    }

    public OrderOperatorBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @ViewInject(R.id.button_1)
    private Button button_1;

    @ViewInject(R.id.button_2)
    private Button button_2;

    private OrderModel mOrder;

    @Override
    protected void initParams() {

    }

    public void onBindOrderInfo(OrderModel orderInfo) {
        mOrder = orderInfo;
        setTextShow();
    }

    private void setTextShow() {
        setVisibility(VISIBLE);
        switch (mOrder.getOrderState()) {

            case OrderState.order_wait_pay:
                if (mOrder.getPayType().equals("1")) {
                    button_1.setVisibility(VISIBLE);
                    button_1.setText("付款");
                    button_1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            orderPay();
                        }
                    });
                } else {
                    button_1.setVisibility(GONE);
                }
                button_2.setVisibility(VISIBLE);
                button_2.setText("取消订单");
                button_2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderCancel();
                    }
                });

                break;

            case OrderState.order_wait_pick_sender:
                button_1.setVisibility(GONE);
                //支付状态
                String payStatus = mOrder.getPayStatus();
                if (payStatus.equals("2")) {
                    button_2.setVisibility(VISIBLE);
                    button_2.setText("取消订单");
                    button_2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            orderCancel();
                        }
                    });
                } else if (payStatus.equals("3")) {
                    button_2.setVisibility(GONE);
                }
                break;

            case OrderState.order_wait_pick:
                button_1.setVisibility(GONE);
                button_2.setVisibility(VISIBLE);
                button_2.setText("确认收货");
                button_2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderTake();
                    }
                });
                break;

            case OrderState.order_wait_judge:
                button_1.setVisibility(GONE);
                button_2.setText("去评价");
                button_2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderJudge();
                    }
                });
                break;

            case OrderState.order_wait_for_confirm_pay:
                button_1.setVisibility(GONE);
                button_2.setText("确认付款");
                button_2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderConfirmPay();
                    }
                });
                break;

            case OrderState.order_cancel:
                setVisibility(GONE);
                break;

            case OrderState.order_finish:
                setVisibility(GONE);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_order_item_operator;
    }

    //(0：取消；1：确认收货；2：申请退货；3：确认退款；4:零售商确认付款)

    private void orderCancel() {
        tip(0, "取消订单", "您确定取消该订单?");
    }

    private void orderPay() {

        PayHelper pay = new PayHelper((Activity) mContext, new PayHelper.PayCallBack() {
            @Override
            public void success() {
                ToastUtil.showToastLong("付款成功");
            }

            @Override
            public void failed(String msg) {
                ToastUtil.showToastLong(msg);
            }
        });

        String orderCode = mOrder.getStoreOrderCode();
        pay.payOrder(orderCode);


    }

    private void orderTake() {
        tip(1, "确认收货", "您确定已经收到货了吗?");
    }

    private void orderConfirmPay() {
        tip(4, "确认付款", "您确定已收到货并且已付款了吗?");
    }

    private void tip(final int type, String title, String msg) {
        AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onOpClick(type);
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.show();
    }

    private void onOpClick(final int type) {
        ApiControl.getApi().orderUpdateType(type, mOrder.getStoreOrderCode(), new DefaultCallback<ResponseModel<Object>>() {
            @Override
            public void success(ResponseModel<Object> result) {
                refresh(type);
            }

            @Override
            public void finish() {

            }
        });
    }

    private void orderJudge() {
        ToastUtil.showToastLong(R.string.building);
    }

    private void refresh(int type) {
        CommonEvent event = new CommonEvent();
        event.refresh_order_type = type;
        event.refresh_order_view = mOrder.getViewType();
        event.setEventTag(CommonEvent.ET_RELOAD_ORDER_INFO);
        EventHelper.postDefaultEvent(event);
    }
}
