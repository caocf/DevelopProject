package com.xhl.bqlh.business.view.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.AppConfig.GlobalParams;
import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.GiftModel;
import com.xhl.bqlh.business.Model.OrderDetail;
import com.xhl.bqlh.business.Model.OrderModel;
import com.xhl.bqlh.business.Model.Type.OrderType;
import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.doman.ModelHelper;
import com.xhl.bqlh.business.utils.SnackUtil;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.helper.DialogMaker;
import com.xhl.bqlh.business.view.helper.ViewHelper;
import com.xhl.bqlh.business.view.ui.activity.bluetooth.BTPrinterActivity;
import com.xhl.bqlh.business.view.ui.activity.bluetooth.printerContent.OrderPrinterFactory;
import com.xhl.bqlh.business.view.ui.activity.bluetooth.printerContent.PrinterFactory;
import com.xhl.bqlh.business.view.ui.bar.OrderBar;
import com.xhl.bqlh.business.view.ui.recyclerHolder.GiftDataHolder;
import com.xhl.bqlh.business.view.ui.recyclerHolder.OrderProductDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;
import com.xhl.xhl_library.ui.recyclerview.layoutManager.FullGridViewManager;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/4/18.
 */
@ContentView(R.layout.activity_order_details)
public class OrderDetailsActivity extends BaseAppActivity {

    @ViewInject(R.id.order_bar)
    private OrderBar mBar;

    @ViewInject(R.id.ll_content)
    private View mContent;

    @ViewInject(R.id.tv_order_people)
    private TextView tv_order_people;

    @ViewInject(R.id.tv_order_location)
    private TextView tv_order_location;

    @ViewInject(R.id.tv_order_phone)
    private TextView tv_order_phone;

    @ViewInject(R.id.tv_order_pay_price)
    private TextView tv_order_pay_price;//订单已支付金额

    @ViewInject(R.id.tv_order_lost_price)
    private TextView tv_order_lost_price;//订单赊账金额

    @ViewInject(R.id.tv_return_product)
    private View tv_return_product;//申请退货

