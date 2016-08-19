package com.xhl.bqlh.view.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xhl.bqlh.Api.ApiControl;
import com.xhl.bqlh.R;
import com.xhl.bqlh.model.AOrderInit;
import com.xhl.bqlh.model.OrderDetail;
import com.xhl.bqlh.model.OrderModel;
import com.xhl.bqlh.model.UserInfo;
import com.xhl.bqlh.model.base.ResponseModel;
import com.xhl.bqlh.utils.ToastUtil;
import com.xhl.bqlh.view.base.BaseAppActivity;
import com.xhl.bqlh.view.base.Common.DefaultCallback;
import com.xhl.bqlh.view.helper.DialogMaker;
import com.xhl.bqlh.view.helper.EventHelper;
import com.xhl.bqlh.view.ui.recyclerHolder.OrderItemProductDataHolder;
import com.xhl.xhl_library.ui.recyclerview.RecyclerAdapter;
import com.xhl.xhl_library.ui.recyclerview.RecyclerDataHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Summer on 2016/7/15.
 */
@ContentView(R.layout.activity_order_confirm)
public class OrderConfirmActivity extends BaseAppActivity {

    @ViewInject(R.id.ll_content)
    private View ll_content;//订单根布局
    @ViewInject(R.id.recycler_view)
    private RecyclerView recyclerView;

    @ViewInject(R.id.tv_location_people)
    private TextView tv_location_people;//收件人

    @ViewInject(R.id.tv_location_phone)
    private TextView tv_location_phone;//收件人手机号

    @ViewInject(R.id.tv_location_details)
    private TextView tv_location_details;//收件人地址

    @ViewInject(R.id.tv_order_pay_type)
    private TextView tv_order_pay_type;//支付方式提示

//    @ViewInject(R.id.tv_order_coupon)
//    private TextView tv_order_coupon;//优惠券使用提示
//
//    @ViewInject(R.id.tv_order_jf)
//    private TextView tv_order_jf;//积分使用提示

    @ViewInject(R.id.tv_price_order)
    private TextView tv_price_order;

    @ViewInject(R.id.tv_price_order_integral)
    private TextView tv_price_order_integral;

    @ViewInject(R.id.tv_price_real_pay)
    private TextView tv_price_real_pay;

    @ViewInject(R.id.tv_free_all_orders)
    private TextView tv_free_all_orders;

