package com.xhl.bqlh.view.ui.activity;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.AppConfig.GlobalParams;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.AOrderDetails;
import com.xhl.bqlh.model.OrderDetail;
import com.xhl.bqlh.model.OrderModel;
import com.xhl.bqlh.model.UserInfo;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.view.base.BaseAppActivity;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.custom.ComplexText;
import com.xhl.bqlh.view.ui.bar.OrderItemProductBar;
import com.xhl.bqlh.view.ui.bar.OrderOperatorBar;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Sum on 16/7/12.
 */
@ContentView(R.layout.activity_order_details)
public class OrderDetailsActivity extends BaseAppActivity {

    @ViewInject(R.id.ll_root_content)
    private View ll_root_content;

    @ViewInject(R.id.tv_r_user)
    private TextView tv_r_user;

    @ViewInject(R.id.tv_r_phone)
    private TextView tv_r_phone;

    @ViewInject(R.id.tv_r_location)
    private TextView tv_r_location;

    @ViewInject(R.id.tv_s_user)
    private TextView tv_s_user;

    @ViewInject(R.id.tv_s_phone)
    private TextView tv_s_phone;

    @ViewInject(R.id.tv_s_location)
    private TextView tv_s_location;

    @ViewInject(R.id.tv_order_num)
    private TextView tv_order_num;

    @ViewInject(R.id.tv_order_state)
    private TextView tv_order_state;

    @ViewInject(R.id.tv_order_time)
    private TextView tv_order_time;

    @ViewInject(R.id.tv_order_pay_time)
    private TextView tv_order_pay_time;

    @ViewInject(R.id.tv_order_pay_type)
    private TextView tv_order_pay_type;

    @ViewInject(R.id.tv_price_order)
    private TextView tv_price_order;

    @ViewInject(R.id.tv_price_order_integral)
    private TextView tv_price_order_integral;

    @ViewInject(R.id.tv_price_real_pay)
    private TextView tv_price_real_pay;

    @ViewInject(R.id.ll_content)
    private LinearLayout ll_content;

    @ViewInject(R.id.op_bar)
    private OrderOperatorBar op_bar;

    private OrderModel mOrder;
    private UserInfo mUser;

    private String mOrderCode;
    private int mViewType;

    @Override
    protected void initParams() {
        String orderCode = getIntent().getStringExtra(GlobalParams.Intent_store_order_code);
        mViewType = getIntent().getIntExtra("view", -1);

        super.initBackBar("订单详情", true, false);
        mOrderCode = orderCode;
        loadOrderInfo();
    }

    private void loadOrderInfo() {
        showLoadingDialog();
        ll_root_content.setVisibility(View.INVISIBLE);
        ApiControl.getApi().orderDetails(mOrderCode, new DefaultCallback<ResponseModel<AOrderDetails>>() {
            @Override
            public void success(ResponseModel<AOrderDetails> result) {
                AOrderDetails obj = result.getObj();
                mUser = obj.getUser();
                mOrder = obj.getOrder();
                mOrder.setViewType(mViewType);

                loadInfo();
                ll_root_content.setVisibility(View.VISIBLE);
            }

            @Override
            public void finish() {
                hideLoadingDialog();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                OrderDetailsActivity.this.finish();
            }
        });
    }

    private void loadInfo() {
        setText(tv_r_user, "收货人：" + mUser.liableName);
        setText(tv_r_phone, "联系方式：" + mUser.liablePhone);
        setText(tv_r_location, "地址：" + mUser.address);

        setText(tv_s_user, "发货人：" + mOrder.getCompanyName());
        setText(tv_s_phone, "联系方式：" + mOrder.getCompanyPhone());
        setText(tv_s_location, "地址：" + mOrder.getAddress());
        setText(tv_order_num, mOrder.getStoreOrderCode());
        int orderState = mOrder.getOrderState();
        setText(tv_order_state, mOrder.getOrderStateDesc());

        setText(tv_order_time, "下单时间：" + mOrder.getOrderTime());
        setText(tv_order_pay_time, "付款时间：" + mOrder.getPayTime());
//        setText(tv_order_pay_type, "支付方式：" + mOrder.getPayTypeDesc());
        String type = "支付方式：" + mOrder.getPayTypeDesc();
        setText(tv_order_pay_type,
                new ComplexText.TextBuilder(type)
                        .setTextColor(ContextCompat.getColor(this, R.color.app_red1), 5, type.length()).Build());

        setText(tv_price_order, "￥" + mOrder.getOrderMoney());
        setText(tv_price_order_integral, mOrder.getIntegral());
        setText(tv_price_real_pay, "￥" + mOrder.getRealPayMoney());

        addChildView();

        op_bar.onBindOrderInfo(mOrder);
    }

    private void addChildView() {

        ll_content.removeAllViews();

        List<OrderDetail> orderDetails = mOrder.getOrderDetailList();
        for (OrderDetail child : orderDetails) {

            OrderItemProductBar bar = new OrderItemProductBar(this);

            bar.bindInfo(child);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);

            bar.setLayoutParams(params);

            AutoUtils.autoSize(bar);

            ll_content.addView(bar);
        }
    }

    private void setText(TextView text, CharSequence content) {

        if (!TextUtils.isEmpty(content)) {
            text.setText(content);
        }
    }

    @Override
    public void onEvent(CommonEvent event) {
        super.onEvent(event);
        if (event.getEventTag() == CommonEvent.ET_RELOAD_ORDER_INFO) {
            //取消后查询不到订单
            if (event.refresh_order_type == 0) {
                finish();
            } else {
                loadOrderInfo();
            }
        }
    }
}