    @Event(R.id.tv_return_product)
    private void onReturnClick(View view) {

        List<OrderDetail> detailList = mOrderInfo.getOrderDetailList();
        ArrayList<ShopCarModel> data = new ArrayList<>();
        for (OrderDetail order : detailList) {
            ShopCarModel shopCarModel = ModelHelper.OrderDetail2ShopCarModel(order);
            data.add(shopCarModel);
        }

        Intent intent = new Intent(this, ConfirmProductActivity.class);
        intent.putExtra(GlobalParams.Intent_shop_id, mOrderInfo.getRetailerId());
        intent.putExtra(GlobalParams.Intent_shop_order_id, storeOrderCode);
        intent.putExtra(ConfirmProductActivity.TYPE_PRODUCT_OPERATOR, ConfirmProductActivity.TYPE_ORDER_RETURN_1);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    @ViewInject(R.id.btn_clear)
    private Button btn_clear;//提交按钮

    @Event(R.id.btn_clear)
    private void onClearClick(View view) {
        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle("清欠");
        View content = View.inflate(this, R.layout.dialog_clear_price, null);

        final EditText ed_price = (EditText) content.findViewById(R.id.ed_price);
        final EditText ed_note = (EditText) content.findViewById(R.id.ed_note);
        TextView tv_price = (TextView) content.findViewById(R.id.tv_price);
        //剩余欠款
        String money = String.valueOf(mCurLostMoney);
        if (!TextUtils.isEmpty(money)) {
            tv_price.setText(getString(R.string.price, money));
        }
        dialog.setView(content);
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String money = ed_price.getEditableText().toString().trim();
                mPayNote = ed_note.getEditableText().toString().trim();

                if (!TextUtils.isEmpty(money)) {
                    mOrderReduce = Float.valueOf(money);
                    if (mOrderReduce <= mCurLostMoney) {
                        clearHint();
                    } else {
                        SnackUtil.shortShow(mToolbar, "清欠金额超出欠款金额");
                    }
                } else {
                    mOrderReduce = 0f;
                }
            }
        });

        dialog.setNegativeButton(R.string.dialog_cancel, null);
        AlertDialog alertDialog = dialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void clearHint() {

        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle(R.string.dialog_title);
        dialog.setMessage("本次清欠金额:" + mOrderReduce + "元");
        dialog.setNegativeButton(R.string.dialog_cancel, null);
        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearMoney();
            }
        });
        dialog.show();
    }

    private void clearMoney() {

        showProgressLoading("提交清欠中");

        ApiControl.getApi().orderRepay(
                mOrderInfo.getOrderCode(),
                mOrderInfo.getStoreOrderCode(),
                String.valueOf(mOrderReduce),
                String.valueOf(mCurLostMoney),
                mOrderInfo.getReceivingId(),
                mPayNote, new Callback.CommonCallback<ResponseModel<String>>() {
                    @Override
                    public void onSuccess(ResponseModel<String> result) {
                        if (result.isSuccess()) {
                            SnackUtil.shortShow(mToolbar, "清欠成功");
                        } else {
                            SnackUtil.shortShow(mToolbar, result.getObj());
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        SnackUtil.shortShow(mToolbar, ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        hideLoadingDialog();
                    }
                });

    }

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    private float mOrderReduce = 0f;//清欠金额
    private String mPayNote;//清欠备注

    private float mCurLostMoney;//当前剩余的欠款
    private float mCurPayMoney;//当前支付金额

    //订单信息
    private String storeOrderCode;
    private String mName;

    private OrderModel mOrderInfo;

    private RecyclerAdapter mAdapter;

    private Callback.Cancelable mCancel;

    @Override
    protected void initParams() {
        super.initToolbar();
        setTitle(R.string.order_details);

        storeOrderCode = getIntent().getStringExtra("storeOrderCode");
        mName = getIntent().getStringExtra("name");

        mAdapter = new RecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new FullGridViewManager(this, 1));
        mRecyclerView.setHasFixedSize(true);

        ViewHelper.setViewGone(mContent, true);
        loadDetails();
    }

    private void loadDetails() {

        showLoadingDialog();

        mCancel = ApiControl.getApi().orderInfo(storeOrderCode, new Callback.CommonCallback<ResponseModel<OrderModel>>() {
            @Override
            public void onSuccess(ResponseModel<OrderModel> result) {
                if (result.isSuccess()) {
                    //状态显示
                    OrderModel obj = result.getObj();
                    mOrderInfo = obj;
                    mBar.detailShow(obj);
                    //订单商品,赠品
                    List<OrderDetail> orderDetailList = obj.getOrderDetailList();
                    loadProductData(orderDetailList, obj.getGiftDetailList());

                    //实付金额
                    mCurPayMoney = obj.getRealPayMoney();
                    tv_order_pay_price.setText(getString(R.string.order_details_pay, mCurPayMoney));

                    //赊账金额
                    float arrears = obj.getArrears();
                    mCurLostMoney = arrears;
                    if (arrears <= 0) {
                        btn_clear.setVisibility(View.GONE);
                    }
                    //获取赊账金额
                    tv_order_lost_price.setText(getString(R.string.order_details_lost_money, arrears));

                    //设置信息
                    tv_order_people.setText(getString(R.string.order_details_people, mName));
                    tv_order_location.setText(getString(R.string.order_details_location, obj.getAddress()));
                    tv_order_phone.setText(getString(R.string.order_details_phone, obj.getPhone()));

                    //订单完成
                    String status = obj.getDistributionStatus();
                    int orderState = Integer.parseInt(status);
                    if (orderState == OrderType.order_state_finish) {
                        tv_return_product.setVisibility(View.VISIBLE);
                    } else {
                        tv_return_product.setVisibility(View.GONE);
                    }
                    ViewHelper.setViewGone(mContent, false);

                } else {
                    SnackUtil.shortShow(mRecyclerView, result.getMessage());
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            OrderDetailsActivity.this.finish();
                        }
                    }, 1000);
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
                hideLoadingDialog();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCancel != null) {
            mCancel.cancel();
        }
    }

    private void loadProductData(List<OrderDetail> details, List<GiftModel> gift) {
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (OrderDetail order : details) {
            holders.add(new OrderProductDataHolder(order));
        }
        if (gift != null) {
            for (GiftModel gif : gift) {
                holders.add(new GiftDataHolder(gif));
            }
        }
        mAdapter.setDataHolders(holders);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu_order_details_acount, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_pay_record) {
            Intent intent = new Intent(this, OrderAccountRecordActivity.class);
            intent.putExtra("storeOrderCode", storeOrderCode);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_print) {
            if (mOrderInfo != null) {
                //方法工厂
                PrinterFactory factory = new OrderPrinterFactory(mOrderInfo);
                String print = factory.create().print();
                Intent intent = new Intent(this, BTPrinterActivity.class);
                intent.putExtra(BTPrinterActivity.TAG_PRINT_CONTENT, print);
                startActivity(intent);
            }
        }
        return true;
    }
}
