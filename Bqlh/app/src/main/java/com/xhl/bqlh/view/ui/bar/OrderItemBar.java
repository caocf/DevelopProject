package com.xhl.bqlh.view.ui.bar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhl.bqlh.AppConfig.GlobalParams;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.OrderDetail;
import com.xhl.bqlh.model.OrderModel;
import com.xhl.bqlh.view.base.BaseBar;
import com.xhl.bqlh.view.helper.DialogMaker;
import com.xhl.bqlh.view.ui.activity.OrderDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Sum on 16/7/12.
 */
public class OrderItemBar extends BaseBar {

    public OrderItemBar(Context context) {
        super(context);
    }

    @ViewInject(R.id.op_bar)
    private OrderOperatorBar op_bar;

    @ViewInject(R.id.tv_shop_name)
    private TextView tv_shop_name;

    @ViewInject(R.id.tv_order_state)
    private TextView tv_order_state;

    @ViewInject(R.id.ll_content)
    private LinearLayout ll_content;

    @ViewInject(R.id.ll_remark)
    private LinearLayout ll_remark;

    @ViewInject(R.id.ed_remark)
    private TextView ed_remark;

    @Event(R.id.ed_remark)
    private void onRemarkClick(View view) {

        AlertDialog.Builder dialog = DialogMaker.getDialog(mContext);

        View content = View.inflate(mContext, R.layout.dialog_input_remark, null);

        final EditText text = (EditText) content.findViewById(R.id.ed_text);
        if (!TextUtils.isEmpty(mOrder.remark)) {
            text.setText(mOrder.remark);
            text.setSelection(0, mOrder.remark.length());
        }
        dialog.setView(content);
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String c = text.getText().toString();
                if (!TextUtils.isEmpty(c)) {
                    ed_remark.setText(c);
                }
                mOrder.remark = c;
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private OrderModel mOrder;

    private boolean mHasDetails = false;

    @Override
    protected void initParams() {
        ll_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                details();
            }
        });
    }

    private void details() {
        if (mHasDetails) {
            String storeOrderCode = mOrder.getStoreOrderCode();
            Intent intent = new Intent(mContext, OrderDetailsActivity.class);
            intent.putExtra(GlobalParams.Intent_store_order_code, storeOrderCode);
            intent.putExtra("view", mOrder.getViewType());
            mContext.startActivity(intent);
        }
    }

    public void onBindData(OrderModel data) {
        if (mOrder == data) {
            return;
        }
        mHasDetails = true;
        mOrder = data;

        tv_shop_name.setText(data.getCompanyName());

        mOrder.getOrderState();

        tv_order_state.setText(mOrder.getOrderStateDesc());

        op_bar.onBindOrderInfo(data);

        ll_remark.setVisibility(GONE);

        addChildView();
    }

    public void onBindInfoData(OrderModel data) {
        if (mOrder == data) {
            return;
        }
        mHasDetails = false;
        mOrder = data;

        tv_shop_name.setText(data.getCompanyName());

        tv_order_state.setVisibility(GONE);

        op_bar.setVisibility(GONE);

        ll_remark.setVisibility(VISIBLE);

        addChildView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_order_item;
    }


    private void addChildView() {

        ll_content.removeAllViews();

        List<OrderDetail> orderDetails = mOrder.getOrderDetailList();
        for (OrderDetail child : orderDetails) {

            OrderItemProductBar bar = new OrderItemProductBar(mContext);

            bar.bindInfo(child);

            bar.setmHasDetails(mHasDetails);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);

            bar.setLayoutParams(params);

            AutoUtils.autoSize(bar);

            ll_content.addView(bar);
        }
    }


}
