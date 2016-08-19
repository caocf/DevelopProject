package com.xhl.bqlh.business.view.ui.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.ProductReturn;
import com.xhl.bqlh.business.Model.ProductReturnDetail;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.base.Common.DefaultCallback;
import com.xhl.bqlh.business.view.custom.ComplexText;
import com.xhl.bqlh.business.view.ui.recyclerHolder.OrderReturnProductDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.layoutManager.FullGridViewManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Summer on 2016/7/28.
 */
@ContentView(R.layout.fragment_order_return_details)
public class OrderReturnDetailsActivity extends BaseAppActivity {

    @ViewInject(R.id.tv_apply_time)
    private TextView tv_apply_time;

    @ViewInject(R.id.tv_apply_order)
    private TextView tv_apply_order;

    @ViewInject(R.id.tv_shop_name)
    private TextView tv_shop_name;

    @ViewInject(R.id.tv_apply_state)
    private TextView tv_apply_state;

    @ViewInject(R.id.tv_remark)
    private TextView tv_remark;

    @ViewInject(R.id.tv_refuse)
    private TextView tv_refuse;

    @ViewInject(R.id.tv_return_money)
    private TextView tv_return_money;

    @ViewInject(R.id.tv_return_money_verify)
    private TextView tv_return_money_verify;

    @ViewInject(R.id.ll_content)
    private View ll_content;

    @ViewInject(R.id.recycler_view)
    private RecyclerView recycler_view;

    private RecyclerAdapter mAdapter;

    private String mReturnId;

    @Override
    protected void initParams() {

        super.initToolbar();
        setTitle("退货单详情");

        mReturnId = getIntent().getStringExtra("id");

        mAdapter = new RecyclerAdapter(this);
        recycler_view.setLayoutManager(new FullGridViewManager(this, 1));
        recycler_view.setAdapter(mAdapter);
        recycler_view.setHasFixedSize(true);
        onRefreshLoadData();
    }

    @Override
    public void onRefreshLoadData() {
        ll_content.setVisibility(View.INVISIBLE);
        showLoadingDialog();
        ApiControl.getApi().productReturnQueryDetails(mReturnId, new DefaultCallback<ResponseModel<ProductReturn>>() {
            @Override
            public void success(ResponseModel<ProductReturn> result) {

                ll_content.setVisibility(View.VISIBLE);

                ProductReturn obj = result.getObj();
                showData(obj);
            }

            @Override
            public void finish() {
                hideLoadingDialog();
            }
        });
    }

    private void showData(ProductReturn data) {
        tv_apply_time.setText("申请时间:" + data.getApplyTime());
        tv_shop_name.setText(data.getRetailerName());
        tv_apply_state.setText(data.getReturnState());

        int color = ContextCompat.getColor(this, R.color.app_price_color);
        String t1 = "申请退款总额:" + data.getApplyReturnMoney()+"元";
        ComplexText.TextBuilder b1 = new ComplexText.TextBuilder(t1);
        b1.setTextColor(color, 7, t1.length());
        tv_return_money.setText(b1.Build());

        String t2 = "确认退款总额:" + data.getVerifyReturnMoney()+"元";
        ComplexText.TextBuilder b2 = new ComplexText.TextBuilder(t2);
        b2.setTextColor(ContextCompat.getColor(this, R.color.app_red), 7, t2.length());
        tv_return_money_verify.setText(b2.Build());

        //订单号
        String storeOrderCode = data.getStoreOrderCode();
        if (!TextUtils.isEmpty(storeOrderCode)) {
            tv_apply_order.setText(getString(R.string.order_num, storeOrderCode));
            tv_apply_order.setVisibility(View.VISIBLE);
        } else {
            tv_apply_order.setVisibility(View.GONE);
        }
        //审核说明
        String auditDesc = data.getAuditDesc();
        if (!TextUtils.isEmpty(auditDesc)) {
            tv_refuse.setText("审核说明:" + auditDesc);
            tv_refuse.setVisibility(View.VISIBLE);
        }
        //退货说明
        String returnDesc = data.getReturnDesc();
        if (!TextUtils.isEmpty(returnDesc)) {
            tv_remark.setText("退货说明:" + returnDesc);
            tv_remark.setVisibility(View.VISIBLE);
        }
        //商品数据
        List<RecyclerDataHolder> holders = new ArrayList<>();
        List<ProductReturnDetail> detailList = data.getReturnDetailList();
        for (ProductReturnDetail pro : detailList) {
            holders.add(new OrderReturnProductDataHolder(pro));
        }
        mAdapter.setDataHolders(holders);
    }
}