    //支付方式
    @Event(value = R.id.ripple_select_pay_type)
    private void onSelectPayTypeClick(View view) {

        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle(R.string.pay_type);
        dialog.setSingleChoiceItems(R.array.pay_array, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        tv_order_pay_type.setText("在线支付");
                        mPayId = 4;
                        break;
                    case 1:
                        tv_order_pay_type.setText("货到付款");
                        mPayId = 0;
                        break;
                }
                mPayType = which + 1;
            }
        });

        dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Event(R.id.btn_confirm_orders)
    private void onConfirmClick(View view) {
        saveOrder();
    }

    private int mPayType = 1;//默认在线支付方式
    private int mPayId = 4;//支付包支付

    private String mOrderInfo;

    private AOrderInit mInit;

    @Override
    protected void initParams() {
        super.initBackBar("确认订单", true, false);
        mOrderInfo = getIntent().getStringExtra("data");
        if (TextUtils.isEmpty(mOrderInfo)) {
            ToastUtil.showToastShort("订单数据异常");
            OrderConfirmActivity.this.finish();
            return;
        }
        loadInfo();
    }

    private void loadInfo() {

        showProgressLoading("订单初始化中...");

        ll_content.setVisibility(View.INVISIBLE);

        ApiControl.getApi().orderCreateInit(mOrderInfo, new DefaultCallback<ResponseModel<AOrderInit>>() {
            @Override
            public void success(ResponseModel<AOrderInit> result) {
                AOrderInit obj = result.getObj();
                mInit = obj;
                showInfo(obj);
            }

            @Override
            public void finish() {
                hideLoadingDialog();
                ll_content.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.showToastShort("订单初始化异常");
                OrderConfirmActivity.this.finish();
            }
        });
    }

    private void showInfo(AOrderInit data) {
        //商品数据
        List<OrderModel> orderList = data.getOrderList();
        addView(orderList);
        //显示信息
        UserInfo user = data.getUser();
        setText(tv_location_people, "收货人：" + user.liableName);
        setText(tv_location_phone, "电话：" + user.liablePhone);
        setText(tv_location_details, "地址：" + user.address);

        //金额信息
        tv_free_all_orders.setText(getString(R.string.pay_money, data.getAllMoney()));
    }

    private void setText(TextView text, String content) {
        if (!TextUtils.isEmpty(content)) {
            text.setText(content);
        }
    }

    private void addView(List<OrderModel> pro) {
        List<RecyclerDataHolder> holders = new ArrayList<>();
        int i = 0;
        for (OrderModel order : pro) {
            holders.add(new OrderItemProductDataHolder(order, i++));
        }
        recyclerView.setAdapter(new RecyclerAdapter(this, holders));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 20;
            }
        });
    }

    private void saveOrder() {

        showProgressLoading("提交订单中...");

        List<OrderModel> orderList = mInit.getOrderList();

        JSONArray jsonArray = saveOrderToJson(orderList);

        ApiControl.getApi().orderCreate(jsonArray.toString(), new DefaultCallback<ResponseModel<HashMap<String, Object>>>() {
            @Override
            public void success(ResponseModel<HashMap<String, Object>> result) {

                HashMap<String, Object> res = result.getObj();
                Double r = (Double) res.get("result");
                if (r == 0) {
                    ToastUtil.showToastShort("提交订单异常");
                } else {
                    EventHelper.postReLoadCarEvent();//刷新购物车
                    EventHelper.postReLoadOrderNumEvent();//刷新订单个数
                    if (mPayType != 2) {//非货到付款
                        goPay();
                    } else {
                        ToastUtil.showToastShort("下单成功");
                        x.task().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                OrderConfirmActivity.this.finish();
                            }
                        }, 1000);
                    }
                }
            }

            @Override
            public void finish() {
                hideLoadingDialog();
            }
        });
    }


    private void goPay() {
        AlertDialog.Builder dialog = DialogMaker.getDialog(this);
        dialog.setTitle("下单成功");
        dialog.setMessage("下单成功,是否立刻去支付？");
        dialog.setPositiveButton("支付", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(OrderConfirmActivity.this, OrderQueryActivity.class);
                intent.putExtra(OrderQueryActivity.TAG_ORDER_TYPE, OrderQueryActivity.ORDER_PAY);
                startActivity(intent);
                finish();
            }
        });
        dialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }


    //订单数据
    //创建生成一组订单数据
    private JSONArray saveOrderToJson(List<OrderModel> orders) {
        JSONArray ordersJson = new JSONArray();
        for (OrderModel order : orders) {
            JSONObject oneOrder = createOneOrder(order);
            ordersJson.put(oneOrder);
        }
        return ordersJson;
    }

    private String getAttr(String attr) {
        if (TextUtils.isEmpty(attr)) {
            return "";
        }
        return attr;
    }

    //创建一个订单数据
    private JSONObject createOneOrder(OrderModel order) {
        JSONObject orderJson = new JSONObject();
        try {
            orderJson.put("companyId", getAttr(order.getCompanyId()));
            orderJson.put("payId", mPayId);
            orderJson.put("couponsMoney", getAttr(order.getCouponsMoney()));//优惠金额
            orderJson.put("payType", mPayType);//1 在线支付,2货到付款
            orderJson.put("liableMail", getAttr(order.getLiableMail()));//责任人邮箱
            orderJson.put("orderType", getAttr(order.getOrderType()));//1：普通订单；2：团购订单；3：消费者促销订单；4：渠道促销订单；5订货会订单；6：积分订单）
            orderJson.put("couId", getAttr(order.getCouId()));//优惠券主题id
            orderJson.put("productType", getAttr(order.getProductType() + ""));
            orderJson.put("remark", getAttr(order.remark));//留言

            //创建订单商品数据
            JSONArray jsonArray = new JSONArray();
            List<OrderDetail> details = order.getOrderDetailList();
            for (OrderDetail de : details) {
                JSONObject oneProduct = createOneProduct(de);
                jsonArray.put(oneProduct);
            }
            orderJson.put("attrOrderDetail", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderJson;
    }

    //创建一个商品数据
    private JSONObject createOneProduct(OrderDetail order) {
        JSONObject itemJson = new JSONObject();
        try {
            String shoppingCartId = order.getShoppingCartId();
            itemJson.put("shoppingCartId", shoppingCartId);
            String goodId = order.getGoodId();
            itemJson.put("goodId", goodId);
            itemJson.put("num", order.getNum());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemJson;
    }


}
